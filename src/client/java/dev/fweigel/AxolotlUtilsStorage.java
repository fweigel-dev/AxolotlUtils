package dev.fweigel;

import dev.fweigel.mobutils.core.client.storage.WorldScopedStorage;
import net.minecraft.client.Minecraft;

public class AxolotlUtilsStorage {

    private static final WorldScopedStorage<SaveData> STORAGE =
            new WorldScopedStorage<>("axolotlutils", SaveData.class, AxolotlUtils.LOGGER);

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
        STORAGE.loadForWorld(client).ifPresentOrElse(data -> {
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
        }, () -> {
            AxolotlUtilsConfig.reset();
            BreedingTracker.reset();
        });
    }

    public static void save() {
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
        STORAGE.save(data);
    }
}
