package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class BlockQuoteElement<L extends LayoutConfig, R> extends ChildBasedElement<L, R> implements Inline {
    protected LayoutData.MarkDownElementPosition position;
    protected float blockWidth;

    public BlockQuoteElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
    }

    @Override
    protected void draw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        drawBlock(xOffset + position.getX(), yOffset + position.getY(), position.getHeight(), renderData);
        super.draw(xOffset + position.getX() + blockWidth, yOffset + position.getY(), mouseX, mouseY, renderData);
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {
        blockWidth = getBlockWidth(layoutData);
        if (layoutData.isLineOccupied()) {
            layoutData.nextLine();
        }
        layoutData.updatePadding(layoutConfig.getSpacingConfig().getBlockQuotePadding());
        LayoutData newLayoutData = new LayoutData(layoutData.getMaxWidth() - blockWidth);
        super.generateLayout(newLayoutData);
        position = layoutData.addElement(LayoutConfig.Alignment.LEFT, layoutData.getMaxWidth(), newLayoutData.getY() + newLayoutData.getLineHeight());
        layoutData.nextLine();
    }

    public abstract void drawBlock(float x, float y, float height, R renderData);

    public abstract float getBlockWidth(LayoutData layoutData);
}
