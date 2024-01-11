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
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.jetbrains.annotations.ApiStatus;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MineMarkElement<S extends Style, R> extends ChildBasedElement<S, R> {
    private final ArrayList<Consumer<Float>> layoutCallbacks = new ArrayList<>();
    private float lastWidth = -1;
    private float height;

    public MineMarkElement(S style, LayoutStyle layoutStyle, Attributes attributes) {
        super(style, layoutStyle, null, null, attributes);
    }

    /**
     * Draw the markdown layout
     *
     * @param x          X-Coordinate of the top left corner
     * @param y          Y-Coordinate of the top left corner
     * @param width      The maximum width of the markdown element
     * @param mouseX     The current X-Coordinate of the mouse
     * @param mouseY     The current Y-Coordinate of the mouse
     * @param renderData Data class passed to all subclassed to aid in rendering
     */
    public void draw(float x, float y, float width, float mouseX, float mouseY, R renderData) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width cannot be zero or negative!");
        }
        if (width != lastWidth) {
            beforeDraw(x, y, width, mouseX, mouseY, renderData);
        }
        this.drawInternal(x, y, mouseX - x, mouseY - y, renderData);
    }


    /**
     * Method to call before drawing the markdown element in case you want to do something in between
     * layout and draw.
     *
     * @param x          X-Coordinate of the top left corner
     * @param y          Y-Coordinate of the top left corner
     * @param width      The maximum width of the markdown element
     * @param mouseX     The current X-Coordinate of the mouse
     * @param mouseY     The current Y-Coordinate of the mouse
     * @param renderData Data class passed to all subclassed to aid in rendering
     */
    public void beforeDraw(float x, float y, float width, float mouseX, float mouseY, R renderData) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width cannot be zero or negative!");
        }
        if (width != lastWidth) {
            generateLayout(new LayoutData(width), renderData);
            lastWidth = width;
        }
        this.beforeDrawInternal(x, y, mouseX - x, mouseY - y, renderData);
    }

    /**
     * Method to call when a mouse button was clicked
     *
     * @param button The button that was clicked
     * @param x      X-Coordinate of the top left corner
     * @param y      Y-Coordinate of the top left corner
     * @param mouseX The current X-Coordinate of the mouse
     * @param mouseY The current Y-Coordinate of the mouse
     */
    public void onMouseClicked(float x, float y, MouseButton button, float mouseX, float mouseY) {
        this.onMouseClickedInternal(button, mouseX - x, mouseY - y);
    }

    /**
     * Method to call if you want the layout to regenerate the next time
     * {@link MineMarkElement#beforeDraw(float, float, float, float, float, Object)} is called.
     */
    @Override
    public void regenerateLayout() {
        lastWidth = -1;
    }

    /**
     * Internal method to generate a new layout.
     * This method shouldn't be called manually, please use {@link MineMarkElement#regenerateLayout()} instead.
     */
    @Override
    @ApiStatus.Internal
    public void generateLayout(LayoutData layoutData, R renderData) {
        layoutData.lockTopSpacing();
        super.generateLayout(layoutData, renderData);
        float bottomSpacing = layoutData.getCurrentLine().getBottomSpacing();
        if (bottomSpacing == 0f && layoutData.isLineEmpty() && layoutData.getPreviousLine() != null) {
            bottomSpacing = layoutData.getPreviousLine().getBottomSpacing();
        }
        height = layoutData.getY() + layoutData.getLineHeight() - bottomSpacing;
        for (Consumer<Float> callback : layoutCallbacks) {
            callback.accept(height);
        }
    }

    /**
     * Add a callback that will be called when the layout is updated,
     * this callback includes the new height of the element
     *
     * @param callback The callback
     */
    public void addLayoutCallback(Consumer<Float> callback) {
        layoutCallbacks.add(callback);
    }

    /**
     * Get the height of this markdown element,
     * will return -1 if {@link MineMarkElement#beforeDraw(float, float, float, float, float, Object)} hasn't been called yet
     *
     * @return The height of this markdown element
     */
    public float getHeight() {
        return height;
    }

    /**
     * Get a tree of this markdown element in string form, useful for debugging
     *
     * @return The tree in String form
     */
    public String getTree() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < children.size(); i++) {
            builder.append("+ ");
            builder.append(children.get(i).buildTree(1));
            if (i + 1 != children.size()) builder.append("\n");
        }
        return builder.toString();
    }
}
