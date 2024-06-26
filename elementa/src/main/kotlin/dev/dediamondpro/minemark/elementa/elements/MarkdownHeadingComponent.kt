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

import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.HeadingElement
import gg.essential.elementa.components.UIBlock
import gg.essential.universal.UMatrixStack
import org.xml.sax.Attributes
import java.awt.Color

class MarkdownHeadingComponent(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, UMatrixStack>?,
    qName: String, attributes: Attributes?
) : HeadingElement<MarkdownStyle, UMatrixStack>(style, layoutStyle, parent, qName, attributes) {
    override fun drawDivider(x: Float, y: Float, width: Float, height: Float, color: Color, matrixStack: UMatrixStack) {
        UIBlock.drawBlockSized(
            matrixStack, color,
            x.toDouble(), y.toDouble(),
            width.toDouble(), height.toDouble()
        )
    }
}