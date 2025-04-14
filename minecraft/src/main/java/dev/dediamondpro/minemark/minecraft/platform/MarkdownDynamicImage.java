/*
 * This file is part of MineMark
 * Copyright (C) 2024-2025 DeDiamondPro
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

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.Closeable;
import java.io.IOException;

public class MarkdownDynamicImage implements Closeable {
    private static long identifierCounter = 0;

    private NativeImage image;
    private ResourceLocation identifier = null;

    public MarkdownDynamicImage(NativeImage image) {
        this.image = image;
    }

    public void draw(int x, int y, int width, int height, MarkdownRenderer renderer) {
        if (identifier == null) {
            // Upload here to make sure we are on the correct thread, this can be an issue on 1.21.5 for some reason
            DynamicTexture texture = new DynamicTexture(/*? if >=1.21.5 {*/ null, /*?}*/ image);
            //? if <1.21 {
            /*ResourceLocation identifier = new ResourceLocation("minemark", "dynamic-image-" + (identifierCounter++));
             *///?} else
            ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath("minemark", "dynamic-image-" + (identifierCounter++));
            Minecraft.getInstance().getTextureManager().register(identifier, texture);
            this.identifier = identifier;
            image = null;
        }
        renderer.drawTexture(identifier, x, y, width, height);
    }

    @Override
    public void close() {
        Minecraft.getInstance().getTextureManager().release(identifier);
    }
}
