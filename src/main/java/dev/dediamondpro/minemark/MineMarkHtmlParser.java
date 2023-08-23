package dev.dediamondpro.minemark;

import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.ElementLoader;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;
import java.util.Map;

public class MineMarkHtmlParser<L extends LayoutConfig, R> extends DefaultHandler {
    private final Map<List<String>, ElementLoader<L, R>> elements;
    private MineMarkElement<L, R> markDown;
    private Element<L, R> currentElement;
    private StringBuilder textBuilder;

    protected MineMarkHtmlParser(Map<List<String>, ElementLoader<L, R>> elements) {
        this.elements = elements;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("minemark")) {
            markDown = new MineMarkElement<>(attributes);
            currentElement = markDown;
            return;
        }
        if (textBuilder != null && !textBuilder.toString().isEmpty()) {
            currentElement.addText(textBuilder.toString());
            textBuilder = null;
        }
        ElementLoader<L, R> elementCreator = findElement(qName);
        if (elementCreator == null) {
            System.out.println("Unknown element " + qName);
            return;
        }
        currentElement = elementCreator.get(currentElement, attributes);
        textBuilder = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("minemark")) return;
        String text = textBuilder == null ? null : textBuilder.toString();
        textBuilder = null;
        if (!hasElement(qName)) return;
        if (text != null) currentElement.addText(text);
        currentElement = currentElement.getParent();
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (textBuilder == null) {
            textBuilder = new StringBuilder();
        } else {
            textBuilder.append(ch, start, length);
        }
    }

    private ElementLoader<L, R> findElement(String qName) {
        for (Map.Entry<List<String>, ElementLoader<L, R>> element : elements.entrySet()) {
            if (element.getKey().contains(qName)) {
                return element.getValue();
            }
        }
        return null;
    }

    private boolean hasElement(String qName) {
        return findElement(qName) != null;
    }

    protected MineMarkElement<L, R> getParsedResult() {
        return markDown;
    }
}
