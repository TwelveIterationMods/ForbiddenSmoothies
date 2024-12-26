package net.blay09.mods.forbiddensmoothies.api;

import java.lang.reflect.InvocationTargetException;

public class ForbiddenSmoothiesAPI {

    private static final InternalMethods internalMethods = loadInternalMethods();

    private static InternalMethods loadInternalMethods() {
        try {
            return (InternalMethods) Class.forName("net.blay09.mods.forbiddensmoothies.InternalMethodsImpl").getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException("Failed to load Forbidden Smoothies API", e);
        }
    }
}
