/*
 * This file is part of MineMark
 * Copyright (C) 2024 DeDiamondPro
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

package dev.dediamondpro.minemark.polyui

import dev.dediamondpro.minemark.MineMarkCore
import dev.dediamondpro.minemark.MineMarkCoreBuilder
import dev.dediamondpro.minemark.elements.Elements
import dev.dediamondpro.minemark.elements.MineMarkElement
import dev.dediamondpro.minemark.polyui.elements.*
import dev.dediamondpro.minemark.utils.MouseButton
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.polyfrost.polyui.color.Colors
import org.polyfrost.polyui.component.Component
import org.polyfrost.polyui.component.Drawable
import org.polyfrost.polyui.component.extensions.events
import org.polyfrost.polyui.event.Event
import org.polyfrost.polyui.unit.Align
import org.polyfrost.polyui.unit.AlignDefault
import org.polyfrost.polyui.unit.Vec2
import java.io.Reader

class MarkdownComponent(
    markdown: MineMarkElement<MarkdownStyle, Drawable>,
    vararg children: Component? = arrayOf(),
    at: Vec2 = Vec2.ZERO,
    alignment: Align = AlignDefault,
    size: Vec2 = Vec2.ZERO,
    visibleSize: Vec2 = Vec2.ZERO,
    palette: Colors.Palette? = null,
    focusable: Boolean = false,
) : Drawable(children = children, at, alignment, size, visibleSize, palette, focusable) {
    constructor(
        markdown: String,
        vararg children: Component? = arrayOf(),
        at: Vec2 = Vec2.ZERO,
        alignment: Align = AlignDefault,
        size: Vec2 = Vec2.ZERO,
        visibleSize: Vec2 = Vec2.ZERO,
        palette: Colors.Palette? = null,
        focusable: Boolean = false,
        style: MarkdownStyle = MarkdownStyle(),
        core: MineMarkCore<MarkdownStyle, Drawable> = defaultCore,
    ) : this(core.parse(style, markdown), children = children, at, alignment, size, visibleSize, palette, focusable)

    constructor(
        markdown: Reader,
        vararg children: Component? = arrayOf(),
        at: Vec2 = Vec2.ZERO,
        alignment: Align = AlignDefault,
        size: Vec2 = Vec2.ZERO,
        visibleSize: Vec2 = Vec2.ZERO,
        palette: Colors.Palette? = null,
        focusable: Boolean = false,
        style: MarkdownStyle = MarkdownStyle(),
        core: MineMarkCore<MarkdownStyle, Drawable> = defaultCore,
    ) : this(core.parse(style, markdown), children = children, at, alignment, size, visibleSize, palette, focusable)

    val parsedMarkdown = markdown.apply {
        addLayoutCallback(this@MarkdownComponent::layoutCallback)
    }

    init {
        events {
            Event.Mouse.Clicked(0).then {
                parsedMarkdown.onMouseClicked(x, y, MouseButton.LEFT, it.x, it.y)
            }
            Event.Mouse.Clicked(1).then {
                parsedMarkdown.onMouseClicked(x, y, MouseButton.RIGHT, it.x, it.y)
            }
            Event.Mouse.Clicked(2).then {
                parsedMarkdown.onMouseClicked(x, y, MouseButton.MIDDLE, it.x, it.y)
            }
        }
    }

    override fun preRender(delta: Long) {
        super.preRender(delta)
        parsedMarkdown.beforeDraw(x, y, width, polyUI.mouseX, polyUI.mouseY, this)
    }

    override fun render() {
        parsedMarkdown.draw(x, y, width, polyUI.mouseX, polyUI.mouseY, this)
        if (parsedMarkdown.needsLayoutRegeneration(width)) {
            // If our elements have decided the layout needs to change, we need to redraw
            needsRedraw = true
        }
    }

    private fun layoutCallback(newHeight: Float) {
        height = newHeight
    }

    companion object {
        private val defaultCore = MineMarkCore.builder<MarkdownStyle, Drawable>()
            .addExtension(StrikethroughExtension.create())
            .addExtension(TablesExtension.create())
            .addPolyUIExtensions()
            .build()
    }
}

fun MineMarkCoreBuilder<MarkdownStyle, Drawable>.addPolyUIExtensions(): MineMarkCoreBuilder<MarkdownStyle, Drawable> {
    return this.setTextElement(::MarkdownTextElement)
        .addElement(Elements.IMAGE, ::MarkdownImageElement)
        .addElement(Elements.HEADING, ::MarkdownHeadingElement)
        .addElement(Elements.HORIZONTAL_RULE, ::MarkdownHorizontalRuleElement)
        .addElement(Elements.CODE_BLOCK, ::MarkdownCodeBlockElement)
        .addElement(Elements.BLOCKQUOTE, ::MarkdownBlockquoteElement)
        .addElement(Elements.LIST_ELEMENT, ::MarkdownListElement)
        .addElement(Elements.TABLE_CELL, ::MarkdownTableCellElement)
}
