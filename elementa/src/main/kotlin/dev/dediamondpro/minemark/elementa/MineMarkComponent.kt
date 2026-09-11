/*
 * This file is part of MineMark
 * Copyright (C) 2024-2026 DeDiamondPro
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License Version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.dediamondpro.minemark.elementa

import dev.dediamondpro.minemark.MineMarkCore
import dev.dediamondpro.minemark.MineMarkCoreBuilder
import dev.dediamondpro.minemark.data.ViewPort
import dev.dediamondpro.minemark.elementa.elements.*
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.Elements
import dev.dediamondpro.minemark.elements.MineMarkElement
import dev.dediamondpro.minemark.utils.MouseButton
import gg.essential.elementa.UIComponent
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.renderer.ElementaExtractor
import gg.essential.elementa.renderer.ImmediateElementaExtractor
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UResolution
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import java.io.Reader


/**
 * A component to rendering Markdown powered by MineMark
 */
class MineMarkComponent(markdown: MineMarkElement<MarkdownStyle, ElementaExtractor>) : UIComponent() {
    constructor(
        markdown: String,
        style: MarkdownStyle = MarkdownStyle(),
        core: MineMarkCore<MarkdownStyle, ElementaExtractor> = defaultCore
    ) : this(core.parse(style, markdown))

    constructor(
        markdown: Reader,
        style: MarkdownStyle = MarkdownStyle(),
        core: MineMarkCore<MarkdownStyle, ElementaExtractor> = defaultCore
    ) : this(core.parse(style, markdown))

    val parsedMarkdown: MineMarkElement<MarkdownStyle, ElementaExtractor> = markdown.apply {
        addLayoutCallback(this@MineMarkComponent::layoutCallback)
    }

    private var screenViewPort: ViewPort =
        ViewPort(0f, 0f, UResolution.scaledWidth.toFloat(), UResolution.scaledHeight.toFloat())

    /**
     * The view port elements are culled against, uses entire screen when null
     */
    var viewPort: ViewPort? = null

    override fun onWindowResize() {
        screenViewPort = ViewPort(0f, 0f, UResolution.scaledWidth.toFloat(), UResolution.scaledHeight.toFloat())
        super.onWindowResize()
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
    }

    override fun extractComponent(extractor: ElementaExtractor) {
        val mouse = this.getMousePosition()
        parsedMarkdown.beforeDraw(
            this.getLeft(),
            this.getTop(),
            this.getWidth(),
            mouse.first,
            mouse.second,
            extractor
        )

        parsedMarkdown.draw(
            this.getLeft(),
            this.getTop(),
            this.getWidth(),
            mouse.first,
            mouse.second,
            viewPort ?: screenViewPort,
            extractor
        )
    }

    @Deprecated(
        "`draw`-style rendering is deprecated. " +
                "Override `extractComponent` instead. " +
                "Call `extract` to extract this component, its effects, and its children.",
        replaceWith = ReplaceWith("extract(extractor)")
    )
    override fun draw(matrixStack: UMatrixStack) {
        beforeDrawCompat(matrixStack)

        extractComponent(ImmediateElementaExtractor(matrixStack))

        @Suppress("DEPRECATION")
        super.draw(matrixStack)
    }

    private fun layoutCallback(newHeight: Float) {
        constrain {
            height = (newHeight).pixels()
        }
    }

    companion object {
        private val defaultCore = MineMarkCore
            .builder<MarkdownStyle, ElementaExtractor>()
            .addExtension(StrikethroughExtension.create())
            .addExtension(TablesExtension.create())
            .addElementaExtensions()
            .build()
    }
}

fun MineMarkCoreBuilder<MarkdownStyle, ElementaExtractor>.addElementaExtensions(): MineMarkCoreBuilder<MarkdownStyle, ElementaExtractor> {
    return this.setTextElement(::MarkdownTextComponent)
        .addElement(Elements.HEADING, ::MarkdownHeadingComponent)
        .addElement(Elements.IMAGE, ::MarkdownImageComponent)
        .addElement(Elements.HORIZONTAL_RULE, ::MarkdownHorizontalRuleComponent)
        .addElement(Elements.LIST_ELEMENT, ::MarkdownListElementComponent)
        .addElement(Elements.BLOCKQUOTE, ::MarkdownBlockquoteComponent)
        .addElement(Elements.CODE_BLOCK, ::MarkdownCodeBlockComponent)
        .addElement(Elements.TABLE_CELL, ::MarkdownTableCellComponent)
}