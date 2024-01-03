package dev.dediamondpro.minemark.elementa

import dev.dediamondpro.minemark.LayoutConfig
import dev.dediamondpro.minemark.elementa.util.ElementaBrowserProvider
import dev.dediamondpro.minemark.providers.BrowserProvider
import dev.dediamondpro.minemark.providers.DefaultImageProvider
import dev.dediamondpro.minemark.providers.ImageProvider
import gg.essential.elementa.font.DefaultFonts
import gg.essential.elementa.font.FontProvider
import java.awt.Color

class LayoutConfigImpl(
    var fontProvider: FontProvider = DefaultFonts.VANILLA_FONT_RENDERER,
    alignment: Alignment = Alignment.LEFT,
    fontSize: Float = 1f,
    textColor: Color = Color.WHITE,
    bold: Boolean = false,
    italic: Boolean = false,
    underlined: Boolean = false,
    strikethrough: Boolean = false,
    partOfLink: Boolean = false,
    preFormatted: Boolean = false,
    spacingConfig: SpacingConfig = SpacingConfig(4f, 4f, 4f, 16f),
    headingConfig: HeadingConfig = HeadingConfig(2f, 1.66f, 1.33f, 1f, 1.2f, 1f),
    imageProvider: ImageProvider = DefaultImageProvider.INSTANCE,
    browserProvider: BrowserProvider = ElementaBrowserProvider
) : LayoutConfig(
    alignment,
    fontSize,
    textColor,
    bold,
    italic,
    underlined,
    strikethrough,
    partOfLink,
    preFormatted,
    spacingConfig,
    headingConfig,
    imageProvider,
    browserProvider
) {
    override fun clone(): LayoutConfigImpl {
        return LayoutConfigImpl(
            fontProvider,
            alignment,
            fontSize,
            Color(textColor.rgb),
            isBold,
            isItalic,
            isUnderlined,
            isStrikethrough,
            isPartOfLink,
            isPreFormatted,
            spacingConfig,
            headingConfig,
            imageProvider,
            browserProvider
        )
    }
}
