package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class ParagraphElement<S extends Style, R> extends ChildBasedElement<S, R> {

    public ParagraphElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected float getPadding(LayoutData layoutData) {
        return style.getParagraphStyle().getPadding();
    }
}
