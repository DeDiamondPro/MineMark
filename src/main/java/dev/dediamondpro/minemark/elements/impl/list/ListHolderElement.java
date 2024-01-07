package dev.dediamondpro.minemark.elements.impl.list;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class ListHolderElement<S extends Style, R> extends ChildBasedElement<S, R> {
    protected ListType listType;
    public ListHolderElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        switch (qName) {
            default:
            case "ol":
                listType = ListType.ORDERED;
                break;
            case "ul":
                listType = ListType.UNORDERED;
                break;
        }
    }

    @Override
    protected float getPadding(LayoutData layoutData, R renderData) {
        return style.getListStyle().getPadding();
    }

    public ListType getListType() {
        return listType;
    }

    public enum ListType{
        ORDERED,
        UNORDERED
    }
}
