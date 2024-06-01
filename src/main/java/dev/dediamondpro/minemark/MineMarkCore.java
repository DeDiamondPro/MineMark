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
import dev.dediamondpro.minemark.style.Style;
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

/**
 * Class responsible for integrating parsing, layout and rendering
 *
 * @param <S> The {@link Style} this core and all associated elements will use
 * @param <R> The class passed to the rendering implementation at render time
 */
public class MineMarkCore<S extends Style, R> {
    private final Parser markdownParser;
    private final HtmlRenderer htmlRenderer;
    private final MineMarkHtmlParser<S, R> htmlParser;
    private final org.ccil.cowan.tagsoup.Parser xmlParser;
    private final ReentrantLock parsingLock = new ReentrantLock();

    /**
     * @param textElement  The text element for this core
     * @param elements     Elements supported to create a layout and render
     * @param extensions   Markdown extensions that should be used when parsing
     * @param urlSanitizer An optional urlSanitizer
     */
    protected MineMarkCore(TextElementCreator<S, R> textElement, List<ElementCreator<S, R>> elements, Iterable<? extends Extension> extensions, @Nullable UrlSanitizer urlSanitizer) {
        this.markdownParser = Parser.builder().extensions(extensions).build();
        HtmlRenderer.Builder htmlRendererBuilder = HtmlRenderer.builder().extensions(extensions);
        if (urlSanitizer != null) {
            htmlRendererBuilder.urlSanitizer(urlSanitizer).sanitizeUrls(true);
        }
        this.htmlRenderer = htmlRendererBuilder.build();
        this.htmlParser = new MineMarkHtmlParser<>(textElement, elements);
        xmlParser = new org.ccil.cowan.tagsoup.Parser();
        xmlParser.setContentHandler(htmlParser);
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown The markdown text to parse
     * @param style    The style passed to all elements
     * @param charSet  The charset to use when parsing the markdown
     * @return The parsed markdown element
     * @throws SAXException An exception during SAX parsing
     * @throws IOException  An IOException during parsing
     */
    public MineMarkElement<S, R> parse(@NotNull S style, @NotNull String markdown, Charset charSet) throws SAXException, IOException {
        Node document = markdownParser.parse(markdown);
        return parseDocument(style, document, charSet);
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown The markdown text to parse
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
     * @param markdown The markdown text to parse
     * @param style    The style passed to all elements
     * @param charSet  The charset to use when parsing the markdown
     * @return The parsed markdown element
     * @throws SAXException An exception during SAX parsing
     * @throws IOException  An IOException during parsing
     */
    public MineMarkElement<S, R> parse(@NotNull S style, @NotNull Reader markdown, Charset charSet) throws SAXException, IOException {
        Node document = markdownParser.parseReader(markdown);
        return parseDocument(style, document, charSet);
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown The markdown text to parse
     * @param style    The style passed to all elements
     * @return The parsed markdown element
     * @throws SAXException An exception during SAX parsing
     * @throws IOException  An IOException during parsing
     */
    public MineMarkElement<S, R> parse(@NotNull S style, @NotNull Reader markdown) throws SAXException, IOException {
        return parse(style, markdown, StandardCharsets.UTF_8);
    }

    private MineMarkElement<S, R> parseDocument(@NotNull S style, Node document, Charset charSet) throws SAXException, IOException {
        String html = "<minemark>\n" + htmlRenderer.render(document) + "</minemark>";
        // Acquire the lock to make sure this thread is the only one using the parser
        parsingLock.lock();
        try (InputStream stream = new ByteArrayInputStream(html.getBytes(charSet))) {
            htmlParser.setStyle(style, new LayoutStyle(style));
            InputSource source = new InputSource(stream);
            source.setEncoding(charSet.name());
            xmlParser.parse(source);
            return htmlParser.getParsedResult();
        } finally {
            htmlParser.cleanUp();
            parsingLock.unlock();
        }
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
