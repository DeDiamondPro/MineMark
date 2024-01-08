package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutData
import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elementa.RenderData
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.TextElement
import gg.essential.elementa.components.UIBlock
import gg.essential.universal.UResolution
import org.xml.sax.Attributes
import java.awt.Color
import kotlin.math.round

class MarkdownTextComponent(
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, RenderData>?,
    qName: String, attributes: Attributes?
) : TextElement<MarkdownStyle, RenderData>(style, layoutStyle, parent, qName, attributes) {
    private val font = style.textStyle.font
    private var scale = layoutStyle.fontSize
    private var prefix = buildString {
        if (layoutStyle.isBold) append("§l")
        if (layoutStyle.isItalic) append("§o")
        if (layoutStyle.isUnderlined) append("§n")
        if (layoutStyle.isStrikethrough) append("§m")
    }

    override fun generateLayout(layoutData: LayoutData?, renderData: RenderData) {
        val mcScale = UResolution.scaleFactor.toFloat()
        scale = round(layoutStyle.fontSize * mcScale) / mcScale
        super.generateLayout(layoutData, renderData)
    }

    override fun drawText(
        text: String,
        x: Float,
        y: Float,
        fontSize: Float,
        color: Color,
        hovered: Boolean,
        position: LayoutData.MarkDownElementPosition,
        renderData: RenderData
    ) {
        prefix = buildString {
            if (layoutStyle.isBold) append("§l")
            if (layoutStyle.isItalic) append("§o")
            if (layoutStyle.isStrikethrough) append("§m")
            if (layoutStyle.isUnderlined || layoutStyle.isPartOfLink && hovered) append("§n")
        }

        renderData.matrixStack.push()
        renderData.matrixStack.scale(scale, scale, 1f)
        font.drawString(
            renderData.matrixStack,
            prefix + text,
            layoutStyle.textColor,
            x / scale, y / scale,
            1f, 1f
        )
        renderData.matrixStack.pop()
    }

    override fun drawInlineCodeBlock(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color,
        renderData: RenderData
    ) {
        UIBlock.drawBlockSized(
            renderData.matrixStack, color,
            x.toDouble(), y.toDouble(),
            width.toDouble(), height.toDouble()
        )
    }

    override fun getTextWidth(text: String, fontSize: Float, renderData: RenderData?): Float {
        return font.getStringWidth(prefix + text, 1f) * scale
    }

    override fun getBaselineHeight(fontSize: Float, renderData: RenderData?): Float {
        return (font.getBaseLineHeight() + font.getShadowHeight()) * scale
    }

    override fun getDescender(fontSize: Float, renderData: RenderData?): Float {
        return font.getBelowLineHeight() * scale
    }
}