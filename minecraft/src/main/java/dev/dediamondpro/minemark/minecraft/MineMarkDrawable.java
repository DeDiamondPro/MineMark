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

package dev.dediamondpro.minemark.minecraft;

import dev.dediamondpro.minemark.MineMarkCore;
import dev.dediamondpro.minemark.MineMarkCoreBuilder;
import dev.dediamondpro.minemark.elements.Elements;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import dev.dediamondpro.minemark.minecraft.elements.*;
import dev.dediamondpro.minemark.minecraft.platform.MarkdownRenderer;
import dev.dediamondpro.minemark.minecraft.style.MarkdownStyle;
import dev.dediamondpro.minemark.utils.MouseButton;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.xml.sax.SAXException;

//#if MC >= 12000
import net.minecraft.client.gui.DrawContext;
//#else
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif

import java.io.IOException;
import java.io.Reader;

public class MineMarkDrawable {
    public static final MineMarkCore<MarkdownStyle, MarkdownRenderer> DEFAULT_CORE = addMinecraftExtensions(MineMarkCore.builder())
            .addExtension(StrikethroughExtension.create())
            .addExtension(TablesExtension.create())
            .build();
    private final MineMarkElement<MarkdownStyle, MarkdownRenderer> parsedMarkdown;

    public MineMarkDrawable(MineMarkElement<MarkdownStyle, MarkdownRenderer> parsedMarkdown) {
        this.parsedMarkdown = parsedMarkdown;
    }

    public MineMarkDrawable(String markdown, MarkdownStyle style, MineMarkCore<MarkdownStyle, MarkdownRenderer> core) throws IOException, SAXException {
        this(core.parse(style, markdown));
    }

    public MineMarkDrawable(String markdown, MarkdownStyle style) throws IOException, SAXException {
        this(markdown, style, DEFAULT_CORE);
    }

    public MineMarkDrawable(String markdown) throws IOException, SAXException {
        this(markdown, new MarkdownStyle());
    }

    public MineMarkDrawable(Reader markdown, MarkdownStyle style, MineMarkCore<MarkdownStyle, MarkdownRenderer> core) throws IOException, SAXException {
        this(core.parse(style, markdown));
    }

    public MineMarkDrawable(Reader markdown, MarkdownStyle style) throws IOException, SAXException {
        this(markdown, style, DEFAULT_CORE);
    }

    public MineMarkDrawable(Reader markdown) throws IOException, SAXException {
        this(markdown, new MarkdownStyle());
    }

    public void draw(float x, float y, float width, float mouseX, float mouseY,
                     //#if MC >= 12000
                     DrawContext drawContext
                     //#else
                     //$$ MatrixStack drawContext
                     //#endif
    ) {
        parsedMarkdown.draw(x, y, width, mouseX, mouseY, new MarkdownRenderer(drawContext));
    }

    public void beforeDraw(float x, float y, float width, float mouseX, float mouseY,
                           //#if MC >= 12000
                           DrawContext drawContext
                           //#else
                           //$$ MatrixStack drawContext
                           //#endif
    ) {
        parsedMarkdown.beforeDraw(x, y, width, mouseX, mouseY, new MarkdownRenderer(drawContext));
    }

    public void onMouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        MouseButton mouseButton;
        switch (button) {
            case 0:
            default:
                mouseButton = MouseButton.LEFT;
                break;
            case 1:
                mouseButton = MouseButton.RIGHT;
                break;
            case 2:
                mouseButton = MouseButton.MIDDLE;
                break;
        }
        parsedMarkdown.onMouseClicked(x, y, mouseButton, mouseX, mouseY);
    }

    public MineMarkElement<MarkdownStyle, MarkdownRenderer> getParsedMarkdown() {
        return parsedMarkdown;
    }

    public float getHeight() {
        return parsedMarkdown.getHeight();
    }

    public void close() {
        parsedMarkdown.close();
    }

    public static MineMarkCoreBuilder<MarkdownStyle, MarkdownRenderer> addMinecraftExtensions(MineMarkCoreBuilder<MarkdownStyle, MarkdownRenderer> core) {
        return core.setTextElement(MarkdownTextElement::new)
                .addElement(Elements.HEADING, MarkdownHeadingElement::new)
                .addElement(Elements.CODE_BLOCK, MarkdownCodeBlockElement::new)
                .addElement(Elements.BLOCKQUOTE, MarkdownBlockQuoteElement::new)
                .addElement(Elements.LIST_ELEMENT, MarkdownListElement::new)
                .addElement(Elements.HORIZONTAL_RULE, MarkdownHorizontalRuleElement::new)
                .addElement(Elements.TABLE_CELL, MarkdownTableCellElement::new)
                .addElement(Elements.IMAGE, MarkdownImageElement::new);
    }
}
