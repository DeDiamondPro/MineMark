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

package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutData
import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.TextElement
import gg.essential.elementa.components.UIBlock
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UResolution
import org.xml.sax.Attributes
import java.awt.Color
import kotlin.math.round

class MarkdownTextComponent(
    text: String,
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, UMatrixStack>?,
    qName: String, attributes: Attributes?
) : TextElement<MarkdownStyle, UMatrixStack>(text, style, layoutStyle, parent, qName, attributes) {
    private val font = style.textStyle.font
    private var scale = layoutStyle.get(LayoutStyle.FONT_SIZE)
    private var prefix = buildString {
        if (layoutStyle.get(LayoutStyle.BOLD)) append("§l")
        if (layoutStyle.get(LayoutStyle.ITALIC)) append("§o")
        if (layoutStyle.get(LayoutStyle.UNDERLINED)) append("§n")
        if (layoutStyle.get(LayoutStyle.STRIKETHROUGH)) append("§m")
    }

    override fun generateLayout(layoutData: LayoutData?, matrixStack: UMatrixStack) {
        val mcScale = UResolution.scaleFactor.toFloat()
        scale = round(layoutStyle.get(LayoutStyle.FONT_SIZE) * mcScale) / mcScale
        super.generateLayout(layoutData, matrixStack)
    }

    override fun drawText(
        text: String,
        x: Float,
        y: Float,
        fontSize: Float,
        color: Color,
        hovered: Boolean,
        position: LayoutData.MarkDownElementPosition,
        matrixStack: UMatrixStack
    ) {
        prefix = buildString {
            if (layoutStyle.get(LayoutStyle.BOLD)) append("§l")
            if (layoutStyle.get(LayoutStyle.ITALIC)) append("§o")
            if (layoutStyle.get(LayoutStyle.STRIKETHROUGH)) append("§m")
            if (layoutStyle.get(LayoutStyle.UNDERLINED) || layoutStyle.get(LayoutStyle.PART_OF_LINK) && hovered) append("§n")
        }

        matrixStack.push()
        matrixStack.scale(scale, scale, 1f)
        font.drawString(
            matrixStack,
            prefix + text,
            layoutStyle.get(LayoutStyle.TEXT_COLOR),
            x / scale, y / scale,
            1f, 1f
        )
        matrixStack.pop()
    }

    override fun drawInlineCodeBlock(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color,
        matrixStack: UMatrixStack
    ) {
        UIBlock.drawBlockSized(
            matrixStack, color,
            x.toDouble(), y.toDouble(),
            width.toDouble(), height.toDouble()
        )
    }

    override fun getTextWidth(text: String, fontSize: Float, matrixStack: UMatrixStack): Float {
        return font.getStringWidth(prefix + text, 1f) * scale
    }

    override fun getBaselineHeight(fontSize: Float, matrixStack: UMatrixStack): Float {
        return (font.getBaseLineHeight() + font.getShadowHeight()) * scale
    }

    override fun getDescender(fontSize: Float, matrixStack: UMatrixStack): Float {
        return font.getBelowLineHeight() * scale
    }
}