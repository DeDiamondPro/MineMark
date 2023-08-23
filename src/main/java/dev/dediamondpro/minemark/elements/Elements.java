package dev.dediamondpro.minemark.elements;

import java.util.Arrays;
import java.util.List;

public enum Elements {
    PARAGRAPH(listOf("p")),
    TEXT(listOf()),
    LINK(listOf("a"));

    public final List<String> tags;

    Elements(List<String> tags) {
        this.tags = tags;
    }

    private static List<String> listOf(String... elements) {
        return Arrays.asList(elements);
    }
}
