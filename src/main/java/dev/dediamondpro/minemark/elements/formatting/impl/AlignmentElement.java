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
import dev.dediamondpro.minemark.elements.formatting.FormattingElement;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class AlignmentElement<S extends Style, R> implements FormattingElement<S, R> {
    protected String alignment;

    @Override
    public void applyStyle(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        alignment = qName.equals("div") ? attributes.getValue("align") : qName;
        if (alignment == null) return;
        switch (alignment) {
            case "left":
                layoutStyle.setAlignment(LayoutStyle.Alignment.LEFT);
                break;
            case "center":
                layoutStyle.setAlignment(LayoutStyle.Alignment.CENTER);
                break;
            case "right":
                layoutStyle.setAlignment(LayoutStyle.Alignment.RIGHT);
                break;
        }
    }

    @Override
    public boolean canBeInline(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        return false;
    }

    @Override
    public boolean appliesTo(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        return qName.equals("center") || (qName.equals("div") && attributes.getValue("align") != null);
    }
}
