package dev.dediamondpro.minemark;

import dev.dediamondpro.minemark.elements.ElementLoader;
import dev.dediamondpro.minemark.elements.Elements;
import dev.dediamondpro.minemark.elements.impl.LinkElement;
import dev.dediamondpro.minemark.elements.impl.ParagraphElement;
import dev.dediamondpro.minemark.elements.impl.formatting.AlignmentElement;
import dev.dediamondpro.minemark.elements.impl.formatting.FormattingElement;
import dev.dediamondpro.minemark.elements.impl.list.ListHolderElement;
import dev.dediamondpro.minemark.style.Style;
import org.commonmark.Extension;
import org.commonmark.renderer.html.UrlSanitizer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineMarkCoreBuilder<S extends Style, R> {
    protected MineMarkCoreBuilder() {
    }

    private final HashMap<List<String>, ElementLoader<S, R>> elements = new HashMap<>();
    private final ArrayList<Extension> extensions = new ArrayList<>();
    private boolean withDefaultElements = true;
    private UrlSanitizer urlSanitizer = null;

    /**
     * Add a supported element to be used
     *
     * @param elementName Tags the element should use
     * @param element     An ElementLoader of that element
     */
    public MineMarkCoreBuilder<S, R> addElement(@NotNull Elements elementName, @NotNull ElementLoader<S, R> element) {
        this.elements.put(elementName.tags, element);
        return this;
    }

    /**
     * Add a supported element to be used
     *
     * @param tags    Tags the element should use
     * @param element An ElementLoader of that element
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
     * Disable default extensions
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
    public void setUrlSanitizer(UrlSanitizer urlSanitizer) {
        this.urlSanitizer = urlSanitizer;
    }

    /**
     * @return a MineMarkCore with the given settings
     */
    public MineMarkCore<S, R> build() {
        if (withDefaultElements) {
            addElement(Elements.PARAGRAPH, ParagraphElement::new);
            addElement(Elements.FORMATTING, FormattingElement::new);
            addElement(Elements.ALIGNMENT, AlignmentElement::new);
            addElement(Elements.LINK, LinkElement::new);
            addElement(Elements.LIST_PARENT, ListHolderElement::new);
        }
        return new MineMarkCore<>(elements, extensions, urlSanitizer);
    }
}
