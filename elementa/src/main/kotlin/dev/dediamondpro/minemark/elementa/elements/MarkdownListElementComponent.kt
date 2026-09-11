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
import dev.dediamondpro.minemark.elements.impl.list.ListElement
import dev.dediamondpro.minemark.elements.impl.list.ListHolderElement
import gg.essential.elementa.font.extractMcScale
import gg.essential.elementa.renderer.ElementaExtractor
import org.xml.sax.Attributes

class MarkdownListElementComponent(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, ElementaExtractor>?,
    qName: String, attributes: Attributes?
) : ListElement<MarkdownStyle, ElementaExtractor>(style, layoutStyle, parent, qName, attributes) {
    private val fontProvider = style.textStyle.font
    private var markerStr: String = when (listType) {
        ListHolderElement.ListType.ORDERED -> "${elementIndex + 1}. "
        ListHolderElement.ListType.UNORDERED -> "● "
        else -> "● "
    }

    override fun drawMarker(x: Float, y: Float, extractor: ElementaExtractor) {
        fontProvider.extractMcScale(
            extractor,
            markerStr,
            layoutStyle.get(LayoutStyle.TEXT_COLOR),
            x, y,
            layoutStyle.get(LayoutStyle.FONT_SIZE)
        )
    }

    override fun getListMarkerWidth(layoutData: LayoutData?, extractor: ElementaExtractor): Float {
        return fontProvider.getStringWidth(markerStr, 1f) * layoutStyle.get(LayoutStyle.FONT_SIZE)
    }

    override fun getMarkerHeight(layoutData: LayoutData?, extractor: ElementaExtractor): Float {
        return (fontProvider.getBaseLineHeight() + fontProvider.getShadowHeight()) * layoutStyle.get(LayoutStyle.FONT_SIZE)
    }
}