package dev.dediamondpro.minemark.elements.impl.text;

import dev.dediamondpro.minemark.config.LayoutConfig;
import dev.dediamondpro.minemark.config.RenderConfig;
import dev.dediamondpro.minemark.elements.Element;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class AbstractTextElement<L extends LayoutConfig, R extends RenderConfig> extends Element<L, R> {
    protected String text;

    public AbstractTextElement(@Nullable Element<L, R> parent, Attributes attributes) {
        super(parent, attributes);
    }

    @Override
    public void addText(String text) {
        this.text = text;
    }
}
