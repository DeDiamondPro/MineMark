<div align="center">

# MineMark
Java Markdown Rendering library
</div>

## Usage
MineMark is build in a modular system, `minemark-core` is the hearth of everything, it is responsible for parsing 
the markdown and generating a layout for it. Then you need a rendering implementation to actually render the markdown.
Currently, there is a Minecraft and Elementa rendering implementation available, but it is relatively easy to create your own if you wish!

Adding MineMark as a dependency (latest MineMark version: ![](https://img.shields.io/badge/dynamic/xml?label=%20&query=/metadata/versioning/versions/version[not(contains(text(),%27%2B%27))][last()]&url=https://maven.dediamondpro.dev/releases/dev/dediamondpro/minemark-core/maven-metadata.xml)):
```kt
repositories {
    maven("https://maven.dediamondpro.dev/releases")
}

dependencies {
    // Add the core as a dependency, replace {version} with the most recent version of MineMark
    implementation("dev.dediamondpro:minemark-core:1{version}")

    // Example to add the Minecraft rendering implementation as a dependency,
    // replace {platform} with the Minecraft version and mod loader (example: 1.20.4-fabric)
    modImplementation("dev.dediamondpro:minemark-minecraft-{platform}:{version}")

    // Example to add the elementa rendering implementation as a dependency
    implementation("dev.dediamondpro:minemark-elementa:{version}")
}
```
It is recommended to **shade and relocate MineMark** if you are using it in a Minecraft mod since no guarantees will be
provided regarding backwards compatibility.

## Minecraft rendering implementation

To use the Minecraft rendering implementation you first have to create a markdown drawable like this
```java
MineMarkDrawable markdown = new MineMarkDrawable("This *is* **where** you input your markdown!");
```
Then to render it call the draw method.
```java
markdown.draw(x, y, width, mouseX, mouseY, drawContext);
```

## Elementa rendering implementation

You can create an MineMark elementa component like this, it will act like any other elementa component.
```kt
MineMarkComponent("This *is* **where** you input your markdown!").constrain {
    x = 0.pixels()
    y = 0.pixels()
    width = 600.pixels()
} childOf window
```

## Creating your own rendering implementation

To create your own rendering implementation, you first have to implement the rendering of the elements you want to 
support. You can choose what elements you implement, the only requirement is that **you have to provide a text element**.
To implement an element, you have to extend its abstract form, for an example for each element I would recommend you
look at the elementa implementation as a reference. An element takes 2 type variables, the first one (`S`) is the style,
so you have to create a class that implements the `Style` interface. The second (`R`) can be any class, it is given to your
elements at render time. If you do not wish to use this you can just set it to be an `Object`.

Once you implemented the elements you want, you have to register them in a core. You can do this as follows:
```java
MineMarkCore<MyStyle, MyRenderObject> core = MineMarkCore.<MyStyle, MyRenderObject>builder()
        // Set the text element to your text element
        .setTextElement(MyTextElement::new)
        // Set the image element to your image element
        .addElement(Elements.IMAGE, MyImageElement::new)
        // Set an element with the html tag "myHtmlTag" to MyElement
        .addElement(Arrays.asList("myHtmlTag"), MyElement::new)
        .build();
```

Then you have to call `core.parse(myStyle, markdown)` to parse the markdown, this will return a `MineMarkElement`.
This element has a `draw`, `beforeDraw` and `onMouseClick` method that should be called by your rendering implementation.
