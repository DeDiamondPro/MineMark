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
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public abstract class CodeBlockElement<S extends Style, R> extends ChildMovingElement<S, R> implements Inline {
    protected final CodeBlockType codeBlockType;

    public CodeBlockElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        this.codeBlockType = layoutStyle.isPreFormatted() ? CodeBlockType.BLOCK : CodeBlockType.INLINE;
        // Update inline variable
        this.isInline = isInline();
        this.layoutStyle = this.layoutStyle.clone();
        this.layoutStyle.setPartOfCodeBlock(true);
    }

    @Override
    @ApiStatus.Internal
    public void generateLayout(LayoutData layoutData, R renderData) {
        switch (codeBlockType) {
            case BLOCK:
                super.generateLayout(layoutData, renderData);
                break;
            case INLINE:
                generateNewLayout(layoutData, renderData);
                break;
        }
    }

    @Override
    protected void drawMarker(float x, float y, float markerWidth, float totalHeight, R renderData) {
        drawBlock(x, y, markerWidth, totalHeight, style.getCodeBlockStyle().getColor(), renderData);
    }

    protected abstract void drawBlock(float x, float y, float width, float height, Color color, R renderData);

    @Override
    protected float getMarkerWidth(LayoutData layoutData, R renderData) {
        return 0f;
    }

    @Override
    protected float getOutsidePadding(LayoutData layoutData, R renderData) {
        return style.getCodeBlockStyle().getBlockOutsidePadding();
    }

    @Override
    protected float getInsidePadding(LayoutData layoutData, R renderData) {
        return style.getCodeBlockStyle().getBlockInsidePadding();
    }

    @Override
    protected MarkerType getMarkerType() {
        return MarkerType.BLOCK;
    }

    @Override
    public boolean isInline() {
        return codeBlockType == CodeBlockType.INLINE;
    }

    public enum CodeBlockType {
        INLINE,
        BLOCK
    }
}
