package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class ChildBasedElement<L extends LayoutConfig, R> extends Element<L, R> {
    public ChildBasedElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
    }

    @Override
    protected void draw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        for (Element<L, R> child : children) {
            child.draw(xOffset, yOffset, mouseX, mouseY, renderData);
        }
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {
        if (!(this instanceof Inline) && !layoutData.isLineEmpty()) {
            layoutData.nextLine();
        }
        float padding = getPadding(layoutData);
        if (padding != 0 && !(parent instanceof NoPadding)) {
            layoutData.setLineHeight(padding);
            layoutData.nextLine();
        }
        for (Element<L, R> child : children) {
            child.generateLayout(layoutData);
        }
        if (!(this instanceof Inline) && !layoutData.isLineEmpty()) {
            layoutData.nextLine();
        }
        if (padding != 0 && !(parent instanceof NoPadding)) {
            layoutData.setLineHeight(padding);
            layoutData.nextLine();
        }
    }

    protected float getPadding(LayoutData layoutData) {
        return 0f;
    }
}