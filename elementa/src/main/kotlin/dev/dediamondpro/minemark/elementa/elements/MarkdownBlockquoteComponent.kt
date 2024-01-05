package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutData
import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elementa.RenderData
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.ChildMovingElement
import dev.dediamondpro.minemark.elements.Element
import gg.essential.elementa.components.UIBlock
import org.xml.sax.Attributes
import java.awt.Color

class MarkdownBlockquoteComponent(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, RenderData>?,
    qName: String, attributes: Attributes?
) : ChildMovingElement<MarkdownStyle, RenderData>(style, layoutStyle, parent, qName, attributes) {
    override fun drawMarker(x: Float, y: Float, height: Float, renderData: RenderData) {
        UIBlock.drawBlockSized(
            renderData.matrixStack,
            Color.WHITE,
            (x + 4f).toDouble(), y.toDouble(),
            2.0, height.toDouble()
        )
    }

    override fun getMarkerWidth(layoutData: LayoutData?): Float {
        return 4f + 2f + 10f
    }

    override fun getPadding(layoutData: LayoutData?): Float {
        // TODO: give blockquote own styling
        return style.getParagraphStyle().padding
    }
}