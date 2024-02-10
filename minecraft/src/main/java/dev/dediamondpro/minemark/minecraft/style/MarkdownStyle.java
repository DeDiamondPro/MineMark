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

package dev.dediamondpro.minemark.minecraft.style;

import dev.dediamondpro.minemark.minecraft.elements.MarkdownTextElement;
import dev.dediamondpro.minemark.minecraft.utils.MinecraftBrowserProvider;
import dev.dediamondpro.minemark.minecraft.utils.MinecraftImageProvider;
import dev.dediamondpro.minemark.providers.DefaultBrowserProvider;
import dev.dediamondpro.minemark.providers.DefaultImageProvider;
import dev.dediamondpro.minemark.style.*;

import java.awt.*;

public class MarkdownStyle implements Style {
    private static final Color LINE_COLOR = new Color(80, 80, 80);

    // Defaults
    public static final MarkdownTextStyle defaultTextStyle = new MarkdownTextStyle(1f, Color.WHITE, 2f, true);
    public static final ParagraphStyleConfig defaultParagraphStyle = new ParagraphStyleConfig(6f);
    public static final LinkStyleConfig defaultLinkStyle = new LinkStyleConfig(new Color(65, 105, 225), MinecraftBrowserProvider.INSTANCE);
    public static final HeadingStyleConfig defaultHeadingStyle = new HeadingStyleConfig(
            new HeadingLevelStyleConfig(2f, 12f, true, LINE_COLOR, 2f, 5f),
            new HeadingLevelStyleConfig(1.66f, 10f, true, LINE_COLOR, 2f, 5f),
            new HeadingLevelStyleConfig(1.33f, 8f),
            new HeadingLevelStyleConfig(1f, 6f),
            new HeadingLevelStyleConfig(0.7f, 4f),
            new HeadingLevelStyleConfig(0.7f, 4f)
    );
    public static final HorizontalRuleStyleConfig defaultHorizontalRuleStyle = new HorizontalRuleStyleConfig(2f, 4f, LINE_COLOR);
    public static final ImageStyleConfig defaultImageStyle = new ImageStyleConfig(MinecraftImageProvider.INSTANCE);
    public static final ListStyleConfig defaultListStyle = new ListStyleConfig(16f, 6f);
    public static final BlockquoteStyleConfig defaultBlockQuoteStyle = new BlockquoteStyleConfig(6f, 4f, 2f, 10f, LINE_COLOR);
    public static final CodeBlockStyleConfig defaultCodeBlockStyle = new CodeBlockStyleConfig(2f, 1f, 6f, 6f, LINE_COLOR);
    public static final TableStyleConfig defaultTableStyle = new TableStyleConfig(6f, 4f, 1f, LINE_COLOR, new Color(0, 0, 0, 150), new Color(0, 0, 0, 0));

    private final MarkdownTextStyle textStyle;
    private final ParagraphStyleConfig paragraphStyle;
    private final LinkStyleConfig linkStyle;
    private final HeadingStyleConfig headingStyle;
    private final HorizontalRuleStyleConfig horizontalRuleStyle;
    private final ImageStyleConfig imageStyle;
    private final ListStyleConfig listStyle;
    private final BlockquoteStyleConfig blockquoteStyle;
    private final CodeBlockStyleConfig codeBlockStyle;
    private final TableStyleConfig tableStyle;

    public MarkdownStyle(MarkdownTextStyle textStyle, ParagraphStyleConfig paragraphStyle, LinkStyleConfig linkStyle, HeadingStyleConfig headingStyle, HorizontalRuleStyleConfig horizontalRuleStyle, ImageStyleConfig imageStyle, ListStyleConfig listStyle, BlockquoteStyleConfig blockquoteStyle, CodeBlockStyleConfig codeBlockStyle, TableStyleConfig tableStyle) {
        this.textStyle = textStyle;
        this.paragraphStyle = paragraphStyle;
        this.linkStyle = linkStyle;
        this.headingStyle = headingStyle;
        this.horizontalRuleStyle = horizontalRuleStyle;
        this.imageStyle = imageStyle;
        this.listStyle = listStyle;
        this.blockquoteStyle = blockquoteStyle;
        this.codeBlockStyle = codeBlockStyle;
        this.tableStyle = tableStyle;
    }

    public MarkdownStyle() {
        this(defaultTextStyle, defaultParagraphStyle, defaultLinkStyle, defaultHeadingStyle, defaultHorizontalRuleStyle, defaultImageStyle, defaultListStyle, defaultBlockQuoteStyle, defaultCodeBlockStyle, defaultTableStyle);
    }

    @Override
    public MarkdownTextStyle getTextStyle() {
        return textStyle;
    }

    @Override
    public ParagraphStyleConfig getParagraphStyle() {
        return paragraphStyle;
    }

    @Override
    public LinkStyleConfig getLinkStyle() {
        return linkStyle;
    }

    @Override
    public HeadingStyleConfig getHeadingStyle() {
        return headingStyle;
    }

    @Override
    public HorizontalRuleStyleConfig getHorizontalRuleStyle() {
        return horizontalRuleStyle;
    }

    @Override
    public ImageStyleConfig getImageStyle() {
        return imageStyle;
    }

    @Override
    public ListStyleConfig getListStyle() {
        return listStyle;
    }

    @Override
    public BlockquoteStyleConfig getBlockquoteStyle() {
        return blockquoteStyle;
    }

    @Override
    public CodeBlockStyleConfig getCodeBlockStyle() {
        return codeBlockStyle;
    }

    @Override
    public TableStyleConfig getTableStyle() {
        return tableStyle;
    }
}
