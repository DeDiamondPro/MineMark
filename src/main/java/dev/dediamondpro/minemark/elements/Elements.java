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

package dev.dediamondpro.minemark.elements;

import java.util.Arrays;
import java.util.List;

public enum Elements {
    PARAGRAPH(listOf("p")),
    TEXT_FORMATTING(listOf("strong", "b", "em", "i", "ins", "u", "del", "s", "pre")),
    HEADING(listOf("h1", "h2", "h3", "h4", "h5", "h6")),
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
