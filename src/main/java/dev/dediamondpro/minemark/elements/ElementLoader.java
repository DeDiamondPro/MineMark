package dev.dediamondpro.minemark.elements;


import dev.dediamondpro.minemark.LayoutConfig;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public interface ElementLoader<L extends LayoutConfig, R> {
    Element<L, R> get(@Nullable Element<L, R> parent, Attributes attributes);
}
