package dev.dediamondpro.minemark.style;

import java.awt.*;

public class CodeBlockStyleConfig {
    private final float inlinePadding;
    private final float blockOutsidePadding;
    private final float blockInsidePadding;
    private final Color color;

    public CodeBlockStyleConfig(float inlinePadding, float blockOutsidePadding, float blockInsidePadding, Color color) {
        this.inlinePadding = inlinePadding;
        this.blockOutsidePadding = blockOutsidePadding;
        this.blockInsidePadding = blockInsidePadding;
        this.color = color;
    }

    public float getInlinePadding() {
        return inlinePadding;
    }

    public float getBlockOutsidePadding() {
        return blockOutsidePadding;
    }

    public float getBlockInsidePadding() {
        return blockInsidePadding;
    }

    public Color getColor() {
        return color;
    }
}
