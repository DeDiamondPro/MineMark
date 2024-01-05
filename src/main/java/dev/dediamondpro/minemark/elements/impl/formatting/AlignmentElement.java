package dev.dediamondpro.minemark.elements.impl.formatting;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class AlignmentElement<S extends Style, R> extends ChildBasedElement<S, R> implements Inline {
    protected String alignment;

    public AlignmentElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        this.layoutStyle = this.layoutStyle.clone();
        alignment = qName.equals("div") ? (attributes != null ? attributes.getValue("align") : null) : qName;
        if (alignment == null) return;
        switch (alignment) {
            case "left":
                this.layoutStyle.setAlignment(LayoutStyle.Alignment.LEFT);
                break;
            case "center":
                this.layoutStyle.setAlignment(LayoutStyle.Alignment.CENTER);
                break;
            case "right":
                this.layoutStyle.setAlignment(LayoutStyle.Alignment.RIGHT);
                break;
        }
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {
        if (layoutData.isLineOccupied()) layoutData.nextLine();
        super.generateLayout(layoutData);
    }

    @Override
    public String toString() {
        return "AlignmentElement {" + qName + "}";
    }
}
