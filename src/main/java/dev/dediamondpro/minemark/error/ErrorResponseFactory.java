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

package dev.dediamondpro.minemark.error;

import dev.dediamondpro.minemark.elements.MineMarkElement;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ErrorResponseFactory<S extends Style, R> {
    enum Phase {
        PARSING,
        LAYOUT,
        DRAW,
        BEFORE_DRAW,
        UNKNOWN
    }

    @Nullable MineMarkElement<S, R> build(@NotNull S style, @NotNull Phase phase, @NotNull Throwable exception);
}

