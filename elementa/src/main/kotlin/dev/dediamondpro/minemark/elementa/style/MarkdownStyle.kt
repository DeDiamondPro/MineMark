package dev.dediamondpro.minemark.elementa.style

import dev.dediamondpro.minemark.elementa.util.ElementaBrowserProvider
import dev.dediamondpro.minemark.providers.DefaultImageProvider
import dev.dediamondpro.minemark.style.*
import gg.essential.elementa.font.DefaultFonts
import gg.essential.elementa.font.FontProvider
import java.awt.Color

data class MarkdownStyle @JvmOverloads constructor(
    private val textStyle: MarkdownTextStyle = MarkdownTextStyle(
        1f, Color.WHITE, 2f, DefaultFonts.VANILLA_FONT_RENDERER
    ),
    private val paragraphStyle: ParagraphStyleConfig = ParagraphStyleConfig(6f),
    private val linkStyle: LinkStyleConfig = LinkStyleConfig(Color(65, 105, 225), ElementaBrowserProvider),
    private val headerStyle: HeadingStyleConfig = HeadingStyleConfig(
        HeadingLevelStyleConfig(2f, 12f, true, LINE_COLOR, 2f, 5f),
        HeadingLevelStyleConfig(1.66f, 10f, true, LINE_COLOR, 2f, 5f),
        HeadingLevelStyleConfig(1.33f, 8f),
        HeadingLevelStyleConfig(1f, 6f),
        HeadingLevelStyleConfig(0.7f, 4f),
        HeadingLevelStyleConfig(0.7f, 4f)
    ),
    private val horizontalRuleStyle: HorizontalRuleStyleConfig = HorizontalRuleStyleConfig(2f, 4f, LINE_COLOR),
    private val imageStyle: ImageStyleConfig = ImageStyleConfig(DefaultImageProvider.INSTANCE),
    private val listStyle: ListStyleConfig = ListStyleConfig(16f, 6f),
    private val blockquoteBlockStyle: BlockquoteStyleConfig = BlockquoteStyleConfig(6f, 4f, 2f, 10f, LINE_COLOR),
    private val codeBlockStyle: CodeBlockStyleConfig = CodeBlockStyleConfig(2f, 1f, 6f, 6f, LINE_COLOR)
) : Style {
    override fun getTextStyle(): MarkdownTextStyle = textStyle
    override fun getParagraphStyle(): ParagraphStyleConfig = paragraphStyle
    override fun getLinkStyle(): LinkStyleConfig = linkStyle
    override fun getHeadingStyle(): HeadingStyleConfig = headerStyle
    override fun getHorizontalRuleStyle(): HorizontalRuleStyleConfig = horizontalRuleStyle
    override fun getImageStyle(): ImageStyleConfig = imageStyle
    override fun getListStyle(): ListStyleConfig = listStyle
    override fun getBlockquoteStyle(): BlockquoteStyleConfig = blockquoteBlockStyle
    override fun getCodeBlockStyle(): CodeBlockStyleConfig = codeBlockStyle

    companion object {
        private val LINE_COLOR = Color(80, 80, 80);
    }
}

class MarkdownTextStyle(
    defaultFontSize: Float, defaultTextColor: Color?, padding: Float, val font: FontProvider
) : TextStyleConfig(defaultFontSize, defaultTextColor, padding)