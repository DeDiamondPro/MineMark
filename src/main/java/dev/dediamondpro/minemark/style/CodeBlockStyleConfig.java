package dev.dediamondpro.minemark.style;

import java.awt.*;

public class CodeBlockStyleConfig {
    private final float inlinePaddingLeftRight;
    private final float inlinePaddingTopBottom;
    private final float blockOutsidePadding;
    private final float blockInsidePadding;
    private final Color color;

    public CodeBlockStyleConfig(float inlinePaddingLeftRight, float inlinePaddingTopBottom, float blockOutsidePadding, float blockInsidePadding, Color color) {
        this.inlinePaddingLeftRight = inlinePaddingLeftRight;
        this.inlinePaddingTopBottom = inlinePaddingTopBottom;
        this.blockOutsidePadding = blockOutsidePadding;
        this.blockInsidePadding = blockInsidePadding;
        this.color = color;
    }

    public float getInlinePaddingLeftRight() {
        return inlinePaddingLeftRight;
    }

    public float getInlinePaddingTopBottom() {
        return inlinePaddingTopBottom;
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
