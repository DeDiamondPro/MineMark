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
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class responsible for integrating parsing, layout and rendering
 */
public class MineMarkCore<L extends LayoutConfig, R> {
    private final Parser markdownParser;
    private final HtmlRenderer htmlRenderer;
    private final MineMarkHtmlParser<L, R> htmlParser;
    private final org.ccil.cowan.tagsoup.Parser xmlParser;
    private final ReentrantLock parsingLock = new ReentrantLock();

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
     * @throws SAXException An exception during SAX parsing
     * @throws IOException An IOException during parsing
     */
    public MineMarkElement<L, R> parse(@NotNull L layoutConfig, @NotNull String markdown) throws SAXException, IOException {
        Node document = markdownParser.parse(markdown);
        return parseDocument(layoutConfig, document);
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown     The markdown text to parse
     * @param layoutConfig The config used at parsing time to create the layout
     * @return The parsed markdown element
     * @throws SAXException An exception during SAX parsing
     * @throws IOException An IOException during parsing
     */
    public MineMarkElement<L, R> parse(@NotNull L layoutConfig, @NotNull Reader markdown) throws SAXException, IOException {
        Node document = markdownParser.parseReader(markdown);
        return parseDocument(layoutConfig, document);
    }

    private MineMarkElement<L, R> parseDocument(@NotNull L layoutConfig, Node document) throws SAXException, IOException {
        String html = "<minemark>\n" + htmlRenderer.render(document) + "</minemark>";
        System.out.println(html);
        try {
            // Acquire the lock to make sure this thread is the only one using the parser
            parsingLock.lock();
            htmlParser.setLayoutConfig(layoutConfig);
            xmlParser.parse(new InputSource(new ByteArrayInputStream(html.getBytes())));
            return htmlParser.getParsedResult();
        } finally {
            htmlParser.cleanUp();
            parsingLock.unlock();
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
