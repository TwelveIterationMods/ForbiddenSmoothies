package net.blay09.mods.forbiddensmoothies.block.entity;

import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.forbiddensmoothies.menu.ModMenus;
import net.blay09.mods.forbiddensmoothies.menu.PrinterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class BlenderBlockEntity extends BalmBlockEntity implements BalmMenuProvider {

    private DefaultContainer container = new DefaultContainer(26) {
        @Override
        public void setChanged() {
            BlenderBlockEntity.this.setChanged();
        }
    };

    private int printingProgress;
    private int printingTotalTime;

    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int i) {
            switch (i) {
                case 0:
                    return BlenderBlockEntity.this.printingProgress;
                case 1:
                    return BlenderBlockEntity.this.printingTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int i, int value) {
            switch (i) {
                case 0:
                    BlenderBlockEntity.this.printingProgress = value;
                case 1:
                    BlenderBlockEntity.this.printingTotalTime = value;
                    break;
            }

        }

        public int getCount() {
            return 2;
        }
    };

    public BlenderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.printer.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.forbiddensmoothies.printer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new PrinterMenu(ModMenus.printer.get(), i, player, container, this.dataAccess);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
    }


    public void load(CompoundTag tag) {
        super.load(tag);

        container.deserialize(tag.getCompound("Items"));
        this.printingProgress = tag.getInt("PrintTime");
        this.printingTotalTime = tag.getInt("PrintTimeTotal");
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Items", container.serialize());
        tag.putInt("PrintTime", this.printingProgress);
        tag.putInt("PrintTimeTotal", this.printingTotalTime);
    }
}
