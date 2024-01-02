package dev.dediamondpro.minemark.elements.impl.formatting;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class FormattingElement<L extends LayoutConfig, R> extends ChildBasedElement<L, R> implements Inline {
    public FormattingElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
        this.layoutConfig = cloneLayoutConfig(layoutConfig);
        switch (qName) {
            case "strong":
                this.layoutConfig.setBold(true);
                break;
            case "em":
                this.layoutConfig.setItalic(true);
                break;
            case "ins":
                this.layoutConfig.setUnderlined(true);
                break;
            case "del":
                this.layoutConfig.setStrikethrough(true);
                break;
        }
    }

    @Override
    public String toString() {
        return "FormattingElement {" + qName + "}";
    }
}
