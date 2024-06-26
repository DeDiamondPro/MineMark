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
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class TableRowElement<S extends Style, R> extends Element<S, R> {
    protected LayoutData.MarkDownElementPosition position;
    protected float cellWidth;
    protected float cellHeight;

    public TableRowElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    public void drawInternal(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        for (int i = 0; i < children.size(); i++) {
            float x = cellWidth * i;
            float y = position.getY();
            children.get(i).drawInternal(
                    xOffset + x, yOffset + y,
                    mouseX - x, mouseY - y, renderData
            );
        }
    }

    @Override
    public void beforeDrawInternal(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        for (int i = 0; i < children.size(); i++) {
            float x = cellWidth * i;
            float y = position.getY();
            children.get(i).beforeDrawInternal(
                    xOffset + x, yOffset + y,
                    mouseX - x, mouseY - y, renderData
            );
        }
    }

    @Override
    public void onMouseClickedInternal(MouseButton button, float mouseX, float mouseY) {
        for (int i = 0; i < children.size(); i++) {
            float x = cellWidth * i;
            float y = position.getY();
            children.get(i).onMouseClickedInternal(button, mouseX - x, mouseY - y);
        }
    }

    @Override
    public void generateLayout(LayoutData layoutData, R renderData) {
        if (children.isEmpty()) return;
        cellWidth = layoutData.getMaxWidth() / (children.isEmpty() ? 1 : children.size());
        cellHeight = 0;
        for (Element<S, R> child : children) {
            LayoutData newLayoutData = new LayoutData(cellWidth);
            child.generateLayoutInternal(newLayoutData, renderData);
            cellHeight = Math.max(cellHeight, newLayoutData.getY() + newLayoutData.getCurrentLine().getHeight());
        }
        position = layoutData.addElement(LayoutStyle.Alignment.LEFT, layoutData.getMaxWidth(), cellHeight);
    }
}

