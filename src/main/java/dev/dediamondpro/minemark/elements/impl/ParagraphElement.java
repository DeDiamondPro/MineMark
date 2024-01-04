package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class ParagraphElement<L extends LayoutConfig, R> extends ChildBasedElement<L, R> {
    public ParagraphElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
    }

    @Override
    protected float getPadding(LayoutData layoutData) {
        return layoutConfig.getSpacingConfig().getParagraphPadding();
    }
}
