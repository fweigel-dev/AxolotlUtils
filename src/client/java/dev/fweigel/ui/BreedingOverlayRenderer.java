package dev.fweigel.ui;

import dev.fweigel.AxolotlColor;
import dev.fweigel.AxolotlUtilsConfig;
import dev.fweigel.BreedingTracker;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class BreedingOverlayRenderer {
    private static final Identifier TOAST_BACKGROUND =
            Identifier.fromNamespaceAndPath("minecraft", "toast/advancement");
    private static final int TOAST_WIDTH = 160;
    private static final int TOAST_HEIGHT = 32;
    private static final int ICON_SIZE = 16;
    private static final int ICON_X = 8;
    private static final int ICON_Y = 8;
    private static final int TEXT_X = 30;
    private static final int FRAME_MS = 50;

    private BreedingOverlayRenderer() {
    }

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!AxolotlUtilsConfig.isBreedingTrackerEnabled()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.getWindow() == null) {
            return;
        }

        if (minecraft.options.hideGui) {
            return;
        }

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int toastX = screenWidth - TOAST_WIDTH;
        int toastY = 0;

        // Render toast background
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TOAST_BACKGROUND,
                toastX, toastY, TOAST_WIDTH, TOAST_HEIGHT);

        // Render axolotl icon at the item slot position (8,8), scaled to 16x16
        AxolotlColor color = AxolotlUtilsConfig.getHudIconColor();
        boolean animated = AxolotlUtilsConfig.isHudAnimated();
        int iconScreenX = toastX + ICON_X;
        int iconScreenY = toastY + ICON_Y;

        if (animated) {
            int frameSize = color.getAnimFrameSize();
            int frameCount = color.getFrameCount();
            int currentFrame = (int) ((System.currentTimeMillis() / FRAME_MS) % frameCount);
            int sheetHeight = frameSize * frameCount;
            float scale = (float) ICON_SIZE / frameSize;

            graphics.pose().pushMatrix();
            graphics.pose().translate(iconScreenX, iconScreenY);
            graphics.pose().scale(scale, scale);
            graphics.blit(RenderPipelines.GUI_TEXTURED, color.getAnimatedTexture(),
                    0, 0, 0, currentFrame * frameSize,
                    frameSize, frameSize, frameSize, sheetHeight);
            graphics.pose().popMatrix();
        } else {
            int texSize = color.getStaticSize();
            float scale = (float) ICON_SIZE / texSize;

            graphics.pose().pushMatrix();
            graphics.pose().translate(iconScreenX, iconScreenY);
            graphics.pose().scale(scale, scale);
            graphics.blit(RenderPipelines.GUI_TEXTURED, color.getStaticTexture(),
                    0, 0, 0, 0,
                    texSize, texSize, texSize, texSize);
            graphics.pose().popMatrix();
        }

        // Render text lines matching advancement toast layout
        int textScreenX = toastX + TEXT_X;
        graphics.drawString(minecraft.font, "Axolotls bred",
                textScreenX, toastY + 7, 0xFFFFFF00, false);
        graphics.drawString(minecraft.font, String.valueOf(BreedingTracker.getCount()),
                textScreenX, toastY + 18, 0xFFFFFFFF, false);
    }
}
