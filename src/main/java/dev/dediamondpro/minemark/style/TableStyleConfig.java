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
