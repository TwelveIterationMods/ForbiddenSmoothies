package net.blay09.mods.forbiddensmoothies.block;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.forbiddensmoothies.block.entity.ModBlockEntities;
import net.blay09.mods.forbiddensmoothies.block.entity.PrinterBlockEntity;
import net.blay09.mods.forbiddensmoothies.item.ModItems;
import net.blay09.mods.forbiddensmoothies.skin.SkinRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrinterBlock extends BaseEntityBlock {

    private static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final BooleanProperty UGLY = CustomBlockStateProperties.UGLY;

    private ItemStack lastHoverStack = ItemStack.EMPTY;
    private String currentRandomName;

    public PrinterBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(2.5f));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(UGLY);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack itemStack) {
        if (level.getBlockEntity(pos) instanceof PrinterBlockEntity printer) {
            boolean useRandomSkin = true;
            final var tagCompound = itemStack.getTag();
            if (tagCompound != null) {
                if (tagCompound.contains("CustomSkin")) {
                    final var customSkin = NbtUtils.readGameProfile(tagCompound.getCompound("CustomSkin"));
                    if (customSkin != null) {
                        printer.setCustomSkin(customSkin);
                        useRandomSkin = false;
                    }
                }
            }
            if (!level.isClientSide && useRandomSkin) {
                final var randomSkin = SkinRegistry.getRandomSkin();
                if (randomSkin != null) {
                    printer.setCustomSkin(new GameProfile(randomSkin.uuid(), randomSkin.name()));
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> tooltip, TooltipFlag flag) {
        final var tagCompound = itemStack.getTag();
        if (tagCompound != null && tagCompound.contains("CustomSkin")) {
            final var customSkin = NbtUtils.readGameProfile(tagCompound.getCompound("CustomSkin"));
            if (customSkin != null) {
                tooltip.add(getSkinTooltip(customSkin.getName()));
            }
        } else {
            if (currentRandomName == null) {
                updateRandomSkinName();
            }

            tooltip.add(getSkinTooltip(currentRandomName));
        }

        if (lastHoverStack != itemStack) {
            updateRandomSkinName();
            lastHoverStack = itemStack;
        }
    }

    protected Component getSkinTooltip(String name) {
        return Component.translatable("tooltip.forbiddensmoothies.printer", name).withStyle(ChatFormatting.GRAY);
    }

    private void updateRandomSkinName() {
        final var randomSkin = SkinRegistry.getRandomSkin();
        currentRandomName = randomSkin != null ? randomSkin.name() : "Steve";
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        final var heldItem = player.getItemInHand(hand);
        if (heldItem.is(ModItems.uglySteelPlating)) {
            return InteractionResult.PASS;
        }

        final var blockEntity = level.getBlockEntity(pos);
        if (!level.isClientSide && blockEntity instanceof PrinterBlockEntity printer) {
            if (player.getAbilities().instabuild && player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.BAMBOO)) {
                printer.getEnergyStorage().setEnergy(printer.getEnergyStorage().getCapacity());
            } else if (heldItem.is(Items.NAME_TAG) && heldItem.hasCustomHoverName()) {
                printer.setCustomSkin(new GameProfile(null, heldItem.getDisplayName().getString()));
            } else {
                Balm.getNetworking().openGui(player, printer);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PrinterBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(UGLY, false);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean wat) {
        if (!state.is(newState.getBlock())) {
            final var blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BalmContainerProvider containerProvider) {
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, pos, containerProvider.getContainer());
                }

                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, wat);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide ? null : createTickerHelper(type,
                ModBlockEntities.printer.get(),
                (level, pos, state2, blockEntity) -> blockEntity.serverTick());
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

}
