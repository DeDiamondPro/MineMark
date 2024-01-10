/*
 * This file is part of MineMark
 * Copyright (C) 2024 DeDiamondPro
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License Version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.dediamondpro.minemark.providers;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DefaultImageProvider implements ImageProvider<BufferedImage> {
    public static final DefaultImageProvider INSTANCE = new DefaultImageProvider();

    protected DefaultImageProvider() {
    }

    @Override
    public void getImage(String src, Consumer<Dimension> dimensionCallback, Consumer<BufferedImage> imageCallback) {
        CompletableFuture.runAsync(() -> {
            try (ImageInputStream in = ImageIO.createImageInputStream(getInputStream(src))) {
                Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
                if (!readers.hasNext()) {
                    throw new IllegalStateException("No image reader found for " + src);
                }
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    dimensionCallback.accept(new Dimension(reader.getWidth(0), reader.getHeight(0)));
                    imageCallback.accept(reader.read(0));
                } finally {
                    reader.dispose();
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to download image " + src, e);
            }
        });
    }

    protected InputStream getInputStream(String src) throws IOException {
        HttpsURLConnection con = (HttpsURLConnection) new URL(src).openConnection();
        return con.getInputStream();
    }
}
