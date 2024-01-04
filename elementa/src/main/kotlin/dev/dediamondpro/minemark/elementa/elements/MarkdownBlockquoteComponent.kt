package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutData
import dev.dediamondpro.minemark.elementa.LayoutConfigImpl
import dev.dediamondpro.minemark.elementa.RenderData
import dev.dediamondpro.minemark.elements.ChildMovingElement
import dev.dediamondpro.minemark.elements.Element
import gg.essential.elementa.components.UIBlock
import org.xml.sax.Attributes
import java.awt.Color

class MarkdownBlockquoteComponent(
    layoutConfig: LayoutConfigImpl,
    parent: Element<LayoutConfigImpl, RenderData>?,
    qName: String, attributes: Attributes?
) : ChildMovingElement<LayoutConfigImpl, RenderData>(layoutConfig, parent, qName, attributes) {
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
        return layoutConfig.spacingConfig.blockQuotePadding
    }
}