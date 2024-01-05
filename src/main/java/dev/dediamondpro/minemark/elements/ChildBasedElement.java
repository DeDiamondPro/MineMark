package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class ChildBasedElement<S extends Style, R> extends Element<S, R> {
    public ChildBasedElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void draw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        for (Element<S, R> child : children) {
            child.draw(xOffset, yOffset, mouseX, mouseY, renderData);
        }
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {
        if (!(this instanceof Inline) && layoutData.isLineOccupied()) {
            layoutData.nextLine();
        }
        float padding = getPadding(layoutData);
        layoutData.updateTopSpacing(padding);
        for (Element<S, R> child : children) {
            child.generateLayout(layoutData);
        }
        layoutData.updateBottomSpacing(padding);
        if (!(this instanceof Inline)) {
            layoutData.nextLine();
        }
    }

    protected float getPadding(LayoutData layoutData) {
        return 0f;
    }
}
