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

import java.util.List;

public class TagBasedElementCreator<S extends Style, R> implements ElementCreator<S, R> {
    private final BasicElementCreator<S, R> elementCreator;
    private final List<String> tags;

    public TagBasedElementCreator(List<String> tags, BasicElementCreator<S, R> elementCreator) {
        this.elementCreator = elementCreator;
        this.tags = tags;
    }

    @Override
    public Element<S, R> createElement(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        return elementCreator.createElement(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    public boolean appliesTo(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        return tags.contains(qName);
    }

    public interface BasicElementCreator<S extends Style, R> {
        Element<S, R> createElement(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes);
    }
}
