package dev.dediamondpro.minemark.elementa.elements

import dev.dediamondpro.minemark.LayoutData
import dev.dediamondpro.minemark.LayoutStyle
import dev.dediamondpro.minemark.elementa.style.MarkdownStyle
import dev.dediamondpro.minemark.elements.Element
import dev.dediamondpro.minemark.elements.impl.TextElement
import gg.essential.elementa.components.UIBlock
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UResolution
import org.xml.sax.Attributes
import java.awt.Color
import kotlin.math.round

class MarkdownTextComponent(
    text: String,
    style: MarkdownStyle,
    layoutStyle: LayoutStyle,
    parent: Element<MarkdownStyle, UMatrixStack>?,
    qName: String, attributes: Attributes?
) : TextElement<MarkdownStyle, UMatrixStack>(text, style, layoutStyle, parent, qName, attributes) {
    private val font = style.textStyle.font
    private var scale = layoutStyle.fontSize
    private var prefix = buildString {
        if (layoutStyle.isBold) append("§l")
        if (layoutStyle.isItalic) append("§o")
        if (layoutStyle.isUnderlined) append("§n")
        if (layoutStyle.isStrikethrough) append("§m")
    }

    override fun generateLayout(layoutData: LayoutData?, matrixStack: UMatrixStack) {
        val mcScale = UResolution.scaleFactor.toFloat()
        scale = round(layoutStyle.fontSize * mcScale) / mcScale
        super.generateLayout(layoutData, matrixStack)
    }

    override fun drawText(
        text: String,
        x: Float,
        y: Float,
        fontSize: Float,
        color: Color,
        hovered: Boolean,
        position: LayoutData.MarkDownElementPosition,
        matrixStack: UMatrixStack
    ) {
        prefix = buildString {
            if (layoutStyle.isBold) append("§l")
            if (layoutStyle.isItalic) append("§o")
            if (layoutStyle.isStrikethrough) append("§m")
            if (layoutStyle.isUnderlined || layoutStyle.isPartOfLink && hovered) append("§n")
        }

        matrixStack.push()
        matrixStack.scale(scale, scale, 1f)
        font.drawString(
            matrixStack,
            prefix + text,
            layoutStyle.textColor,
            x / scale, y / scale,
            1f, 1f
        )
        matrixStack.pop()
    }

    override fun drawInlineCodeBlock(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color,
        matrixStack: UMatrixStack
    ) {
        UIBlock.drawBlockSized(
            matrixStack, color,
            x.toDouble(), y.toDouble(),
            width.toDouble(), height.toDouble()
        )
    }

    override fun getTextWidth(text: String, fontSize: Float, matrixStack: UMatrixStack): Float {
        return font.getStringWidth(prefix + text, 1f) * scale
    }

    override fun getBaselineHeight(fontSize: Float, matrixStack: UMatrixStack): Float {
        return (font.getBaseLineHeight() + font.getShadowHeight()) * scale
    }

    override fun getDescender(fontSize: Float, matrixStack: UMatrixStack): Float {
        return font.getBelowLineHeight() * scale
    }
}