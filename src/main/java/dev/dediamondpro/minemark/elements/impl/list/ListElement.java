package dev.dediamondpro.minemark.elements.impl.list;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.ChildMovingElement;
import dev.dediamondpro.minemark.elements.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class ListElement<L extends LayoutConfig, R> extends ChildMovingElement<L, R> {
    protected final ListHolderElement.ListType listType;
    protected final int elementIndex;
    protected float markerWidth;

    public ListElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
        if (!(parent instanceof ListHolderElement)) {
            throw new IllegalArgumentException("List element has no surrounding list holder!");
        }
        ListHolderElement<L, R> holder = (ListHolderElement<L, R>) parent;
        listType = holder.getListType();
        elementIndex = holder.getChildren().indexOf(this);
    }


    @Override
    protected float getPadding(LayoutData layoutData) {
        return layoutConfig.getSpacingConfig().getTextPadding();
    }

    @Override
    protected MarkerType getMarkerType() {
        return MarkerType.ONE_LINE;
    }

    @Override
    protected final void drawMarker(float x, float y, float totalHeight, R renderData) {
        drawMarker(x + marker.getWidth() - markerWidth, y, renderData);
    }

    @Override
    protected final float getMarkerWidth(LayoutData layoutData) {
        markerWidth = getMarkerWidth();
        return Math.max(markerWidth, layoutConfig.getSpacingConfig().getListIndentSpacing());
    }

    protected abstract void drawMarker(float x, float y, R renderData);

    protected abstract float getMarkerWidth();
}
