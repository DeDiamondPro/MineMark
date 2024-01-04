package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.impl.TextElement;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.ArrayList;

public abstract class Element<L extends LayoutConfig, R> {
    protected final @Nullable Element<L, R> parent;
    protected final ArrayList<Element<L, R>> children = new ArrayList<>();
    protected final String qName;
    protected final Attributes attributes;
    protected L layoutConfig;
    protected String text;

    /**
     * Base Element Constructor used by {@link ElementLoader}
     *
     * @param layoutConfig The configuration used to generate the layout positioning
     * @param parent       Parent element, null in top level element {@link MineMarkElement}
     * @param qName        The name of the HTML tag
     * @param attributes   The attributes of the HTML tag, null for text {@link TextElement}
     */
    public Element(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        this.layoutConfig = layoutConfig;
        if (parent != null) parent.getChildren().add(this);
        this.parent = parent;
        this.qName = qName;
        this.attributes = attributes;
    }

    public void setText(String text) {
        if (text == null) {
            throw new IllegalStateException("Text is already set, it can not be set twice.");
        }
        this.text = text;
    }

    public @Nullable Element<L, R> getParent() {
        return parent;
    }

    public ArrayList<Element<L, R>> getChildren() {
        return children;
    }

    protected L cloneLayoutConfig(L layoutConfig) {
        try {
            return (L) layoutConfig.clone();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Could not cast layoutConfig.clone() to custom layoutConfig class, is clone implemented properly?");
        }
    }

    public L getLayoutConfig() {
        return layoutConfig;
    }

    protected abstract void generateLayout(LayoutData layoutData);

    public void regenerateLayout() {
        if (parent == null) {
            throw new IllegalStateException("No top level MineMarkElement found to regenerate layout with.");
        }
        parent.regenerateLayout();
    }

    protected abstract void draw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData);

    protected void beforeDraw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        for (Element<L, R> child : children) {
            child.beforeDraw(xOffset, yOffset, mouseX, mouseY, renderData);
        }
    }

    protected void onMouseClicked(MouseButton button, float mouseX, float mouseY) {
        for (Element<L, R> child : children) {
            child.onMouseClicked(button, mouseX, mouseY);
        }
    }

    /**
     * Build a tree of elements for debugging purposes
     */
    public String buildTree(int depth) {
        StringBuilder builder = new StringBuilder();
        builder.append(this);
        for (Element<L, R> child : children) {
            builder.append("\n");
            for (int i = 0; i < depth; i++) builder.append("  ");
            builder.append("+ ");
            builder.append(child.buildTree(depth + 1));
        }
        return builder.toString();
    }
}
