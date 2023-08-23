package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.elementa.LayoutConfigImpl
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.text.TextElement
import gg.essential.elementa.components.UIText
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UMatrixStack
import org.xml.sax.Attributes

class TextElement(
    parent: Element<LayoutConfigImpl, UMatrixStack>?,
    attributes: Attributes?,
) : TextElement<LayoutConfigImpl, UMatrixStack>(parent, attributes) {
    private var element: UIText? = null

    override fun draw(x: Float, y: Float, width: Float, matrixStack: UMatrixStack) {
        if (element == null) element = UIText(text)
        element?.constrain {
            this.x = x.pixels()
            this.y = y.pixels()
        }
        element?.draw(matrixStack)
    }
}