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

public class HorizontalRuleStyleConfig {
    private final float height;
    private final float padding;
    private final Color color;

    public HorizontalRuleStyleConfig(float height, float padding, Color color) {
        this.height = height;
        this.padding = padding;
        this.color = color;
    }

    public float getHeight() {
        return height;
    }

    public float getPadding() {
        return padding;
    }

    public Color getColor() {
        return color;
    }
}
