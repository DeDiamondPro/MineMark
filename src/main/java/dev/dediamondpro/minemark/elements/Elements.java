package dev.dediamondpro.minemark.elements;

import java.util.Arrays;
import java.util.List;

public enum Elements {
    PARAGRAPH(listOf("p")),
    FORMATTING(listOf("strong", "b", "em", "i", "ins", "u", "del", "s", "pre")),
    HEADING(listOf("h1", "h2", "h3", "h4", "h5", "h6")),
    ALIGNMENT(listOf("div", "center")),
    LINK(listOf("a")),
    IMAGE(listOf("img")),
    LIST_PARENT(listOf("ol", "ul")),
    LIST_ELEMENT(listOf("li")),
    HORIZONTAL_RULE(listOf("hr")),
    BLOCKQUOTE(listOf("blockquote")),
    CODE_BLOCK(listOf("code")),
    TABLE(listOf("table")),
    TABLE_ROW(listOf("tr")),
    TABLE_CELL(listOf("td", "th")),
    ;

    public final List<String> tags;

    Elements(List<String> tags) {
        this.tags = tags;
    }

    private static List<String> listOf(String... elements) {
        return Arrays.asList(elements);
    }
}
