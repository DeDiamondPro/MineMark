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

public class HeadingStyleConfig {
    private final HeadingLevelStyleConfig h1;
    private final HeadingLevelStyleConfig h2;
    private final HeadingLevelStyleConfig h3;
    private final HeadingLevelStyleConfig h4;
    private final HeadingLevelStyleConfig h5;
    private final HeadingLevelStyleConfig h6;

    public HeadingStyleConfig(HeadingLevelStyleConfig h1, HeadingLevelStyleConfig h2, HeadingLevelStyleConfig h3, HeadingLevelStyleConfig h4, HeadingLevelStyleConfig h5, HeadingLevelStyleConfig h6) {
        this.h1 = h1;
        this.h2 = h2;
        this.h3 = h3;
        this.h4 = h4;
        this.h5 = h5;
        this.h6 = h6;
    }

    public HeadingLevelStyleConfig getH1() {
        return h1;
    }

    public HeadingLevelStyleConfig getH2() {
        return h2;
    }

    public HeadingLevelStyleConfig getH3() {
        return h3;
    }

    public HeadingLevelStyleConfig getH4() {
        return h4;
    }

    public HeadingLevelStyleConfig getH5() {
        return h5;
    }

    public HeadingLevelStyleConfig getH6() {
        return h6;
    }
}
