package dev.dediamondpro.minemark;

import dev.dediamondpro.minemark.elements.ElementLoader;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import org.commonmark.Extension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.html.UrlSanitizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Class responsible for integrating parsing, layout and rendering
 */
public class MineMarkCore<L extends LayoutConfig, R> {
    private final Parser markdownParser;
    private final HtmlRenderer htmlRenderer;
    private final MineMarkHtmlParser<L, R> htmlParser;
    private final org.ccil.cowan.tagsoup.Parser xmlParser;

    /**
     * @param elements     Elements supported to create a layout and render
     * @param extensions   Markdown extensions that should be used when parsing
     * @param urlSanitizer An optional urlSanitizer
     */
    protected MineMarkCore(Map<List<String>, ElementLoader<L, R>> elements, Iterable<? extends Extension> extensions, @Nullable UrlSanitizer urlSanitizer) {
        this.markdownParser = Parser.builder().extensions(extensions).build();
        HtmlRenderer.Builder htmlRendererBuilder = HtmlRenderer.builder().extensions(extensions);
        if (urlSanitizer != null) {
            htmlRendererBuilder.urlSanitizer(urlSanitizer).sanitizeUrls(true);
        }
        this.htmlRenderer = htmlRendererBuilder.build();
        this.htmlParser = new MineMarkHtmlParser<>(elements);
        xmlParser = new org.ccil.cowan.tagsoup.Parser();
        xmlParser.setContentHandler(htmlParser);
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown     The markdown text to parse
     * @param layoutConfig The config used at parsing time to create the layout
     * @return The parsed markdown element
     * @throws RuntimeException When parsing fails or an error occurs
     */
    public MineMarkElement<L, R> parse(@NotNull L layoutConfig, @NotNull String markdown) {
        long start1 = System.currentTimeMillis();
        Node document = markdownParser.parse(markdown);
        String html = "<minemark>\n" + htmlRenderer.render(document) + "</minemark>";
        System.out.println(html);
        long start2 = System.currentTimeMillis();
        System.out.println("Finished generating html, took " + (start2 - start1) + "ms");
        try {
            htmlParser.setLayoutConfig(layoutConfig);
            xmlParser.parse(new InputSource(new ByteArrayInputStream(html.getBytes())));
            return htmlParser.getParsedResult();
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            htmlParser.cleanUp();
            System.out.println("Finished parsing html, took " + (System.currentTimeMillis() - start2) + "ms");
        }
    }

    /**
     * @param <L> A class that is given to elements at parse time
     * @param <R> A class that is given to elements at render time
     * @return The builder used to create the core
     */
    public static <L extends LayoutConfig, R> MineMarkCoreBuilder<L, R> builder() {
        return new MineMarkCoreBuilder<>();
    }
}
