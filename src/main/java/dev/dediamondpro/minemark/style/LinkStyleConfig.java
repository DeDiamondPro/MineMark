package dev.dediamondpro.minemark.style;

import dev.dediamondpro.minemark.providers.BrowserProvider;

import java.awt.*;

public class LinkStyleConfig {
    private final Color textColor;
    private final BrowserProvider browserProvider;

    public LinkStyleConfig(Color textColor, BrowserProvider browserProvider) {
        this.textColor = textColor;
        this.browserProvider = browserProvider;
    }

    public Color getTextColor() {
        return textColor;
    }

    public BrowserProvider getBrowserProvider() {
        return browserProvider;
    }
}
