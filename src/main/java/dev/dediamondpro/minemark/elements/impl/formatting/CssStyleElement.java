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
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.elements.creators.ElementCreator;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.ColorFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class CssStyleElement<S extends Style, R> extends ChildBasedElement<S, R> implements Inline {
    private boolean isInline = true;

    public CssStyleElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;
        String inlineCss = attributes.getValue("style");
        if (inlineCss == null) return;
        this.layoutStyle = this.layoutStyle.clone();
        String[] parts = inlineCss.split(";");
        for (String part : parts) {
            String[] propertyValue = part.split(":");
            if (propertyValue.length != 2) continue;
            String property = propertyValue[0].trim();
            String value = propertyValue[1].trim();
            switch (property) {
                case "color":
                    this.layoutStyle.setTextColor(ColorFactory.web(value));
                    break;
                case "text-align":
                    switch (value.toLowerCase()) {
                        case "left":
                            this.layoutStyle.setAlignment(LayoutStyle.Alignment.LEFT);
                            isInline = false;
                            break;
                        case "center":
                            this.layoutStyle.setAlignment(LayoutStyle.Alignment.CENTER);
                            isInline = false;
                            break;
                        case "right":
                            this.layoutStyle.setAlignment(LayoutStyle.Alignment.RIGHT);
                            isInline = false;
                            break;
                    }
                    break;
                case "text-decoration":
                    for (String decoration : value.split(" ")) {
                        switch (decoration) {
                            case "underline":
                                this.layoutStyle.setUnderlined(true);
                                break;
                            case "line-through":
                                this.layoutStyle.setStrikethrough(true);
                                break;
                        }
                    }
                    break;
                case "font-size":
                    Float fontSize = null;
                    if (value.endsWith("px")) {
                        fontSize = style.getTextStyle().adaptFontSize(Float.parseFloat(value.substring(0, value.length() - 2)));
                    } else if (value.endsWith("%")) {
                        fontSize = layoutStyle.getFontSize() * (Float.parseFloat(value.substring(0, value.length() - 1)) / 100);
                    }
                    if (fontSize != null) {
                        this.layoutStyle.setFontSize(fontSize);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean isInline() {
        return isInline;
    }

    @Override
    public String toString() {
        return "CssStyleElement {" + attributes.getValue("style") + "}";
    }

    public static class CssStyleElementCreator<S extends Style, R> implements ElementCreator<S, R> {

        @Override
        public Element<S, R> createElement(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
            return new CssStyleElement<>(style, layoutStyle, parent, qName, attributes);
        }

        @Override
        public boolean appliesTo(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
            return attributes.getValue("style") != null;
        }
    }
}
