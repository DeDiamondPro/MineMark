package dev.dediamondpro.minemark.style;

import java.awt.*;

public class TextStyleConfig {
    private final float defaultFontSize;
    private final Color defaultTextColor;
    private final float padding;

    public TextStyleConfig(float defaultFontSize, Color defaultTextColor, float padding) {
        this.defaultFontSize = defaultFontSize;
        this.defaultTextColor = defaultTextColor;
        this.padding = padding;
    }

    public float getDefaultFontSize() {
        return defaultFontSize;
    }

    public Color getDefaultTextColor() {
        return defaultTextColor;
    }

    public float getPadding() {
        return padding;
    }
}
