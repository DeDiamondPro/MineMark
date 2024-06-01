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
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.ArrayList;

public class LinkElement<S extends Style, R> extends ChildBasedElement<S, R> implements Inline {
    protected final ArrayList<LayoutData.MarkDownElementPosition> positions = new ArrayList<>();
    protected final String link;

    public LinkElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        this.link = attributes != null ? attributes.getValue("href") : null;
        if (link != null) {
            this.layoutStyle = this.layoutStyle.clone();
            this.layoutStyle.setPartOfLink(true);
            // Only change the text color if no custom color is active
            if (style.getTextStyle().getDefaultTextColor().equals(this.layoutStyle.getTextColor())) {
                this.layoutStyle.setTextColor(style.getLinkStyle().getTextColor());
            }
        }
    }

    @Override
    @ApiStatus.Internal
    public void onMouseClickedInternal(MouseButton button, float mouseX, float mouseY) {
        if ((button == MouseButton.LEFT || button == MouseButton.MIDDLE) && isAnyInside(mouseX, mouseY) && link != null) {
            style.getLinkStyle().getBrowserProvider().browse(link);
        } else {
            super.onMouseClickedInternal(button, mouseX, mouseY);
        }
    }
    @Override
    @ApiStatus.Internal
    public void generateLayout(LayoutData layoutData, R renderData) {
        positions.clear();
        layoutData.addElementListener(this::onElementAdded);
        super.generateLayout(layoutData, renderData);
        layoutData.removeElementListener();
    }

    protected boolean isAnyInside(float x, float y) {
        for (LayoutData.MarkDownElementPosition position : positions) {
            if (position.isInside(x, y)) {
                return true;
            }
        }
        return false;
    }

    protected void onElementAdded(LayoutData.MarkDownElementPosition position) {
        positions.add(position);
    }

    @Override
    public String toString() {
        return "LinkElement {" + link + "}";
    }
}
