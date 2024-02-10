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
import dev.dediamondpro.minemark.elements.impl.BlockQuoteElement;
import dev.dediamondpro.minemark.elements.impl.CodeBlockElement;
import dev.dediamondpro.minemark.minecraft.platform.MarkdownRenderer;
import dev.dediamondpro.minemark.minecraft.style.MarkdownStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public class MarkdownBlockQuoteElement extends BlockQuoteElement<MarkdownStyle, MarkdownRenderer> {
    public MarkdownBlockQuoteElement(@NotNull MarkdownStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<MarkdownStyle, MarkdownRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawBlock(float x, float y, float width, float height, Color color, MarkdownRenderer renderer) {
        renderer.drawRect((int) x, (int) y, (int) width, (int) height, color.getRGB());
    }
}
