package dev.dediamondpro.minemark.elements.impl.list;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

// TODO: implement
public abstract class ListElement<L extends LayoutConfig, R> extends Element<L, R> {
    protected final ListHolderElement.ListType listType;
    protected final int elementIndex;

    public ListElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
        if (!(parent instanceof ListHolderElement)) {
            throw new IllegalArgumentException("List element has no surrounding list holder!");
        }
        ListHolderElement<L, R> holder = (ListHolderElement<L, R>) parent;
        listType = holder.getListType();
        elementIndex = holder.getElementPosition(this);
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {

    }

    @Override
    protected void draw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {

    }

    protected abstract void drawMarker(float x, float y, R renderData);

    protected abstract float getMarkerWidth(ListHolderElement.ListType listType, int elementIndex);

    protected abstract float getMarkerHeight(ListHolderElement.ListType listType, int elementIndex);
}
