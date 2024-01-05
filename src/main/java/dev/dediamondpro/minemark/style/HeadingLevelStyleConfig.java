package dev.dediamondpro.minemark.style;


import java.awt.*;

public class HeadingLevelStyleConfig {
    private final float fontSize;
    private final float padding;
    private final boolean hasDivider;
    private final Color dividerColor;
    private final float dividerHeight;
    private final float spaceBeforeDivider;

    public HeadingLevelStyleConfig(float fontSize, float padding, boolean hasDivider, Color dividerColor, float dividerHeight, float spaceBeforeDivider) {
        this.fontSize = fontSize;
        this.padding = padding;
        this.hasDivider = hasDivider;
        this.dividerColor = dividerColor;
        this.dividerHeight = dividerHeight;
        this.spaceBeforeDivider = spaceBeforeDivider;
    }

    public HeadingLevelStyleConfig(float fontSize, float padding) {
        this(fontSize, padding, false, null, 0f, 0f);
    }

    public float getFontSize() {
        return fontSize;
    }

    public float getPadding() {
        return padding;
    }

    public boolean hasDivider() {
        return hasDivider;
    }

    public Color getDividerColor() {
        return dividerColor;
    }

    public float getDividerHeight() {
        return dividerHeight;
    }

    public float getSpaceBeforeDivider() {
        return spaceBeforeDivider;
    }
}
