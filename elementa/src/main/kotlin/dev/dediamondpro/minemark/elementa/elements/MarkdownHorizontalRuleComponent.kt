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

import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.HorizontalRuleElement
import gg.essential.elementa.renderer.ElementaExtractor
import gg.essential.elementa.renderer.fillMcScaleXYWH
import org.xml.sax.Attributes
import java.awt.Color

class MarkdownHorizontalRuleComponent(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, ElementaExtractor>?,
    qName: String, attributes: Attributes?
) : HorizontalRuleElement<MarkdownStyle, ElementaExtractor>(style, layoutStyle, parent, qName, attributes) {
    override fun drawLine(x: Float, y: Float, width: Float, height: Float, color: Color, extractor: ElementaExtractor) {
        extractor.fillMcScaleXYWH(x, y, width, height, color)
    }
}