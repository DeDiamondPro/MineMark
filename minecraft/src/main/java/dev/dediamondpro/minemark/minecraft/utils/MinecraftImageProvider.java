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

package dev.dediamondpro.minemark.minecraft.utils;

import dev.dediamondpro.minemark.minecraft.platform.MarkdownDynamicImage;
import dev.dediamondpro.minemark.providers.ImageProvider;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MinecraftImageProvider implements ImageProvider<MarkdownDynamicImage> {
    public static final MinecraftImageProvider INSTANCE = new MinecraftImageProvider();

    private MinecraftImageProvider() {
    }

    @Override
    public void getImage(String src, Consumer<Dimension> dimensionCallback, Consumer<MarkdownDynamicImage> imageCallback) {
        CompletableFuture.runAsync(() -> {
            try {
                BufferedImage bufferedImage = ImageIO.read(getInputStream(src));
                dimensionCallback.accept(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));

                NativeImage image = new NativeImage(bufferedImage.getWidth(), bufferedImage.getHeight(), true);
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    for (int x = 0; x < bufferedImage.getWidth(); x++) {
                        image.setColor(x, y, bufferedImage.getRGB(x, y));
                    }
                }

                imageCallback.accept(MarkdownDynamicImage.of(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, Util.getIoWorkerExecutor());
    }

    public InputStream getInputStream(String src) throws IOException {
        return new URL(src).openStream();
    }
}
