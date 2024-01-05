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
    private val linkStyle: LinkStyleConfig = LinkStyleConfig(Color(65, 105, 225), true, ElementaBrowserProvider),
    private val headerStyle: HeaderStyleConfig = HeaderStyleConfig(
        HeaderLevelStyleConfig(2f, 12f, true, Color(80, 80, 80), 2f, 5f),
        HeaderLevelStyleConfig(1.66f, 10f, true, Color(80, 80, 80), 2f, 5f),
        HeaderLevelStyleConfig(1.33f, 8f),
        HeaderLevelStyleConfig(1f, 6f),
        HeaderLevelStyleConfig(0.7f, 4f),
        HeaderLevelStyleConfig(0.7f, 4f)
    ),
    private val imageStyle: ImageStyleConfig = ImageStyleConfig(DefaultImageProvider.INSTANCE),
    private val listStyle: ListStyleConfig = ListStyleConfig(16f, 6f)
) : Style {
    override fun getTextStyle(): MarkdownTextStyle = textStyle
    override fun getParagraphStyle(): ParagraphStyleConfig = paragraphStyle
    override fun getLinkStyle(): LinkStyleConfig = linkStyle
    override fun getHeaderStyle(): HeaderStyleConfig = headerStyle
    override fun getImageStyle(): ImageStyleConfig = imageStyle
    override fun getListStyle(): ListStyleConfig = listStyle
}

class MarkdownTextStyle(
    defaultFontSize: Float,
    defaultTextColor: Color?,
    padding: Float,
    val font: FontProvider
) : TextStyleConfig(defaultFontSize, defaultTextColor, padding)