package dev.dediamondpro.minemark.elements;

import dev.dediamondpro.minemark.LayoutConfig;
import org.xml.sax.Attributes;

public final class MineMarkElement<L extends LayoutConfig, R> extends Element<L, R> {
    public MineMarkElement(Attributes attributes) {
        super(null, attributes);
    }

    @Override
    public void draw(float x, float y, float width, R renderConfig) {

    }
}
