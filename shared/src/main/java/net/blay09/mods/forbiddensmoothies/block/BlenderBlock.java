package net.blay09.mods.forbiddensmoothies.block;

import com.google.common.collect.ImmutableMap;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.forbiddensmoothies.block.entity.BlenderBlockEntity;
import net.blay09.mods.forbiddensmoothies.block.entity.ModBlockEntities;
import net.blay09.mods.forbiddensmoothies.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlenderBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = box(3, 0, 3, 13, 16, 13);
    private static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final BooleanProperty UGLY = CustomBlockStateProperties.UGLY;

    public BlenderBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(2.5f));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(UGLY);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (player.getItemInHand(hand).is(ModItems.uglySteelPlating)) {
            return InteractionResult.PASS;
        }

        final var blockEntity = level.getBlockEntity(pos);
        if (!level.isClientSide && blockEntity instanceof BlenderBlockEntity blender) {
            if (player.getAbilities().instabuild && player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.BAMBOO)) {
                blender.getEnergyStorage().setEnergy(blender.getEnergyStorage().getCapacity());
            }
            Balm.getNetworking().openGui(player, blender);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlenderBlockEntity(pos, state);
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
                ModBlockEntities.blender.get(),
                (level, pos, state2, blockEntity) -> blockEntity.serverTick());
    }

}
