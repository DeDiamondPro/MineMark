package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.BasicElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.providers.ImageProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public abstract class ImageElement<L extends LayoutConfig, R> extends BasicElement<L, R> implements Inline {
    private static final Pattern pixelPattern = Pattern.compile("^\\d+(px)?$");
    protected float imageWidth = -1;
    protected float imageHeight = -1;
    protected float width;
    protected float height;
    protected final String src;
    protected BufferedImage image = null;

    public ImageElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
        assert attributes != null;
        this.src = attributes.getValue("src");
        layoutConfig.getImageProvider().getImage(src, this::onDimensionsReceived, this::onImageReceived);
    }

    protected void onDimensionsReceived(ImageProvider.Dimension dimension) {
        imageWidth = dimension.getWidth();
        imageHeight = dimension.getHeight();
        regenerateLayout();
    }

    protected void onImageReceived(BufferedImage image) {
        this.image = image;
    }

    @Override
    protected void generateLayout(LayoutData layoutData) {
        calculateDimensions(layoutData);
        super.generateLayout(layoutData);
    }

    @Override
    protected void drawElement(float x, float y, R renderData) {
        if (image == null) return;
        drawImage(image, x, y, width, height, renderData);
    }

    public abstract void drawImage(BufferedImage image, float x, float y, float width, float height, R renderData);

    @Override
    protected float getWidth(LayoutData layoutData) {
        return width;
    }

    @Override
    protected float getHeight(LayoutData layoutData) {
        return height;
    }

    protected void calculateDimensions(LayoutData layoutData) {
        // We already have a width and height for this image and don't need to update it
        if (width > 0 && height > 0) return;
        width = -1;
        height = -1;
        String desiredWidth = attributes.getValue("width");
        String desiredHeight = attributes.getValue("height");
        // We don't support % for height because the total height is dependent on the height of this element and vice versa
        if (desiredHeight != null && pixelPattern.matcher(desiredHeight).matches()) {
            height = Float.parseFloat(desiredHeight.replaceAll("[^0-9]", ""));
        }
        if (desiredWidth != null) {
            if (pixelPattern.matcher(desiredWidth).matches()) {
                width = Float.parseFloat(desiredWidth.replaceAll("[^0-9]", ""));
            } else if (desiredWidth.endsWith("%")) {
                width = (Float.parseFloat(desiredWidth.replaceAll("[^0-9]", "")) / 100f) * layoutData.getMaxWidth();
            }
            if (width > layoutData.getMaxWidth()) {
                width = layoutData.getMaxWidth();
                // Since the width changes, we can no longer rely on the aspect ratio to be correct
                height = -1;
            }
        }
        // Sizes were provided
        if (width != -1 && height != -1) return;
        // Unable to determine size, image width and height not yet loaded
        if (imageWidth == -1 || imageHeight == -1) {
            if (width == -1) width = 0;
            if (height == -1) height = 0;
            return;
        }
        if (width == -1 && height == -1) {
            width = Math.min(imageWidth, layoutData.getMaxWidth());
        }
        // Find the missing element based on aspect ratio of the image
        if (width == -1) {
            width = (height * imageWidth) / imageHeight;
        }
        if (height == -1) {
            height = (width * imageHeight) / imageWidth;
        }
    }

    @Override
    public String toString() {
        return "ImageElement {" + src + ", " + width + "x" + height + "}";
    }
}
