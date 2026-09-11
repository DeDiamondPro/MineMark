/*
 * This file is part of MineMark
 * Copyright (C) 2024-2026 DeDiamondPro
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

package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.data.ViewPort;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class TextElement<S extends Style, R> extends Element<S, R> implements Inline {
    protected final Pattern LEADING_WHITESPACE = Pattern.compile("^ +");
    protected final HashMap<LayoutData.MarkDownElementPosition, String> lines = new HashMap<>();
    protected final String text;
    protected float baseLineHeight;
    protected float ascender;
    protected float descender;
    protected float leftX = Float.POSITIVE_INFINITY;
    protected float topY = Float.POSITIVE_INFINITY;
    protected float rightX = Float.NEGATIVE_INFINITY;
    protected float bottomY = Float.NEGATIVE_INFINITY;

    public TextElement(@NotNull String text, @NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        this.text = text;
    }

    @Override
    public void generateLayout(LayoutData layoutData, R renderData) {
        // Reset values
        lines.clear();
        ArrayList<String> allLines = new ArrayList<>();
        leftX = Float.POSITIVE_INFINITY;
        topY = Float.POSITIVE_INFINITY;
        rightX = Float.NEGATIVE_INFINITY;
        bottomY = Float.NEGATIVE_INFINITY;

        String[] predefinedLines = text.split("\n", -1);
        for (int i = 0; i < predefinedLines.length; i++) {
            String line = predefinedLines[i].replace("\n", "");
            allLines.addAll(wrapText(line, i == 0 ? layoutData.getX() : 0f, layoutData.getMaxWidth(), renderData));
        }

        float codeBlockPadding = layoutStyle.get(LayoutStyle.PART_OF_CODE_BLOCK) ? style.getCodeBlockStyle().getInlinePaddingTopBottom() : 0f;
        float padding = Math.max(style.getTextStyle().getPadding(), codeBlockPadding);
        float fontSize = layoutStyle.get(LayoutStyle.FONT_SIZE);
        baseLineHeight = getBaselineHeight(fontSize, renderData);
        ascender = getAscender(fontSize, renderData);
        descender = getDescender(fontSize, renderData);
        // The inline code block bg is drawn bigger than the line, needs to be part of culling bounds
        float backgroundPadding = getInlineCodeBlockPadding();
        float topBound = Math.max(ascender, backgroundPadding);
        float bottomBound = Math.max(descender, backgroundPadding);

        for (int i = 0; i < allLines.size(); i++) {
            String line = allLines.get(i);
            layoutData.updatePadding(padding);
            LayoutData.MarkDownElementPosition position = layoutData.addElement(layoutStyle.get(LayoutStyle.ALIGNMENT), getAdjustedTextWidth(line, fontSize, renderData), baseLineHeight);
            lines.put(position, line);
            // Update text bounds
            leftX = Math.min(leftX, position.getX());
            rightX = Math.max(rightX, position.getRightX());
            topY = Math.min(topY, position.getY() - topBound);
            bottomY = Math.max(bottomY, position.getBottomY() + bottomBound);

            if (i != allLines.size() - 1) {
                layoutData.nextLine();
            }
        }
    }

    @Override
    public void drawInternal(float xOffset, float yOffset, float mouseX, float mouseY, @Nullable ViewPort viewPort, R renderData) {
        boolean hovered = false;
        for (LayoutData.MarkDownElementPosition position : lines.keySet()) {
            if (position.isInside(mouseX, mouseY)) {
                hovered = true;
                break;
            }
        }

        boolean partOfCodeBlock = layoutStyle.get(LayoutStyle.PART_OF_CODE_BLOCK);

        float paddingLeftRight = partOfCodeBlock ? style.getCodeBlockStyle().getInlinePaddingLeftRight() : 0f;
        float paddingTopBottom = getInlineCodeBlockPadding();
        float topBound = Math.max(ascender, paddingTopBottom);
        float bottomBound = Math.max(descender, paddingTopBottom);
        for (Map.Entry<LayoutData.MarkDownElementPosition, String> line : lines.entrySet()) {
            LayoutData.MarkDownElementPosition position = line.getKey();
            // Cull text per line
            if (viewPort != null && !viewPort.isInViewPort(
                    position.getX() + xOffset, position.getY() + yOffset - topBound,
                    position.getRightX() + xOffset, position.getBottomY() + yOffset + bottomBound
            )) {
                continue;
            }
            String text = line.getValue();
            // preformatted = non-inline code block
            if (partOfCodeBlock && !layoutStyle.get(LayoutStyle.PRE_FORMATTED)) {
                drawInlineCodeBlock(
                        position.getX() + xOffset, position.getY() + yOffset - paddingTopBottom,
                        position.getWidth(), position.getHeight() + paddingTopBottom * 2,
                        style.getCodeBlockStyle().getColor(), renderData
                );
            }
            drawText(
                    text, position.getX() + xOffset + paddingLeftRight,
                    position.getY() + yOffset - ascender, layoutStyle.get(LayoutStyle.FONT_SIZE),
                    layoutStyle.get(LayoutStyle.TEXT_COLOR), hovered, position, renderData
            );
        }
    }


    protected List<String> wrapText(String text, float startX, float maxWidth, R renderData) {
        ArrayList<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        float fontSize = layoutStyle.get(LayoutStyle.FONT_SIZE);

        String[] words = text.split("(?= )");
        float actualMaxWidth = maxWidth - startX;
        boolean firstOfLine = actualMaxWidth == maxWidth;
        for (String word : words) {
            word = word.replace('\u00A0', ' ');
            // If this is the first word on the line, replace all leading whitespace chars (unless this is preformatted)
            if (firstOfLine && !layoutStyle.get(LayoutStyle.PRE_FORMATTED)) {
                word = LEADING_WHITESPACE.matcher(word).replaceAll("");
                firstOfLine = false;
            }
            if (getAdjustedTextWidth(currentLine + word, fontSize, renderData) <= actualMaxWidth) {
                currentLine.append(word);
            } else {
                String finishedText = currentLine.toString();
                if (!finishedText.isEmpty() || actualMaxWidth != maxWidth) {
                    lines.add(finishedText);
                }
                // This is the first word after wrapping, replace all leading whitespace chars
                String cleanedWord = LEADING_WHITESPACE.matcher(word).replaceAll("");
                currentLine = new StringBuilder();
                actualMaxWidth = maxWidth;
                if (getAdjustedTextWidth(cleanedWord, fontSize, renderData) > actualMaxWidth) {
                    String wordCarry = cleanedWord;
                    while (!wordCarry.isEmpty()) {
                        if (currentLine.length() != 0) lines.add(currentLine.toString());
                        currentLine = new StringBuilder(wordCarry);
                        while (getAdjustedTextWidth(currentLine.toString(), fontSize, renderData) > actualMaxWidth && currentLine.length() > 1) {
                            currentLine.deleteCharAt(currentLine.length() - 1);
                        }
                        wordCarry = wordCarry.substring(currentLine.length());
                    }
                } else {
                    currentLine.append(cleanedWord);
                }
            }
        }
        lines.add(currentLine.toString());

        return lines;
    }

    protected float getAdjustedTextWidth(@NotNull String text, float fontSize, R renderData) {
        return getTextWidth(text, fontSize, renderData) + (layoutStyle.get(LayoutStyle.PART_OF_CODE_BLOCK) ? (style.getCodeBlockStyle().getInlinePaddingLeftRight() * 2f) : 0);
    }

    protected abstract void drawText(@NotNull String text, float x, float y, float fontSize, Color color, boolean hovered, LayoutData.MarkDownElementPosition position, @NotNull R renderData);

    protected abstract void drawInlineCodeBlock(float x, float y, float width, float height, Color color, @NotNull R renderData);

    protected abstract float getTextWidth(@NotNull String text, float fontSize, R renderData);

    protected abstract float getBaselineHeight(float fontSize, R renderData);

    protected float getAscender(float fontSize, R renderData) {
        return 0f;
    }

    protected float getDescender(float fontSize, R renderData) {
        return 0f;
    }

    /**
     * @return Padding of the inline code block background, 0 if no background
     */
    protected float getInlineCodeBlockPadding() {
        return layoutStyle.get(LayoutStyle.PART_OF_CODE_BLOCK) && !layoutStyle.get(LayoutStyle.PRE_FORMATTED)
                ? style.getCodeBlockStyle().getInlinePaddingTopBottom() : 0f;
    }

    @Override
    public boolean shouldDraw(@NotNull ViewPort viewPort, float xOffset, float yOffset) {
        return viewPort.isInViewPort(leftX + xOffset, topY + yOffset, rightX + xOffset, bottomY + yOffset);
    }

    @Override
    public String toString() {
        return "TextElement {" + text + "}";
    }
}
