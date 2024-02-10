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

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.table.TableCellElement;
import dev.dediamondpro.minemark.minecraft.platform.MarkdownRenderer;
import dev.dediamondpro.minemark.minecraft.style.MarkdownStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public class MarkdownTableCellElement extends TableCellElement<MarkdownStyle, MarkdownRenderer> {
    public MarkdownTableCellElement(@NotNull MarkdownStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<MarkdownStyle, MarkdownRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawCellBackground(float x, float y, float width, float height, Color color, MarkdownRenderer renderer) {
        renderer.drawRect((int) x, (int) y, (int) width, (int) height, color.getRGB());
    }

    @Override
    protected void drawBorderLine(float x, float y, float width, float height, Color color, MarkdownRenderer renderer) {
        renderer.drawRect((int) x, (int) y, (int) width, (int) height, color.getRGB());
    }
}
