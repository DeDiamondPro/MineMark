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

package dev.dediamondpro.minemark.providers;

import java.util.function.Consumer;

public interface ImageProvider<I> {

    /**
     * Function to get an image, image is returned with callbacks
     *
     * @param src               The source URL of the image
     * @param dimensionCallback Callback for when the dimensions of the image are received
     * @param imageCallback     Callback for when the whole image is received
     */
    void getImage(String src, Consumer<Dimension> dimensionCallback, Consumer<I> imageCallback);

    class Dimension {
        private final float width;
        private final float height;

        public Dimension(float width, float height) {
            this.width = width;
            this.height = height;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }
    }
}
