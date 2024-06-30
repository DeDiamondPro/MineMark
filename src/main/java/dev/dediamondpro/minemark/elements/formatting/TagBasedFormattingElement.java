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
import dev.dediamondpro.minemark.elements.Elements;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.Attributes;

import java.util.Collections;
import java.util.List;

/**
 * Utility class for formatting elements that apply based on solely the HTML tag
 */
public abstract class TagBasedFormattingElement<S extends Style, R> implements FormattingElement<S, R> {
    protected final List<String> tags;

    public TagBasedFormattingElement(List<String> tags) {
        this.tags = tags;
    }

    public TagBasedFormattingElement(Elements tag) {
        this(tag.tags);
    }

    public TagBasedFormattingElement(String tag) {
        this(Collections.singletonList(tag));
    }

    @Override
    public boolean appliesTo(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        return tags.contains(qName);
    }
}
