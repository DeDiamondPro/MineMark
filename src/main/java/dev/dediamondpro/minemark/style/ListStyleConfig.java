package dev.dediamondpro.minemark.style;

public class ListStyleConfig {
    private final float indentation;
    private final float padding;

    public ListStyleConfig(float indentation, float padding) {
        this.indentation = indentation;
        this.padding = padding;
    }

    public float getIndentation() {
        return indentation;
    }

    public float getPadding() {
        return padding;
    }
}
