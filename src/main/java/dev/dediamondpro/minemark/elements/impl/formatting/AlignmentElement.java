package dev.dediamondpro.minemark.elements.impl.formatting;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class AlignmentElement<L extends LayoutConfig, R> extends ChildBasedElement<L, R> implements Inline {
    protected String alignment;

    public AlignmentElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
        this.layoutConfig = cloneLayoutConfig(layoutConfig);
        alignment = qName.equals("div") ? (attributes != null ? attributes.getValue("align") : null) : qName;
        if (alignment == null) return;
        switch (alignment) {
            case "left":
                this.layoutConfig.setAlignment(LayoutConfig.Alignment.LEFT);
                break;
            case "center":
                this.layoutConfig.setAlignment(LayoutConfig.Alignment.CENTER);
                break;
            case "right":
                this.layoutConfig.setAlignment(LayoutConfig.Alignment.RIGHT);
                break;
        }
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {
        if (!layoutData.isLineEmpty()) layoutData.nextLine();
        super.generateLayout(layoutData);
    }

    @Override
    public String toString() {
        return "AlignmentElement {" + qName + "}";
    }
}
