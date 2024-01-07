package dev.dediamondpro.minemark.elements.impl.list;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildMovingElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class ListElement<S extends Style, R> extends ChildMovingElement<S, R> {
    protected final ListHolderElement.ListType listType;
    protected final int elementIndex;
    protected float actualMarkerWidth;

    public ListElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        if (!(parent instanceof ListHolderElement)) {
            throw new IllegalArgumentException("List element has no surrounding list holder!");
        }
        ListHolderElement<S, R> holder = (ListHolderElement<S, R>) parent;
        listType = holder.getListType();
        elementIndex = holder.getChildren().indexOf(this);
    }


    @Override
    protected float getOutsidePadding(LayoutData layoutData, R renderData) {
        return style.getTextStyle().getPadding();
    }

    @Override
    protected MarkerType getMarkerType() {
        return MarkerType.ONE_LINE;
    }

    @Override
    protected final void drawMarker(float x, float y, float markerWidth, float totalHeight, R renderData) {
        drawMarker(x + markerWidth - actualMarkerWidth, y, renderData);
    }

    @Override
    protected final float getMarkerWidth(LayoutData layoutData, R renderData) {
        actualMarkerWidth = getListMarkerWidth(layoutData, renderData);
        return Math.max(actualMarkerWidth, style.getListStyle().getIndentation());
    }

    protected abstract void drawMarker(float x, float y, R renderData);

    protected abstract float getListMarkerWidth(LayoutData layoutData, R renderData);
}
