package dev.dediamondpro.minemark.providers;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public interface ImageProvider {

    /**
     * Function to get an image, image is returned with callbacks
     *
     * @param src               The source URL of the image
     * @param dimensionCallback Callback for when the dimensions of the image are received
     * @param imageCallback     Callback for when the whole image is received
     */
    void getImage(String src, Consumer<Dimension> dimensionCallback, Consumer<BufferedImage> imageCallback);

    class Dimension {
        private final float width;
        private final float height;

        public Dimension(float width, float height) {
            this.width = width;
            this.height = height;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }
    }
}
