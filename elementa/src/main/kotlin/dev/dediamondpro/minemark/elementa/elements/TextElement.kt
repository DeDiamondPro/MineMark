package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.elementa.config.LayoutConfigImpl
import dev.dediamondpro.minemark.elementa.config.RenderConfigImpl
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.text.TextElement
import gg.essential.elementa.components.UIText
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import org.xml.sax.Attributes

class TextElement(
    parent: Element<LayoutConfigImpl, RenderConfigImpl>?,
    attributes: Attributes?,
) : TextElement<LayoutConfigImpl, RenderConfigImpl>(parent, attributes) {
    private var element: UIText? = null

    override fun draw(x: Float, y: Float, width: Float, renderConfig: RenderConfigImpl) {
        if (element == null) element = UIText(text)
        element?.constrain {
            this.x = x.pixels()
            this.y = y.pixels()
        }
        element?.draw(renderConfig.matrixStack)
    }
}