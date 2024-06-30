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

package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class BasicElement<S extends Style, R> extends Element<S, R> {
    protected LayoutData.MarkDownElementPosition position;

    public BasicElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    public void drawInternal(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        drawElement(
                position.getX() + xOffset, position.getY() + yOffset,
                position.getWidth(), position.getHeight(), renderData
        );
    }

    @Override
    public void generateLayout(LayoutData layoutData, R renderData) {
        float width = getWidth(layoutData, renderData);
        float height = getHeight(layoutData, renderData);
        float padding = getPadding(layoutData, renderData);
        if (layoutData.getX() + width > layoutData.getMaxWidth()) {
            layoutData.nextLine();
        }
        layoutData.updatePadding(padding);
        position = layoutData.addElement(layoutStyle.getAlignment(), width, height);
    }

    protected abstract void drawElement(float x, float y, float width, float height, R renderData);

    protected abstract float getWidth(LayoutData layoutData, R renderData);

    protected abstract float getHeight(LayoutData layoutData, R renderData);

    protected float getPadding(LayoutData layoutData, R renderData) {
        return 0f;
    }
}
