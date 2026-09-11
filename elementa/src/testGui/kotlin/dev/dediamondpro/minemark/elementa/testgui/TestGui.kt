/*
 * This file is part of MineMark
 * Copyright (C) 2026 DeDiamondPro
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

package dev.dediamondpro.minemark.elementa.testgui

import dev.dediamondpro.minemark.data.ViewPort
import dev.dediamondpro.minemark.elementa.MineMarkComponent
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elementa.style.MarkdownTextStyle
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.font.DefaultFonts
import gg.essential.universal.UKeyboard
import gg.essential.universal.UScreen
import gg.essential.universal.standalone.runUniversalCraft
import java.awt.Color

private const val MARKDOWN_RESOURCE = "/test.md"

private const val CULLING_TOP = 0.1f
private const val CULLING_BOTTOM = 0.9f

fun main() = runUniversalCraft("MineMark Test GUI", 1280, 720) { window ->
    UScreen.displayScreen(MineMarkTestScreen())
    window.renderScreenUntilClosed()
}

class MineMarkTestScreen : WindowScreen(ElementaVersion.V11) {
    private val container by lazy {
        UIBlock(Color(23, 23, 23)).constrain {
            width = 100.percent
            height = 100.percent
        } childOf window
    }

    private val scroll by lazy {
        ScrollComponent().constrain {
            x = 10.pixels
            y = 0.pixels
            width = 100.percent - 20.pixels
            height = 100.percent
        } childOf container
    }

    private val cullingLines by lazy {
        listOf(CULLING_TOP, CULLING_BOTTOM).map { fraction ->
            UIBlock(Color(255, 85, 85)).constrain {
                y = RelativeConstraint(fraction)
                width = 100.percent
                height = 1.pixels
            }
        }
    }

    private var markdown: MineMarkComponent? = null
    private var culling = false

    init {
        loadMarkdown()
        container.addUpdateFunc { _, _ -> updateViewPort() }
    }

    private fun loadMarkdown() {
        scroll.clearChildren()
        val font = DefaultFonts.VANILLA_FONT_RENDERER
        val markdown = MineMarkTestScreen::class.java.getResourceAsStream(MARKDOWN_RESOURCE)
            ?.bufferedReader()?.use { it.readText() }
            ?: "**Missing `$MARKDOWN_RESOURCE` on the classpath.**"
        val style = MarkdownStyle(textStyle = MarkdownTextStyle(1f, Color.WHITE, 2f, font))
        this.markdown = MineMarkComponent(markdown, style).constrain {
            width = 100.percent
        } childOf scroll
        updateViewPort()
    }

    private fun toggleCulling() {
        culling = !culling
        if (culling) {
            cullingLines.forEach { it childOf container }
        } else {
            cullingLines.forEach { container.removeChild(it) }
        }
        updateViewPort()
    }

    /**
     * Runs every frame so the view port keeps following the window when it is resized.
     */
    private fun updateViewPort() {
        markdown?.viewPort = if (culling) {
            val top = window.getTop() + window.getHeight() * CULLING_TOP
            val bottom = window.getTop() + window.getHeight() * CULLING_BOTTOM
            ViewPort(window.getLeft(), top, window.getWidth(), bottom - top)
        } else {
            null
        }
    }

    override fun onKeyPressed(keyCode: Int, typedChar: Char, modifiers: UKeyboard.Modifiers?) {
        when (keyCode) {
            UKeyboard.KEY_R -> {
                loadMarkdown()
                return
            }

            UKeyboard.KEY_C -> {
                toggleCulling()
                return
            }
        }
        super.onKeyPressed(keyCode, typedChar, modifiers)
    }
}
