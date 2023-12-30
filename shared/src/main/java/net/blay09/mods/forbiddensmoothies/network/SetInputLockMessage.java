package net.blay09.mods.forbiddensmoothies.network;

import net.blay09.mods.forbiddensmoothies.menu.PrinterMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class SetInputLockMessage {

    private final boolean locked;

    public SetInputLockMessage(boolean locked) {
        this.locked = locked;
    }


    public static void encode(SetInputLockMessage message, FriendlyByteBuf buf) {
        buf.writeBoolean(message.locked);
    }

    public static SetInputLockMessage decode(FriendlyByteBuf buf) {
        final var locked = buf.readBoolean();
        return new SetInputLockMessage(locked);
    }

    public static void handle(ServerPlayer player, SetInputLockMessage message) {
        if (!(player.containerMenu instanceof PrinterMenu printerMenu)) {
            return;
        }

        printerMenu.setLockedInputs(message.locked);
    }
}
