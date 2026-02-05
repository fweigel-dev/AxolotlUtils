package dev.fweigel;

public class AxolotlUtilsConfig {
    private static boolean highlightBlueEnabled = false;
    private static boolean coloredBucketsEnabled = false;
    private static boolean breedingTrackerEnabled = true;
    private static AxolotlColor hudIconColor = AxolotlColor.LUCY;
    private static boolean hudAnimated = false;

    public static boolean isHighlightBlueEnabled() {
        return highlightBlueEnabled;
    }

    public static boolean isColoredBucketsEnabled() {
        return coloredBucketsEnabled;
    }

    public static boolean isBreedingTrackerEnabled() {
        return breedingTrackerEnabled;
    }

    public static void setHighlightBlueEnabled(boolean enabled) {
        highlightBlueEnabled = enabled;
    }

    public static void setColoredBucketsEnabled(boolean enabled) {
        coloredBucketsEnabled = enabled;
    }

    public static void setBreedingTrackerEnabled(boolean enabled) {
        breedingTrackerEnabled = enabled;
    }

    public static void toggleHighlightBlue() {
        highlightBlueEnabled = !highlightBlueEnabled;
    }

    public static void toggleColoredBuckets() {
        coloredBucketsEnabled = !coloredBucketsEnabled;
    }

    public static void toggleBreedingTracker() {
        breedingTrackerEnabled = !breedingTrackerEnabled;
    }

    public static AxolotlColor getHudIconColor() {
        return hudIconColor;
    }

    public static void setHudIconColor(AxolotlColor color) {
        hudIconColor = color;
    }

    public static void cycleHudIconColor() {
        hudIconColor = hudIconColor.next();
    }

    public static boolean isHudAnimated() {
        return hudAnimated;
    }

    public static void setHudAnimated(boolean animated) {
        hudAnimated = animated;
    }

    public static void toggleHudAnimated() {
        hudAnimated = !hudAnimated;
    }

    public static void reset() {
        highlightBlueEnabled = false;
        coloredBucketsEnabled = false;
        breedingTrackerEnabled = true;
        hudIconColor = AxolotlColor.LUCY;
        hudAnimated = false;
    }
}
