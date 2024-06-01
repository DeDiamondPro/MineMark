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
import dev.dediamondpro.minemark.elements.creators.ElementCreator;
import dev.dediamondpro.minemark.elements.creators.TagBasedElementCreator;
import dev.dediamondpro.minemark.elements.creators.TextElementCreator;
import dev.dediamondpro.minemark.style.Style;
import org.commonmark.Extension;
import org.commonmark.renderer.html.UrlSanitizer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MineMarkCoreBuilder<S extends Style, R> {
    protected MineMarkCoreBuilder() {
    }

    private final ArrayList<ElementCreator<S, R>> elements = new ArrayList<>();
    private final ArrayList<Extension> extensions = new ArrayList<>();
    private TextElementCreator<S, R> textElement = null;
    private boolean withDefaultElements = true;
    private UrlSanitizer urlSanitizer = null;

    /**
     * Set the text element that should be used.
     *
     * @param textElement The {@link TextElementCreator} of that text element
     */
    public MineMarkCoreBuilder<S, R> setTextElement(@NotNull TextElementCreator<S, R> textElement) {
        this.textElement = textElement;
        return this;
    }

    /**
     * Add a supported element to be used
     *
     * @param element An {@link ElementCreator} of that element
     */
    public MineMarkCoreBuilder<S, R> addElement(@NotNull ElementCreator<S, R> element) {
        this.elements.add(element);
        return this;
    }

    /**
     * Add a supported element to be used
     *
     * @param tags    HTML tags the element will be applied to
     * @param element An {@link ElementCreator} of that element
     */
    public MineMarkCoreBuilder<S, R> addElement(@NotNull List<String> tags, @NotNull TagBasedElementCreator.BasicElementCreator<S, R> element) {
        return addElement(new TagBasedElementCreator<>(tags, element));
    }

    /**
     * Add a supported element to be used
     *
     * @param tag         HTML tags the element will be applied to
     * @param element     An {@link TagBasedElementCreator.BasicElementCreator} of that element
     */
    public MineMarkCoreBuilder<S, R> addElement(@NotNull String tag, @NotNull TagBasedElementCreator.BasicElementCreator<S, R> element) {
        return addElement(Collections.singletonList(tag), element);
    }

    /**
     * Add a supported element to be used
     *
     * @param elementName HTML tags the element will be applied to
     * @param element     An {@link TagBasedElementCreator.BasicElementCreator} of that element
     */
    public MineMarkCoreBuilder<S, R> addElement(@NotNull Elements elementName, @NotNull TagBasedElementCreator.BasicElementCreator<S, R> element) {
        return addElement(elementName.tags, element);
    }

    /**
     * Add supported elements to be used
     *
     * @param elements A Map with all elements that should be added
     */
    public MineMarkCoreBuilder<S, R> addElements(@NotNull List<ElementCreator<S, R>> elements) {
        for (ElementCreator<S, R> element : elements) {
            addElement(element);
        }
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
            addElement(Elements.LINK, LinkElement::new);
            addElement(Elements.LIST_PARENT, ListHolderElement::new);
            addElement(Elements.TABLE, TableHolderElement::new);
            addElement(Elements.TABLE_ROW, TableRowElement::new);
            addElement(new AlignmentElement.AlignmentElementCreator<>());
        }
        return new MineMarkCore<>(textElement, elements, extensions, urlSanitizer);
    }
}
