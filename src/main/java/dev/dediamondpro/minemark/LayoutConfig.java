package dev.dediamondpro.minemark;

import dev.dediamondpro.minemark.providers.BrowserProvider;
import dev.dediamondpro.minemark.providers.DefaultBrowserProvider;
import dev.dediamondpro.minemark.providers.DefaultImageProvider;
import dev.dediamondpro.minemark.providers.ImageProvider;

import java.awt.*;

/**
 * Class that will be given to an element at parsing time
 * Default variables used to control the layout
 */
public class LayoutConfig {
    private Alignment alignment;
    private float fontSize;
    private Color textColor;
    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private boolean strikethrough;
    private boolean partOfLink;
    private boolean preFormatted;
    private final SpacingConfig spacingConfig;
    private final HeadingConfig headingConfig;
    private final ImageProvider imageProvider;
    private final BrowserProvider browserProvider;

    public LayoutConfig(Alignment alignment, float fontSize, Color textColor, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean partOfLink, boolean preFormatted, SpacingConfig spacingConfig, HeadingConfig headingConfig, ImageProvider imageProvider, BrowserProvider browserProvider) {
        this.alignment = alignment;
        this.fontSize = fontSize;
        this.textColor = textColor;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.partOfLink = partOfLink;
        this.preFormatted = preFormatted;
        this.strikethrough = strikethrough;
        this.spacingConfig = spacingConfig;
        this.headingConfig = headingConfig;
        this.imageProvider = imageProvider;
        this.browserProvider = browserProvider;
    }

    public LayoutConfig(float defaultFontSize, SpacingConfig spacingConfig, HeadingConfig headingConfig, ImageProvider imageProvider, BrowserProvider browserProvider) {
        this(Alignment.LEFT, defaultFontSize, Color.WHITE, false, false, false, false, false, false, spacingConfig, headingConfig, imageProvider, browserProvider);
    }

    public LayoutConfig(float defaultFontSize, SpacingConfig spacingConfig, HeadingConfig headingConfig) {
        this(defaultFontSize, spacingConfig, headingConfig, DefaultImageProvider.INSTANCE, DefaultBrowserProvider.INSTANCE);
    }

    public LayoutConfig clone() {
        return new LayoutConfig(alignment, fontSize, new Color(textColor.getRGB()), bold, italic, underlined, strikethrough, partOfLink, preFormatted, spacingConfig, headingConfig, imageProvider, browserProvider);
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public void setUnderlined(boolean underlined) {
        this.underlined = underlined;
    }

    public boolean isStrikethrough() {
        return strikethrough;
    }

    public void setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
    }

    public boolean isPartOfLink() {
        return partOfLink;
    }

    public void setPartOfLink(boolean partOfLink) {
        this.partOfLink = partOfLink;
    }

    public boolean isPreFormatted() {
        return preFormatted;
    }

    public void setPreFormatted(boolean preFormatted) {
        this.preFormatted = preFormatted;
    }

    public SpacingConfig getSpacingConfig() {
        return spacingConfig;
    }

    public HeadingConfig getHeadingConfig() {
        return headingConfig;
    }

    public ImageProvider getImageProvider() {
        return imageProvider;
    }

    public BrowserProvider getBrowserProvider() {
        return browserProvider;
    }

    public enum Alignment {
        CENTER, LEFT, RIGHT
    }

    public static class SpacingConfig {
        private final float textSpacing;
        private final float paragraphPadding;
        private final float listPadding;
        private final float listIndentSpacing;

        public SpacingConfig(float textSpacing, float paragraphPadding, float listPadding, float listIndentSpacing) {
            this.textSpacing = textSpacing;
            this.paragraphPadding = paragraphPadding;
            this.listPadding = listPadding;
            this.listIndentSpacing = listIndentSpacing;
        }

        public float getTextSpacing() {
            return textSpacing;
        }

        public float getParagraphPadding() {
            return paragraphPadding;
        }

        public float getListPadding() {
            return listPadding;
        }

        public float getListIndentSpacing() {
            return listIndentSpacing;
        }
    }

    public static class HeadingConfig {
        private final float h1FontSize;
        private final float h2FontSize;
        private final float h3FontSize;
        private final float h4FontSize;
        private final float h5FontSize;
        private final float h6FontSize;

        public HeadingConfig(float h1FontSize, float h2FontSize, float h3FontSize, float h4FontSize, float h5FontSize, float h6FontSize) {
            this.h1FontSize = h1FontSize;
            this.h2FontSize = h2FontSize;
            this.h3FontSize = h3FontSize;
            this.h4FontSize = h4FontSize;
            this.h5FontSize = h5FontSize;
            this.h6FontSize = h6FontSize;
        }

        public float getH1FontSize() {
            return h1FontSize;
        }

        public float getH2FontSize() {
            return h2FontSize;
        }

        public float getH3FontSize() {
            return h3FontSize;
        }

        public float getH4FontSize() {
            return h4FontSize;
        }

        public float getH5FontSize() {
            return h5FontSize;
        }

        public float getH6FontSize() {
            return h6FontSize;
        }
    }
}
