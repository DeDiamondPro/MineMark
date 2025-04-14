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

package com.example.examplemod;

import dev.dediamondpro.minemark.minecraft.MineMarkDrawable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.xml.sax.SAXException;


import java.io.IOException;

public class MarkdownTestGui extends Screen {
    private final MineMarkDrawable markdown;

    protected MarkdownTestGui() {
        super(Component.literal("Markdown Test GUI"));
        try {
            String markdownString = "Test **string** *with* a<br>newline <u>because</u> ~~why~~ not and also a [link](https://example.com), this should also automatically wrap if I make this text long enough<br>"
                    + "Image: ![](https://picsum.photos/2000/1000)"
                    + "\n# Heading 1"
                    + "\n## Heading 2"
                    + "\n### Heading 3"
                    + "\n#### Heading 4"
                    + "\n##### Heading 5"
                    + "\n---"
                    + "\nThis `is an inline` codeblock!"
                    + "\n```\nAnd this one isn't inline\nand has multiple lines!\n```"
                    + "\n> And this is a blockquote<br>because why not?"
                    + "\n1. And an ordered list"
                    + "\n   - With an unordered list inside"
                    + "\n   - like this"
                    + "\n2. :)"
                    + "\n\n| Hi | Test | E       | E | E |\n" +
                    "|----|------|---------|---|---|\n" +
                    "| E  | E    | E       | E |   |\n" +
                    "| E  | E    | EEEEEEE |   | E |\n" +
                    "| E  | E    | E       | E |   |";
            markdown = new MineMarkDrawable(markdownString);
            // System.out.println(markdown.getParsedMarkdown().getTree());
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        markdown.draw(20, 20, 300, mouseX, mouseY, context);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        markdown.onMouseClicked(20, 20, (float) mouseX, (float) mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        markdown.close();
        super.onClose();
    }
}
