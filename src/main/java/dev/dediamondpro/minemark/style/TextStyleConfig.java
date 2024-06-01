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

package dev.dediamondpro.minemark.style;

import java.awt.*;
import java.util.function.Function;

public class TextStyleConfig {
    private final float defaultFontSize;
    private final Color defaultTextColor;
    private final float padding;
    /**
     * Function used to adapt font size from px to the unit the rendering implementation uses
     */
    private final Function<Float, Float> adaptFontSize;

    public TextStyleConfig(float defaultFontSize, Color defaultTextColor, float padding, Function<Float, Float> adaptFontSize) {
        this.defaultFontSize = defaultFontSize;
        this.defaultTextColor = defaultTextColor;
        this.padding = padding;
        this.adaptFontSize = adaptFontSize;
    }

    public TextStyleConfig(float defaultFontSize, Color defaultTextColor, float padding) {
        this(defaultFontSize, defaultTextColor, padding, (size) -> size);
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

    public float adaptFontSize(float size) {
        return adaptFontSize.apply(size);
    }
}
