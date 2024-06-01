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

import dev.dediamondpro.minemark.elements.Elements;
import dev.dediamondpro.minemark.elements.impl.LinkElement;
import dev.dediamondpro.minemark.elements.impl.ParagraphElement;
import dev.dediamondpro.minemark.elements.impl.formatting.AlignmentElement;
import dev.dediamondpro.minemark.elements.impl.formatting.FormattingElement;
import dev.dediamondpro.minemark.elements.impl.list.ListHolderElement;
import dev.dediamondpro.minemark.elements.impl.table.TableHolderElement;
import dev.dediamondpro.minemark.elements.impl.table.TableRowElement;
import dev.dediamondpro.minemark.elements.loaders.ElementLoader;
import dev.dediamondpro.minemark.elements.loaders.TextElementLoader;
import dev.dediamondpro.minemark.style.Style;
import org.commonmark.Extension;
import org.commonmark.renderer.html.UrlSanitizer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MineMarkCoreBuilder<S extends Style, R> {
    protected MineMarkCoreBuilder() {
    }

    private final LinkedHashMap<List<String>, ElementLoader<S, R>> elements = new LinkedHashMap<>();
    private final ArrayList<Extension> extensions = new ArrayList<>();
    private TextElementLoader<S, R> textElement = null;
    private boolean withDefaultElements = true;
    private UrlSanitizer urlSanitizer = null;

    /**
     * Set the text element that should be used.
     *
     * @param textElement The {@link TextElementLoader} of that text element
     */
    public MineMarkCoreBuilder<S, R> setTextElement(@NotNull TextElementLoader<S, R> textElement) {
        this.textElement = textElement;
        return this;
    }

    /**
     * Add a supported element to be used
     *
     * @param elementName Tags the element should use
     * @param element     An {@link ElementLoader} of that element
     */
    public MineMarkCoreBuilder<S, R> addElement(@NotNull Elements elementName, @NotNull ElementLoader<S, R> element) {
        this.elements.put(elementName.tags, element);
        return this;
    }

    /**
     * Add a supported element to be used
     *
     * @param tags    Tags the element should use
     * @param element An {@link ElementLoader} of that element
     */
    public MineMarkCoreBuilder<S, R> addElement(@NotNull List<String> tags, @NotNull ElementLoader<S, R> element) {
        this.elements.put(tags, element);
        return this;
    }

    /**
     * Add supported elements to be used
     *
     * @param elements A Map with all elements that should be added
     */
    public MineMarkCoreBuilder<S, R> addElements(@NotNull Map<Elements, ElementLoader<S, R>> elements) {
        for (Map.Entry<Elements, ElementLoader<S, R>> element : elements.entrySet()) {
            addElement(element.getKey(), element.getValue());
        }
        return this;
    }

    /**
     * Add supported elements to be used
     *
     * @param elements A Map with all elements that should be added
     */
    public MineMarkCoreBuilder<S, R> addElementsString(@NotNull Map<List<String>, ElementLoader<S, R>> elements) {
        this.elements.putAll(elements);
        return this;
    }

    /**
     * Add a commonmark extension to the Markdown parser
     *
     * @param extension The extension
     */
    public MineMarkCoreBuilder<S, R> addExtension(Extension extension) {
        extensions.add(extension);
        return this;
    }

    /**
     * Disable default elements
     */
    public MineMarkCoreBuilder<S, R> withoutDefaultElements() {
        withDefaultElements = false;
        return this;
    }

    /**
     * Make the core use an url sanitizer and enable url sanitization
     *
     * @param urlSanitizer The url sanitizer
     */
    public MineMarkCoreBuilder<S, R> setUrlSanitizer(UrlSanitizer urlSanitizer) {
        this.urlSanitizer = urlSanitizer;
        return this;
    }

    /**
     * @return a MineMarkCore with the given settings
     */
    public MineMarkCore<S, R> build() {
        if (textElement == null) {
            throw new IllegalArgumentException("A text element has to be provided by using \"setTextElement(textElement\"");
        }
        if (withDefaultElements) {
            addElement(Elements.PARAGRAPH, ParagraphElement::new);
            addElement(Elements.FORMATTING, FormattingElement::new);
            addElement(Elements.ALIGNMENT, AlignmentElement::new);
            addElement(Elements.LINK, LinkElement::new);
            addElement(Elements.LIST_PARENT, ListHolderElement::new);
            addElement(Elements.TABLE, TableHolderElement::new);
            addElement(Elements.TABLE_ROW, TableRowElement::new);
        }
        return new MineMarkCore<>(textElement, elements, extensions, urlSanitizer);
    }
}
