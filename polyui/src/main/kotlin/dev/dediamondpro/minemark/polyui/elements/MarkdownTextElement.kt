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


package dev.dediamondpro.minemark.polyui.elements

import dev.dediamondpro.minemark.LayoutData.MarkDownElementPosition
import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.TextElement
import dev.dediamondpro.minemark.polyui.MarkdownStyle
import org.polyfrost.polyui.color.PolyColor
import org.polyfrost.polyui.color.toPolyColor
import org.polyfrost.polyui.component.Drawable
import org.polyfrost.polyui.component.extensions.events
import org.polyfrost.polyui.event.Event
import org.xml.sax.Attributes
import java.awt.Color
import kotlin.math.floor

class MarkdownTextElement(
    text: String,
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, Drawable>?,
    qName: String,
    attributes: Attributes?,
) : TextElement<MarkdownStyle, Drawable>(text, style, layoutStyle, parent, qName, attributes) {
    private val textColor: PolyColor = layoutStyle.textColor.toPolyColor()
    private val codeBlockColor: PolyColor = style.codeBlockStyle.color.toPolyColor()
    private val font =
        when {
            layoutStyle.isPartOfCodeBlock -> style.codeBlockStyle.codeFont
            layoutStyle.isBold && layoutStyle.isItalic -> style.textStyle.italicBoldFont
            layoutStyle.isBold -> style.textStyle.boldFont
            layoutStyle.isItalic -> style.textStyle.italicNormalFont
            else -> style.textStyle.normalFont
        }
    private var lineHeight = -1f
    private var registeredEvent = false
    private var wasHovered = false

    private fun registerEvent(drawable: Drawable) {
        if (registeredEvent) return
        registeredEvent = true

        if (!layoutStyle.isPartOfLink) return
        drawable.events {
            Event.Mouse.Moved.then {
                var hovered = false
                for (position in lines.keys) {
                    if (position.isInside(drawable.polyUI.mouseX, drawable.polyUI.mouseY)) {
                        hovered = true
                        break
                    }
                }
                if (hovered != wasHovered) {
                    wasHovered = hovered
                    drawable.needsRedraw = true
                }
            }
        }
    }

    override fun beforeDrawInternal(
        xOffset: Float,
        yOffset: Float,
        mouseX: Float,
        mouseY: Float,
        drawable: Drawable
    ) {
        registerEvent(drawable)
        super.beforeDrawInternal(xOffset, yOffset, mouseX, mouseY, drawable)
    }

    override fun drawText(
        text: String,
        x: Float,
        y: Float,
        fontSize: Float,
        color: Color,
        hovered: Boolean,
        position: MarkDownElementPosition,
        drawable: Drawable,
    ) {
        if (layoutStyle.isUnderlined || layoutStyle.isPartOfLink && hovered) {
            drawable.renderer.rect(x, position.bottomY, position.width, floor(layoutStyle.fontSize / 8), textColor)
        }
        if (layoutStyle.isStrikethrough) {
            drawable.renderer.rect(
                x,
                y + position.height / 2f - fontSize / 8f,
                position.width,
                floor(layoutStyle.fontSize / 8),
                textColor,
            )
        }
        drawable.renderer.text(font, x, y, text, textColor, fontSize)
    }

    override fun drawInlineCodeBlock(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color,
        drawable: Drawable,
    ) {
        drawable.renderer.rect(x, y, width, height, codeBlockColor, radius = 2f)
    }

    override fun getTextWidth(
        text: String,
        fontSize: Float,
        drawable: Drawable,
    ): Float {
        return drawable.renderer.textBounds(font, text, fontSize).x
    }

    override fun getBaselineHeight(
        fontSize: Float,
        drawable: Drawable,
    ): Float {
        if (lineHeight == -1f) {
            lineHeight = drawable.renderer.textBounds(font, text, fontSize).y
        }
        return lineHeight
    }
}
