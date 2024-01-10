package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.HorizontalRuleElement
import gg.essential.elementa.components.UIBlock
import gg.essential.universal.UMatrixStack
import org.xml.sax.Attributes
import java.awt.Color

class MarkdownHorizontalRuleComponent(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, UMatrixStack>?,
    qName: String, attributes: Attributes?
) : HorizontalRuleElement<MarkdownStyle, UMatrixStack>(style, layoutStyle, parent, qName, attributes) {
    override fun drawLine(x: Float, y: Float, width: Float, height: Float, color: Color, matrixStack: UMatrixStack) {
        UIBlock.drawBlockSized(
            matrixStack, color,
            x.toDouble(), y.toDouble(),
            width.toDouble(), height.toDouble()
        )
    }
}