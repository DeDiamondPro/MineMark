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
import dev.dediamondpro.minemark.utils.StyleEntry;

import java.awt.*;
import java.util.HashMap;

/**
 * Class that will be given to an element at parsing time
 * Default variables used to control the layout
 */
public class LayoutStyle {
    private final HashMap<StyleEntry<?>, Object> styles;

    public LayoutStyle(HashMap<StyleEntry<?>, Object> styles) {
        this.styles = styles;
    }

    public LayoutStyle(Style style) {
        this(new HashMap<>());
        put(FONT_SIZE, style.getTextStyle().getDefaultFontSize());
        put(TEXT_COLOR, style.getTextStyle().getDefaultTextColor());
    }

    public LayoutStyle clone() {
        return new LayoutStyle((HashMap<StyleEntry<?>, Object>) styles.clone());
    }

    /**
     * Add a style entry
     *
     * @param styleEntry The entry of which a value will be added
     * @param value      The value to be added
     */
    public <T> void put(StyleEntry<T> styleEntry, T value) {
        styles.put(styleEntry, value);
    }

    /**
     * Add a style entry (alias of put)
     *
     * @param styleEntry The entry of which a value will be added
     * @param value      The value to be added
     */
    public <T> void set(StyleEntry<T> styleEntry, T value) {
        styles.put(styleEntry, value);
    }


    /**
     * Remove a style entry
     *
     * @param styleEntry The entry to be removed
     */
    public <T> void remove(StyleEntry<T> styleEntry) {
        styles.remove(styleEntry);
    }

    /**
     * Check if this LayoutStyle contains an entry for this styleEntry
     * <br><b>NOTE</b>: This method does not check if styleEntry has a default value registered, so the get function
     * might style return a value even tho this method returns false, to check if get will return null or not,
     * use hasOrDefault.
     *
     * @param styleEntry The styleEntry to check
     * @return True if present, else false
     */
    public <T> boolean has(StyleEntry<T> styleEntry) {
        return styles.containsKey(styleEntry);
    }


    /**
     * Check if this LayoutStyle contains an entry for this styleEntry, or the entry has a default value registered
     *
     * @param styleEntry The styleEntry to check
     * @return True if present or a default is registered, else false
     */
    public <T> boolean hasOrDefault(StyleEntry<T> styleEntry) {
        return has(styleEntry) || styleEntry.getDefaultValue() != null;
    }

    /**
     * Get the value from an entry, or the default value of this entry if there is a default registered
     *
     * @param styleEntry The entry to get
     * @return The value of the entry, or the default value if there is one registered, else null.
     */
    public <T> T get(StyleEntry<T> styleEntry) {
        return getOrDefault(styleEntry, styleEntry.getDefaultValue());
    }

    /**
     * Get the value from an entry, or if this layoutStyle does not contain the entry, return a default.
     * The default provided in this method takes priority over the default registered in styleEntry.
     *
     * @param styleEntry   The entry to get
     * @param defaultValue The default value if there is no value present for the entry
     * @return The value of the entry, or defaultValue
     */
    public <T> T getOrDefault(StyleEntry<T> styleEntry, T defaultValue) {
        return styleEntry.getStyleClass().cast(styles.getOrDefault(styleEntry, defaultValue));
    }

    // Default style entries used by MineMark's default elements
    public static final StyleEntry<Alignment> ALIGNMENT = new StyleEntry<>(Alignment.class, Alignment.LEFT);
    public static final StyleEntry<Float> FONT_SIZE = new StyleEntry<>(Float.class);
    public static final StyleEntry<Color> TEXT_COLOR = new StyleEntry<>(Color.class);
    public static final StyleEntry<Boolean> BOLD = new StyleEntry<>(Boolean.class, false);
    public static final StyleEntry<Boolean> ITALIC = new StyleEntry<>(Boolean.class, false);
    public static final StyleEntry<Boolean> UNDERLINED = new StyleEntry<>(Boolean.class, false);
    public static final StyleEntry<Boolean> STRIKETHROUGH = new StyleEntry<>(Boolean.class, false);
    public static final StyleEntry<Boolean> PART_OF_LINK = new StyleEntry<>(Boolean.class, false);
    public static final StyleEntry<Boolean> PRE_FORMATTED = new StyleEntry<>(Boolean.class, false);
    public static final StyleEntry<Boolean> PART_OF_CODE_BLOCK = new StyleEntry<>(Boolean.class, false);

    public enum Alignment {
        CENTER, LEFT, RIGHT
    }

    @Deprecated
    public Alignment getAlignment() {
        return get(ALIGNMENT);
    }

    @Deprecated
    public void setAlignment(Alignment alignment) {
        set(LayoutStyle.ALIGNMENT, alignment);
    }

    @Deprecated
    public float getFontSize() {
        return get(FONT_SIZE);
    }

    @Deprecated
    public void setFontSize(float fontSize) {
        set(LayoutStyle.FONT_SIZE, fontSize);
    }

    @Deprecated
    public Color getTextColor() {
        return get(TEXT_COLOR);
    }

    @Deprecated
    public void setTextColor(Color textColor) {
        set(LayoutStyle.TEXT_COLOR, textColor);
    }

    @Deprecated
    public boolean isBold() {
        return get(BOLD);
    }

    @Deprecated
    public void setBold(boolean bold) {
        set(LayoutStyle.BOLD, bold);
    }

    @Deprecated
    public boolean isItalic() {
        return get(ITALIC);
    }

    @Deprecated
    public void setItalic(boolean italic) {
        set(LayoutStyle.ITALIC, italic);
    }

    @Deprecated
    public boolean isUnderlined() {
        return get(UNDERLINED);
    }

    @Deprecated
    public void setUnderlined(boolean underlined) {
        set(LayoutStyle.UNDERLINED, underlined);
    }

    @Deprecated
    public boolean isStrikethrough() {
        return get(STRIKETHROUGH);
    }

    @Deprecated
    public void setStrikethrough(boolean strikethrough) {
        set(LayoutStyle.STRIKETHROUGH, strikethrough);
    }

    @Deprecated
    public boolean isPartOfLink() {
        return get(PART_OF_LINK);
    }

    @Deprecated
    public void setPartOfLink(boolean partOfLink) {
        set(LayoutStyle.PART_OF_LINK, partOfLink);
    }

    @Deprecated
    public boolean isPreFormatted() {
        return get(PRE_FORMATTED);
    }

    @Deprecated
    public void setPreFormatted(boolean preFormatted) {
        set(LayoutStyle.PRE_FORMATTED, preFormatted);
    }

    @Deprecated
    public boolean isPartOfCodeBlock() {
        return get(PART_OF_CODE_BLOCK);
    }

    @Deprecated
    public void setPartOfCodeBlock(boolean partOfCodeBlock) {
        set(LayoutStyle.PART_OF_CODE_BLOCK, partOfCodeBlock);
    }
}
