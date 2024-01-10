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

public class HeadingLevelStyleConfig {
    private final float fontSize;
    private final float padding;
    private final boolean hasDivider;
    private final Color dividerColor;
    private final float dividerHeight;
    private final float spaceBeforeDivider;

    public HeadingLevelStyleConfig(float fontSize, float padding, boolean hasDivider, Color dividerColor, float dividerHeight, float spaceBeforeDivider) {
        this.fontSize = fontSize;
        this.padding = padding;
        this.hasDivider = hasDivider;
        this.dividerColor = dividerColor;
        this.dividerHeight = dividerHeight;
        this.spaceBeforeDivider = spaceBeforeDivider;
    }

    public HeadingLevelStyleConfig(float fontSize, float padding) {
        this(fontSize, padding, false, null, 0f, 0f);
    }

    public float getFontSize() {
        return fontSize;
    }

    public float getPadding() {
        return padding;
    }

    public boolean hasDivider() {
        return hasDivider;
    }

    public Color getDividerColor() {
        return dividerColor;
    }

    public float getDividerHeight() {
        return dividerHeight;
    }

    public float getSpaceBeforeDivider() {
        return spaceBeforeDivider;
    }
}
