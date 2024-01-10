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

package dev.dediamondpro.minemark.elements.impl.formatting;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class AlignmentElement<S extends Style, R> extends ChildBasedElement<S, R> {
    protected String alignment;

    public AlignmentElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        this.layoutStyle = this.layoutStyle.clone();
        alignment = qName.equals("div") ? (attributes != null ? attributes.getValue("align") : null) : qName;
        if (alignment == null) return;
        switch (alignment) {
            case "left":
                this.layoutStyle.setAlignment(LayoutStyle.Alignment.LEFT);
                break;
            case "center":
                this.layoutStyle.setAlignment(LayoutStyle.Alignment.CENTER);
                break;
            case "right":
                this.layoutStyle.setAlignment(LayoutStyle.Alignment.RIGHT);
                break;
        }
    }

    @Override
    public String toString() {
        return "AlignmentElement {" + qName + "}";
    }
}
