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

    public AxolotlUtilsScreen() {
        super(Component.literal("Axolotl Utils"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 50;
        int buttonWidth = 240;
        int buttonHeight = 20;
        int sectionSpacing = 44;

        // Feature 1: Easy Find Blue Axolotls
        highlightToggle = Button.builder(
                Component.literal(getHighlightLabel()),
                button -> {
                    AxolotlUtilsConfig.toggleHighlightBlue();
                    highlightToggle.setMessage(Component.literal(getHighlightLabel()));
                    AxolotlUtilsStorage.save();
                }
        ).bounds(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(highlightToggle);

        // Feature 2: Colored Axolotl Buckets
        coloredBucketsToggle = Button.builder(
                Component.literal(getColoredBucketsLabel()),
                button -> {
                    AxolotlUtilsConfig.toggleColoredBuckets();
                    coloredBucketsToggle.setMessage(Component.literal(getColoredBucketsLabel()));
                    AxolotlUtilsStorage.save();
                }
        ).bounds(centerX - buttonWidth / 2, startY + sectionSpacing, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(coloredBucketsToggle);

        // Feature 3: Breeding Tracker
        breedingToggle = Button.builder(
                Component.literal(getBreedingLabel()),
                button -> {
                    AxolotlUtilsConfig.toggleBreedingTracker();
                    breedingToggle.setMessage(Component.literal(getBreedingLabel()));
                    boolean enabled = AxolotlUtilsConfig.isBreedingTrackerEnabled();
                    iconColorButton.active = enabled;
                    animationToggle.active = enabled;
                    AxolotlUtilsStorage.save();
                }
        ).bounds(centerX - buttonWidth / 2, startY + sectionSpacing * 2, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(breedingToggle);

        // Reset breeding counter
        int resetY = startY + sectionSpacing * 2 + 46;
        this.addRenderableWidget(Button.builder(
                Component.literal("Reset Counter"),
                button -> {
                    BreedingTracker.reset();
                    AxolotlUtilsStorage.save();
                }
        ).bounds(centerX - 50, resetY, 100, buttonHeight).build());

        // Icon color cycle button
        int subButtonWidth = 160;
        int iconColorY = resetY + 24;
        iconColorButton = Button.builder(
                Component.literal(getIconColorLabel()),
                button -> {
                    AxolotlUtilsConfig.cycleHudIconColor();
                    iconColorButton.setMessage(Component.literal(getIconColorLabel()));
                    AxolotlUtilsStorage.save();
                }
        ).bounds(centerX - subButtonWidth / 2, iconColorY, subButtonWidth, buttonHeight).build();
        iconColorButton.active = AxolotlUtilsConfig.isBreedingTrackerEnabled();
        this.addRenderableWidget(iconColorButton);

        // Animation toggle button
        int animToggleY = iconColorY + 24;
        animationToggle = Button.builder(
                Component.literal(getAnimationLabel()),
                button -> {
                    AxolotlUtilsConfig.toggleHudAnimated();
                    animationToggle.setMessage(Component.literal(getAnimationLabel()));
                    AxolotlUtilsStorage.save();
                }
        ).bounds(centerX - subButtonWidth / 2, animToggleY, subButtonWidth, buttonHeight).build();
        animationToggle.active = AxolotlUtilsConfig.isBreedingTrackerEnabled();
        this.addRenderableWidget(animationToggle);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.fillGradient(0, 0, this.width, this.height, 0xC0101010, 0xD0101010);
        super.render(graphics, mouseX, mouseY, delta);

        int centerX = this.width / 2;
        int startY = 50;
        int sectionSpacing = 44;

        graphics.drawCenteredString(this.font, this.title, centerX, 20, 0xFFFFFF);

        // Section labels
        graphics.drawCenteredString(this.font, "Easy Find Blue Axolotls",
                centerX, startY - 12, 0xA0A0A0);

        graphics.drawCenteredString(this.font, "Colored Axolotl Buckets",
                centerX, startY + sectionSpacing - 12, 0xA0A0A0);

        graphics.drawCenteredString(this.font, "Breeding Tracker",
                centerX, startY + sectionSpacing * 2 - 12, 0xA0A0A0);

        // Breeding count display
        String countText = "Axolotls bred: " + BreedingTracker.getCount();
        graphics.drawCenteredString(this.font, countText,
                centerX, startY + sectionSpacing * 2 + 26, 0x55FF55);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private String getHighlightLabel() {
        return "Highlight Blue: " + (AxolotlUtilsConfig.isHighlightBlueEnabled() ? "ON" : "OFF");
    }

    private String getColoredBucketsLabel() {
        return "Colored Buckets: " + (AxolotlUtilsConfig.isColoredBucketsEnabled() ? "ON" : "OFF");
    }

    private String getBreedingLabel() {
        return "Breeding Tracker: " + (AxolotlUtilsConfig.isBreedingTrackerEnabled() ? "ON" : "OFF");
    }

    private String getIconColorLabel() {
        return "Icon Color: " + AxolotlUtilsConfig.getHudIconColor().getDisplayName();
    }

    private String getAnimationLabel() {
        return "Animated Icon: " + (AxolotlUtilsConfig.isHudAnimated() ? "ON" : "OFF");
    }
}
