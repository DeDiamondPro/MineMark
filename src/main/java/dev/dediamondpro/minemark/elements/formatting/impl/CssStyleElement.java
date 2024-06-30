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
import dev.dediamondpro.minemark.utils.ColorFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

public class CssStyleElement<S extends Style, R> implements FormattingElement<S, R> {
    protected final List<String> supportedProperties = new ArrayList<String>() {{
        add("color");
        add("text-align");
        add("text-decoration");
        add("font-size");
    }};

    @Override
    public void applyStyle(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        String inlineCss = attributes.getValue("style");
        if (inlineCss == null) return;
        String[] parts = inlineCss.split(";");
        for (String part : parts) {
            String[] propertyValue = part.split(":");
            if (propertyValue.length != 2) continue;
            String property = propertyValue[0].trim();
            String value = propertyValue[1].trim();
            switch (property) {
                case "color":
                    layoutStyle.setTextColor(ColorFactory.web(value));
                    break;
                case "text-align":
                    switch (value.toLowerCase()) {
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
                    break;
                case "text-decoration":
                    for (String decoration : value.split(" ")) {
                        switch (decoration) {
                            case "underline":
                                layoutStyle.setUnderlined(true);
                                break;
                            case "line-through":
                                layoutStyle.setStrikethrough(true);
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
                        layoutStyle.setFontSize(fontSize);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean canBeInline(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        return !attributes.getValue("style").contains("text-align");
    }

    @Override
    public boolean appliesTo(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        String styleCss = attributes.getValue("style");
        if (styleCss == null) return false;
        for (String property : supportedProperties) {
            if (styleCss.contains(property)) return true;
        }
        return false;
    }
}
