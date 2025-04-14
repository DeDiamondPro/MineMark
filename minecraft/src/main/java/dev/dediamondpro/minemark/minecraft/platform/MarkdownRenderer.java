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

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

public class MarkdownRenderer {
    private final GuiGraphics context;

    public MarkdownRenderer(GuiGraphics context) {
        this.context = context;
    }

    public void push() {
        context.pose().pushPose();
    }

    public void pop() {
        context.pose().popPose();
    }

    public void scale(float x, float y, float z) {
        context.pose().scale(x, y, z);
    }

    public void drawText(String text, int x, int y, float scale, int color, boolean hasShadow) {
        Font font = Minecraft.getInstance().font;
        push();
        scale(scale, scale, 1f);
        context.drawString(font, text, (int) (x / scale), (int) (y / scale), color, hasShadow);
        pop();
    }

    public float getTextWidth(String text, float scale) {
        Font font = Minecraft.getInstance().font;
        return font.width(text) * scale;
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public void drawTexture(ResourceLocation identifier, int x, int y, int width, int height) {
        context.blit(identifier, x, y, 0, 0, width, height, width, height);
    }

    public GuiGraphics getDrawContext() {
        return context;
    }
}
