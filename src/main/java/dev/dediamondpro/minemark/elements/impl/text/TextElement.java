package dev.dediamondpro.minemark.elements.impl.text;

import dev.dediamondpro.minemark.config.LayoutConfig;
import dev.dediamondpro.minemark.config.RenderConfig;
import dev.dediamondpro.minemark.elements.Element;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class TextElement<L extends LayoutConfig, R extends RenderConfig> extends AbstractTextElement<L, R> {

    public TextElement(@Nullable Element<L, R> parent, Attributes attributes) {
        super(parent, attributes);
    }

    @Override
    public String toString() {
        return text;
    }
}
