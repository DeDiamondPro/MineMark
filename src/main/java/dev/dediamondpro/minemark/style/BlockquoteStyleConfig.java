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
