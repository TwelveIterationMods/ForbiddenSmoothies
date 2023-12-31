package net.blay09.mods.forbiddensmoothies.block.entity;

import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.container.SubContainer;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothiesConfig;
import net.blay09.mods.forbiddensmoothies.crafting.BlenderRecipe;
import net.blay09.mods.forbiddensmoothies.crafting.ModRecipes;
import net.blay09.mods.forbiddensmoothies.crafting.PrinterRecipe;
import net.blay09.mods.forbiddensmoothies.menu.BlenderMenu;
import net.blay09.mods.forbiddensmoothies.menu.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public class BlenderBlockEntity extends BalmBlockEntity implements BalmMenuProvider, BalmContainerProvider, BalmEnergyStorageProvider {

    private static final int SYNC_INTERVAL = 10;

    public static final int DATA_PROGRESS = 0;
    public static final int DATA_MAX_PROGRESS = 1;
    public static final int DATA_LOCKED_INPUTS = 2;
    public static final int DATA_ENERGY = 3;
    public static final int DATA_MAX_ENERGY = 4;
    public static final int DATA_ENERGY_CONSUMPTION = 5;

    private final RandomSource randomSource = RandomSource.create();

    private final DefaultContainer container = new DefaultContainer(26) {
        @Override
        public void setChanged() {
            BlenderBlockEntity.this.setChanged();
        }

        @Override
        public boolean canPlaceItem(int slot, ItemStack itemStack) {
            if (outputContainer.containsOuterSlot(slot)) {
                return false;
            }

            if (getItem(slot).isEmpty() && lockedInputs) {
                return false;
            }

            return super.canPlaceItem(slot, itemStack);
        }
    };
    private final SubContainer inputContainer = new SubContainer(container, 0, 8);
    private final SubContainer recipeInputContainer = new SubContainer(container, 0, 8) {
        @Override
        public ItemStack getItem(int slot) {
            final var item = super.getItem(slot);
            return lockedInputs && item.getCount() == 1 ? ItemStack.EMPTY : item;
        }
    };
    private final SubContainer outputContainer = new SubContainer(container, 8, 26);

    private final EnergyStorage energyStorage = new EnergyStorage(getMaxEnergy()) {
        @Override
        public int fill(int maxFill, boolean simulate) {
            setChanged();
            return super.fill(maxFill, simulate);
        }

        @Override
        public int drain(int maxDrain, boolean simulate) {
            setChanged();
            return super.drain(maxDrain, simulate);
        }

        @Override
        public void setEnergy(int energy) {
            super.setEnergy(energy);
            setChanged();
        }
    };

    private boolean lockedInputs;
    private int progress;
    private int maxProgress;
    private int energyCostPerTick;

    private BlenderRecipe currentRecipe;

    private boolean dirtyForSync;
    private int ticksSinceLastSync;

    private float animationTicks;
    private float animationEndingTicks;

    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int i) {
            return switch (i) {
                case DATA_PROGRESS -> BlenderBlockEntity.this.progress;
                case DATA_MAX_PROGRESS -> BlenderBlockEntity.this.maxProgress;
                case DATA_LOCKED_INPUTS -> BlenderBlockEntity.this.lockedInputs ? 1 : 0;
                case DATA_ENERGY -> BlenderBlockEntity.this.energyStorage.getEnergy();
                case DATA_MAX_ENERGY -> BlenderBlockEntity.this.energyStorage.getCapacity();
                case DATA_ENERGY_CONSUMPTION -> energyCostPerTick;
                default -> 0;
            };
        }

        public void set(int i, int value) {
            switch (i) {
                case DATA_PROGRESS -> BlenderBlockEntity.this.progress = value;
                case DATA_MAX_PROGRESS -> BlenderBlockEntity.this.maxProgress = value;
                case DATA_LOCKED_INPUTS -> BlenderBlockEntity.this.lockedInputs = value != 0;
                case DATA_ENERGY -> BlenderBlockEntity.this.energyStorage.setEnergy(value);
            }

        }

        public int getCount() {
            return 6;
        }
    };

    public BlenderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.blender.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.forbiddensmoothies.blender");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BlenderMenu(ModMenus.blender.get(), i, player, container, this.dataAccess);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
    }

    public void load(CompoundTag tag) {
        super.load(tag);

        container.deserialize(tag.getCompound("Items"));
        if (tag.contains("Energy")) {
            energyStorage.deserialize(tag.get("Energy"));
        }
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        lockedInputs = tag.getBoolean("LockedInputs");
        energyCostPerTick = tag.getInt("EnergyCostPerTick");
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Items", container.serialize());
        tag.put("Energy", energyStorage.serialize());
        tag.putInt("Progress", this.progress);
        tag.putInt("MaxProgress", this.maxProgress);
        tag.putBoolean("LockedInputs", this.lockedInputs);
    }

    @Override
    protected void writeUpdateTag(CompoundTag tag) {
        super.writeUpdateTag(tag);
        tag.put("Items", container.serialize());
        tag.putInt("EnergyCostPerTick", energyCostPerTick);
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public Container getContainer(Direction side) {
        return side == Direction.UP ? inputContainer : outputContainer;
    }

    public void serverTick() {
        if (ticksSinceLastSync >= SYNC_INTERVAL && dirtyForSync) {
            sync();
            dirtyForSync = false;
            ticksSinceLastSync = 0;
        } else {
            ticksSinceLastSync++;
        }

        transferOutputs();

        maxProgress = getTotalProcessingTicks();
        currentRecipe = selectRecipe(randomSource, this.currentRecipe).orElse(null);

        final var lastEnergyCostPerTick = energyCostPerTick;
        energyCostPerTick = currentRecipe != null ? determineEnergyCostPerTick() : 0;
        if (lastEnergyCostPerTick != energyCostPerTick) {
            dirtyForSync = true;
        }

        if (currentRecipe != null && canFitRecipeResults(currentRecipe) && energyStorage.drain(energyCostPerTick, true) >= energyCostPerTick) {
            progress++;
            energyStorage.drain(energyCostPerTick, false);

            if (progress >= maxProgress) {
                progress = 0;
                final var output = currentRecipe.assemble(recipeInputContainer, level.registryAccess());
                for (final var ingredient : currentRecipe.getIngredients()) {
                    for (int i = 0; i < recipeInputContainer.getContainerSize(); i++) {
                        final var slotStack = recipeInputContainer.getItem(i);
                        if (ingredient.test(slotStack)) {
                            slotStack.shrink(1);
                            break;
                        }
                    }
                }
                ContainerUtils.insertItemStacked(outputContainer, output, false);
                currentRecipe = null;
            }
        } else {
            progress = 0;
            energyCostPerTick = 0;
        }
    }

    private void transferOutputs() {
        if (level == null) {
            return;
        }

        final var blockEntityBelow = level.getBlockEntity(worldPosition.below());
        if (blockEntityBelow instanceof PrinterBlockEntity printer) {
            for (int i = 0; i < outputContainer.getContainerSize(); i++) {
                final var slotStack = outputContainer.getItem(i);
                if (!slotStack.isEmpty()) {
                    final var rest = ContainerUtils.insertItemStacked(printer.getInputContainer(), slotStack, false);
                    outputContainer.setItem(i, rest);
                    if (!rest.isEmpty()) {
                        break;
                    }
                }
            }
        }
    }

    private int getTotalProcessingTicks() {
        return ForbiddenSmoothiesConfig.getActive().blender.processingTicks;
    }

    private int getMaxEnergy() {
        return ForbiddenSmoothiesConfig.getActive().blender.maxEnergy;
    }

    private int determineEnergyCostPerTick() {
        return ForbiddenSmoothiesConfig.getActive().blender.energyPerTick;
    }

    private boolean canFitRecipeResults(BlenderRecipe recipe) {
        final var rest = ContainerUtils.insertItemStacked(outputContainer, recipe.getResultItem(level.registryAccess()), true);
        return rest.isEmpty();
    }

    private Optional<BlenderRecipe> selectRecipe(RandomSource random, @Nullable BlenderRecipe currentRecipe) {
        if (level == null) {
            return Optional.empty();
        }

        if (currentRecipe != null && currentRecipe.matches(recipeInputContainer, level)) {
            return Optional.of(currentRecipe);
        }

        final var availableRecipes = level.getRecipeManager().getRecipesFor(ModRecipes.blenderRecipeType, recipeInputContainer, level);
        return WeightedRandom.getRandomItem(random, availableRecipes);
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public Container getInputContainer() {
        return inputContainer;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        dirtyForSync = true;
    }

    public float animate(float partialTicks) {
        if (energyCostPerTick > 0) {
            animationTicks += partialTicks;
            animationEndingTicks = 0f;
        }
        return animationTicks;
    }

    public float animateEnding(float partialTicks) {
        if (energyCostPerTick <= 0) {
            animationEndingTicks += partialTicks;
        }
        return animationEndingTicks;
    }
}
