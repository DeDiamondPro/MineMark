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

package dev.dediamondpro.minemark.utils;

import java.util.*;

/**
 * Removes whitespaces that shouldn't be rendered
 */
public class HtmlWhiteSpaceUtil {
    public static final HtmlWhiteSpaceUtil INSTANCE = new HtmlWhiteSpaceUtil();

    // Elements always starting a new line
    private static final Set<String> BLOCK_ELEMENTS = new HashSet<>(Arrays.asList(
            "p", "div", "hr", "h1", "h2", "h3", "h4", "h5", "h6", "hgroup", "ul", "ol", "li", "dl", "dt", "dd",
            "blockquote", "pre", "table", "caption", "thead", "tbody", "tfoot", "tr", "td", "th", "center",
            "section", "article", "aside", "nav", "header", "footer", "main", "figure", "figcaption",
            "details", "summary", "address", "form", "fieldset", "legend", "colgroup", "col", "optgroup",
            "option", "noscript", "body", "html"
    ));
    // Container elements in line where after these spaces should not be removed
    private static final Set<String> REPLACED_ELEMENTS = new HashSet<>(Arrays.asList(
            "img", "input", "select", "textarea", "button", "video", "audio", "iframe", "embed", "object",
            "canvas", "svg", "picture"
    ));

    private HtmlWhiteSpaceUtil() {
    }

    public String removeUnnecessaryWhiteSpace(String html) {
        return new Collapser(html).collapse();
    }

    /**
     * Walks the HTML and copies over what is needed for rendering
     */
    private static final class Collapser {
        private final String html;
        private final StringBuilder output;
        private boolean preformatted = false;
        /** A collapsible run of whitespace has been read, but we don't know yet if it renders */
        private boolean pendingSpace = false;
        /** Where that run of whitespace belongs in the output, it is only written once we know it renders */
        private int spaceIndex = 0;
        private boolean lineStart = true;

        private Collapser(String html) {
            this.html = html;
            this.output = new StringBuilder(html.length());
        }

        private String collapse() {
            int index = 0;
            while (index < html.length()) {
                int markup = nextMarkup(index);
                if (markup > index) {
                    appendText(index, markup);
                }
                if (markup == html.length()) {
                    break;
                }
                index = appendMarkup(markup);
            }
            return output.toString();
        }

        /**
         * @return The index of the next tag, comment or declaration, the length of the HTML if there is none
         */
        private int nextMarkup(int from) {
            int index = from;
            while (true) {
                index = html.indexOf('<', index);
                if (index == -1) {
                    return html.length();
                }
                // A < that doesn't start markup is text, the same way a browser treats it
                if (index + 1 < html.length() && startsMarkup(html.charAt(index + 1))) {
                    return index;
                }
                index++;
            }
        }

        /**
         * Copies over the text between the given indices, collapsing the whitespace in it
         */
        private void appendText(int start, int end) {
            if (preformatted) {
                output.append(html, start, end);
                return;
            }
            int index = start;
            while (index < end) {
                if (isCollapsibleWhiteSpace(html.charAt(index))) {
                    markWhiteSpace();
                    index++;
                    continue;
                }
                int wordEnd = index;
                boolean normalize = false;
                while (wordEnd < end && !isCollapsibleWhiteSpace(html.charAt(wordEnd))) {
                    normalize |= needsNormalizing(html.charAt(wordEnd));
                    wordEnd++;
                }
                writePendingSpace();
                appendWord(index, wordEnd, normalize);
                lineStart = false;
                index = wordEnd;
            }
        }

        /**
         * Copies over a run of text without any collapsible whitespace in it
         */
        private void appendWord(int start, int end, boolean normalize) {
            if (!normalize) {
                output.append(html, start, end);
                return;
            }
            for (int index = start; index < end; index++) {
                char character = html.charAt(index);
                output.append(needsNormalizing(character) ? '\u00A0' : character);
            }
        }

