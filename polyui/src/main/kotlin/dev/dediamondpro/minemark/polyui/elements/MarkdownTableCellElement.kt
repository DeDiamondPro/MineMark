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
import dev.dediamondpro.minemark.elements.impl.table.TableCellElement
import org.polyfrost.polyui.color.PolyColor
import dev.dediamondpro.minemark.polyui.MarkdownStyle
import org.polyfrost.polyui.color.toPolyColor
import org.polyfrost.polyui.component.Drawable
import org.polyfrost.polyui.renderer.Renderer
import org.xml.sax.Attributes
import java.awt.Color

class MarkdownTableCellElement(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, Drawable>?,
    qName: String,
    attributes: Attributes?,
) : TableCellElement<MarkdownStyle, Drawable>(style, layoutStyle, parent, qName, attributes) {
    private lateinit var fillPolyColor: PolyColor
    private val borderPolyColor = style.tableStyle.borderColor.toPolyColor()

    override fun drawCellBackground(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color,
        drawable: Drawable
    ) {
        if (!this::fillPolyColor.isInitialized) {
            fillPolyColor = color.toPolyColor()
        }
        drawable.renderer.rect(x, y, width, height, fillPolyColor)
    }

    override fun drawBorderLine(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color,
        drawable: Drawable
    ) {
        drawable.renderer.rect(x, y, width, height, borderPolyColor)
    }
}
