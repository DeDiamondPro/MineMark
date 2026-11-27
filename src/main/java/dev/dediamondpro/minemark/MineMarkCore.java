/*
 * This file is part of MineMark
 * Copyright (C) 2024-2026 DeDiamondPro
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
import dev.dediamondpro.minemark.error.ErrorResponseFactory;
import dev.dediamondpro.minemark.error.ErrorResponseFactoryProvider;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.HtmlWhiteSpaceUtil;
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
    private final @Nullable ErrorResponseFactory<S, R> errorResponseFactory;

    /**
     * @param textElement          The text element for this core
     * @param elements             Elements supported to create a layout and render
     * @param formattingElements   Elements that apply a formatting style
     * @param extensions           Markdown extensions that should be used when parsing
     * @param urlSanitizer         An optional urlSanitizer
     * @param errorResponseFactory The factory responsible for providing an element if an error occurred,
     *                             to be rendered to the user
     */
    protected MineMarkCore(
            TextElementCreator<S, R> textElement,
            List<ElementCreator<S, R>> elements,
            List<FormattingElement<S, R>> formattingElements,
            Iterable<? extends Extension> extensions,
            @Nullable UrlSanitizer urlSanitizer,
            @Nullable ErrorResponseFactoryProvider<S, R> errorResponseFactory
    ) {
        this.markdownParser = Parser.builder().extensions(extensions).build();
        HtmlRenderer.Builder htmlRendererBuilder = HtmlRenderer.builder().extensions(extensions);
        if (urlSanitizer != null) {
            htmlRendererBuilder.urlSanitizer(urlSanitizer).sanitizeUrls(true);
        }
        this.htmlRenderer = htmlRendererBuilder.build();
        this.htmlParser = new MineMarkHtmlParser<>(textElement, elements, formattingElements);
        this.xmlParser = new org.ccil.cowan.tagsoup.Parser();
        this.xmlParser.setContentHandler(htmlParser);
        this.errorResponseFactory = errorResponseFactory == null ? null : errorResponseFactory.build(this);
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown   The Markdown text to parse
     * @param style      The style passed to all elements
     * @param charSet    The charset to use when parsing the markdown
     * @param catchError Catch errors during parsing and tree initialization,
     *                   only works if an error response factory is set and provides a non-null element
     * @return The parsed markdown element
     * @throws SAXException An exception during SAX parsing
     * @throws IOException  An IOException during parsing
     */
    public @NotNull MineMarkElement<S, R> parse(@NotNull S style, @NotNull String markdown, @NotNull Charset charSet, boolean catchError) throws SAXException, IOException {
        return parseDocument(style, () -> markdownParser.parse(markdown), charSet, catchError);
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
    public @NotNull MineMarkElement<S, R> parse(@NotNull S style, @NotNull String markdown, @NotNull Charset charSet) throws SAXException, IOException {
        return parse(style, markdown, charSet, true);
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
    public @NotNull MineMarkElement<S, R> parse(@NotNull S style, @NotNull String markdown) throws SAXException, IOException {
        return parse(style, markdown, StandardCharsets.UTF_8);
    }

    /**
     * Parse markdown to an element used to render it
     *
     * @param markdown   The Markdown text to parse
     * @param style      The style passed to all elements
     * @param charSet    The charset to use when parsing the markdown
     * @param catchError Catch errors during parsing and tree initialization,
     *                   only works if an error response factory is set and provides a non-null element
     * @return The parsed markdown element
     * @throws SAXException An exception during SAX parsing
     * @throws IOException  An IOException during parsing
     */
    public @NotNull MineMarkElement<S, R> parse(@NotNull S style, @NotNull Reader markdown, @NotNull Charset charSet, boolean catchError) throws SAXException, IOException {
        return parseDocument(style, () -> markdownParser.parseReader(markdown), charSet, catchError);
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
    public @NotNull MineMarkElement<S, R> parse(@NotNull S style, @NotNull Reader markdown, @NotNull Charset charSet) throws SAXException, IOException {
        return parse(style, markdown, charSet, true);
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
    public @NotNull MineMarkElement<S, R> parse(@NotNull S style, @NotNull Reader markdown) throws SAXException, IOException {
        return parse(style, markdown, StandardCharsets.UTF_8);
    }

    private @NotNull MineMarkElement<S, R> parseDocument(@NotNull S style, @NotNull DocumentSupplier documentSupplier, @NotNull Charset charSet, boolean catchErrors) throws SAXException, IOException {
        try {
            return buildElement(style, documentSupplier.get(), charSet);
        } catch (Exception | Error e) {
            // Errors are only caught if and only if an error element is able to be created.
            if (!catchErrors || errorResponseFactory == null) throw e;
            MineMarkElement<S, R> errorResponse = errorResponseFactory.build(style, ErrorResponseFactory.Phase.PARSING, e);
            if (errorResponse == null) throw e;
            return errorResponse;
        }
    }

    private @NotNull MineMarkElement<S, R> buildElement(@NotNull S style, @NotNull Node document, @NotNull Charset charSet) throws SAXException, IOException {
        // Render the document to HTML
        String html = htmlRenderer.render(document);
        // Get the wrapper to wrap the content with, make sure the html does not include it
        String wrapper = getMineMarkWrapper(html);
        // Prepare the HTML for parsing
        html = "<" + wrapper + ">" + html + "</" + wrapper + ">";
        // Remove unnecessary whitespaces from the html
        html = HtmlWhiteSpaceUtil.INSTANCE.removeUnnecessaryWhiteSpace(html);
        // Acquire the lock to make sure this thread is the only one using the parser
        parsingLock.lock();
        try (InputStream stream = new ByteArrayInputStream(html.getBytes(charSet))) {
            htmlParser.setStyle(style, new LayoutStyle(style));
            htmlParser.setWrapper(wrapper);
            InputSource source = new InputSource(stream);
            source.setEncoding(charSet.name());
            xmlParser.parse(source);
            MineMarkElement<S, R> element = htmlParser.getParsedResult();
            element.setErrorResponseFactory(errorResponseFactory);
            return element;
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

    @FunctionalInterface
    private interface DocumentSupplier {
        Node get() throws IOException;
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
