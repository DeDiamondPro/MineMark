package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
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

public abstract class TextElement<S extends Style, R> extends Element<S, R> implements Inline {
    protected final HashMap<LayoutData.MarkDownElementPosition, String> lines = new HashMap<>();
    protected float baseLineHeight;
    protected float ascender;
    protected float descender;

    public TextElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    public void generateLayout(LayoutData layoutData) {
        lines.clear();
        ArrayList<String> allLines = new ArrayList<>();
        String[] predefinedLines = text.replaceAll(" +", " ").split("\n", -1);
        for (int i = 0; i < predefinedLines.length; i++) {
            String line = predefinedLines[i].replace("\n", "");
            if (layoutStyle.isPreFormatted()) {
                allLines.add(line);
            } else {
                allLines.addAll(wrapText(line, i == 0 ? layoutData.getX() : 0f, layoutData.getMaxWidth()));
            }
        }
        float codeBlockPadding = layoutStyle.isPartOfCodeBlock() ? style.getCodeBlockStyle().getInlinePaddingTopBottom() : 0f;
        float padding = Math.max(style.getTextStyle().getPadding(), codeBlockPadding);
        float fontSize = layoutStyle.getFontSize();
        baseLineHeight = getBaselineHeight(fontSize);
        ascender = getAscender(fontSize);
        descender = getDescender(fontSize);
        for (int i = 0; i < allLines.size(); i++) {
            String line = allLines.get(i);
            layoutData.updatePadding(padding);
            lines.put(layoutData.addElement(layoutStyle.getAlignment(), getAdjustedTextWidth(line, fontSize), baseLineHeight), line);
            if (i != allLines.size() - 1) {
                layoutData.nextLine();
            }
        }
    }

    @Override
    public void draw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        boolean hovered = false;
        for (LayoutData.MarkDownElementPosition position : lines.keySet()) {
            if (position.isInside(mouseX, mouseY)) {
                hovered = true;
                break;
            }
        }
        float paddingLeftRight = layoutStyle.isPartOfCodeBlock() ? style.getCodeBlockStyle().getInlinePaddingLeftRight() : 0f;
        float paddingTopBottom = layoutStyle.isPartOfCodeBlock() ? style.getCodeBlockStyle().getInlinePaddingTopBottom() : 0f;
        for (Map.Entry<LayoutData.MarkDownElementPosition, String> line : lines.entrySet()) {
            LayoutData.MarkDownElementPosition position = line.getKey();
            String text = line.getValue();
            if (layoutStyle.isPartOfCodeBlock()) {
                drawInlineCodeBlock(
                        position.getX() + xOffset, position.getY() + yOffset - paddingTopBottom,
                        position.getWidth(), position.getHeight() + paddingTopBottom * 2,
                        style.getCodeBlockStyle().getColor(), renderData
                );
            }
            drawText(
                    text, position.getX() + xOffset + paddingLeftRight,
                    position.getY() + yOffset - ascender,
                    hovered, renderData
            );
        }
    }


    protected List<String> wrapText(String text, float startX, float maxWidth) {
        ArrayList<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        float fontSize = layoutStyle.getFontSize();

        String[] words = text.split("(?= )");
        float actualMaxWidth = maxWidth - startX;
        for (String word : words) {
            word = word.replace('\u00A0', ' ');
            if (getAdjustedTextWidth(currentLine + word, fontSize) <= actualMaxWidth) {
                currentLine.append(word);
            } else {
                lines.add(currentLine.toString());
                String cleanedWord = word.replaceAll("^ ", "");
                currentLine = new StringBuilder();
                actualMaxWidth = maxWidth;
                if (getAdjustedTextWidth(cleanedWord, fontSize) > actualMaxWidth) {
                    String wordCarry = cleanedWord;
                    while (!wordCarry.isEmpty()) {
                        if (currentLine.length() != 0) lines.add(currentLine.toString());
                        currentLine = new StringBuilder(wordCarry);
                        while (getAdjustedTextWidth(currentLine.toString(), fontSize) > actualMaxWidth && currentLine.length() > 1) {
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

    protected float getAdjustedTextWidth(@NotNull String text, float fontSize) {
        return getTextWidth(text, fontSize) + (layoutStyle.isPartOfCodeBlock() ? (style.getCodeBlockStyle().getInlinePaddingLeftRight() * 2f) : 0);
    }

    protected abstract void drawText(@NotNull String text, float x, float y, boolean hovered, @NotNull R renderData);

    protected abstract void drawInlineCodeBlock(float x, float y, float width, float height, Color color, @NotNull R renderData);

    protected abstract float getTextWidth(@NotNull String text, float fontSize);

    protected abstract float getBaselineHeight(float fontSize);

    protected abstract float getAscender(float fontSize);

    protected abstract float getDescender(float fontSize);

    @Override
    public String toString() {
        return "TextElement {" + text + "}";
    }
}
