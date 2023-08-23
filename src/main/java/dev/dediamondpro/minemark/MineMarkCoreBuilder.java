package dev.dediamondpro.minemark;

import dev.dediamondpro.minemark.config.LayoutConfig;
import dev.dediamondpro.minemark.config.RenderConfig;
import dev.dediamondpro.minemark.elements.ElementLoader;
import dev.dediamondpro.minemark.elements.Elements;
import dev.dediamondpro.minemark.elements.impl.ParagraphElement;
import org.commonmark.Extension;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineMarkCoreBuilder<L extends LayoutConfig, R extends RenderConfig> {
    protected MineMarkCoreBuilder() {
    }

    private final HashMap<List<String>, ElementLoader<L, R>> elements = new HashMap<>();
    private final ArrayList<Extension> extensions = new ArrayList<>();
    private boolean withDefaultExtensions = false;

    /**
     * Add a supported element to be used
     *
     * @param elementName Tags the element should use
     * @param element     An ElementLoader of that element
     */
    public MineMarkCoreBuilder<L, R> addElement(@NotNull Elements elementName, @NotNull ElementLoader<L, R> element) {
        this.elements.put(elementName.tags, element);
        return this;
    }

    /**
     * Add a supported element to be used
     *
     * @param tags    Tags the element should use
     * @param element An ElementLoader of that element
     */
    public MineMarkCoreBuilder<L, R> addElement(@NotNull List<String> tags, @NotNull ElementLoader<L, R> element) {
        this.elements.put(tags, element);
        return this;
    }

    /**
     * Add supported elements to be used
     *
     * @param elements A Map with all elements that should be added
     */
    public MineMarkCoreBuilder<L, R> addElements(@NotNull Map<Elements, ElementLoader<L, R>> elements) {
        for (Map.Entry<Elements, ElementLoader<L, R>> element : elements.entrySet()) {
            addElement(element.getKey(), element.getValue());
        }
        return this;
    }

    /**
     * Add supported elements to be used
     *
     * @param elements A Map with all elements that should be added
     */
    public MineMarkCoreBuilder<L, R> addElementsString(@NotNull Map<List<String>, ElementLoader<L, R>> elements) {
        this.elements.putAll(elements);
        return this;
    }

    /**
     * Add a commonmark extension to the Markdown parser
     *
     * @param extension The extension
     */
    public MineMarkCoreBuilder<L, R> addExtension(Extension extension) {
        extensions.add(extension);
        return this;
    }

    /**
     * Disable default extensions
     */
    public MineMarkCoreBuilder<L, R> withoutDefaultExtensions() {
        withDefaultExtensions = false;
        return this;
    }

    /**
     * @return a MineMarkCore with the given settings
     */
    public MineMarkCore<L, R> build() {
        if (withDefaultExtensions) {
            addElement(Elements.PARAGRAPH, ParagraphElement::new);
        }
        return new MineMarkCore<>(elements, extensions);
    }
}
