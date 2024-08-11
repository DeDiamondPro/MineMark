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

import dev.dediamondpro.minemark.elements.MineMarkElement;
import dev.dediamondpro.minemark.elements.creators.ElementCreator;
import dev.dediamondpro.minemark.elements.creators.TextElementCreator;
import dev.dediamondpro.minemark.elements.formatting.FormattingElement;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.PrefixedReader;
import org.commonmark.Extension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.html.UrlSanitizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * Class responsible for integrating parsing, layout and rendering
 *
 * @param <S> The {@link Style} this core and all associated elements will use
 * @param <R> The class passed to the rendering implementation at render time
 */
public class MineMarkCore<S extends Style, R> {
    private static final Pattern ACTIVATION_PATTERN = Pattern.compile("<minemark-activator>.*?</minemark-activator>", Pattern.DOTALL);
    private final Parser markdownParser;
    private final HtmlRenderer htmlRenderer;
    private final MineMarkHtmlParser<S, R> htmlParser;
    private final org.ccil.cowan.tagsoup.Parser xmlParser;
    private final ReentrantLock parsingLock = new ReentrantLock();

    /**
     * @param textElement        The text element for this core
     * @param elements           Elements supported to create a layout and render
     * @param formattingElements Elements that apply a formatting style
     * @param extensions         Markdown extensions that should be used when parsing
     * @param urlSanitizer       An optional urlSanitizer
     */
    protected MineMarkCore(TextElementCreator<S, R> textElement, List<ElementCreator<S, R>> elements, List<FormattingElement<S, R>> formattingElements, Iterable<? extends Extension> extensions, @Nullable UrlSanitizer urlSanitizer) {
        this.markdownParser = Parser.builder().extensions(extensions).build();
        HtmlRenderer.Builder htmlRendererBuilder = HtmlRenderer.builder().extensions(extensions);
        if (urlSanitizer != null) {
            htmlRendererBuilder.urlSanitizer(urlSanitizer).sanitizeUrls(true);
        }
        this.htmlRenderer = htmlRendererBuilder.build();
        this.htmlParser = new MineMarkHtmlParser<>(textElement, elements, formattingElements);
        xmlParser = new org.ccil.cowan.tagsoup.Parser();
        xmlParser.setContentHandler(htmlParser);
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown The Markdown text to parse
     * @param style    The style passed to all elements
     * @param charSet  The charset to use when parsing the markdown
     * @return The parsed markdown element
     * @throws SAXException An exception during SAX parsing
     * @throws IOException  An IOException during parsing
     */
    public MineMarkElement<S, R> parse(@NotNull S style, @NotNull String markdown, @NotNull Charset charSet) throws SAXException, IOException {
        // Trick the markdown renderer to activate early,
        // this makes it so some problematic whitespaces are handled for us
        markdown = "<minemark-activator>\n\n**MineMark-activation**\n\n</minemark-activator>" + markdown;
        Node document = markdownParser.parse(markdown);
        return parseDocument(style, document, charSet);
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown The Markdown text to parse
     * @param style    The style passed to all elements
     * @return The parsed markdown element
     * @throws SAXException An exception during SAX parsing
     * @throws IOException  An IOException during parsing
     */
    public MineMarkElement<S, R> parse(@NotNull S style, @NotNull String markdown) throws SAXException, IOException {
        return parse(style, markdown, StandardCharsets.UTF_8);
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown The Markdown text to parse
     * @param style    The style passed to all elements
     * @param charSet  The charset to use when parsing the markdown
     * @return The parsed markdown element
     * @throws SAXException An exception during SAX parsing
     * @throws IOException  An IOException during parsing
     */
    public MineMarkElement<S, R> parse(@NotNull S style, @NotNull Reader markdown, @NotNull Charset charSet) throws SAXException, IOException {
        // Trick the markdown renderer to activate early,
        // this makes it so some problematic whitespaces are handled for us
        markdown = new PrefixedReader("<minemark-activator>\n\n**MineMark-activation**\n\n</minemark-activator>", markdown);
        Node document = markdownParser.parseReader(markdown);
        return parseDocument(style, document, charSet);
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown The Markdown text to parse
     * @param style    The style passed to all elements
     * @return The parsed markdown element
     * @throws SAXException An exception during SAX parsing
     * @throws IOException  An IOException during parsing
     */
    public MineMarkElement<S, R> parse(@NotNull S style, @NotNull Reader markdown) throws SAXException, IOException {
        return parse(style, markdown, StandardCharsets.UTF_8);
    }

    private MineMarkElement<S, R> parseDocument(@NotNull S style, Node document, @NotNull Charset charSet) throws SAXException, IOException {
        // Render the document to HTML
        String html = htmlRenderer.render(document);
        // Remove the markdown activation part
        html = ACTIVATION_PATTERN.matcher(html).replaceFirst("");
        // Get the wrapper to wrap the content with, make sure the html does not include it
        String wrapper = getMineMarkWrapper(html);
        // Prepare the HTML for parsing
        html = "<" + wrapper + ">" + html + "</" + wrapper + ">";
        // Acquire the lock to make sure this thread is the only one using the parser
        parsingLock.lock();
        try (InputStream stream = new ByteArrayInputStream(html.getBytes(charSet))) {
            htmlParser.setStyle(style, new LayoutStyle(style));
            htmlParser.setWrapper(wrapper);
            InputSource source = new InputSource(stream);
            source.setEncoding(charSet.name());
            xmlParser.parse(source);
            return htmlParser.getParsedResult();
        } finally {
            htmlParser.cleanUp();
            parsingLock.unlock();
        }
    }

    private String getMineMarkWrapper(String html) {
        String wrapper = "minemark";
        int num = 0;
        while (html.contains(wrapper)) {
            wrapper = "minemark-" + num;
            num++;
        }
        return wrapper;
    }

    /**
     * @param <S> The {@link Style} this core and all associated elements will use
     * @param <R> The class passed to the rendering implementation at render time
     * @return The builder used to create the core
     */
    public static <S extends Style, R> MineMarkCoreBuilder<S, R> builder() {
        return new MineMarkCoreBuilder<>();
    }
}
