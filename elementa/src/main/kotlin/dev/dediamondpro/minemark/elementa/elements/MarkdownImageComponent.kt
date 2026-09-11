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
import dev.dediamondpro.minemark.elementa.util.EmptyImage
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.ImageElement
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.image.extractMcScale
import gg.essential.elementa.renderer.ElementaExtractor
import org.xml.sax.Attributes
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.concurrent.CompletableFuture

class MarkdownImageComponent(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, ElementaExtractor>?,
    qName: String, attributes: Attributes?
) : ImageElement<MarkdownStyle, ElementaExtractor, BufferedImage>(style, layoutStyle, parent, qName, attributes) {
    private var uiImage: UIImage? = null

    override fun drawImage(
        image: BufferedImage,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        extractor: ElementaExtractor
    ) {
        if (uiImage == null) {
            uiImage = UIImage(CompletableFuture.supplyAsync { image }, EmptyImage).apply {
                // Use linear scaling for Markdown images instead of nearest neighbor
                textureMinFilter = UIImage.TextureScalingMode.LINEAR
                textureMagFilter = UIImage.TextureScalingMode.LINEAR
            }
        }
        uiImage?.extractMcScale(extractor, x, y, width, height, Color.WHITE)
    }
}