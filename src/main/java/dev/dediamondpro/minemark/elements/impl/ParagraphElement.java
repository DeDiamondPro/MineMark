package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.config.LayoutConfig;
import dev.dediamondpro.minemark.config.RenderConfig;
import dev.dediamondpro.minemark.elements.Element;
import org.xml.sax.Attributes;

public class ParagraphElement<L extends LayoutConfig, R extends RenderConfig> extends Element<L, R> {
    public ParagraphElement(Element<L, R> parent, Attributes attributes) {
        super(parent, attributes);
    }

    @Override
    public void addText(String text) {
        // TODO: Make text work
        /*TextElement<L, R> element = new TextElement<>(this, attributes);
        element.addText(text);*/
    }

    @Override
    public void draw(float x, float y, float width,R renderConfig) {
        for (Element<L, R> child : children) {
            child.draw(x, y, width, renderConfig);
        }
    }

    @Override
    public String toString() {
        return children.toString();
    }
}
