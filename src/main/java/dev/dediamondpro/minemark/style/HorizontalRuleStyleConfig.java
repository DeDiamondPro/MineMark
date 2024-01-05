package dev.dediamondpro.minemark.style;

import java.awt.*;

public class HorizontalRuleStyleConfig {
    private final float height;
    private final float padding;
    private final Color color;

    public HorizontalRuleStyleConfig(float height, float padding, Color color) {
        this.height = height;
        this.padding = padding;
        this.color = color;
    }

    public float getHeight() {
        return height;
    }

    public float getPadding() {
        return padding;
    }

    public Color getColor() {
        return color;
    }
}
