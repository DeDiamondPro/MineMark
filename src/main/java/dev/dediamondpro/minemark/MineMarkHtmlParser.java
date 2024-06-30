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

package dev.dediamondpro.minemark;

import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.EmptyElement;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import dev.dediamondpro.minemark.elements.creators.ElementCreator;
import dev.dediamondpro.minemark.elements.creators.TextElementCreator;
import dev.dediamondpro.minemark.elements.formatting.FormattingElement;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class MineMarkHtmlParser<S extends Style, R> extends DefaultHandler {
    private final List<ElementCreator<S, R>> elements;
    private final List<FormattingElement<S, R>> formattingElements;
    private final TextElementCreator<S, R> textElementCreator;
    private MineMarkElement<S, R> markDown;
    private Element<S, R> currentElement;
    private LayoutStyle layoutStyle;
    private S style;
    private StringBuilder textBuilder = new StringBuilder();
    private boolean isPreFormatted = false;

    protected MineMarkHtmlParser(TextElementCreator<S, R> textElementCreator, List<ElementCreator<S, R>> elements, List<FormattingElement<S, R>> formattingElements) {
        this.textElementCreator = textElementCreator;
        this.elements = elements;
        this.formattingElements = formattingElements;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "minemark":
                currentElement = markDown = new MineMarkElement<>(style, layoutStyle, attributes);
                return;
            case "br":
                textBuilder.append("\n");
                return;
            case "pre":
                isPreFormatted = true;
                break;
        }
        addText();

        LayoutStyle originalLayoutStyle = currentElement.getLayoutStyle();
        LayoutStyle layoutStyle = originalLayoutStyle;
        boolean canBeInline = true;
        for (FormattingElement<S, R> element : formattingElements) {
            if (!element.appliesTo(style, layoutStyle, currentElement, qName, attributes)) {
                continue;
            }
            // If a formatting element applies, clone the layout style. This should only be done once
            if (layoutStyle == originalLayoutStyle) {
                layoutStyle = layoutStyle.clone();
            }

            element.applyStyle(style, layoutStyle, currentElement, qName, attributes);

            canBeInline = canBeInline && element.canBeInline(style, layoutStyle, currentElement, qName, attributes);
        }

        Element<S, R> newElement = createElement(style, layoutStyle, currentElement, qName, attributes);
        currentElement = newElement != null ? newElement : new EmptyElement<>(style, layoutStyle, currentElement, qName, attributes);
        if (!canBeInline) {
            currentElement.setInline(false);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "minemark":
            case "br":
                return;
            case "pre":
                isPreFormatted = false;
                break;
        }
        addText();
        currentElement = currentElement.getParent();
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        // All newlines are ignored unless this element is preformatted
        int newLength = length;
        if (isPreFormatted) {
            if (ch[start] == '\n') {
                newLength--;
            }
            if (ch[start + length - 1] == '\n') {
                newLength--;
            }
        } else {
            for (int i = start; i < start + length; i++) {
                if (ch[i] == '\n') {
                    newLength--;
                }
            }
        }

        char[] modifiedCh = new char[newLength];
        int index = 0;
        for (int i = start; i < start + length; i++) {
            if (ch[i] != '\n' || (isPreFormatted && i != start && i != start + length - 1)) {
                modifiedCh[index++] = ch[i];
            }
        }
        textBuilder.append(modifiedCh);
    }

    private void addText() {
        String text = textBuilder.toString();
        if (text.isEmpty()) return;
        textElementCreator.createElement(textBuilder.toString(), style, currentElement.getLayoutStyle(), currentElement, "text", null);
        textBuilder = new StringBuilder();
    }

    /**
     * Try to create an element for the given tag
     *
     * @param style       The style of the element
     * @param layoutStyle The layout style of the element
     * @param parent      The parent element, null if top level element
     * @param qName       The name of the HTML tag
     * @param attributes  The attributes of the HTML tag, null for text
     * @return The first element that applies to the parameters, if none apply returns null
     */
    private Element<S, R> createElement(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        for (ElementCreator<S, R> element : elements) {
            if (!element.appliesTo(style, layoutStyle, parent, qName, attributes)) {
                continue;
            }
            return element.createElement(style, layoutStyle, parent, qName, attributes);
        }
        return null;
    }

    protected void setStyle(S style, LayoutStyle layoutStyle) {
        this.style = style;
        this.layoutStyle = layoutStyle;
    }

    protected MineMarkElement<S, R> getParsedResult() {
        return markDown;
    }

    protected void cleanUp() {
        markDown = null;
        currentElement = null;
        layoutStyle = null;
        style = null;
        textBuilder = new StringBuilder();
        isPreFormatted = false;
    }
}