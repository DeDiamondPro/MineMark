package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.ArrayList;

public class LinkElement<S extends Style, R> extends ChildBasedElement<S, R> implements Inline {
    protected final ArrayList<LayoutData.MarkDownElementPosition> positions = new ArrayList<>();
    protected final String link;

    public LinkElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        this.link = attributes != null ? attributes.getValue("href") : null;
        if (link != null) {
            this.layoutStyle = this.layoutStyle.clone();
            this.layoutStyle.setTextColor(style.getLinkStyle().getTextColor());
            this.layoutStyle.setPartOfLink(true);
        }
    }

    @Override
    protected void onMouseClicked(MouseButton button, float mouseX, float mouseY) {
        if ((button == MouseButton.LEFT || button == MouseButton.MIDDLE) && isAnyInside(mouseX, mouseY) && link != null) {
            style.getLinkStyle().getBrowserProvider().browse(link);
        } else {
            super.onMouseClicked(button, mouseX, mouseY);
        }
    }

    @Override
    protected void beforeDraw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        super.beforeDraw(xOffset, yOffset, mouseX, mouseY, renderData);
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {
        positions.clear();
        layoutData.addElementListener(this::onElementAdded);
        super.generateLayout(layoutData);
        layoutData.removeElementListener();
    }

    protected boolean isAnyInside(float x, float y) {
        for (LayoutData.MarkDownElementPosition position : positions) {
            if (position.isInside(x, y)) {
                return true;
            }
        }
        return false;
    }

    protected void onElementAdded(LayoutData.MarkDownElementPosition position) {
        positions.add(position);
    }

    @Override
    public String toString() {
        return "LinkElement {" + link + "}";
    }
}
