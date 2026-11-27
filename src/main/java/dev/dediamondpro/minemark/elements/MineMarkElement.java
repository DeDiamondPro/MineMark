/*
 * This file is part of MineMark
 * Copyright (C) 2024-2026 DeDiamondPro
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
import dev.dediamondpro.minemark.data.ViewPort;
import dev.dediamondpro.minemark.error.ErrorResponseFactory;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MineMarkElement<S extends Style, R> extends ChildBasedElement<S, R> {
    private final ArrayList<Consumer<Float>> layoutCallbacks = new ArrayList<>();
    private @Nullable ErrorResponseFactory<S, R> errorResponseFactory;
    private MineMarkElement<S, R> errorResponse;
    private boolean errored = false;
    private float lastWidth = -1;
    private float height;

    public MineMarkElement(S style, LayoutStyle layoutStyle, Attributes attributes, @Nullable ErrorResponseFactory<S, R> errorResponseFactory) {
        super(style, layoutStyle, null, null, attributes);
        this.errorResponseFactory = errorResponseFactory;
    }

    public MineMarkElement(S style, LayoutStyle layoutStyle, Attributes attributes) {
        this(style, layoutStyle, attributes, null);
    }

    /**
     * Draw the markdown layout
     *
     * @param x          X-Coordinate of the top left corner
     * @param y          Y-Coordinate of the top left corner
     * @param width      The maximum width of the markdown element
     * @param mouseX     The current X-Coordinate of the mouse
     * @param mouseY     The current Y-Coordinate of the mouse
     * @param viewPort   The currently visible content, content outside the viewport will not be rendered
     * @param renderData Data class passed to all subclassed to aid in rendering
     */
    public void draw(float x, float y, float width, float mouseX, float mouseY, @Nullable ViewPort viewPort, R renderData) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width cannot be zero or negative!");
        }
        if (width != lastWidth) {
            beforeDraw(x, y, width, mouseX, mouseY, renderData);
        }
        this.trySafe(
                ErrorResponseFactory.Phase.DRAW,
                () -> this.drawInternal(x, y, mouseX - x, mouseY - y, viewPort, renderData),
                (errorElement) -> errorElement.draw(x, y, width, mouseX, mouseY, renderData)
        );
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
        this.draw(x, y, width, mouseX, mouseY, null, renderData);
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
            generateLayoutInternal(new LayoutData(width), renderData);
            lastWidth = width;
        }
        this.trySafe(
                ErrorResponseFactory.Phase.BEFORE_DRAW,
                () -> this.beforeDrawInternal(x, y, mouseX - x, mouseY - y, renderData),
                (errorElement) -> errorElement.beforeDraw(x, y, width, mouseX, mouseY, renderData)
        );
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
        // Don't use try-safe, since here an error doesn't mean we stop rendering
        if (errored) {
            if (errorResponse != null) errorResponse.onMouseClicked(x, y, button, mouseX, mouseY);
            return;
        }
        try {
            this.onMouseClickedInternal(button, mouseX - x, mouseY - y);
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
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
        trySafe(
                ErrorResponseFactory.Phase.LAYOUT,
                () -> {
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
                },
                (errorElement) -> errorElement.generateLayout(layoutData, renderData)
        );
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
        return errored && errorResponse != null ? errorResponse.getHeight() : height;
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

    /**
     * Set the factory that creates the element to be rendered in case an error occurs during rendering,
     * to disable this, set it to null, this will cause nothing to render if an error occured.
     *
     * @param errorResponseFactory The response factory
     */
    public void setErrorResponseFactory(@Nullable ErrorResponseFactory<S, R> errorResponseFactory) {
        this.errorResponseFactory = errorResponseFactory;
    }

    private void trySafe(ErrorResponseFactory.Phase phase, Runnable action, Consumer<MineMarkElement<S, R>> errorAction) {
        if (!errored) {
            try {
                action.run();
            } catch (Exception | Error e) {
                errored = true;
                if (errorResponseFactory != null) {
                    errorResponse = errorResponseFactory.build(style, phase, e);
                    if (errorResponse != null) {
                        // Make the parent aware of the size of the child error element
                        errorResponse.addLayoutCallback((newHeight) -> {
                            for (Consumer<Float> callback : layoutCallbacks) {
                                callback.accept(newHeight);
                            }
                        });
                    }
                }
                e.printStackTrace();
            }
        }

        if (errored && errorResponse != null) {
            // Once we are in an errored state propagate everything to the error element.
            try {
                errorAction.accept(errorResponse);
            } catch (Exception | Error e) {
                errorResponse = null; // Error response also can't render cleanly
                e.printStackTrace();
            }
        }
    }
}
