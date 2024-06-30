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

public class StyleType<T> {
    private final String id;
    private final Class<T> styleClass;

    public StyleType(String id, Class<T> styleClass) {
        this.id = id;
        this.styleClass = styleClass;
    }

    public String getId() {
        return id;
    }

    public Class<T> getStyleClass() {
        return styleClass;
    }

    public static <T> StyleType<T> of(String id, Class<T> styleClass) {
        return new StyleType<T>(id, styleClass);
    }
}
