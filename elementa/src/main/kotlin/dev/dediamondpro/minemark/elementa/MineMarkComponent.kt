package dev.dediamondpro.minemark.elementa

import dev.dediamondpro.minemark.MineMarkCore
import dev.dediamondpro.minemark.MineMarkCoreBuilder
import dev.dediamondpro.minemark.elementa.elements.*
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.Elements
import dev.dediamondpro.minemark.utils.MouseButton
import gg.essential.elementa.UIComponent
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UMatrixStack
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TablesExtension

class MineMarkComponent(
    markdown: String,
    style: MarkdownStyle = MarkdownStyle(),
    core: MineMarkCore<MarkdownStyle, UMatrixStack> = defaultCore
) : UIComponent() {

    private val parsedMarkdown = core.parse(style, markdown).apply {
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
            matrixStack
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
            matrixStack
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
            .builder<MarkdownStyle, UMatrixStack>()
            .addExtension(StrikethroughExtension.create())
            .addExtension(TablesExtension.create())
            .addElementaExtensions()
            .build()
    }
}

fun MineMarkCoreBuilder<MarkdownStyle, UMatrixStack>.addElementaExtensions(): MineMarkCoreBuilder<MarkdownStyle, UMatrixStack> {
    return this.setTextElement(::MarkdownTextComponent)
        .addElement(Elements.HEADING, ::MarkdownHeadingComponent)
        .addElement(Elements.IMAGE, ::MarkdownImageComponent)
        .addElement(Elements.HORIZONTAL_RULE, ::MarkdownHorizontalRuleComponent)
        .addElement(Elements.LIST_ELEMENT, ::MarkdownListElementComponent)
        .addElement(Elements.BLOCKQUOTE, ::MarkdownBlockquoteComponent)
        .addElement(Elements.CODE_BLOCK, ::MarkdownCodeBlockComponent)
        .addElement(Elements.TABLE_CELL, ::MarkdownTableCellComponent)
}