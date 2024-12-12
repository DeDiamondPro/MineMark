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

import dev.dediamondpro.minemark.providers.DefaultBrowserProvider
import dev.dediamondpro.minemark.style.*
import org.polyfrost.polyui.PolyUI
import org.polyfrost.polyui.component.Drawable
import org.polyfrost.polyui.data.Font
import java.awt.Color

class MarkdownStyle
@JvmOverloads
constructor(
    private val textStyle: MarkdownTextStyle = MarkdownTextStyle(),
    private val paragraphStyle: ParagraphStyleConfig = ParagraphStyleConfig(6f),
    private val linkStyle: LinkStyleConfig = LinkStyleConfig(Color(65, 105, 225), DefaultBrowserProvider.INSTANCE),
    private val headerStyle: HeadingStyleConfig =
        HeadingStyleConfig(
            HeadingLevelStyleConfig(32f, 12f, true, LINE_COLOR, 2f, 5f),
            HeadingLevelStyleConfig(24f, 10f, true, LINE_COLOR, 2f, 5f),
            HeadingLevelStyleConfig(19f, 8f),
            HeadingLevelStyleConfig(16f, 6f),
            HeadingLevelStyleConfig(13f, 4f),
            HeadingLevelStyleConfig(13f, 4f),
        ),
    private val horizontalRuleStyle: HorizontalRuleStyleConfig = HorizontalRuleStyleConfig(2f, 4f, LINE_COLOR),
    private val imageStyle: ImageStyleConfig = ImageStyleConfig(MarkdownImageProvider),
    private val listStyle: ListStyleConfig = ListStyleConfig(32f, 6f),
    private val blockquoteBlockStyle: BlockquoteStyleConfig = BlockquoteStyleConfig(6f, 4f, 2f, 10f, LINE_COLOR),
    private val codeBlockStyle: CodeBlockStyle = CodeBlockStyle(),
    private val tableStyle: TableStyleConfig = TableStyleConfig(
        6f, 4f, 1f, LINE_COLOR, Color(0, 0, 0, 150), Color(0, 0, 0, 0)
    ),
) : Style {
    override fun getTextStyle(): MarkdownTextStyle = textStyle

    override fun getParagraphStyle(): ParagraphStyleConfig = paragraphStyle

    override fun getLinkStyle(): LinkStyleConfig = linkStyle

    override fun getHeadingStyle(): HeadingStyleConfig = headerStyle

    override fun getHorizontalRuleStyle(): HorizontalRuleStyleConfig = horizontalRuleStyle

    override fun getImageStyle(): ImageStyleConfig = imageStyle

    override fun getListStyle(): ListStyleConfig = listStyle

    override fun getBlockquoteStyle(): BlockquoteStyleConfig = blockquoteBlockStyle

    override fun getCodeBlockStyle(): CodeBlockStyle = codeBlockStyle

    override fun getTableStyle(): TableStyleConfig = tableStyle

    companion object {
        internal val LINE_COLOR = Color(80, 80, 80)
    }
}

class MarkdownTextStyle(
    val normalFont: Font = PolyUI.defaultFonts.medium,
    val boldFont: Font = PolyUI.defaultFonts.bold,
    val italicNormalFont: Font = PolyUI.defaultFonts.mediumItalic,
    val italicBoldFont: Font = PolyUI.defaultFonts.boldItalic,
    defaultFontSize: Float = 16f,
    defaultTextColor: Color = Color.WHITE,
    padding: Float = (normalFont.lineSpacing - 1f) * defaultFontSize / 2f,
) : TextStyleConfig(defaultFontSize, defaultTextColor, padding)

class CodeBlockStyle(
    val codeFont: Font = PolyUI.monospaceFont,
    inlinePaddingLeftRight: Float = 2f,
    inlinePaddingTopBottom: Float = 1f,
    blockOutsidePadding: Float = 6f,
    blockInsidePadding: Float = 6f,
    color: Color = MarkdownStyle.LINE_COLOR,
) : CodeBlockStyleConfig(inlinePaddingLeftRight, inlinePaddingTopBottom, blockOutsidePadding, blockInsidePadding, color)
