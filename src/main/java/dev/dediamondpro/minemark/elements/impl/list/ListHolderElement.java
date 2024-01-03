package dev.dediamondpro.minemark.elements.impl.list;

import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.NoPadding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class ListHolderElement<L extends LayoutConfig, R> extends ChildBasedElement<L, R> {
    protected ListType listType;
    public ListHolderElement(@NotNull L layoutConfig, @Nullable Element<L, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
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
    protected float getPadding(LayoutData layoutData) {
        return layoutConfig.getSpacingConfig().getListPadding();
    }

    public int getElementPosition(Element<L, R> element) {
        return children.indexOf(element);
    }

    public ListType getListType() {
        return listType;
    }

    public enum ListType{
        ORDERED,
        UNORDERED
    }
}
