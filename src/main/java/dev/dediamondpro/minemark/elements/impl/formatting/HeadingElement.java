package dev.dediamondpro.minemark.elements.impl.formatting;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.style.HeadingLevelStyleConfig;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public abstract class HeadingElement<S extends Style, R> extends ChildBasedElement<S, R> implements Inline {
    protected final HeadingType headingType;
    protected final HeadingLevelStyleConfig headingStyle;
    protected LayoutData.MarkDownElementPosition dividerPosition;

    public HeadingElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        this.headingType = HeadingType.getFromHtmlTag(qName);
        this.headingStyle = headingType.getHeadingStyle(style);
        this.layoutStyle = this.layoutStyle.clone();
        this.layoutStyle.setFontSize(headingStyle.getFontSize());
    }

    @Override
    protected void draw(float xOffset, float yOffset, float mouseX, float mouseY, R renderData) {
        super.draw(xOffset, yOffset, mouseX, mouseY, renderData);
        if (headingStyle.hasDivider()) {
            drawDivider(
                    xOffset + dividerPosition.getX(), yOffset + dividerPosition.getY(),
                    dividerPosition.getWidth(), dividerPosition.getHeight(), headingStyle.getDividerColor(), renderData
            );
        }
    }

    @Override
    protected void generateLayout(LayoutData layoutData, R renderData) {
        if (layoutData.isLineOccupied()) {
            layoutData.nextLine();
        }
        super.generateLayout(layoutData, renderData);
        if (headingStyle.hasDivider()) {
            layoutData.setBottomSpacing(headingStyle.getSpaceBeforeDivider());
            layoutData.nextLine();
            dividerPosition = layoutData.addElement(layoutStyle.getAlignment(), layoutData.getMaxWidth(), headingStyle.getDividerHeight());
            layoutData.setBottomSpacing(headingStyle.getPadding());
        }
        layoutData.nextLine();
    }

    @Override
    protected float getPadding(LayoutData layoutData, R renderData) {
        return headingStyle.getPadding();
    }

    protected abstract void drawDivider(float x, float y, float width, float height, Color color, R renderData);

    @Override
    public String toString() {
        return "HeadingElement {" + headingType + "}";
    }

    public enum HeadingType {
        H1, H2, H3, H4, H5, H6;

        public HeadingLevelStyleConfig getHeadingStyle(Style style) {
            switch (this) {
                case H1:
                    return style.getHeadingStyle().getH1();
                case H2:
                    return style.getHeadingStyle().getH2();
                case H3:
                    return style.getHeadingStyle().getH3();
                case H4:
                    return style.getHeadingStyle().getH4();
                case H5:
                    return style.getHeadingStyle().getH5();
                case H6:
                    return style.getHeadingStyle().getH6();
            }
            throw new IllegalStateException("This should literally never be thrown.");
        }

        public static HeadingType getFromHtmlTag(String tag) {
            for (HeadingType type : HeadingType.values()) {
                if (tag.equals(type.name().toLowerCase())) {
                    return type;
                }
            }
            throw new IllegalStateException("Unknown heading tag  \" " + tag + "\"");
        }
    }
}
