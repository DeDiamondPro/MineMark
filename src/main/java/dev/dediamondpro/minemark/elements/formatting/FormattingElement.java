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

package dev.dediamondpro.minemark.elements.formatting;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

/**
 * An element that applies a specific format, multiple formatting elements can be applied to a single HTML tag
 */
public interface FormattingElement<S extends Style, R> {

    /**
     * Apply the style of this formatting element to the current layoutStyle.
     *
     * @param style        The style of the element
     * @param layoutStyle  The layout style of the element
     * @param parent       The parent element
     * @param qName        The name of the HTML tag
     * @param attributes   The attributes of the HTML tag
     */
    void applyStyle(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes);

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

    /**
     * Check if the element can be inline, if not the element associated with the HTML tag can never be inline
     *
     * @param style        The style of the element
     * @param layoutStyle  The layout style of the element
     * @param parent       The parent element
     * @param qName        The name of the HTML tag
     * @param attributes   The attributes of the HTML tag
     * @return True if the element applies to the given parameters
     */
    default boolean canBeInline(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        return true;
    }
}
