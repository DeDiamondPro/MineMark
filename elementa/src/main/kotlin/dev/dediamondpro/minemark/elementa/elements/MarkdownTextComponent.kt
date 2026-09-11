/*
 * This file is part of MineMark
 * Copyright (C) 2024-2026 DeDiamondPro
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

package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutData
import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.TextElement
import gg.essential.elementa.font.extractMcScale
import gg.essential.elementa.renderer.ElementaExtractor
import gg.essential.elementa.renderer.fillMcScaleXYWH
import org.xml.sax.Attributes
import java.awt.Color
import kotlin.math.round

class MarkdownTextComponent(
    text: String,
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, ElementaExtractor>?,
    qName: String, attributes: Attributes?
) : TextElement<MarkdownStyle, ElementaExtractor>(text, style, layoutStyle, parent, qName, attributes) {
    private val font = style.textStyle.font
    private var scale = layoutStyle.get(LayoutStyle.FONT_SIZE)
    private var prefix = buildString {
        if (layoutStyle.get(LayoutStyle.BOLD)) append("§l")
        if (layoutStyle.get(LayoutStyle.ITALIC)) append("§o")
        if (layoutStyle.get(LayoutStyle.UNDERLINED)) append("§n")
        if (layoutStyle.get(LayoutStyle.STRIKETHROUGH)) append("§m")
    }

    override fun generateLayout(layoutData: LayoutData?, extractor: ElementaExtractor) {
        val mcScale = extractor.guiScale
        scale = round(layoutStyle.get(LayoutStyle.FONT_SIZE) * mcScale) / mcScale
        super.generateLayout(layoutData, extractor)
    }

    override fun drawText(
        text: String,
        x: Float,
        y: Float,
        fontSize: Float,
        color: Color,
        hovered: Boolean,
        position: LayoutData.MarkDownElementPosition,
        extractor: ElementaExtractor
    ) {
        prefix = buildString {
            if (layoutStyle.get(LayoutStyle.BOLD)) append("§l")
            if (layoutStyle.get(LayoutStyle.ITALIC)) append("§o")
            if (layoutStyle.get(LayoutStyle.STRIKETHROUGH)) append("§m")
            if (layoutStyle.get(LayoutStyle.UNDERLINED) || layoutStyle.get(LayoutStyle.PART_OF_LINK) && hovered) append("§n")
        }

        font.extractMcScale(
            extractor,
            prefix + text,
            layoutStyle.get(LayoutStyle.TEXT_COLOR),
            x, y,
            scale
        )
    }

    override fun drawInlineCodeBlock(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color,
        extractor: ElementaExtractor
    ) {
        extractor.fillMcScaleXYWH(x, y, width, height, color)
    }

    override fun getTextWidth(text: String, fontSize: Float, extractor: ElementaExtractor): Float {
        return font.getStringWidth(prefix + text, 1f) * scale
    }

    override fun getBaselineHeight(fontSize: Float, extractor: ElementaExtractor): Float {
        return (font.getBaseLineHeight() + font.getShadowHeight()) * scale
    }

    override fun getDescender(fontSize: Float, extractor: ElementaExtractor): Float {
        return font.getBelowLineHeight() * scale
    }
}