package net.blay09.mods.forbiddensmoothies.skin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothiesConfig;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SkinRegistry {

    public record Entry(UUID uuid, String name) {
    }

    private static final Random random = new Random();
    private static final List<Entry> availableSkins = new ArrayList<>();

    public static void load() {
        if (!ForbiddenSmoothiesConfig.getActive().remoteAutoSieveSkins) {
            availableSkins.clear();
            final var loadAutoSieveSkins = new Thread(() -> {
                try {
                    final var remoteURL = new URL("https://perks.blay09.net/api/lists/BlayTheNinth");
                    final var in = remoteURL.openStream();
                    final var gson = new Gson();
                    final var reader = new JsonReader(new InputStreamReader(in));
                    final List<Entry> result = gson.fromJson(reader, new TypeToken<List<Entry>>() {
                    }.getType());
                    synchronized (availableSkins) {
                        availableSkins.addAll(result);
                    }
                    reader.close();
                } catch (Throwable e) {
                    ForbiddenSmoothies.logger.error("Could not load remote skins: ", e);
                }
            });
            loadAutoSieveSkins.start();
        }
    }

    @Nullable
    public static Entry getRandomSkin() {
        synchronized (availableSkins) {
            if (availableSkins.isEmpty()) {
                return null;
            }

            return availableSkins.get(random.nextInt(availableSkins.size()));
        }
    }
}
