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

public abstract class CodeBlockElement<S extends Style, R> extends ChildMovingElement<S, R> {
    protected final CodeBlockType codeBlockType;

    public CodeBlockElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        this.codeBlockType = layoutStyle.isPreFormatted() ? CodeBlockType.BLOCK : CodeBlockType.INLINE;
        if (this.codeBlockType == CodeBlockType.INLINE) {
            this.layoutStyle = this.layoutStyle.clone();
            this.layoutStyle.setPartOfCodeBlock(true);
        }
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {
        switch (codeBlockType) {
            case BLOCK:
                super.generateLayout(layoutData);
                break;
            case INLINE:
                generateNewLayout(layoutData);
                break;
        }
    }

    @Override
    protected void drawMarker(float x, float y, float markerWidth, float totalHeight, R renderData) {
        drawBlock(x, y, markerWidth, totalHeight, style.getCodeBlockStyle().getColor(), renderData);
    }

    protected abstract void drawBlock(float x, float y, float width, float height, Color color, R renderData);

    @Override
    protected float getMarkerWidth(LayoutData layoutData) {
        return 0f;
    }

    @Override
    protected float getOutsidePadding(LayoutData layoutData) {
        return style.getCodeBlockStyle().getBlockOutsidePadding();
    }

    @Override
    protected float getInsidePadding(LayoutData layoutData) {
        return style.getCodeBlockStyle().getBlockInsidePadding();
    }

    @Override
    protected MarkerType getMarkerType() {
        return MarkerType.BLOCK;
    }

    public enum CodeBlockType {
        INLINE,
        BLOCK
    }
}