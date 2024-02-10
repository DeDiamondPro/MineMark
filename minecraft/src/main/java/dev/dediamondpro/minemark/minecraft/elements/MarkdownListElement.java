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

package dev.dediamondpro.minemark.minecraft.elements;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.list.ListElement;
import dev.dediamondpro.minemark.minecraft.platform.MarkdownRenderer;
import dev.dediamondpro.minemark.minecraft.style.MarkdownStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class MarkdownListElement extends ListElement<MarkdownStyle, MarkdownRenderer> {
    private final String markerStr;

    public MarkdownListElement(@NotNull MarkdownStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<MarkdownStyle, MarkdownRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        switch (listType) {
            case ORDERED:
                markerStr = (elementIndex + 1) + ". ";
                break;
            case UNORDERED:
            default:
                markerStr = "● ";
                break;
        }
    }

    @Override
    protected void drawMarker(float x, float y, MarkdownRenderer renderer) {
        renderer.drawText(markerStr, (int) x, (int) y, layoutStyle.getFontSize(), layoutStyle.getTextColor().getRGB(), style.getTextStyle().hasShadow());
    }

    @Override
    protected float getListMarkerWidth(LayoutData layoutData, MarkdownRenderer renderer) {
        return renderer.getTextWidth(markerStr, layoutStyle.getFontSize());
    }

    @Override
    protected float getMarkerHeight(LayoutData layoutData, MarkdownRenderer renderData) {
        return 8f * layoutStyle.getFontSize();
    }
}
