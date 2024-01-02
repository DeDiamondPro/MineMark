package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;
import java.util.ArrayList;

public class LinkElement<L extends LayoutConfig, R> extends ChildBasedElement<L, R> implements Inline {
    protected final ArrayList<LayoutData.MarkDownElementPosition> positions = new ArrayList<>();
    protected final String link;
    protected boolean wasUnderlined;
    protected boolean hovered;

    public LinkElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
        this.link = attributes != null ? attributes.getValue("href") : null;
        if (link != null) {
            this.layoutConfig = cloneLayoutConfig(this.layoutConfig);
            this.layoutConfig.setTextColor(new Color(65, 105, 225));
            this.layoutConfig.setPartOfLink(true);
        }
    }

    @Override
    protected void onMouseClicked(MouseButton button, float mouseX, float mouseY) {
        if ((button == MouseButton.LEFT || button == MouseButton.MIDDLE) && isAnyInside(mouseX, mouseY) && link != null) {
            layoutConfig.getBrowserProvider().browse(link);
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
