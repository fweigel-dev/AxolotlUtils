package dev.fweigel;

public final class BreedingStateMachine {

    static final int FEED_WINDOW = 600;

    int firstFedId = -1;
    long firstFedTick = -1;
    boolean watchingForBaby = false;
    int parentAId = -1;
    int parentBId = -1;

    private final Runnable onBreedDetected;

    BreedingStateMachine(Runnable onBreedDetected) {
        this.onBreedDetected = onBreedDetected;
    }

    void handleFeed(int entityId, long currentTick) {
        if (firstFedId == -1 || currentTick - firstFedTick > FEED_WINDOW) {
            firstFedId = entityId;
            firstFedTick = currentTick;
            watchingForBaby = false;
        } else if (entityId != firstFedId) {
            watchingForBaby = true;
            parentAId = firstFedId;
            parentBId = entityId;
            firstFedId = -1;
            firstFedTick = -1;
        }
    }

    void handleHeartParticles(int entityId) {
        if (!watchingForBaby) {
            return;
        }
        if (entityId == parentAId || entityId == parentBId) {
            onBreedDetected.run();
            watchingForBaby = false;
        }
    }

    void reset() {
        firstFedId = -1;
        firstFedTick = -1;
        watchingForBaby = false;
        parentAId = -1;
        parentBId = -1;
    }
}
