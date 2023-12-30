package net.blay09.mods.forbiddensmoothies.client;

import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.forbiddensmoothies.client.gui.screen.*;
import net.blay09.mods.forbiddensmoothies.menu.ModMenus;

public class ModScreens {
    public static void initialize(BalmScreens screens) {
        screens.registerScreen(ModMenus.printer::get, PrinterScreen::new);
        screens.registerScreen(ModMenus.blender::get, BlenderScreen::new);
    }
}
