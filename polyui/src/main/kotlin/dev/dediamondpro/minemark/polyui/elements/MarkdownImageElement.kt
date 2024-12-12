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
import dev.dediamondpro.minemark.elements.impl.ImageElement
import dev.dediamondpro.minemark.polyui.MarkdownComponent
import dev.dediamondpro.minemark.providers.ImageProvider
import dev.dediamondpro.minemark.polyui.MarkdownStyle
import org.polyfrost.polyui.component.Drawable
import org.polyfrost.polyui.data.PolyImage
import org.polyfrost.polyui.renderer.Renderer
import org.polyfrost.polyui.unit.Vec2
import org.xml.sax.Attributes

class MarkdownImageElement(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, Drawable>?,
    qName: String,
    attributes: Attributes?,
) : ImageElement<MarkdownStyle, Drawable, PolyImage>(style, layoutStyle, parent, qName, attributes) {
    private var loadFailed: Boolean = false

    override fun beforeDrawInternal(
        xOffset: Float,
        yOffset: Float,
        mouseX: Float,
        mouseY: Float,
        drawable: Drawable
    ) {
        super.beforeDrawInternal(xOffset, yOffset, mouseX, mouseY, drawable)
        if (image.size.isZero && imageWidth == -1f && imageHeight == -1f && !loadFailed) {
            // Image is most likely not initialized
            try {
                drawable.renderer.initImage(image, Vec2(width, height))
            } catch (_: Exception) {
                // It is possible initialization fails, if this happens we will just draw nothing
                loadFailed = true
                return
            }
            if (image.size.isZero) {
                drawable.needsRedraw = true // Keep polling, image might be loading asynchronously
            }
        }
        if (!image.size.isZero && imageWidth == -1f && imageHeight == -1f && !loadFailed) {
            onDimensionsReceived(ImageProvider.Dimension(image.size.x, image.size.y))
            return // We have to regenerate layout, this also calls for a redraw
        }
    }

    override fun drawImage(
        image: PolyImage,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        drawable: Drawable,
    ) {
         if (width == 0f && height == 0f || imageWidth == -1f && imageHeight == -1f || loadFailed) {
            return // Nothing to draw, might not be initialized
        }
        drawable.renderer.image(image, x, y, width, height)
    }

    override fun onImageReceived(image: PolyImage) {
        if (!image.size.isZero) {
            onDimensionsReceived(ImageProvider.Dimension(image.size.x, image.size.y))
        }
        super.onImageReceived(image)
    }
}
