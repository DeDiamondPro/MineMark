package dev.dediamondpro.minemark.providers;

import java.net.URI;
import java.net.URISyntaxException;

public class DefaultBrowserProvider implements BrowserProvider {
    public static final DefaultBrowserProvider INSTANCE = new DefaultBrowserProvider();

    protected DefaultBrowserProvider() {
    }

    @Override
    public void browse(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(url));
        } catch (java.io.IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
