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
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

//#if MC >= 12000
import net.minecraft.client.gui.DrawContext;
//#else
//$$ import net.minecraft.client.gui.DrawableHelper;
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//#endif

public class MarkdownRenderer {
    //#if MC >= 12000
    private final DrawContext context;
    //#else
    //$$ private final MatrixStack matrices;
    //#endif

    //#if MC >= 12000
    public MarkdownRenderer(DrawContext context) {
        this.context = context;
    }
    //#else
    //$$ public MarkdownRenderer(MatrixStack matrices) {
    //$$     this.matrices = matrices;
    //$$ }
    //#endif

    public void push() {
        getMatrices().push();
    }

    public void pop() {
        getMatrices().pop();
    }

    public void scale(float x, float y, float z) {
        getMatrices().scale(x, y, z);
    }

    public void drawText(String text, int x, int y, float scale, int color, boolean hasShadow) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        push();
        scale(scale, scale, 1f);
        //#if MC >= 12000
        context.drawText(textRenderer, text, (int) (x / scale), (int) (y / scale), color, hasShadow);
        //#else
        //$$ if (hasShadow) {
        //$$     textRenderer.drawWithShadow(matrices, text,(int) (x / scale), (int) (y / scale), color);
        //$$ } else {
        //$$     textRenderer.draw(matrices, text, (int) (x / scale), (int) (y / scale), color);
        //$$ }
        //#endif
        pop();
    }

    public float getTextWidth(String text, float scale) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        return textRenderer.getWidth(text) * scale;
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        //#if MC >= 12000
        context.fill(x, y, x + width, y + height, color);
        //#else
        //$$ DrawableHelper.fill(matrices, x, y, x + width, y + height, color);
        //#endif
    }

    public void drawTexture(Identifier identifier, int x, int y, int width, int height) {
        //#if MC >= 12000
        context.drawTexture(identifier, x, y, 0, 0, width, height, width, height);
        //#else
        //$$ RenderSystem.setShaderTexture(0, identifier);
        //$$ DrawableHelper.drawTexture(matrices, x, y, 0, 0, width, height, width, height);
        //#endif
    }

    public MatrixStack getMatrices() {
        //#if MC >= 12000
        return context.getMatrices();
        //#else
        //$$ return matrices;
        //#endif
    }
}
