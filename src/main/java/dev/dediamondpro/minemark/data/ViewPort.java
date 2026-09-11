/*
 * This file is part of MineMark
 * Copyright (C) 2026 DeDiamondPro
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

package dev.dediamondpro.minemark.data;

public class ViewPort {
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    /**
     * Create a new view port object
     *
     * @param x      The top left x of the view port
     * @param y      The top left y of the view port
     * @param width  The width of the view port
     * @param height The height of the view port
     */
    public ViewPort(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getLeftX() {
        return x;
    }

    public float getTopY() {
        return y;
    }

    public float getRightX() {
        return x + width;
    }

    public float getBottomY() {
        return y + height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isInViewPort(float leftX, float topY, float rightX, float bottomY) {
        return !(leftX > getRightX() || rightX < getLeftX()
                || topY > getBottomY() || bottomY < getTopY());
    }

    public boolean isInViewPortVertical(float topY, float bottomY) {
        return !(topY > getBottomY() || bottomY < getTopY());
    }
}
