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

package dev.dediamondpro.minemark.elements.impl.table;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildMovingElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public abstract class TableCellElement<S extends Style, R> extends ChildMovingElement<S, R> {
    protected int rowIndex;
    protected int cellIndex;
    protected float cellHeight;
    protected Color fillColor;

    public TableCellElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        this.layoutStyle = this.layoutStyle.clone();
        if (qName.equals("th")) {
            this.layoutStyle.setBold(true);
            this.layoutStyle.setAlignment(LayoutStyle.Alignment.CENTER);
        }
        if (attributes != null && attributes.getValue("align") != null) {
            switch (attributes.getValue("align")) {
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
    }

    @Override
    protected void drawMarker(float x, float y, float markerWidth, float totalHeight, R renderData) {
        drawCellBackground(x, y, markerWidth, cellHeight, fillColor, renderData);
        float borderThickness = style.getTableStyle().getBorderThickness();
        if (rowIndex == 0) {
            // Top
            drawBorderLine(
                    x, y, markerWidth, borderThickness,
                    style.getTableStyle().getBorderColor(), renderData
            );
        }
        // Bottom
        drawBorderLine(
                x, y + cellHeight - borderThickness, markerWidth, borderThickness,
                style.getTableStyle().getBorderColor(), renderData
        );
        if (cellIndex == 0) {
            // Left
            drawBorderLine(
                    x, y, borderThickness, cellHeight,
                    style.getTableStyle().getBorderColor(), renderData
            );
        }
        // Right
        drawBorderLine(
                x + markerWidth - borderThickness, y, borderThickness, cellHeight,
                style.getTableStyle().getBorderColor(), renderData
        );
    }

    protected abstract void drawCellBackground(float x, float y, float width, float height, Color color, R renderData);

    protected abstract void drawBorderLine(float x, float y, float width, float height, Color color, R renderData);

    @Override
    public void generateLayout(LayoutData layoutData, R renderData) {
        super.generateLayout(layoutData, renderData);
        cellHeight = -1;
    }

    @Override
    public void beforeDrawInternal(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        super.beforeDrawInternal(xOffset, yOffset, mouseX, mouseY, renderData);
        if (cellHeight != -1) return;
        assert parent != null;
        assert parent.getParent() != null;

        cellHeight = ((TableRowElement<S, R>) parent).cellHeight;
        rowIndex = parent.getParent().getChildren().indexOf(parent);
        cellIndex = parent.getChildren().indexOf(this);

        fillColor = rowIndex % 2 == 0
                ? style.getTableStyle().getEvenFillColor()
                : style.getTableStyle().getOddFillColor();
    }

    @Override
    protected float getMarkerWidth(LayoutData layoutData, R renderData) {
        return 0f;
    }

    @Override
    protected float getInsidePadding(LayoutData layoutData, R renderData) {
        return style.getTableStyle().getInsidePadding();
    }

    @Override
    protected MarkerType getMarkerType() {
        return MarkerType.BLOCK;
    }
}

