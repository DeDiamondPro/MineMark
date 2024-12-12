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

import dev.dediamondpro.minemark.polyui.MarkdownComponent
import org.polyfrost.polyui.PolyUI
import org.polyfrost.polyui.renderer.impl.GLFWWindow
import org.polyfrost.polyui.renderer.impl.NVGRenderer
import org.polyfrost.polyui.unit.Vec2
import org.polyfrost.polyui.unit.by

fun main() {
    val window = GLFWWindow("PolyUI Markdown Test", 800, 1000)
    val polyUI =
        PolyUI(
            MarkdownComponent(
                markdown = "Test **string** *with* a<br>newline <u>because</u> ~~why~~ not and also a "
                        + "[link](https://polyfrost.org), this should also automatically wrap if I make this text long "
                        + "enough<br>"
                        + "Image: ![](https://picsum.photos/100/100.jpg) <br>"
                        + "HTML Image: <img src=\"https://picsum.photos/200/100.jpg\" width=\"200px\" height=\"100px\">"
                        + "\n# Heading 1"
                        + "\n## Heading 2"
                        + "\n### Heading 3"
                        + "\n#### Heading 4"
                        + "\n##### Heading 5"
                        + "\n---"
                        + "\nThis `is an inline` codeblock!"
                        + "\n```\nAnd this one isn't     inline\nand has multiple lines!\n```"
                        + "\n> And this is a blockquote<br>because why not?"
                        + "\n1. And an ordered list"
                        + "\n   - With an unordered list inside"
                        + "\n   - like this"
                        + "\n2. :)"
                        + "\n\n| Hi | Test | E       | E | E |\n" +
                        "|----|------|---------|---|---|\n" +
                        "| E  | E    | E       | E |   |\n" +
                        "| E  | E    | EEEEEEE |   | E |\n" +
                        "| E  | E    | E       | E |   |",
                size = Vec2(600f, 1000f)
            ),
            renderer = NVGRenderer,
            size = 800f by 1000f
        )
    window.open(polyUI)
}