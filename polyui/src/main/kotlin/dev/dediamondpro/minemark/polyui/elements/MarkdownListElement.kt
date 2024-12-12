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

import dev.dediamondpro.minemark.LayoutData
import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.list.ListElement
import dev.dediamondpro.minemark.elements.impl.list.ListHolderElement
import org.polyfrost.polyui.color.PolyColor
import dev.dediamondpro.minemark.polyui.MarkdownStyle
import org.polyfrost.polyui.color.toPolyColor
import org.polyfrost.polyui.component.Drawable
import org.polyfrost.polyui.renderer.Renderer
import org.polyfrost.polyui.unit.Vec2
import org.xml.sax.Attributes

class MarkdownListElement(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, Drawable>?,
    qName: String,
    attributes: Attributes?,
) : ListElement<MarkdownStyle, Drawable>(style, layoutStyle, parent, qName, attributes) {
    private val markerStr: String =
        when (listType) {
            ListHolderElement.ListType.ORDERED -> "${elementIndex + 1}. "
            ListHolderElement.ListType.UNORDERED -> "- "
            else -> "- "
        }
    private var markerBounds: Vec2? = null

    override fun drawMarker(
        x: Float,
        y: Float,
        drawable: Drawable,
    ) {
        drawable.renderer.text(
            style.textStyle.normalFont,
            x,
            y,
            markerStr,
            style.textStyle.defaultTextColor.toPolyColor(),
            style.textStyle.defaultFontSize,
        )
    }

    private fun getMarkerBounds(renderer: Renderer): Vec2 {
        if (markerBounds == null) {
            markerBounds = renderer.textBounds(style.textStyle.normalFont, markerStr, style.textStyle.defaultFontSize)
        }
        return markerBounds!!
    }

    override fun getListMarkerWidth(
        layoutData: LayoutData,
        drawable: Drawable,
    ): Float {
        return getMarkerBounds(drawable.renderer).x
    }

    override fun getMarkerHeight(
        layoutData: LayoutData,
        drawable: Drawable,
    ): Float {
        return getMarkerBounds(drawable.renderer).y
    }
}
