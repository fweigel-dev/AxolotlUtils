package dev.fweigel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BreedingHeuristicTest {

    private int breedCount;
    private BreedingStateMachine sm;

    @BeforeEach
    void setUp() {
        breedCount = 0;
        sm = new BreedingStateMachine(() -> breedCount++);
    }

    @Test
    void feedingOneAxolotlDoesNotWatch() {
        sm.handleFeed(1, 0);

        assertFalse(sm.watchingForBaby);
        assertEquals(1, sm.firstFedId);
    }

    @Test
    void feedingTwoDifferentAxolotlsStartsWatching() {
        sm.handleFeed(1, 0);
        sm.handleFeed(2, 10);

        assertTrue(sm.watchingForBaby);
        assertEquals(1, sm.parentAId);
        assertEquals(2, sm.parentBId);
        assertEquals(-1, sm.firstFedId);
    }

    @Test
    void feedingSameAxolotlTwiceDoesNotWatch() {
        sm.handleFeed(1, 0);
        sm.handleFeed(1, 10);

        assertFalse(sm.watchingForBaby);
    }

    @Test
    void feedWindowExpiry() {
        sm.handleFeed(1, 0);
        sm.handleFeed(2, BreedingStateMachine.FEED_WINDOW + 1);

        assertFalse(sm.watchingForBaby);
        assertEquals(2, sm.firstFedId);
    }

    @Test
    void feedJustWithinWindow() {
        sm.handleFeed(1, 0);
        sm.handleFeed(2, BreedingStateMachine.FEED_WINDOW);

        assertTrue(sm.watchingForBaby);
    }

    @Test
    void heartParticlesOnParentDetectsBreed() {
        sm.handleFeed(1, 0);
        sm.handleFeed(2, 10);

        sm.handleHeartParticles(1);

        assertEquals(1, breedCount);
        assertFalse(sm.watchingForBaby);
    }

    @Test
    void heartParticlesOnEitherParentDetectsBreed() {
        sm.handleFeed(1, 0);
        sm.handleFeed(2, 10);

        sm.handleHeartParticles(2);

        assertEquals(1, breedCount);
    }

    @Test
    void heartParticlesOnUnrelatedEntityIgnored() {
        sm.handleFeed(1, 0);
        sm.handleFeed(2, 10);

        sm.handleHeartParticles(99);

        assertEquals(0, breedCount);
        assertTrue(sm.watchingForBaby);
    }

    @Test
    void heartParticlesWhileNotWatchingIgnored() {
        sm.handleFeed(1, 0);

        sm.handleHeartParticles(1);

        assertEquals(0, breedCount);
    }

    @Test
    void breedDetectedOnlyOnce() {
        sm.handleFeed(1, 0);
        sm.handleFeed(2, 10);

        sm.handleHeartParticles(1);
        sm.handleHeartParticles(2);

        assertEquals(1, breedCount);
    }

    @Test
    void secondBreedingCycleAfterFirst() {
        sm.handleFeed(1, 0);
        sm.handleFeed(2, 10);
        sm.handleHeartParticles(1);

        sm.handleFeed(3, 100);
        sm.handleFeed(4, 110);
        sm.handleHeartParticles(3);

        assertEquals(2, breedCount);
    }

    @Test
    void resetClearsAllState() {
        sm.handleFeed(1, 0);
        sm.handleFeed(2, 10);

        sm.reset();

        assertFalse(sm.watchingForBaby);
        assertEquals(-1, sm.firstFedId);
        assertEquals(-1, sm.parentAId);
        assertEquals(-1, sm.parentBId);

        sm.handleHeartParticles(1);
        assertEquals(0, breedCount);
    }
}
