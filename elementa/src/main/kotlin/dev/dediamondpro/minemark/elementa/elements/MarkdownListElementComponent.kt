package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutData
import dev.dediamondpro.minemark.elementa.LayoutConfigImpl
import dev.dediamondpro.minemark.elementa.RenderData
import dev.dediamondpro.minemark.elements.BasicElement
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.list.ListElement
import dev.dediamondpro.minemark.elements.impl.list.ListHolderElement
import gg.essential.elementa.components.UIBlock
import org.xml.sax.Attributes
import java.awt.Color

class MarkdownListElementComponent(
    layoutConfig: LayoutConfigImpl,
    parent: Element<LayoutConfigImpl, RenderData>?,
    qName: String, attributes: Attributes?
) : ListElement<LayoutConfigImpl, RenderData>(layoutConfig, parent, qName, attributes) {
    private val fontProvider = layoutConfig.fontProvider
    private var marker: String = when (listType) {
        ListHolderElement.ListType.ORDERED -> "${elementIndex + 1}. "
        ListHolderElement.ListType.UNORDERED -> "● "
        else -> "● "
    }

    override fun drawMarker(x: Float, y: Float, renderData: RenderData) {
        val scale = layoutConfig.fontSize
        renderData.matrixStack.push()
        renderData.matrixStack.scale(scale, scale, 1f)
        fontProvider.drawString(
            renderData.matrixStack,
            marker,
            layoutConfig.textColor,
            x / scale, y / scale,
            1f, 1f
        )
        renderData.matrixStack.pop()
    }

    override fun getMarkerWidth(): Float {
        return fontProvider.getStringWidth(marker, 1f) * layoutConfig.fontSize
    }

    override fun getMarkerHeight(): Float {
        return fontProvider.getStringHeight(marker, 1f) * layoutConfig.fontSize
    }

    override fun generateLayout(layoutData: LayoutData?) {
        super.generateLayout(layoutData)
    }
}