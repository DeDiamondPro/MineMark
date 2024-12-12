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

import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.HeadingElement
import org.polyfrost.polyui.color.PolyColor
import dev.dediamondpro.minemark.polyui.MarkdownStyle
import org.polyfrost.polyui.color.toPolyColor
import org.polyfrost.polyui.component.Drawable
import org.polyfrost.polyui.renderer.Renderer
import org.xml.sax.Attributes
import java.awt.Color

class MarkdownHeadingElement(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, Drawable>?,
    qName: String,
    attributes: Attributes?,
) : HeadingElement<MarkdownStyle, Drawable>(style, layoutStyle, parent, qName, attributes) {
    // If this is a heading without divider it is possible the color is null
    private var polyColor: PolyColor? = headingStyle.dividerColor?.toPolyColor()

    override fun drawDivider(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color,
        drawable: Drawable,
    ) {
        drawable.renderer.rect(x, y, width, height, polyColor!!)
    }
}
