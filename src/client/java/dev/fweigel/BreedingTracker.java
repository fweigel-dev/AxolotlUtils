package dev.fweigel;

public class BreedingTracker {
    private static int bredCount = 0;
    private static int fishUsedCount = 0;
    private static TrackingMode mode = TrackingMode.PENDING;

    public static int getCount() {
        return bredCount;
    }

    public static void setCount(int count) {
        bredCount = count;
    }

    public static void increment() {
        bredCount++;
    }

    public static void reset() {
        bredCount = 0;
    }

    public static int getFishUsedCount() {
        return fishUsedCount;
    }

    public static void setFishUsedCount(int count) {
        fishUsedCount = count;
    }

    public static void incrementFishUsed() {
        fishUsedCount++;
    }

    public static void resetFishUsed() {
        fishUsedCount = 0;
    }

    public static TrackingMode getMode() {
        return mode;
    }

    public static void setMode(TrackingMode newMode) {
        mode = newMode;
    }

    public static void resetSession() {
        bredCount = 0;
        fishUsedCount = 0;
        mode = TrackingMode.PENDING;
    }
}
