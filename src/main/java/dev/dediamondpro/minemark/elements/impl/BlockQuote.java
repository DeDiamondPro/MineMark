package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildMovingElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public abstract class BlockQuote<S extends Style, R> extends ChildMovingElement<S, R> {

    public BlockQuote(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawMarker(float x, float y, float totalHeight, R renderData) {
        drawBlock(
                x + style.getBlockquoteStyle().getSpacingLeft(), y, style.getBlockquoteStyle().getBlockWidth(),
                totalHeight, style.getBlockquoteStyle().getBlockColor(), renderData
        );
    }

    @Override
    protected float getMarkerWidth(LayoutData layoutData) {
        return style.getBlockquoteStyle().getSpacingLeft()
                + style.getBlockquoteStyle().getBlockWidth()
                + style.getBlockquoteStyle().getSpacingRight();
    }

    @Override
    protected float getPadding(LayoutData layoutData) {
        return style.getBlockquoteStyle().getPadding();
    }

    protected abstract void drawBlock(float x, float y, float width, float height, Color color, R renderData);
}
