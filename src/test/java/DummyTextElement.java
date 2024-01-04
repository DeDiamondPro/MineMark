import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.TextElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class DummyTextElement extends TextElement<LayoutConfig, Object> {
    public DummyTextElement(@NotNull LayoutConfig layoutConfig, @Nullable Element<LayoutConfig, Object> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(layoutConfig, parent, qName, attributes);
    }

    @Override
    public void generateLayout(LayoutData layoutData) {
        super.generateLayout(layoutData);
    }

    @Override
    public void drawText(@NotNull String text, float x, float bottomY, boolean hovered, @NotNull Object renderData) {

    }

    @Override
    public float getTextWidth(@NotNull String text) {
        return text.length();
    }

    @Override
    public float getTextHeight(@NotNull String text) {
        return 1;
    }
}
