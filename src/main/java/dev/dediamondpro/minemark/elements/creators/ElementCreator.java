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

package dev.dediamondpro.minemark.elements.creators;


import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.Attributes;

public interface ElementCreator<S extends Style, R> {
    /**
     * Create an element from the given parameters
     *
     * @param style        The style of the element
     * @param layoutStyle  The layout style of the element
     * @param parent       The parent element
     * @param qName        The name of the HTML tag
     * @param attributes   The attributes of the HTML tag
     * @return The created element
     */
    Element<S, R> createElement(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes);


    /**
     * Check if the element applies to the given parameters and can be created with them
     *
     * @param style        The style of the element
     * @param layoutStyle  The layout style of the element
     * @param parent       The parent element
     * @param qName        The name of the HTML tag
     * @param attributes   The attributes of the HTML tag
     * @return True if the element applies to the given parameters
     */
     boolean appliesTo(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes);
}
