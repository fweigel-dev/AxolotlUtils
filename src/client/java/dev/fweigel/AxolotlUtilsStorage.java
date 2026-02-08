package dev.fweigel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AxolotlUtilsStorage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static String currentWorldId = null;

    private static class SaveData {
        boolean highlightBlueEnabled;
        boolean coloredBucketsEnabled;
        boolean breedingTrackerEnabled;
        int bredCount;
        int fishUsedCount;
        String hudIconColor;
        Boolean hudAnimated;
        Float axolotlVolume;
        Boolean showFishTracker;
        Boolean fishBucketLockEnabled;
    }

    public static void loadForWorld(Minecraft client) {
        currentWorldId = resolveWorldId(client);
        Path path = getSavePath();
        if (path == null || !Files.exists(path)) {
            AxolotlUtilsConfig.reset();
            BreedingTracker.reset();
            return;
        }
        try {
            String json = Files.readString(path);
            SaveData data = GSON.fromJson(json, SaveData.class);
            if (data != null) {
                AxolotlUtilsConfig.setHighlightBlueEnabled(data.highlightBlueEnabled);
                AxolotlUtilsConfig.setColoredBucketsEnabled(data.coloredBucketsEnabled);
                AxolotlUtilsConfig.setBreedingTrackerEnabled(data.breedingTrackerEnabled);
                BreedingTracker.setCount(data.bredCount);
                BreedingTracker.setFishUsedCount(data.fishUsedCount);
                AxolotlUtilsConfig.setHudIconColor(AxolotlColor.fromName(data.hudIconColor));
                AxolotlUtilsConfig.setHudAnimated(data.hudAnimated != null && data.hudAnimated);
                AxolotlUtilsConfig.setAxolotlVolume(data.axolotlVolume != null ? data.axolotlVolume : 1.0f);
                AxolotlUtilsConfig.setShowFishTracker(data.showFishTracker != null && data.showFishTracker);
                AxolotlUtilsConfig.setFishBucketLockEnabled(data.fishBucketLockEnabled != null && data.fishBucketLockEnabled);
            }
        } catch (IOException e) {
            AxolotlUtils.LOGGER.error("Failed to load Axolotl Utils data", e);
        }
    }

    public static void save() {
        Path path = getSavePath();
        if (path == null) {
            return;
        }
        SaveData data = new SaveData();
        data.highlightBlueEnabled = AxolotlUtilsConfig.isHighlightBlueEnabled();
        data.coloredBucketsEnabled = AxolotlUtilsConfig.isColoredBucketsEnabled();
        data.breedingTrackerEnabled = AxolotlUtilsConfig.isBreedingTrackerEnabled();
        data.bredCount = BreedingTracker.getCount();
        data.fishUsedCount = BreedingTracker.getFishUsedCount();
        data.hudIconColor = AxolotlUtilsConfig.getHudIconColor().name();
        data.hudAnimated = AxolotlUtilsConfig.isHudAnimated();
        data.axolotlVolume = AxolotlUtilsConfig.getAxolotlVolume();
        data.showFishTracker = AxolotlUtilsConfig.isShowFishTracker();
        data.fishBucketLockEnabled = AxolotlUtilsConfig.isFishBucketLockEnabled();
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(data));
        } catch (IOException e) {
            AxolotlUtils.LOGGER.error("Failed to save Axolotl Utils data", e);
        }
    }

    private static Path getSavePath() {
        if (currentWorldId == null) {
            return null;
        }
        Minecraft mc = Minecraft.getInstance();
        return mc.gameDirectory.toPath().resolve("axolotlutils").resolve(currentWorldId + ".json");
    }

    private static String resolveWorldId(Minecraft client) {
        if (client.getSingleplayerServer() != null) {
            return client.getSingleplayerServer().getWorldData().getLevelName();
        }
        if (client.getCurrentServer() != null) {
            return "Multiplayer_" + client.getCurrentServer().ip.replace(":", "_");
        }
        return "unknown";
    }
}
