package dev.dediamondpro.minemark.elements.impl.formatting;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HeadingElement<L extends LayoutConfig, R> extends ChildBasedElement<L, R> {
    protected final HeadingType headingType;

    public HeadingElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
        this.headingType = HeadingType.getFromHtmlTag(qName);
        this.layoutConfig = cloneLayoutConfig(layoutConfig);
        this.layoutConfig.setFontSize(headingType.getFontSize(this.layoutConfig.getHeadingConfig()));
    }

    @Override
    public String toString() {
        return "HeadingElement {" + headingType + "}";
    }

    public enum HeadingType {
        H1, H2, H3, H4, H5, H6;

        public float getFontSize(LayoutConfig.HeadingConfig config) {
            switch (this) {
                case H1:
                    return config.getH1FontSize();
                case H2:
                    return config.getH2FontSize();
                case H3:
                    return config.getH3FontSize();
                case H4:
                    return config.getH4FontSize();
                case H5:
                    return config.getH5FontSize();
                case H6:
                    return config.getH6FontSize();
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
