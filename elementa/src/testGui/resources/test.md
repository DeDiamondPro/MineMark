# MineMark test document

Regular paragraph text with **bold**, *italic*, ***bold italic***, ~~strikethrough~~,
`inline code` and a [link to the repository](https://github.com/DeDiamondPro/MineMark).
Long paragraphs should wrap at the width of the window, so resize it to check the layout
recalculates instead of clipping or overlapping the text below it.

<hr>

# Heading level 1
## Heading level 2
### Heading level 3
#### Heading level 4
##### Heading level 5
###### Heading level 6

---

## Lists

- Unordered item
- Item with `code` and **bold**
    - Nested item
    - Another nested item
        - Third level
- Last item

1. Ordered item
2. Second item
    1. Nested ordered item
    2. Another one
3. Third item

## Blockquote

> Blockquotes can contain **formatting**, `code` and [links](https://github.com/DeDiamondPro/MineMark).
>
> They can also span multiple paragraphs.


> Blockquote inception
> > Because why wouldn't you do this
> > > Its great!

## Code block

```java
MineMarkCore<MarkdownStyle, UMatrixStack> core = MineMarkCore.builder()
        .addExtension(TablesExtension.create())
        .addElementaExtensions()
        .build();
```

## Table

| Element     | Supported | Notes                   |
|-------------|-----------|-------------------------|
| Headings    | yes       | Levels 1 through 6      |
| Lists       | yes       | Ordered and unordered   |
| Tables      | yes       | Via commonmark tables   |
| Images      | yes       | Downloaded over HTTPS   |

## Image

![](https://picsum.photos/300/200)

Text ![](https://picsum.photos/100/200) and ![](https://picsum.photos/200/100) images
