package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.elementa.LayoutConfigImpl
import dev.dediamondpro.minemark.elementa.RenderData
import dev.dediamondpro.minemark.elementa.util.EmptyImage
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.ImageElement
import dev.dediamondpro.minemark.elements.impl.LinkElement
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import org.xml.sax.Attributes
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.concurrent.CompletableFuture

class MarkdownImageComponent(
    layoutConfig: LayoutConfigImpl,
    parent: Element<LayoutConfigImpl, RenderData>?,
    qName: String, attributes: Attributes?
) : ImageElement<LayoutConfigImpl, RenderData>(layoutConfig, parent, qName, attributes) {
    private var uiImage: UIImage? = null

    override fun drawImage(
        image: BufferedImage,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        renderData: RenderData
    ) {
        if (uiImage == null) {
            uiImage = UIImage(CompletableFuture.supplyAsync { image }, EmptyImage, EmptyImage)
        }
        uiImage?.drawImage(
            renderData.matrixStack,
            x.toDouble(),
            y.toDouble(),
            width.toDouble(),
            height.toDouble(),
            Color.WHITE
        )
    }
}