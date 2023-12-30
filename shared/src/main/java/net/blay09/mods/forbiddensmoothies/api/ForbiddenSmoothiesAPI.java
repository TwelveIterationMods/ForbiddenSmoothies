package net.blay09.mods.forbiddensmoothies.api;

import net.blay09.mods.forbiddensmoothies.InternalMethodsImpl;

import java.lang.reflect.InvocationTargetException;

public class ForbiddenSmoothiesAPI {

    private static final InternalMethodsImpl internalMethods = loadInternalMethods();

    private static InternalMethodsImpl loadInternalMethods() {
        try {
            return (InternalMethodsImpl) Class.forName("net.blay09.mods.forbiddensmoothies.InternalMethodsImpl").getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException("Failed to load Crafting for Blockheads API", e);
        }
    }
}
