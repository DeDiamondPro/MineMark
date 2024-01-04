package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class BasicElement<L extends LayoutConfig, R> extends Element<L, R> {
    private float width;
    private float height;
    protected LayoutData.MarkDownElementPosition position;

    public BasicElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
    }

    @Override
    protected void draw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        drawElement(position.getX() + xOffset, position.getBottomY() - height + yOffset, renderData);
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {
        width = getWidth(layoutData);
        height = getHeight(layoutData);
        if ((!(this instanceof Inline) && layoutData.isLineOccupied()) || layoutData.getX() + width > layoutData.getMaxWidth()) {
            layoutData.nextLine();
        }
        position = layoutData.addElement(layoutConfig.getAlignment(), width, height);
        if (!(this instanceof Inline) && layoutData.isLineOccupied()) {
            layoutData.nextLine();
        }
    }

    protected abstract void drawElement(float x, float y, R renderData);

    protected abstract float getWidth(LayoutData layoutData);

    protected abstract float getHeight(LayoutData layoutData);
}
