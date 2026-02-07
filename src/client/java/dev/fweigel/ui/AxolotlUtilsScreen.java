package dev.fweigel.ui;

import dev.fweigel.AxolotlUtilsConfig;
import dev.fweigel.AxolotlUtilsStorage;
import dev.fweigel.BreedingTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AxolotlUtilsScreen extends Screen {
    private Button highlightToggle;
    private Button coloredBucketsToggle;
    private Button breedingToggle;
    private Button iconColorButton;
    private Button animationToggle;

    private static final int BUTTON_WIDTH = 200;
    private static final int HALF_BUTTON_WIDTH = 96;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_GAP = 24;

    private final Component featuresHeader = Component.translatable("axolotlutils.screen.section.features");
    private final Component breedingHeader = Component.translatable("axolotlutils.screen.section.breeding");

    public AxolotlUtilsScreen() {
        super(Component.translatable("axolotlutils.screen.title"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;

        // --- Features section ---
        int featuresHeaderY = 36;
        int y = featuresHeaderY + 14;

        highlightToggle = addRenderableWidget(Button.builder(
                getHighlightLabel(),
                button -> {
                    AxolotlUtilsConfig.toggleHighlightBlue();
                    highlightToggle.setMessage(getHighlightLabel());
                    AxolotlUtilsStorage.save();
                }
        ).bounds(cx - BUTTON_WIDTH / 2, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_GAP;

        coloredBucketsToggle = addRenderableWidget(Button.builder(
                getColoredBucketsLabel(),
                button -> {
                    AxolotlUtilsConfig.toggleColoredBuckets();
                    coloredBucketsToggle.setMessage(getColoredBucketsLabel());
                    AxolotlUtilsStorage.save();
                }
        ).bounds(cx - BUTTON_WIDTH / 2, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        // --- Breeding Tracker section ---
        int breedingSectionY = y + BUTTON_GAP + 16;
        y = breedingSectionY + 14;

        breedingToggle = addRenderableWidget(Button.builder(
                getBreedingLabel(),
                button -> {
                    AxolotlUtilsConfig.toggleBreedingTracker();
                    breedingToggle.setMessage(getBreedingLabel());
                    boolean enabled = AxolotlUtilsConfig.isBreedingTrackerEnabled();
                    iconColorButton.active = enabled;
                    animationToggle.active = enabled;
                    AxolotlUtilsStorage.save();
                }
        ).bounds(cx - BUTTON_WIDTH / 2, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        y += BUTTON_GAP;

        // Icon color + animation side by side
        int gap = 8;
        int leftX = cx - HALF_BUTTON_WIDTH - gap / 2;
        int rightX = cx + gap / 2;

        iconColorButton = addRenderableWidget(Button.builder(
                getIconColorLabel(),
                button -> {
                    AxolotlUtilsConfig.cycleHudIconColor();
                    iconColorButton.setMessage(getIconColorLabel());
                    AxolotlUtilsStorage.save();
                }
        ).bounds(leftX, y, HALF_BUTTON_WIDTH, BUTTON_HEIGHT).build());
        iconColorButton.active = AxolotlUtilsConfig.isBreedingTrackerEnabled();

        animationToggle = addRenderableWidget(Button.builder(
                getAnimationLabel(),
                button -> {
                    AxolotlUtilsConfig.toggleHudAnimated();
                    animationToggle.setMessage(getAnimationLabel());
                    AxolotlUtilsStorage.save();
                }
        ).bounds(rightX, y, HALF_BUTTON_WIDTH, BUTTON_HEIGHT).build());
        animationToggle.active = AxolotlUtilsConfig.isBreedingTrackerEnabled();

        y += BUTTON_GAP;

        // Reset counter - smaller, centered
        addRenderableWidget(Button.builder(
                Component.translatable("axolotlutils.screen.reset_counter"),
                button -> {
                    BreedingTracker.reset();
                    AxolotlUtilsStorage.save();
                }
        ).bounds(cx - 50, y, 100, BUTTON_HEIGHT).build());

        // --- Done button at bottom ---
        addRenderableWidget(Button.builder(
                Component.translatable("gui.done"),
                button -> this.onClose()
        ).bounds(cx - BUTTON_WIDTH / 2, this.height - 28, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        int cx = this.width / 2;

        // Title
        graphics.drawCenteredString(this.font, this.title, cx, 15, 0xFFFFFFFF);

        // --- Features section header ---
        int featuresHeaderY = 36;
        graphics.drawCenteredString(this.font, this.featuresHeader, cx, featuresHeaderY, 0xFFFFFFFF);
        drawSeparator(graphics, cx, featuresHeaderY + 10);

        // --- Breeding Tracker section header ---
        int breedingSectionY = featuresHeaderY + 14 + BUTTON_GAP * 2 + 16;
        graphics.drawCenteredString(this.font, this.breedingHeader, cx, breedingSectionY, 0xFFFFFFFF);
        drawSeparator(graphics, cx, breedingSectionY + 10);

    }

    private void drawSeparator(GuiGraphics graphics, int cx, int y) {
        int halfWidth = 80;
        graphics.fill(cx - halfWidth, y, cx + halfWidth, y + 1, 0x40FFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private String stateText(boolean on) {
        return Component.translatable(on ? "axolotlutils.state.on" : "axolotlutils.state.off").getString();
    }

    private Component getHighlightLabel() {
        return Component.translatable("axolotlutils.screen.highlight_blue", stateText(AxolotlUtilsConfig.isHighlightBlueEnabled()));
    }

    private Component getColoredBucketsLabel() {
        return Component.translatable("axolotlutils.screen.colored_buckets", stateText(AxolotlUtilsConfig.isColoredBucketsEnabled()));
    }

    private Component getBreedingLabel() {
        return Component.translatable("axolotlutils.screen.breeding_tracker", stateText(AxolotlUtilsConfig.isBreedingTrackerEnabled()));
    }

    private Component getIconColorLabel() {
        return Component.translatable("axolotlutils.screen.icon_color", AxolotlUtilsConfig.getHudIconColor().getDisplayName());
    }

    private Component getAnimationLabel() {
        return Component.translatable("axolotlutils.screen.animated", stateText(AxolotlUtilsConfig.isHudAnimated()));
    }
}
