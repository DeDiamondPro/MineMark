package dev.dediamondpro.minemark.elements;


import dev.dediamondpro.minemark.config.LayoutConfig;
import dev.dediamondpro.minemark.config.RenderConfig;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public interface ElementLoader<L extends LayoutConfig, R extends RenderConfig> {
    Element<L, R> get(@Nullable Element<L, R> parent, Attributes attributes);
}
