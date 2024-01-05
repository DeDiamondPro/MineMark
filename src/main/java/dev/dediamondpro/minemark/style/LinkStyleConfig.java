package dev.dediamondpro.minemark.style;

import dev.dediamondpro.minemark.providers.BrowserProvider;

import java.awt.*;

public class LinkStyleConfig {
    private final Color textColor;
    private final boolean underlineHovered;
    private final BrowserProvider browserProvider;

    public LinkStyleConfig(Color textColor, boolean underlineHovered, BrowserProvider browserProvider) {
        this.textColor = textColor;
        this.underlineHovered = underlineHovered;
        this.browserProvider = browserProvider;
    }

    public Color getTextColor() {
        return textColor;
    }

    public boolean isUnderlineHovered() {
        return underlineHovered;
    }

    public BrowserProvider getBrowserProvider() {
        return browserProvider;
    }
}
