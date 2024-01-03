package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutData
import dev.dediamondpro.minemark.elementa.LayoutConfigImpl
import dev.dediamondpro.minemark.elementa.RenderData
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.text.TextElement
import gg.essential.universal.UResolution
import org.xml.sax.Attributes
import kotlin.math.floor
import kotlin.math.round

class MarkdownTextComponent(
    layoutConfig: LayoutConfigImpl,
    parent: Element<LayoutConfigImpl, RenderData>?,
    qName: String, attributes: Attributes?
) : TextElement<LayoutConfigImpl, RenderData>(layoutConfig, parent, qName, attributes) {
    private val font = layoutConfig.fontProvider
    private var scale = layoutConfig.fontSize
    private var prefix = buildString {
        if (layoutConfig.isBold) append("§l")
        if (layoutConfig.isItalic) append("§o")
        if (layoutConfig.isUnderlined) append("§n")
        if (layoutConfig.isStrikethrough) append("§m")
    }

    override fun generateLayout(layoutData: LayoutData?) {
        val mcScale = UResolution.scaleFactor.toFloat()
        scale = round(layoutConfig.fontSize * mcScale) / mcScale
        super.generateLayout(layoutData)
    }

    override fun drawText(text: String, x: Float, bottomY: Float, hovered: Boolean, renderData: RenderData) {
        prefix = buildString {
            if (layoutConfig.isBold) append("§l")
            if (layoutConfig.isItalic) append("§o")
            if (layoutConfig.isStrikethrough) append("§m")
            if (layoutConfig.isUnderlined || layoutConfig.isPartOfLink && hovered) append("§n")
        }

        val y = bottomY - getTextHeight(text)
        renderData.matrixStack.push()
        renderData.matrixStack.scale(scale, scale, 1f)
        font.drawString(
            renderData.matrixStack,
            prefix + text,
            layoutConfig.textColor,
            x / scale, y / scale,
            1f, 1f
        )
        renderData.matrixStack.pop()
    }

    override fun getTextWidth(text: String): Float {
        return font.getStringWidth(prefix + text, 1f) * scale
    }

    override fun getTextHeight(text: String): Float {
        return font.getStringHeight(prefix + text, 1f) * scale
    }
}