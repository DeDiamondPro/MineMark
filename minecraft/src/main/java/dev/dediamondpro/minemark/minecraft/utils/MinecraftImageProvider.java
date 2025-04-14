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

import com.mojang.blaze3d.platform.NativeImage;
import dev.dediamondpro.minemark.minecraft.platform.MarkdownDynamicImage;
import dev.dediamondpro.minemark.providers.ImageProvider;
import net.minecraft.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", out);
                NativeImage nativeImage = NativeImage.read(new ByteArrayInputStream(out.toByteArray()));
                imageCallback.accept(new MarkdownDynamicImage(nativeImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, Util.ioPool());
    }

    public InputStream getInputStream(String src) throws IOException {
        return new URL(src).openStream();
    }
}
