package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.function.Consumer;

public final class MineMarkElement<S extends Style, R> extends ChildBasedElement<S, R> {
    private final ArrayList<Consumer<Float>> layoutCallbacks = new ArrayList<>();
    private float lastWidth = -1;
    private float height;

    public MineMarkElement(S style, LayoutStyle layoutStyle, Attributes attributes) {
        super(style, layoutStyle, null, null, attributes);
    }

    @Override
    public void generateLayout(LayoutData layoutData, R renderData) {
        super.generateLayout(layoutData, renderData);
        height = layoutData.getY() + layoutData.getLineHeight();
        for (Consumer<Float> callback : layoutCallbacks) {
            callback.accept(height);
        }
    }

    @Override
    public void regenerateLayout() {
        lastWidth = -1;
    }

    public void beforeDraw(float x, float y, float width, float mouseX, float mouseY, R renderData) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width cannot be zero or negative!");
        }
        if (width != lastWidth) {
            generateLayout(new LayoutData(width), renderData);
            lastWidth = width;
        }
        this.beforeDraw(x, y, mouseX - x, mouseY - y, renderData);
    }

    public void draw(float x, float y, float width, float mouseX, float mouseY, R renderData) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width cannot be zero or negative!");
        }
        if (width != lastWidth) {
            throw new IllegalArgumentException("Draw has been called without calling beforeDraw, this is not allowed!");
        }
        this.draw(x, y, mouseX - x, mouseY - y, renderData);
    }


    public void onMouseClicked(float x, float y, MouseButton button, float mouseX, float mouseY) {
        this.onMouseClicked(button, mouseX - x, mouseY - y);
    }

    public float getHeight() {
        return height;
    }

    public String getTree() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < children.size(); i++) {
            builder.append("+ ");
            builder.append(children.get(i).buildTree(1));
            if (i + 1 != children.size()) builder.append("\n");
        }
        return builder.toString();
    }

    public void addLayoutCallback(Consumer<Float> callback) {
        layoutCallbacks.add(callback);
    }
}
