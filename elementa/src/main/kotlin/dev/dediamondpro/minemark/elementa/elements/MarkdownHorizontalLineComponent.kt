package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutData
import dev.dediamondpro.minemark.elementa.LayoutConfigImpl
import dev.dediamondpro.minemark.elementa.RenderData
import dev.dediamondpro.minemark.elements.BasicElement
import dev.dediamondpro.minemark.elements.Element
import gg.essential.elementa.components.UIBlock
import org.xml.sax.Attributes
import java.awt.Color

class MarkdownHorizontalLineComponent(
    layoutConfig: LayoutConfigImpl,
    parent: Element<LayoutConfigImpl, RenderData>?,
    qName: String, attributes: Attributes?
) : BasicElement<LayoutConfigImpl, RenderData>(layoutConfig, parent, qName, attributes) {
    override fun drawElement(x: Float, y: Float, renderData: RenderData) {
        UIBlock.drawBlockSized(
            renderData.matrixStack,
            Color.WHITE,
            x.toDouble(),
            y.toDouble(),
            position!!.line.width.toDouble(),
            2.0
        )
    }

    override fun getWidth(layoutData: LayoutData): Float {
        return layoutData.maxWidth
    }

    override fun getHeight(layoutData: LayoutData): Float {
        return 2f
    }
}