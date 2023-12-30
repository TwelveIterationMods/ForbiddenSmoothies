package net.blay09.mods.forbiddensmoothies.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class ModMenus {

    public static DeferredObject<MenuType<PrinterMenu>> printer;
    public static DeferredObject<MenuType<BlenderMenu>> blender;

    public static void initialize(BalmMenus menus) {
        printer = menus.registerMenu(id("printer"), (windowId, inv, data) -> new PrinterMenu(printer.get(), windowId, inv.player));

        blender = menus.registerMenu(id("blender"), (windowId, inv, data) -> new BlenderMenu(blender.get(), windowId, inv.player));
    }

    @NotNull
    private static ResourceLocation id(String name) {
        return new ResourceLocation(ForbiddenSmoothies.MOD_ID, name);
    }
}
