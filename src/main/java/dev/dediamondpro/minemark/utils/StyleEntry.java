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

package dev.dediamondpro.minemark.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StyleEntry<T> {
    private final @NotNull Class<T> styleClass;
    private final @Nullable T defaultValue;

    /**
     * Create a new styleEntry, used to access a specific style config parameter
     *
     * @param styleClass The type the entry should store
     * @param defaultValue The default value, or null if none
     */
    public StyleEntry(@NotNull Class<T> styleClass, @Nullable T defaultValue) {
        this.styleClass = styleClass;
        this.defaultValue = defaultValue;
    }

    /**
     * Create a new styleEntry, used to access a specific style config parameter
     *
     * @param styleClass The type the entry should store
     */
    public StyleEntry(@NotNull Class<T> styleClass) {
        this(styleClass, null);
    }

    public @NotNull Class<T> getStyleClass() {
        return styleClass;
    }

    public @Nullable T getDefaultValue() {
        return defaultValue;
    }
}
