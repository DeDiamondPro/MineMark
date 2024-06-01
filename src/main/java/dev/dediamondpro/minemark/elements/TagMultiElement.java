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

package dev.dediamondpro.minemark.elements;


import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.loaders.ElementLoader;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.ArrayList;

/**
 * An element that is used to apply multiple elements to the same HTML tag.
 */
public class TagMultiElement<S extends Style, R> extends Element<S, R> {
    private Element<S, R> deepestChildElement = this;

    public TagMultiElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    public void generateLayout(LayoutData layoutData, R renderData) {
        for (Element<S, R> child : children) {
            child.generateLayout(layoutData, renderData);
        }
    }

    @Override
    public @NotNull ArrayList<Element<S, R>> getChildren() {
        return deepestChildElement.children;
    }

    public void addElement(@NotNull ElementLoader<S, R> elementLoader, S style, LayoutStyle layoutStyle, @NotNull String qName, @Nullable Attributes attributes) {
        deepestChildElement = elementLoader.createElement(style, layoutStyle, deepestChildElement, qName, attributes);
    }
}
