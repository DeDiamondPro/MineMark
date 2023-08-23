package dev.dediamondpro.minemark;

import dev.dediamondpro.minemark.elements.ElementLoader;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import org.commonmark.Extension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for integrating parsing, layout and rendering
 */
public class MineMarkCore<L extends LayoutConfig, R> {
    private final Parser markdownParser;
    private final HtmlRenderer htmlRenderer;
    private final MineMarkHtmlParser<L, R> htmlParser;
    private final SAXParser saxParser;

    /**
     * @param elements   Elements supported to create a layout and render
     * @param extensions Markdown extensions that should be used when parsing
     */
    protected MineMarkCore(Map<List<String>, ElementLoader<L, R>> elements, Iterable<? extends Extension> extensions) {
        this.markdownParser = Parser.builder().extensions(extensions).build();
        this.htmlRenderer = HtmlRenderer.builder().extensions(extensions).build();
        this.htmlParser = new MineMarkHtmlParser<>(elements);
        try {
            saxParser = SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown The markdown text to parse
     * @return The parsed markdown element
     * @throws IllegalStateException When parsing fails or an error occurs
     */
    public MineMarkElement<L, R> parse(String markdown) {
        Node document = markdownParser.parse(markdown);
        String html = "<minemark>\n" + htmlRenderer.render(document) + "</minemark>";
        try {
            saxParser.parse(new ByteArrayInputStream(html.getBytes()), htmlParser);
            return htmlParser.getParsedResult();
        } catch (SAXException | IOException e) {
            throw new IllegalStateException(e);
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
