package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

/**
 * An element that moves all child elements to the right, useful for blockquotes, lists, ...
 */
public abstract class ChildMovingElement<S extends Style, R> extends Element<S, R> {
    protected final MarkerType markerType = getMarkerType();
    protected LayoutData.MarkDownElementPosition marker;
    protected float totalHeight;
    protected float extraXOffset;
    protected float extraYOffset;

    public ChildMovingElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    @ApiStatus.Internal
    public void generateLayout(LayoutData layoutData, R renderData) {
        if (layoutData.isLineOccupied()) {
            layoutData.nextLine();
        }

        float markerWidth = getMarkerWidth(layoutData, renderData);
        float outsidePadding = getOutsidePadding(layoutData, renderData);
        float insidePadding = getInsidePadding(layoutData, renderData);
        layoutData.updatePadding(outsidePadding);

        LayoutData newLayoutData = new LayoutData(
                layoutData.getMaxWidth() - markerWidth - insidePadding * (markerType == MarkerType.BLOCK ? 2f : 1f)
        );

        if (markerType == MarkerType.ONE_LINE) {
            marker = layoutData.addElement(LayoutStyle.Alignment.LEFT, markerWidth, getMarkerHeight(layoutData, renderData));
            newLayoutData.setTopSpacing(layoutData.getCurrentLine().getTopSpacing());
            newLayoutData.lockTopSpacing();
            newLayoutData.setLineHeight(layoutData.getLineHeight());
            newLayoutData.setBottomSpacing(layoutData.getCurrentLine().getBottomSpacing());
        }
        LayoutData.MarkDownLine firstLine = newLayoutData.getCurrentLine();

        generateNewLayout(newLayoutData, renderData);

        totalHeight = newLayoutData.getY() + newLayoutData.getCurrentLine().getHeight();

        if (markerType == MarkerType.ONE_LINE) {
            layoutData.updateLineHeight(firstLine.getRawHeight());
            newLayoutData.setBottomSpacing(layoutData.getCurrentLine().getBottomSpacing());
            if (newLayoutData.getCurrentLine() != firstLine) {
                layoutData.nextLine();
                layoutData.setLineHeight(newLayoutData.getCurrentLine().getBottomY() - firstLine.getHeight());
            }
        } else {
            marker = layoutData.addElement(
                    LayoutStyle.Alignment.LEFT,
                    markerType == MarkerType.BLOCK ? layoutData.getMaxWidth() : markerWidth,
                    totalHeight + insidePadding * 2
            );
        }
        layoutData.nextLine();

        extraXOffset = (markerType == MarkerType.BLOCK ? 0f : marker.getRightX()) + insidePadding;
        extraYOffset = (markerType == MarkerType.ONE_LINE ? marker.getLine().getY() : marker.getY()) + insidePadding;
    }

    protected void generateNewLayout(LayoutData layoutData, R renderData) {
        for (Element<S, R> child : children) {
            child.generateLayout(layoutData, renderData);
        }
    }

    @Override
    @ApiStatus.Internal
    public void drawInternal(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        if (marker != null) {
            drawMarker(marker.getX() + xOffset, marker.getY() + yOffset, marker.getWidth(), marker.getHeight(), renderData);
        }
        for (Element<S, R> child : children) {
            child.drawInternal(
                    xOffset + extraXOffset, yOffset + extraYOffset,
                    mouseX - extraXOffset, mouseY - extraYOffset, renderData
            );
        }
    }

    @Override
    @ApiStatus.Internal
    public void beforeDrawInternal(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        super.beforeDrawInternal(
                xOffset + extraXOffset, yOffset + extraXOffset,
                mouseX - extraXOffset, mouseY - extraYOffset, renderData
        );
    }

    @Override
    @ApiStatus.Internal
    public void onMouseClickedInternal(MouseButton button, float mouseX, float mouseY) {
        super.onMouseClickedInternal(button, mouseX - extraXOffset, mouseY - extraYOffset);
    }

    protected abstract void drawMarker(float x, float y, float markerWidth, float totalHeight, R renderData);

    protected MarkerType getMarkerType() {
        return MarkerType.FULL;
    }

    protected abstract float getMarkerWidth(LayoutData layoutData, R renderData);

    /**
     * @return The height of the marker, this only has to be implemented with {@link MarkerType#ONE_LINE}
     */
    protected float getMarkerHeight(LayoutData layoutData, R renderData) {
        throw new IllegalStateException("\"getMarkerHeight\" should be implemented for \"MarkerType.ONE_LINE\"!");
    }

    protected float getOutsidePadding(LayoutData layoutData, R renderData) {
        return 0f;
    }

    protected float getInsidePadding(LayoutData layoutData, R renderData) {
        return 0f;
    }

    protected enum MarkerType {
        ONE_LINE,
        FULL,
        BLOCK
    }
}
