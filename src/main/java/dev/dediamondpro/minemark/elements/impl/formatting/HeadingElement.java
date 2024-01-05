package dev.dediamondpro.minemark.elements.impl.formatting;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HeadingElement<S extends Style, R> extends ChildBasedElement<S, R> {
    protected final HeadingType headingType;

    public HeadingElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        this.headingType = HeadingType.getFromHtmlTag(qName);
        this.layoutStyle = this.layoutStyle.clone();
        this.layoutStyle.setFontSize(headingType.getFontSize(style));
    }

    @Override
    public String toString() {
        return "HeadingElement {" + headingType + "}";
    }

    public enum HeadingType {
        H1, H2, H3, H4, H5, H6;

        public float getFontSize(Style style) {
            switch (this) {
                case H1:
                    return style.getHeaderStyle().getH1().getFontSize();
                case H2:
                    return style.getHeaderStyle().getH2().getFontSize();
                case H3:
                    return style.getHeaderStyle().getH3().getFontSize();
                case H4:
                    return style.getHeaderStyle().getH4().getFontSize();
                case H5:
                    return style.getHeaderStyle().getH5().getFontSize();
                case H6:
                    return style.getHeaderStyle().getH6().getFontSize();
            }
            throw new IllegalStateException("This should literally never be thrown.");
        }

        public static HeadingType getFromHtmlTag(String tag) {
            for (HeadingType type : HeadingType.values()) {
                if (tag.equals(type.name().toLowerCase())) {
                    return type;
                }
            }
            throw new IllegalStateException("Unknown heading tag  \" " + tag + "\"");
        }
    }
}
