package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class BasicElement<S extends Style, R> extends Element<S, R> {
    private float width;
    private float height;
    protected LayoutData.MarkDownElementPosition position;

    public BasicElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void draw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        drawElement(
                position.getX() + xOffset, position.getBottomY() - height + yOffset,
                position.getWidth(), position.getHeight(), renderData
        );
    }

    @Override
    protected void generateLayout(LayoutData layoutData, R renderData) {
        width = getWidth(layoutData, renderData);
        height = getHeight(layoutData, renderData);
        float padding = getPadding(layoutData, renderData);
        if ((!(this instanceof Inline) && layoutData.isLineOccupied()) || layoutData.getX() + width > layoutData.getMaxWidth()) {
            layoutData.nextLine();
        }
        layoutData.updatePadding(padding);
        position = layoutData.addElement(layoutStyle.getAlignment(), width, height);
        if (!(this instanceof Inline) && layoutData.isLineOccupied()) {
            layoutData.nextLine();
        }
    }

    protected abstract void drawElement(float x, float y, float width, float height, R renderData);

    protected abstract float getWidth(LayoutData layoutData, R renderData);

    protected abstract float getHeight(LayoutData layoutData, R renderData);

    protected float getPadding(LayoutData layoutData, R renderData) {
        return 0f;
    }
}
