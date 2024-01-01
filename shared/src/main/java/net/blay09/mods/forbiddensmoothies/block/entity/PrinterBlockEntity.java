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
import net.blay09.mods.forbiddensmoothies.crafting.ModRecipes;
import net.blay09.mods.forbiddensmoothies.crafting.PrinterRecipe;
import net.blay09.mods.forbiddensmoothies.menu.ModMenus;
import net.blay09.mods.forbiddensmoothies.menu.PrinterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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


public class PrinterBlockEntity extends BalmBlockEntity implements BalmMenuProvider, BalmContainerProvider, BalmEnergyStorageProvider {

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
            PrinterBlockEntity.this.setChanged();
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

    private boolean dirtyForSync;
    private int ticksSinceLastSync;

    private PrinterRecipe currentRecipe;
    private ItemStack currentResultItem = ItemStack.EMPTY;

    private float animationTime;

    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int i) {
            return switch (i) {
                case DATA_PROGRESS -> PrinterBlockEntity.this.progress;
                case DATA_MAX_PROGRESS -> PrinterBlockEntity.this.maxProgress;
                case DATA_LOCKED_INPUTS -> PrinterBlockEntity.this.lockedInputs ? 1 : 0;
                case DATA_ENERGY -> PrinterBlockEntity.this.energyStorage.getEnergy();
                case DATA_MAX_ENERGY -> PrinterBlockEntity.this.energyStorage.getCapacity();
                case DATA_ENERGY_CONSUMPTION -> energyCostPerTick;
                default -> 0;
            };
        }

        public void set(int i, int value) {
            switch (i) {
                case DATA_PROGRESS -> PrinterBlockEntity.this.progress = value;
                case DATA_MAX_PROGRESS -> PrinterBlockEntity.this.maxProgress = value;
                case DATA_LOCKED_INPUTS -> PrinterBlockEntity.this.lockedInputs = value != 0;
                case DATA_ENERGY -> PrinterBlockEntity.this.energyStorage.setEnergy(value);
            }

        }

        public int getCount() {
            return 6;
        }
    };

    public PrinterBlockEntity(BlockPos pos, BlockState state) {
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
        if (tag.contains("Energy")) {
            energyStorage.deserialize(tag.get("Energy"));
        }
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        lockedInputs = tag.getBoolean("LockedInputs");

        if (tag.contains("CurrentResultItem")) {
            currentResultItem = ItemStack.of(tag.getCompound("CurrentResultItem"));
        }
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
        tag.put("CurrentResultItem", currentResultItem.save(new CompoundTag()));
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

        maxProgress = getTotalProcessingTicks();

        final var lastRecipe = currentRecipe;
        final var recipe = selectRecipe(randomSource, currentRecipe).orElse(null);
        if (lastRecipe != recipe) {
            dirtyForSync = true;
        }

        currentRecipe = recipe;
        currentResultItem = recipe != null ? recipe.getResultItem(level.registryAccess()) : ItemStack.EMPTY;

        final var lastEnergyCostPerTick = energyCostPerTick;
        energyCostPerTick = recipe != null ? determineEnergyCostPerTick() : 0;
        if (lastEnergyCostPerTick != energyCostPerTick) {
            dirtyForSync = true;
        }

        if (recipe != null && canFitRecipeResults(recipe) && energyStorage.drain(energyCostPerTick, true) >= energyCostPerTick) {
            progress++;
            energyStorage.drain(energyCostPerTick, false);

            if (progress >= maxProgress) {
                progress = 0;
                final var output = recipe.assemble(recipeInputContainer, level.registryAccess());
                for (final var ingredient : recipe.getIngredients()) {
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
                currentResultItem = ItemStack.EMPTY;
            }
        } else {
            progress = 0;
            energyCostPerTick = 0;
        }
    }

    private int getTotalProcessingTicks() {
        return ForbiddenSmoothiesConfig.getActive().printer.processingTicks;
    }

    private int getMaxEnergy() {
        return ForbiddenSmoothiesConfig.getActive().printer.maxEnergy;
    }

    private int determineEnergyCostPerTick() {
        return ForbiddenSmoothiesConfig.getActive().printer.energyPerTick;
    }

    private boolean canFitRecipeResults(PrinterRecipe recipe) {
        final var rest = ContainerUtils.insertItemStacked(outputContainer, recipe.getResultItem(level.registryAccess()), true);
        return rest.isEmpty();
    }

    private Optional<PrinterRecipe> selectRecipe(RandomSource random, @Nullable PrinterRecipe currentRecipe) {
        if (level == null) {
            return Optional.empty();
        }

        if (currentRecipe != null && currentRecipe.matches(recipeInputContainer, level)) {
            return Optional.of(currentRecipe);
        }

        final var availableRecipes = level.getRecipeManager().getRecipesFor(ModRecipes.printerRecipeType, recipeInputContainer, level);
        return WeightedRandom.getRandomItem(random, availableRecipes);
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public Container getInputContainer() {
        return inputContainer;
    }

    public ItemStack getCurrentResultItem() {
        return currentResultItem;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        dirtyForSync = true;
    }

    public float animate(float partialTicks) {
        if (energyCostPerTick > 0 || true) {
            animationTime += partialTicks;
        }
        return animationTime;
    }
}
