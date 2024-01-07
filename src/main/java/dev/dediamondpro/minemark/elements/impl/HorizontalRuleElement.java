package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.BasicElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public abstract class HorizontalRuleElement<S extends Style, R> extends BasicElement<S, R> {

    public HorizontalRuleElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawElement(float x, float y, float width, float height, R renderData) {
        drawLine(x, y, width, height, style.getHorizontalRuleStyle().getColor(), renderData);
    }

    protected abstract void drawLine(float x, float y, float width, float height, Color color, R renderData);

    @Override
    protected float getWidth(LayoutData layoutData, R renderData) {
        return layoutData.getMaxWidth();
    }

    @Override
    protected float getHeight(LayoutData layoutData, R renderData) {
        return style.getHorizontalRuleStyle().getHeight();
    }

    @Override
    protected float getPadding(LayoutData layoutData, R renderData) {
        return style.getHorizontalRuleStyle().getPadding();
    }
}
