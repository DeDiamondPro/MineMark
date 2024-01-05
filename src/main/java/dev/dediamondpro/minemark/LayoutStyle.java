package dev.dediamondpro.minemark;

import dev.dediamondpro.minemark.style.Style;

import java.awt.*;

/**
 * Class that will be given to an element at parsing time
 * Default variables used to control the layout
 */
public class LayoutStyle {
    private Alignment alignment;
    private float fontSize;
    private Color textColor;
    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private boolean strikethrough;
    private boolean partOfLink;
    private boolean preFormatted;

    public LayoutStyle(Alignment alignment, float fontSize, Color textColor, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean partOfLink, boolean preFormatted) {
        this.alignment = alignment;
        this.fontSize = fontSize;
        this.textColor = textColor;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.partOfLink = partOfLink;
        this.preFormatted = preFormatted;
        this.strikethrough = strikethrough;
    }

    public LayoutStyle(Style style) {
        this(Alignment.LEFT, style.getTextStyle().getDefaultFontSize(), Color.WHITE, false, false, false, false, false, false);
    }

    public LayoutStyle clone() {
        return new LayoutStyle(alignment, fontSize, new Color(textColor.getRGB()), bold, italic, underlined, strikethrough, partOfLink, preFormatted);
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public void setUnderlined(boolean underlined) {
        this.underlined = underlined;
    }

    public boolean isStrikethrough() {
        return strikethrough;
    }

    public void setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
    }

    public boolean isPartOfLink() {
        return partOfLink;
    }

    public void setPartOfLink(boolean partOfLink) {
        this.partOfLink = partOfLink;
    }

    public boolean isPreFormatted() {
        return preFormatted;
    }

    public void setPreFormatted(boolean preFormatted) {
        this.preFormatted = preFormatted;
    }


    public enum Alignment {
        CENTER, LEFT, RIGHT
    }
}
