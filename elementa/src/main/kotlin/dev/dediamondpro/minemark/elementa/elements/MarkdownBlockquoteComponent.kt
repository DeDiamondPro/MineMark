package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elementa.RenderData
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.BlockQuoteElement
import gg.essential.elementa.components.UIBlock
import org.xml.sax.Attributes
import java.awt.Color

class MarkdownBlockquoteComponent(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, RenderData>?,
    qName: String, attributes: Attributes?
) : BlockQuoteElement<MarkdownStyle, RenderData>(style, layoutStyle, parent, qName, attributes) {

    override fun drawBlock(x: Float, y: Float, width: Float, height: Float, color: Color, renderData: RenderData) {
        UIBlock.drawBlockSized(
            renderData.matrixStack, color,
            x.toDouble(), y.toDouble(),
            width.toDouble(), height.toDouble()
        )
    }
}