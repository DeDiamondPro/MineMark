package dev.dediamondpro.minemark.elementa

import dev.dediamondpro.minemark.MineMarkCore
import dev.dediamondpro.minemark.MineMarkCoreBuilder
import dev.dediamondpro.minemark.elementa.elements.*
import dev.dediamondpro.minemark.elements.Elements
import dev.dediamondpro.minemark.utils.MouseButton
import gg.essential.elementa.UIComponent
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UMatrixStack

class MineMarkComponent(
    markdown: String,
    layoutConfig: LayoutConfigImpl = LayoutConfigImpl(),
    core: MineMarkCore<LayoutConfigImpl, RenderData> = defaultCore
) : UIComponent() {

    private val parsedMarkdown = core.parse(layoutConfig, markdown).apply {
        addLayoutCallback(this@MineMarkComponent::layoutCallback)
    }

    override fun afterInitialization() {
        super.afterInitialization()
        onMouseClick {
            when (it.mouseButton) {
                0 -> MouseButton.LEFT
                1 -> MouseButton.RIGHT
                2 -> MouseButton.MIDDLE
                else -> null
            }?.let { button ->
                parsedMarkdown.onMouseClicked(this.getLeft(), this.getTop(), button, it.absoluteX, it.absoluteY)
            }
        }
        println(parsedMarkdown.tree)
    }

    override fun beforeDraw(matrixStack: UMatrixStack) {
        val mouse = this.getMousePosition()
        parsedMarkdown.beforeDraw(
            this.getLeft(),
            this.getTop(),
            this.getWidth(),
            mouse.first,
            mouse.second,
            RenderData(matrixStack)
        )
        super.beforeDraw(matrixStack)
    }

    override fun draw(matrixStack: UMatrixStack) {
        beforeDraw(matrixStack)
        val mouse = this.getMousePosition()
        parsedMarkdown.draw(
            this.getLeft(),
            this.getTop(),
            this.getWidth(),
            mouse.first,
            mouse.second,
            RenderData(matrixStack)
        )
        super.draw(matrixStack)
    }

    private fun layoutCallback(newHeight: Float) {
        constrain {
            height = (newHeight).pixels()
        }
    }

    companion object {
        private val defaultCore = MineMarkCore
            .builder<LayoutConfigImpl, RenderData>()
            .addElementaExtensions()
            .build()

        fun MineMarkCoreBuilder<LayoutConfigImpl, RenderData>.addElementaExtensions(): MineMarkCoreBuilder<LayoutConfigImpl, RenderData> {
            return this.addElement(Elements.TEXT, ::MarkdownTextComponent)
                .addElement(Elements.HEADING, ::MarkdownHeadingComponent)
                .addElement(Elements.IMAGE, ::MarkdownImageComponent)
                .addElement(Elements.HORIZONTAL_LINE, ::MarkdownHorizontalLineComponent)
                .addElement(Elements.LIST_ELEMENT, ::MarkdownListElementComponent)
                .addElement(Elements.BLOCKQUOTE, ::MarkdownBlockquoteComponent)
        }
    }
}