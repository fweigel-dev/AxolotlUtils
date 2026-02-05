package dev.fweigel;

import net.minecraft.resources.Identifier;

public enum AxolotlColor {
    LUCY("Lucy", 763, 116, 141),
    BLUE("Blue", 763, 103, 159),
    CYAN("Cyan", 763, 103, 159),
    GOLD("Gold", 763, 103, 159),
    WILD("Wild", 535, 103, 159);

    private final String displayName;
    private final Identifier staticTexture;
    private final Identifier animatedTexture;
    private final int staticSize;
    private final int frameCount;
    private final int animFrameSize;

    AxolotlColor(String displayName, int staticSize, int frameCount, int animFrameSize) {
        this.displayName = displayName;
        this.staticSize = staticSize;
        this.frameCount = frameCount;
        this.animFrameSize = animFrameSize;
        String key = name().toLowerCase();
        this.staticTexture = Identifier.fromNamespaceAndPath(
                AxolotlUtils.MOD_ID, "textures/gui/axolotl_" + key + ".png");
        this.animatedTexture = Identifier.fromNamespaceAndPath(
                AxolotlUtils.MOD_ID, "textures/gui/axolotl_" + key + "_anim.png");
    }

    public String getDisplayName() {
        return displayName;
    }

    public Identifier getStaticTexture() {
        return staticTexture;
    }

    public Identifier getAnimatedTexture() {
        return animatedTexture;
    }

    public int getStaticSize() {
        return staticSize;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getAnimFrameSize() {
        return animFrameSize;
    }

    public AxolotlColor next() {
        AxolotlColor[] values = values();
        return values[(ordinal() + 1) % values.length];
    }

    public static AxolotlColor fromName(String name) {
        if (name == null) {
            return LUCY;
        }
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return LUCY;
        }
    }
}
