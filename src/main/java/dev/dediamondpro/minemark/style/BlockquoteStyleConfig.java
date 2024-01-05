package dev.dediamondpro.minemark.style;

import java.awt.*;

public class BlockquoteStyleConfig {
    private final float padding;
    private final float spacingLeft;
    private final float blockWidth;
    private final float spacingRight;
    private final Color blockColor;

    public BlockquoteStyleConfig(float padding, float spacingLeft, float blockWidth, float spacingRight, Color blockColor) {
        this.padding = padding;
        this.spacingLeft = spacingLeft;
        this.blockWidth = blockWidth;
        this.spacingRight = spacingRight;
        this.blockColor = blockColor;
    }

    public float getPadding() {
        return padding;
    }

    public float getSpacingLeft() {
        return spacingLeft;
    }

    public float getBlockWidth() {
        return blockWidth;
    }

    public float getSpacingRight() {
        return spacingRight;
    }

    public Color getBlockColor() {
        return blockColor;
    }
}
