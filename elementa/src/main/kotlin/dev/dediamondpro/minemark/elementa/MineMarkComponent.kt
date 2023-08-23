package dev.dediamondpro.minemark.elementa

import dev.dediamondpro.minemark.MineMarkCore
import dev.dediamondpro.minemark.elementa.config.LayoutConfigImpl
import dev.dediamondpro.minemark.elementa.config.RenderConfigImpl
import dev.dediamondpro.minemark.elementa.elements.TextElement
import dev.dediamondpro.minemark.elements.Elements
import gg.essential.elementa.UIComponent
import gg.essential.universal.UMatrixStack

class MineMarkComponent(markdown: String) : UIComponent() {
    private val parsedMarkdown = core.parse(markdown)

    override fun draw(matrixStack: UMatrixStack) {
        parsedMarkdown.draw(this.getLeft(), this.getTop(), this.getWidth(), RenderConfigImpl(matrixStack))
        super.draw(matrixStack)
    }

    companion object {
        private val core = MineMarkCore
            .builder<LayoutConfigImpl, RenderConfigImpl>()
            .addElement(Elements.TEXT, ::TextElement)
            .build()
    }
}