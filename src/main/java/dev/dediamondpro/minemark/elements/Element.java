package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutConfig;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.ArrayList;

public abstract class Element<L extends LayoutConfig, R> {
    private final @Nullable Element<L, R> parent;
    protected final ArrayList<Element<L, R>> children = new ArrayList<>();
    protected final Attributes attributes;

    public Element(@Nullable Element<L, R> parent, Attributes attributes) {
        if (parent != null) parent.getChildren().add(this);
        this.parent = parent;
        this.attributes = attributes;
    }

    public void addText(String text) {
        // This only has to be implemented by elements who use the text
    }

    public @Nullable Element<L, R> getParent() {
        return parent;
    }

    public ArrayList<Element<L, R>> getChildren() {
        return children;
    }

    public abstract void draw(float x, float y, float width, R renderConfig);
}
