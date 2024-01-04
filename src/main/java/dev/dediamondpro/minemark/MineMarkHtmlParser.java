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
    private L layoutConfig;
    private StringBuilder textBuilder = new StringBuilder();
    private boolean isPreFormatted = false;

    protected MineMarkHtmlParser(Map<List<String>, ElementLoader<L, R>> elements) {
        this.elements = elements;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "minemark":
                markDown = new MineMarkElement<>(layoutConfig, attributes);
                currentElement = markDown;
                return;
            case "br":
                textBuilder.append("\n");
                return;
            case "pre":
                isPreFormatted = true;
                break;
        }
        addText();
        ElementLoader<L, R> elementCreator = findElement(qName);
        if (elementCreator == null) {
            System.out.println("Unknown element " + qName);
            return;
        }
        currentElement = elementCreator.get(currentElement.getLayoutConfig(), currentElement, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "minemark":
            case "br":
                return;
            case "pre":
                isPreFormatted = false;
                break;
        }
        addText();
        if (!hasElement(qName)) return;
        currentElement = currentElement.getParent();
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        // All newlines are ignored unless this element is preformatted
        int newLength = length;
        if (isPreFormatted) {
            if (ch[start] == '\n') {
                newLength--;
            }
            if (ch[start + length - 1] == '\n') {
                newLength--;
            }
        } else {
            for (int i = start; i < start + length; i++) {
                if (ch[i] == '\n') {
                    newLength--;
                }
            }
        }

        char[] modifiedCh = new char[newLength];
        int index = 0;
        for (int i = start; i < start + length; i++) {
            if (ch[i] != '\n' || (isPreFormatted && i != start && i != start + length - 1)) {
                modifiedCh[index++] = ch[i];
            }
        }
        textBuilder.append(modifiedCh);
    }

    private void addText() {
        String text = textBuilder.toString();
        if (text.isEmpty()) return;
        ElementLoader<L, R> textLoader = findElement("text");
        if (textLoader == null) {
            throw new IllegalArgumentException("No text element provided!");
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
        textBuilder = new StringBuilder();
        isPreFormatted = false;
    }
}