        /**
         * Copies over the markup starting at the given index
         *
         * @return The index right after the markup
         */
        private int appendMarkup(int start) {
            char second = html.charAt(start + 1);
            if (second == '!' || second == '?') {
                // Comments and declarations render nothing, but they don't interrupt the text around them either
                int end = html.startsWith("<!--", start) ? endOfComment(start) : endOfTag(start + 2);
                output.append(html, start, end);
                return end;
            }

            boolean closing = second == '/';
            int nameStart = closing ? start + 2 : start + 1;
            int nameEnd = nameStart;
            while (nameEnd < html.length() && isNameChar(html.charAt(nameEnd))) {
                nameEnd++;
            }
            String name = html.substring(nameStart, nameEnd).toLowerCase(Locale.ROOT);
            int end = endOfTag(nameEnd);

            if (preformatted) {
                if (!BLOCK_ELEMENTS.contains(name)) {
                    // Anything that isn't block level is part of the preformatted element, line breaks included
                    output.append(html, start, end);
                    return end;
                }
                // The html parser ends a preformatted element at the next block level element, whether that is its
                // own closing tag or not, so one that is never closed can't swallow the rest of the document
                preformatted = false;
            }
            if (name.equals("br") || BLOCK_ELEMENTS.contains(name)) {
                output.append(html, start, end);
                startNewLine();
                // A self-closing preformatted element has no content, it would never be closed again
                if (name.equals("pre") && !closing && !isSelfClosing(html, start, end)) {
                    preformatted = true;
                }
                return end;
            }
            if (REPLACED_ELEMENTS.contains(name)) {
                // A replaced element renders something, so it ends the whitespace run around it like text does
                writePendingSpace();
                output.append(html, start, end);
                lineStart = false;
                return end;
            }
            // An inline tag renders nothing by itself, so it doesn't decide the fate of the whitespace around it
            output.append(html, start, end);
            return end;
        }

        private void startNewLine() {
            // Whitespace at the end of a line is never rendered, so drop whatever is still pending
            pendingSpace = false;
            lineStart = true;
        }

        private void markWhiteSpace() {
            if (pendingSpace) {
                // A run of whitespace only ever renders as the single space the run started with
                return;
            }
            pendingSpace = true;
            spaceIndex = output.length();
        }

        private void writePendingSpace() {
            if (!pendingSpace) {
                return;
            }
            pendingSpace = false;
            // Whitespace at the start of a line is never rendered
            if (lineStart) {
                return;
            }
            // Any inline tags the run was interrupted by have been written since, put the space back in front of them
            output.insert(spaceIndex, ' ');
        }

        /**
         * @return The index right after the tag that has its attributes starting at the given index
         */
        private int endOfTag(int from) {
            char quote = 0;
            for (int index = from; index < html.length(); index++) {
                char character = html.charAt(index);
                if (quote != 0) {
                    if (character == quote) {
                        quote = 0;
                    }
                } else if (character == '"' || character == '\'') {
                    // A > inside an attribute value doesn't end the tag
                    quote = character;
                } else if (character == '>') {
                    return index + 1;
                }
            }
            return html.length();
        }

        /**
         * @return The index right after the comment starting at the given index
         */
        private int endOfComment(int start) {
            // <!--> and <!---> are empty comments, they end without a closing -->
            int content = start + 4;
            if (content < html.length() && html.charAt(content) == '>') {
                return content + 1;
            }
            if (content + 1 < html.length() && html.charAt(content) == '-' && html.charAt(content + 1) == '>') {
                return content + 2;
            }
            return endOf(content, "-->");
        }

        /**
         * @return The index right after the first occurrence of the token, the length of the html if there is none
         */
        private int endOf(int from, String token) {
            int end = html.indexOf(token, from);
            return end == -1 ? html.length() : end + token.length();
        }

        private static boolean isSelfClosing(String html, int start, int end) {
            return end - start > 2 && html.charAt(end - 1) == '>' && html.charAt(end - 2) == '/';
        }

        private static boolean startsMarkup(char character) {
            return character == '!' || character == '?' || character == '/'
                    || (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
        }

        private static boolean isNameChar(char character) {
            return character != '>' && character != '/' && !isCollapsibleWhiteSpace(character);
        }

        /**
         * @return If this is a whitespace character that collapses into the single space of its run
         */
        private static boolean isCollapsibleWhiteSpace(char character) {
            if (character == ' ' || character == '\t' || character == '\n'
                    || character == '\u000B' || character == '\f' || character == '\r') {
                return true;
            }
            return Character.isSpaceChar(character) && !isTextSpace(character);
        }

        /**
         * @return If this space is a character in its own right instead of a gap that may collapse away
         */
        private static boolean isTextSpace(char character) {
            return character == '\u3000' || isNonBreakingSpace(character);
        }

        /**
         * @return If this space is meant to stay where it is
         */
        private static boolean isNonBreakingSpace(char character) {
            return character == '\u00A0' || character == '\u2007' || character == '\u202F';
        }

        /**
         * @return If this is a non-breaking space the layout and the fonts in use don't know about
         */
        private static boolean needsNormalizing(char character) {
            return character == '\u2007' || character == '\u202F';
        }
    }
}
