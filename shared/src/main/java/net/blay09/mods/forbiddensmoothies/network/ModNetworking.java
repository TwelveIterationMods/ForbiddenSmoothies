package net.blay09.mods.forbiddensmoothies.network;

import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ModNetworking {

    public static void initialize(BalmNetworking networking) {
        networking.registerServerboundPacket(id("set_input_lock"),
                SetInputLockMessage.class,
                SetInputLockMessage::encode,
                SetInputLockMessage::decode,
                SetInputLockMessage::handle);
    }

    @NotNull
    private static ResourceLocation id(String name) {
        return new ResourceLocation(ForbiddenSmoothies.MOD_ID, name);
    }

}
