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

public class CodeBlockStyleConfig {
    private final float inlinePaddingLeftRight;
    private final float inlinePaddingTopBottom;
    private final float blockOutsidePadding;
    private final float blockInsidePadding;
    private final Color color;

    public CodeBlockStyleConfig(float inlinePaddingLeftRight, float inlinePaddingTopBottom, float blockOutsidePadding, float blockInsidePadding, Color color) {
        this.inlinePaddingLeftRight = inlinePaddingLeftRight;
        this.inlinePaddingTopBottom = inlinePaddingTopBottom;
        this.blockOutsidePadding = blockOutsidePadding;
        this.blockInsidePadding = blockInsidePadding;
        this.color = color;
    }

    public float getInlinePaddingLeftRight() {
        return inlinePaddingLeftRight;
    }

    public float getInlinePaddingTopBottom() {
        return inlinePaddingTopBottom;
    }

    public float getBlockOutsidePadding() {
        return blockOutsidePadding;
    }

    public float getBlockInsidePadding() {
        return blockInsidePadding;
    }

    public Color getColor() {
        return color;
    }
}
