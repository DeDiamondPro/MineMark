package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elementa.util.EmptyImage
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.ImageElement
import gg.essential.elementa.components.UIImage
import gg.essential.universal.UMatrixStack
import org.xml.sax.Attributes
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.concurrent.CompletableFuture

class MarkdownImageComponent(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, UMatrixStack>?,
    qName: String, attributes: Attributes?
) : ImageElement<MarkdownStyle, UMatrixStack, BufferedImage>(style, layoutStyle, parent, qName, attributes) {
    private var uiImage: UIImage? = null

    override fun drawImage(
        image: BufferedImage,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        matrixStack: UMatrixStack
    ) {
        if (uiImage == null) {
            uiImage = UIImage(CompletableFuture.supplyAsync { image }, EmptyImage)
        }
        uiImage?.drawImage(
            matrixStack,
            x.toDouble(),
            y.toDouble(),
            width.toDouble(),
            height.toDouble(),
            Color.WHITE
        )
    }
}