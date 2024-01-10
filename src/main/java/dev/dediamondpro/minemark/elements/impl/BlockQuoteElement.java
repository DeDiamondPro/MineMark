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

package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildMovingElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public abstract class BlockQuoteElement<S extends Style, R> extends ChildMovingElement<S, R> {

    public BlockQuoteElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawMarker(float x, float y, float markerWidth, float totalHeight, R renderData) {
        drawBlock(
                x + style.getBlockquoteStyle().getSpacingLeft(), y, style.getBlockquoteStyle().getBlockWidth(),
                totalHeight, style.getBlockquoteStyle().getBlockColor(), renderData
        );
    }

    @Override
    protected float getMarkerWidth(LayoutData layoutData, R renderData) {
        return style.getBlockquoteStyle().getSpacingLeft()
                + style.getBlockquoteStyle().getBlockWidth()
                + style.getBlockquoteStyle().getSpacingRight();
    }

    @Override
    protected float getOutsidePadding(LayoutData layoutData, R renderData) {
        return style.getBlockquoteStyle().getPadding();
    }

    protected abstract void drawBlock(float x, float y, float width, float height, Color color, R renderData);
}
