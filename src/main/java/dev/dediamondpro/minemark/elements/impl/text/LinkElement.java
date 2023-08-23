package dev.dediamondpro.minemark.elements.impl.text;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.elements.Element;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public abstract class LinkElement<L extends LayoutConfig, R> extends AbstractTextElement<L, R> {
    private final String link;

    public LinkElement(@Nullable Element<L, R> parent, Attributes attributes) {
        super(parent, attributes);
        this.link = attributes.getValue("href");
    }

    @Override
    public String toString() {
        return text + "=" + link;
    }
}
