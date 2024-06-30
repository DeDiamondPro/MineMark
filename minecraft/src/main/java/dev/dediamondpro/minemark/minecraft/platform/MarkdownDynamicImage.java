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

package dev.dediamondpro.minemark.minecraft.platform;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.Closeable;
import java.io.IOException;

public class MarkdownDynamicImage implements Closeable {
    private final Identifier identifier;

    public MarkdownDynamicImage(Identifier identifier) {
        this.identifier = identifier;
    }

    public void draw(int x, int y, int width, int height, MarkdownRenderer renderer) {
        renderer.drawTexture(identifier, x, y, width, height);
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().getTextureManager().destroyTexture(identifier);
    }

    public static MarkdownDynamicImage of(NativeImage image) throws IOException {
        NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
        Identifier identifier = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("minemark", texture);
        return new MarkdownDynamicImage(identifier);
    }
}
