package dev.fweigel;

public class BreedingTracker {
    private static int bredCount = 0;
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

    public static TrackingMode getMode() {
        return mode;
    }

    public static void setMode(TrackingMode newMode) {
        mode = newMode;
    }

    public static void resetSession() {
        bredCount = 0;
        mode = TrackingMode.PENDING;
    }
}
