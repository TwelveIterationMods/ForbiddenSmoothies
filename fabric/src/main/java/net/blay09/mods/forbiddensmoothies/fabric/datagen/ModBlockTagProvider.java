package net.blay09.mods.forbiddensmoothies.fabric.datagen;

import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider<Block> {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        TagKey<Block> mineablePickaxeTag = TagKey.create(Registries.BLOCK, new ResourceLocation("minecraft", "mineable/pickaxe"));
        FabricTagProvider<Block>.FabricTagBuilder mineableBuilder = getOrCreateTagBuilder(mineablePickaxeTag);
        mineableBuilder.add(ModBlocks.printer, ModBlocks.blender);
    }

}
