package dev.dediamondpro.minemark.style;

public interface Style {

    TextStyleConfig getTextStyle();

    ParagraphStyleConfig getParagraphStyle();

    LinkStyleConfig getLinkStyle();

    HeadingStyleConfig getHeadingStyle();

    HorizontalRuleStyleConfig getHorizontalRuleStyle();

    ImageStyleConfig getImageStyle();

    ListStyleConfig getListStyle();

    BlockquoteStyleConfig getBlockquoteStyle();

    CodeBlockStyleConfig getCodeBlockStyle();
}
