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
import dev.dediamondpro.minemark.elements.impl.TextElement;
import dev.dediamondpro.minemark.minecraft.platform.MarkdownRenderer;
import dev.dediamondpro.minemark.minecraft.style.MarkdownStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public class MarkdownTextElement extends TextElement<MarkdownStyle, MarkdownRenderer> {
    public MarkdownTextElement(@NotNull String text, @NotNull MarkdownStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<MarkdownStyle, MarkdownRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(text, style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawText(@NotNull String text, float x, float y, float fontSize, Color color, boolean hovered, LayoutData.MarkDownElementPosition position, @NotNull MarkdownRenderer renderer) {
        renderer.drawText(getPrefix(hovered) + text, (int) x, (int) y, fontSize, color.getRGB(), style.getTextStyle().hasShadow());
    }

    @Override
    protected void drawInlineCodeBlock(float x, float y, float width, float height, Color color, @NotNull MarkdownRenderer renderer) {
        renderer.drawRect((int) x, (int) y, (int) width, (int) height, color.getRGB());
    }

    @Override
    protected float getTextWidth(@NotNull String text, float fontSize, MarkdownRenderer renderer) {
        return renderer.getTextWidth(getPrefix(false) + text, fontSize);
    }

    private String getPrefix(boolean hovered) {
        StringBuilder prefixBuilder = new StringBuilder();
        if (layoutStyle.isBold()) prefixBuilder.append("§l");
        if (layoutStyle.isItalic()) prefixBuilder.append("§o");
        if (layoutStyle.isStrikethrough()) prefixBuilder.append("§m");
        if (layoutStyle.isUnderlined() || layoutStyle.isPartOfLink() && hovered) prefixBuilder.append("§n");
        return prefixBuilder.toString();
    }

    @Override
    protected float getBaselineHeight(float fontSize, MarkdownRenderer renderer) {
        return 8f * fontSize;
    }

    @Override
    protected float getDescender(float fontSize, MarkdownRenderer renderData) {
        return fontSize;
    }
}
