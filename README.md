<div align="center">

# MineMark
Java Markdown Rendering library
</div>

## Usage
MineMark is build in a modular system, `minemark-core` is the hearth of everything, it is responsible for parsing 
the markdown and generating a layout for it. Then you need a rendering implementation to actually render the markdown.
Currently, only an elementa render system is provided, but it is relatively fast to create your own rendering 
implementation if you wish to do that.

Adding MineMark as a dependency:
```kt
repositories {
    maven("https://maven.dediamondpro.dev/releases")
}

dependencies {
    // Add the core as a dependency
    implementation("dev.dediamondpro:minemark-core:1.0.0")
    // Add elementa rendering implementation as a dependency
    implementation("dev.dediamondpro:minemark-elementa:1.0.0")
}
```

How you render a markdown element will differ from one rendering implementation to another, here is an example of how 
you would do it with the elementa rendering implementation:
```kt
MineMarkComponent("This *is* **were** you input your markdown!").constrain {
    x = 0.pixels()
    y = 0.pixels()
    width = 600.pixels()
} childOf window
```

## Creating your own rendering implementation
To create your own rendering implementation, you first have to implement the rendering of the elements you want to 
support. You can choose what elements you implement, the only requirement is that **you have to provide a text element**.
To implement an element, you have to extend its abstract form, for an example for each element I would recommend you
look at the elementa implementation as a reference. An element takes 2 type variables, the first one (S) is the style,
so you have to create a class that implements the `Style` interface. The second can be any class, it is given to your
elements at render time. If you do not wish to use this you can just set it to be an `Object`.

Once you implemented the elements you want, you have to register them in a core. You can do this as follows:
```java
MineMarkCore<MyStyle, MyRenderObject> core = MineMarkCore.<MyStyle, MyRenderObject>builder()
        // Set the text element to your text element
        .setTextElement(MyTextElement)
        // Set the image element to your image element
        .addElement(Elements.IMAGE, MyImageElement)
        // Set an element with the html tag "myHtmlTag" to MyElement
        .addElement(Arrays.asList("myHtmlTag"), MyElement)
        .build();
```

Then you have to call `core.parse(myStyle, markdown)` to parse the markdown, this will return a `MineMarkElement`.
This element has a `draw`, `beforeDraw` and `onMouseClick` method that should be called by your rendering implementation.