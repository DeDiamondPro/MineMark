package dev.dediamondpro.minemark;

import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.ElementLoader;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;
import java.util.Map;

public class MineMarkHtmlParser<L extends LayoutConfig, R> extends DefaultHandler {
    private final Map<List<String>, ElementLoader<L, R>> elements;
    private MineMarkElement<L, R> markDown;
    private Element<L, R> currentElement;
    private L layoutConfig;
    private StringBuilder textBuilder;

    protected MineMarkHtmlParser(Map<List<String>, ElementLoader<L, R>> elements) {
        this.elements = elements;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("minemark")) {
            markDown = new MineMarkElement<>(layoutConfig, attributes);
            currentElement = markDown;
            return;
        }
        addText();
        ElementLoader<L, R> elementCreator = findElement(qName);
        if (elementCreator == null) {
            System.out.println("Unknown element " + qName);
            //textBuilder.append("<").append(qName).append(">");
            return;
        }
        currentElement = elementCreator.get(currentElement.getLayoutConfig(), currentElement, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        boolean hasElement = hasElement(qName);
        if (qName.equals("minemark")) return;
        //if (!hasElement) textBuilder.append("</").append(qName).append(">");
        addText();
        if (!hasElement) return;
        currentElement = currentElement.getParent();
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (textBuilder == null) {
            textBuilder = new StringBuilder();
        }
        textBuilder.append(ch, start, length);
    }

    private void addText() {
        String text = textBuilder == null ? null : textBuilder.toString();
        if (text == null || text.isEmpty()) return;
        ElementLoader<L, R> textLoader = findElement("text");
        if (textLoader == null) {
            throw new IllegalStateException("No text element provided");
        }
        textLoader.get(currentElement.getLayoutConfig(), currentElement, "text", null).setText(textBuilder.toString());
        textBuilder = new StringBuilder();
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

    protected void setLayoutConfig(L layoutConfig) {
        this.layoutConfig = layoutConfig;
    }

    protected MineMarkElement<L, R> getParsedResult() {
        return markDown;
    }

    protected void cleanUp() {
        markDown = null;
        currentElement = null;
        layoutConfig = null;
        textBuilder = null;
    }
}