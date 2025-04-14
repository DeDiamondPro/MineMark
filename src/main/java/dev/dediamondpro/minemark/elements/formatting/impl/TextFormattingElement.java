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

package dev.dediamondpro.minemark.elements.formatting.impl;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Elements;
import dev.dediamondpro.minemark.elements.formatting.TagBasedFormattingElement;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class TextFormattingElement<S extends Style, R> extends TagBasedFormattingElement<S, R> {
    public TextFormattingElement() {
        super(Elements.TEXT_FORMATTING);
    }

    @Override
    public void applyStyle(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        switch (qName) {
            case "strong":
            case "b":
                layoutStyle.set(LayoutStyle.BOLD, true);
                break;
            case "em":
            case "i":
                layoutStyle.set(LayoutStyle.ITALIC, true);
                break;
            case "ins":
            case "u":
                layoutStyle.set(LayoutStyle.UNDERLINED, true);
                break;
            case "del":
            case "s":
                layoutStyle.set(LayoutStyle.STRIKETHROUGH, true);
                break;
            case "pre":
                layoutStyle.set(LayoutStyle.PRE_FORMATTED, true);
                break;
        }
    }
}
