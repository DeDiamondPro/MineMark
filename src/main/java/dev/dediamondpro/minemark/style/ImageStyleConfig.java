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

import dev.dediamondpro.minemark.providers.ImageProvider;

public class ImageStyleConfig {
    private final ImageProvider<?> imageProvider;

    public ImageStyleConfig(ImageProvider<?> imageProvider) {
        this.imageProvider = imageProvider;
    }

    public ImageProvider<?> getImageProvider() {
        return imageProvider;
    }
}
