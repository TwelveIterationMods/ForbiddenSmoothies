package net.blay09.mods.forbiddensmoothies.client;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.blay09.mods.forbiddensmoothies.client.render.*;
import net.blay09.mods.forbiddensmoothies.block.entity.ModBlockEntities;
import net.minecraft.client.renderer.RenderType;

public class ModRenderers {

    public static void initialize(BalmRenderers renderers) {
        renderers.registerBlockEntityRenderer(ModBlockEntities.printer::get, WorkbenchRenderer::new);

        renderers.setBlockRenderType(() -> ModBlocks.printer, RenderType.cutout());
    }

}
