package dev.dediamondpro.minemark.elements.impl;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TextElement<S extends Style, R> extends Element<S, R> implements Inline {
    protected final HashMap<LayoutData.MarkDownElementPosition, String> lines = new HashMap<>();

    public TextElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    public void generateLayout(LayoutData layoutData) {
        lines.clear();
        ArrayList<String> allLines = new ArrayList<>();
        String[] predefinedLines = text.split("\n", -1);
        for (int i = 0; i < predefinedLines.length; i++) {
            String line = predefinedLines[i].replace("\n", "");
            if (layoutStyle.isPreFormatted()) {
                allLines.add(line);
            } else {
                allLines.addAll(wrapText(line, i == 0 ? layoutData.getX() : 0f, layoutData.getMaxWidth()));
            }
        }
        for (int i = 0; i < allLines.size(); i++) {
            String line = allLines.get(i);
            layoutData.updatePadding(style.getTextStyle().getPadding() * layoutStyle.getFontSize());
            lines.put(layoutData.addElement(layoutStyle.getAlignment(), getTextWidth(line), getTextHeight(line)), line);
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
        for (Map.Entry<LayoutData.MarkDownElementPosition, String> line : lines.entrySet()) {
            LayoutData.MarkDownElementPosition position = line.getKey();
            String text = line.getValue();
            drawText(
                    text, position.getX() + xOffset,
                    position.getBottomY() + yOffset,
                    hovered, renderData
            );
        }
    }


    protected List<String> wrapText(String text, float startX, float maxWidth) {
        ArrayList<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        String[] words = text.split("(?= )");
        float actualMaxWidth = maxWidth - startX;
        for (String word : words) {
            word = word.replace('\u00A0', ' ');
            if (getTextWidth(currentLine + word) <= actualMaxWidth) {
                currentLine.append(word);
            } else {
                lines.add(currentLine.toString());
                String cleanedWord = word.replaceAll("^ ", "");
                currentLine = new StringBuilder();
                actualMaxWidth = maxWidth;
                if (getTextWidth(cleanedWord) > actualMaxWidth) {
                    String wordCarry = cleanedWord;
                    while (!wordCarry.isEmpty()) {
                        if (currentLine.length() != 0) lines.add(currentLine.toString());
                        currentLine = new StringBuilder(wordCarry);
                        while (getTextWidth(currentLine.toString()) > actualMaxWidth && currentLine.length() > 1) {
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

    public abstract void drawText(@NotNull String text, float x, float bottomY, boolean hovered, @NotNull R renderData);

    public abstract float getTextWidth(@NotNull String text);

    public abstract float getTextHeight(@NotNull String text);

    @Override
    public String toString() {
        return "TextElement {" + text + "}";
    }
}
