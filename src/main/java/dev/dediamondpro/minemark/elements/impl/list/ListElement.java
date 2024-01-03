package dev.dediamondpro.minemark.elements.impl.list;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.elements.NoPadding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class ListElement<L extends LayoutConfig, R> extends ChildBasedElement<L, R> implements Inline, NoPadding {
    protected final ListHolderElement.ListType listType;
    protected final int elementIndex;
    protected LayoutData.MarkDownElementPosition markerPosition;
    protected float markerWidth;
    protected float markerHeight;
    protected float indentX;

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
        // Marker is always aligned to the left
        markerWidth = getMarkerWidth();
        markerHeight = getMarkerHeight();
        indentX = Math.max(markerWidth, layoutConfig.getSpacingConfig().getListIndentSpacing());
        markerPosition = layoutData.addElement(LayoutConfig.Alignment.LEFT, indentX, markerHeight);
        LayoutData newLayoutData = new LayoutData(layoutData.getMaxWidth() - indentX);
        newLayoutData.updateLineHeight(layoutData.getLineHeight());
        LayoutData.MarkDownLine firstLine = newLayoutData.getCurrentLine();
        super.generateLayout(newLayoutData);
        layoutData.updateLineHeight(firstLine.getHeight());
        layoutData.addX(firstLine.getWidth());
        layoutData.nextLine();
        if (newLayoutData.getCurrentLine() != firstLine) {
            layoutData.setLineHeight(newLayoutData.getY() + newLayoutData.getLineHeight() - firstLine.getHeight());
            layoutData.nextLine();
        }
    }

    @Override
    protected void draw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        drawMarker(
                markerPosition.getRightX() - markerWidth + xOffset,
                markerPosition.getY() + yOffset, renderData
        );
        super.draw(
                xOffset + indentX, yOffset + markerPosition.getLine().getY(),
                mouseX - indentX, mouseY - markerPosition.getLine().getY(), renderData
        );
    }

    protected abstract void drawMarker(float x, float y, R renderData);

    protected abstract float getMarkerWidth();

    protected abstract float getMarkerHeight();
}
