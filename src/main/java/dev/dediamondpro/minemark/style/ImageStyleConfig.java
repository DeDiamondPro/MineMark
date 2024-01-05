package dev.dediamondpro.minemark.style;

import dev.dediamondpro.minemark.providers.ImageProvider;

public class ImageStyleConfig {
    private final ImageProvider imageProvider;

    public ImageStyleConfig(ImageProvider imageProvider) {
        this.imageProvider = imageProvider;
    }

    public ImageProvider getImageProvider() {
        return imageProvider;
    }
}
