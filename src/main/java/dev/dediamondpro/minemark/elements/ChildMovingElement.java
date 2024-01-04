package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

/**
 * An element that moves all child elements to the right, useful for blockquotes, lists, ...
 */
public abstract class ChildMovingElement<L extends LayoutConfig, R> extends Element<L, R> {
    protected final MarkerType markerType = getMarkerType();
    protected LayoutData.MarkDownElementPosition marker;
    protected float totalHeight;
    protected float extraXOffset;
    protected float extraYOffset;

    public ChildMovingElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {
        if (layoutData.isLineOccupied()) {
            layoutData.nextLine();
        }

        float xMovement = getMarkerWidth(layoutData);
        float padding = getPadding(layoutData);
        layoutData.updatePadding(padding);

        LayoutData newLayoutData = new LayoutData(layoutData.getMaxWidth() - xMovement);

        if (markerType == MarkerType.ONE_LINE) {
            marker = layoutData.addElement(LayoutConfig.Alignment.LEFT, xMovement, getMarkerHeight(layoutData));
            newLayoutData.setTopSpacing(layoutData.getCurrentLine().getTopSpacing());
            newLayoutData.lockTopSpacing();
            newLayoutData.setLineHeight(layoutData.getLineHeight());
            newLayoutData.setBottomSpacing(layoutData.getCurrentLine().getBottomSpacing());
        }
        LayoutData.MarkDownLine firstLine = newLayoutData.getCurrentLine();

        generateNewLayout(newLayoutData);

        totalHeight = newLayoutData.getY() + newLayoutData.getCurrentLine().getHeight();

        if (markerType == MarkerType.ONE_LINE) {
            layoutData.updateLineHeight(firstLine.getRawHeight());
                newLayoutData.setBottomSpacing(layoutData.getCurrentLine().getBottomSpacing());
            if (newLayoutData.getCurrentLine() != firstLine) {
                layoutData.nextLine();
                layoutData.setLineHeight(newLayoutData.getCurrentLine().getBottomY() - firstLine.getHeight());
            }
        } else {
            marker = layoutData.addElement(LayoutConfig.Alignment.LEFT, xMovement, totalHeight);
        }
        layoutData.nextLine();

        extraXOffset = marker.getRightX();
        extraYOffset = markerType == MarkerType.ONE_LINE ? marker.getLine().getY() : marker.getY();
    }

    protected void generateNewLayout(LayoutData layoutData) {
        for (Element<L, R> child : children) {
            child.generateLayout(layoutData);
        }
    }

    @Override
    protected void draw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        drawMarker(marker.getX() + xOffset, marker.getY() + yOffset, totalHeight, renderData);
        for (Element<L, R> child : children) {
            child.draw(
                    xOffset + extraXOffset, yOffset + extraYOffset,
                    mouseX - extraXOffset, mouseY - extraYOffset, renderData
            );
        }
    }

    @Override
    protected void beforeDraw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        super.beforeDraw(
                xOffset + extraXOffset, yOffset + extraXOffset,
                mouseX - extraXOffset, mouseY - extraYOffset, renderData
        );
    }

    @Override
    protected void onMouseClicked(MouseButton button, float mouseX, float mouseY) {
        super.onMouseClicked(button, mouseX - extraXOffset, mouseY - extraYOffset);
    }

    protected abstract void drawMarker(float x, float y, float totalHeight, R renderData);

    protected MarkerType getMarkerType() {
        return MarkerType.FULL;
    }

    protected abstract float getMarkerWidth(LayoutData layoutData);

    /**
     * @return The height of the marker, this only has to be implemented with {@link MarkerType#ONE_LINE}
     */
    protected float getMarkerHeight(LayoutData layoutData) {
        throw new IllegalStateException("\"getMarkerHeight\" should be implemented for \"MarkerType.ONE_LINE\"!");
    }

    protected float getPadding(LayoutData layoutData) {
        return 0f;
    }

    protected enum MarkerType {
        ONE_LINE,
        FULL
    }
}
