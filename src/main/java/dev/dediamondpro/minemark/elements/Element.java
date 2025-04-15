/*
 * This file is part of MineMark
 * Copyright (C) 2024 DeDiamondPro
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License Version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.impl.TextElement;
import dev.dediamondpro.minemark.elements.creators.ElementCreator;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.ArrayList;

/**
 * Base class for an element that should render content, only one can apply per HTML tag.
 */
public abstract class Element<S extends Style, R> implements AutoCloseable {
    protected final @Nullable Element<S, R> parent;
    protected final ArrayList<Element<S, R>> children = new ArrayList<>();
    protected final String qName;
    protected final Attributes attributes;
    protected final S style;
    protected LayoutStyle layoutStyle;
    protected boolean isInline = (this instanceof Inline) && ((Inline) this).isInline();

    /**
     * Base Element Constructor used by {@link ElementCreator}
     *
     * @param layoutStyle The configuration used to generate the layout positioning
     * @param parent      Parent element, null in top level element {@link MineMarkElement}
     * @param qName       The name of the HTML tag
     * @param attributes  The attributes of the HTML tag, null for text {@link TextElement}
     */
    public Element(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        this.style = style;
        this.layoutStyle = layoutStyle;
        if (parent != null) parent.getChildren().add(this);
        this.parent = parent;
        this.qName = qName;
        this.attributes = attributes;
    }

    /**
     * Called when an element is ended and all children are created.
     */
    @ApiStatus.OverrideOnly
    public void complete() {

    }

    /**
     * Internal method for drawing an element, should never be used directly.
     */
    @ApiStatus.OverrideOnly
    public void drawInternal(float xOffset, float yOffset, float mouseX, float mouseY, R renderData){
        for (Element<S, R> child : children) {
            child.drawInternal(xOffset, yOffset, mouseX, mouseY, renderData);
        }
    }

    /**
     * Internal method called before drawing an element, should never be used directly.
     */
    @ApiStatus.OverrideOnly
    public void beforeDrawInternal(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        for (Element<S, R> child : children) {
            child.beforeDrawInternal(xOffset, yOffset, mouseX, mouseY, renderData);
        }
    }

    /**
     * Internal method for called when the mouse is clicked, should never be used directly.
     */
    @ApiStatus.OverrideOnly
    public void onMouseClickedInternal(MouseButton button, float mouseX, float mouseY) {
        for (Element<S, R> child : children) {
            child.onMouseClickedInternal(button, mouseX, mouseY);
        }
    }

    /**
     * Internal method for doing layout generation and handling if this element is inline or not.
     */
    @ApiStatus.Internal
    public void generateLayoutInternal(LayoutData layoutData, R renderData) {
        if (!isInline && layoutData.isLineModified()) {
            layoutData.nextLine();
        }
        generateLayout(layoutData, renderData);
        if (!isInline && layoutData.isLineModified()) {
            layoutData.nextLine();
        }
    }

    /**
     * Method for generating the layout of this element, should never be used directly.
     */
    protected abstract void generateLayout(LayoutData layoutData, R renderData);

    /**
     * Call this method to regenerate the layout of all associated elements
     */
    public void regenerateLayout() {
        if (parent == null) {
            throw new IllegalStateException("No top level MineMarkElement found to regenerate layout with.");
        }
        parent.regenerateLayout();
    }

    /**
     * Close all resources opened by associated elements
     */
    @Override
    public void close() {
        for (Element<S, R> child : children) {
            child.close();
        }
    }

    /**
     * Build a tree of elements for debugging purposes
     * <p>
     * Use {@link MineMarkElement#getTree()} to get the full tree
     */
    @ApiStatus.Internal
    public String buildTree(int depth) {
        StringBuilder builder = new StringBuilder();
        builder.append(this);
        for (Element<S, R> child : children) {
            builder.append("\n");
            for (int i = 0; i < depth; i++) builder.append("  ");
            builder.append("+ ");
            builder.append(child.buildTree(depth + 1));
        }
        return builder.toString();
    }

    public @Nullable Element<S, R> getParent() {
        return parent;
    }

    public @NotNull ArrayList<Element<S, R>> getChildren() {
        return children;
    }

    public S getStyle() {
        return style;
    }

    public LayoutStyle getLayoutStyle() {
        return layoutStyle;
    }

    public String getQName() {
        return qName;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setInline(boolean inline) {
        this.isInline = inline;
    }
}
