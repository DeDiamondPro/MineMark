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
            case "b":
                this.layoutConfig.setBold(true);
                break;
            case "em":
            case "i":
                this.layoutConfig.setItalic(true);
                break;
            case "ins":
            case "u":
                this.layoutConfig.setUnderlined(true);
                break;
            case "del":
            case "s":
                this.layoutConfig.setStrikethrough(true);
                break;
            case "pre":
                this.layoutConfig.setPreFormatted(true);
                break;
        }
    }

    @Override
    public String toString() {
        return "FormattingElement {" + qName + "}";
    }
}
