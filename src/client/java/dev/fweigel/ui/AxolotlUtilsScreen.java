package dev.fweigel.ui;

import dev.fweigel.AxolotlUtilsConfig;
import dev.fweigel.AxolotlUtilsStorage;
import dev.fweigel.BreedingTracker;
import dev.fweigel.mobutils.core.client.ui.ModOptionsList;
import dev.fweigel.mobutils.core.client.ui.ModOptionsList.CardSpec;
import dev.fweigel.mobutils.core.client.ui.ModSettingsScreen;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class AxolotlUtilsScreen extends ModSettingsScreen {

    private static final Identifier IMG_HIGHLIGHT_ON  = id("highlight_on.png");
    private static final Identifier IMG_HIGHLIGHT_OFF = id("highlight_off.png");
    private static final Identifier IMG_BUCKETS_ON    = id("buckets_on.png");
    private static final Identifier IMG_BUCKETS_OFF   = id("buckets_off.png");
    private static final Identifier IMG_BREEDING_ON   = id("breeding_on.png");
    private static final Identifier IMG_BREEDING_OFF  = id("breeding_off.png");
    private static final Identifier IMG_FISH_LOCK_ON  = id("fish_lock_on.png");
    private static final Identifier IMG_FISH_LOCK_OFF = id("fish_lock_off.png");

    private Button displayModeButton;
    private Button iconColorButton;
    private Button animationToggle;

    public AxolotlUtilsScreen() {
        super(Component.translatable("axolotlutils.screen.title"));
    }

    @Override
    protected void addOptions(ModOptionsList list) {
        // ── Row 1: Highlight Blue (left) | Colored Buckets (right) ───────────
        list.addSplitCard(
            CardSpec.image(() -> AxolotlUtilsConfig.isHighlightBlueEnabled()  ? IMG_HIGHLIGHT_ON : IMG_HIGHLIGHT_OFF),
            buildHalfButton(this::getHighlightLabel, () -> {
                AxolotlUtilsConfig.toggleHighlightBlue();
                AxolotlUtilsStorage.save();
            }),
            CardSpec.image(() -> AxolotlUtilsConfig.isColoredBucketsEnabled() ? IMG_BUCKETS_ON : IMG_BUCKETS_OFF),
            buildHalfButton(this::getColoredBucketsLabel, () -> {
                AxolotlUtilsConfig.toggleColoredBuckets();
                AxolotlUtilsStorage.save();
            })
        );

        // ── Row 2: Breeding Tracker (left) | Fish Bucket Lock (right) ────────
        Button breedingBtn = buildHalfButton(this::getBreedingLabel, () -> {
            AxolotlUtilsConfig.toggleBreedingTracker();
            boolean on = AxolotlUtilsConfig.isBreedingTrackerEnabled();
            displayModeButton.active = on;
            iconColorButton.active   = on;
            animationToggle.active   = on;
            AxolotlUtilsStorage.save();
        });
        list.addSplitCard(
            CardSpec.image(() -> AxolotlUtilsConfig.isBreedingTrackerEnabled() ? IMG_BREEDING_ON  : IMG_BREEDING_OFF),
            breedingBtn,
            CardSpec.image(() -> AxolotlUtilsConfig.isFishBucketLockEnabled()  ? IMG_FISH_LOCK_ON : IMG_FISH_LOCK_OFF),
            buildHalfButton(this::getFishBucketLockLabel, () -> {
                AxolotlUtilsConfig.toggleFishBucketLock();
                AxolotlUtilsStorage.save();
            })
        );

        // ── Display mode (full-width) ─────────────────────────────────────────
        displayModeButton = buildWideButton(this::getDisplayModeLabel, () -> {
            AxolotlUtilsConfig.toggleShowFishTracker();
            AxolotlUtilsStorage.save();
        });
        displayModeButton.active = AxolotlUtilsConfig.isBreedingTrackerEnabled();
        list.addWide(displayModeButton);

        // ── Icon color | Animation ────────────────────────────────────────────
        iconColorButton = buildHalfButton(this::getIconColorLabel, () -> {
            AxolotlUtilsConfig.cycleHudIconColor();
            AxolotlUtilsStorage.save();
        });
        iconColorButton.active = AxolotlUtilsConfig.isBreedingTrackerEnabled();

        animationToggle = buildHalfButton(this::getAnimationLabel, () -> {
            AxolotlUtilsConfig.toggleHudAnimated();
            AxolotlUtilsStorage.save();
        });
        animationToggle.active = AxolotlUtilsConfig.isBreedingTrackerEnabled();
        list.addSplit(iconColorButton, animationToggle);

        // ── Reset bred | Reset fish ───────────────────────────────────────────
        list.addSplit(
            buildHalfButton(() -> Component.translatable("axolotlutils.screen.reset_bred"), () -> {
                BreedingTracker.reset();
                AxolotlUtilsStorage.save();
            }),
            buildHalfButton(() -> Component.translatable("axolotlutils.screen.reset_fish"), () -> {
                BreedingTracker.resetFishUsed();
                AxolotlUtilsStorage.save();
            })
        );

        // ── Volume slider (full-width) ────────────────────────────────────────
        list.addWide(new AbstractSliderButton(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT,
                getVolumeLabel(AxolotlUtilsConfig.getAxolotlVolume()),
                AxolotlUtilsConfig.getAxolotlVolume()) {
            @Override protected void updateMessage() { setMessage(getVolumeLabel((float) this.value)); }
            @Override protected void applyValue()    { AxolotlUtilsConfig.setAxolotlVolume((float) this.value); AxolotlUtilsStorage.save(); }
        });
    }

    // ── Utilities ──────────────────────────────────────────────────────────────

    private static Identifier id(String path) {
        return Identifier.parse("axolotlutils:textures/gui/preview/" + path);
    }

    private String stateText(boolean on) {
        return Component.translatable(on ? "axolotlutils.state.on" : "axolotlutils.state.off").getString();
    }

    // ── Label suppliers ────────────────────────────────────────────────────────

    private Component getHighlightLabel() {
        return Component.translatable("axolotlutils.screen.highlight_blue.card",
                stateText(AxolotlUtilsConfig.isHighlightBlueEnabled()));
    }

    private Component getColoredBucketsLabel() {
        return Component.translatable("axolotlutils.screen.colored_buckets.card",
                stateText(AxolotlUtilsConfig.isColoredBucketsEnabled()));
    }

    private Component getFishBucketLockLabel() {
        return Component.translatable("axolotlutils.screen.fish_bucket_lock.card",
                stateText(AxolotlUtilsConfig.isFishBucketLockEnabled()));
    }

    private Component getBreedingLabel() {
        return Component.translatable("axolotlutils.screen.breeding_tracker.card",
                stateText(AxolotlUtilsConfig.isBreedingTrackerEnabled()));
    }

    private Component getIconColorLabel() {
        return Component.translatable("axolotlutils.screen.icon_color",
                AxolotlUtilsConfig.getHudIconColor().getDisplayName());
    }

    private Component getAnimationLabel() {
        return Component.translatable("axolotlutils.screen.animated",
                stateText(AxolotlUtilsConfig.isHudAnimated()));
    }

    private Component getDisplayModeLabel() {
        String mode = Component.translatable(AxolotlUtilsConfig.isShowFishTracker()
                ? "axolotlutils.screen.display.fish_used"
                : "axolotlutils.screen.display.bred_count").getString();
        return Component.translatable("axolotlutils.screen.display_mode", mode);
    }

    private Component getVolumeLabel(float volume) {
        return Component.translatable("axolotlutils.screen.sound_volume",
                Math.round(volume * 100) + "%");
    }
}
