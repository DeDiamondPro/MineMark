package dev.dediamondpro.minemark.style;

import java.awt.*;

public class TableStyleConfig {
    private final float insidePadding;
    private final float borderThickness;
    private final Color borderColor;
    private final Color evenFillColor;
    private final Color oddFillColor;

    public TableStyleConfig(float insidePadding, float borderThickness, Color borderColor, Color evenFillColor, Color oddFillColor) {
        this.insidePadding = insidePadding;
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
        this.evenFillColor = evenFillColor;
        this.oddFillColor = oddFillColor;
    }

    public float getInsidePadding() {
        return insidePadding;
    }

    public float getBorderThickness() {
        return borderThickness;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getEvenFillColor() {
        return evenFillColor;
    }

    public Color getOddFillColor() {
        return oddFillColor;
    }
}
