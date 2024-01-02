package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class ParagraphElement<L extends LayoutConfig, R> extends ChildBasedElement<L, R> {
    public ParagraphElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {
        if (!layoutData.isLineEmpty()) {
            layoutData.nextLine();
        }
        layoutData.setLineHeight(layoutConfig.getPaddingConfig().getParagraphPadding());
        layoutData.nextLine();
        super.generateLayout(layoutData);
        layoutData.setLineHeight(layoutConfig.getPaddingConfig().getParagraphPadding());
        layoutData.nextLine();
    }
}
