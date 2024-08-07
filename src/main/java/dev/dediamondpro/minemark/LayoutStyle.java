/*
 * This file is part of MineMark
 * Copyright (C) 2024 DeDiamondPro
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License Version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.dediamondpro.minemark;

import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.StyleType;

import java.awt.*;
import java.util.HashMap;

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
    private boolean partOfCodeBlock;
    private boolean preFormatted;
    private final HashMap<StyleType<?>, Object> customStyles;

    public LayoutStyle(Alignment alignment, float fontSize, Color textColor, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean partOfLink, boolean partOfCodeBlock, boolean preFormatted, HashMap<StyleType<?>, Object> customStyles) {
        this.alignment = alignment;
        this.fontSize = fontSize;
        this.textColor = textColor;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.partOfLink = partOfLink;
        this.preFormatted = preFormatted;
        this.partOfCodeBlock = partOfCodeBlock;
        this.strikethrough = strikethrough;
        this.customStyles = customStyles;
    }

    public LayoutStyle(Style style) {
        this(Alignment.LEFT, style.getTextStyle().getDefaultFontSize(), style.getTextStyle().getDefaultTextColor(), false, false, false, false, false, false, false, new HashMap<>());
    }

    public LayoutStyle clone() {
        return new LayoutStyle(alignment, fontSize, new Color(textColor.getRGB()), bold, italic, underlined, strikethrough, partOfLink, partOfCodeBlock, preFormatted, (HashMap<StyleType<?>, Object>) customStyles.clone());
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

    public boolean isPartOfCodeBlock() {
        return partOfCodeBlock;
    }

    public void setPartOfCodeBlock(boolean partOfCodeBlock) {
        this.partOfCodeBlock = partOfCodeBlock;
    }

    public <T> void put(StyleType<T> styleType, T value) {
        customStyles.put(styleType, value);
    }

    public <T> void remove(StyleType<T> styleType) {
        customStyles.remove(styleType);
    }

    public <T> boolean has(StyleType<T> styleType) {
        return customStyles.containsKey(styleType);
    }

    public <T> T get(StyleType<T> styleType) {
        return styleType.getStyleClass().cast(customStyles.get(styleType));
    }

    public <T> T getOrDefault(StyleType<T> styleType, T defaultValue) {
        return styleType.getStyleClass().cast(customStyles.getOrDefault(styleType, defaultValue));
    }

    public enum Alignment {
        CENTER, LEFT, RIGHT
    }
}
