package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutData
import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.list.ListElement
import dev.dediamondpro.minemark.elements.impl.list.ListHolderElement
import gg.essential.universal.UMatrixStack
import org.xml.sax.Attributes

class MarkdownListElementComponent(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, UMatrixStack>?,
    qName: String, attributes: Attributes?
) : ListElement<MarkdownStyle, UMatrixStack>(style, layoutStyle, parent, qName, attributes) {
    private val fontProvider = style.textStyle.font
    private var markerStr: String = when (listType) {
        ListHolderElement.ListType.ORDERED -> "${elementIndex + 1}. "
        ListHolderElement.ListType.UNORDERED -> "● "
        else -> "● "
    }

    override fun drawMarker(x: Float, y: Float, matrixStack: UMatrixStack) {
        val scale = layoutStyle.fontSize
        matrixStack.push()
        matrixStack.scale(scale, scale, 1f)
        fontProvider.drawString(
            matrixStack,
            markerStr,
            layoutStyle.textColor,
            x / scale, y / scale,
            1f, 1f
        )
        matrixStack.pop()
    }

    override fun getListMarkerWidth(layoutData: LayoutData?, matrixStack: UMatrixStack): Float {
        return fontProvider.getStringWidth(markerStr, 1f) * layoutStyle.fontSize
    }

    override fun getMarkerHeight(layoutData: LayoutData?, matrixStack: UMatrixStack): Float {
        return (fontProvider.getBaseLineHeight() + fontProvider.getShadowHeight()) * layoutStyle.fontSize
    }
}