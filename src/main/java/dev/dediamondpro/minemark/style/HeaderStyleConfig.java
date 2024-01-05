package dev.dediamondpro.minemark.style;

public class HeaderStyleConfig {
    private final HeaderLevelStyleConfig h1;
    private final HeaderLevelStyleConfig h2;
    private final HeaderLevelStyleConfig h3;
    private final HeaderLevelStyleConfig h4;
    private final HeaderLevelStyleConfig h5;
    private final HeaderLevelStyleConfig h6;

    public HeaderStyleConfig(HeaderLevelStyleConfig h1, HeaderLevelStyleConfig h2, HeaderLevelStyleConfig h3, HeaderLevelStyleConfig h4, HeaderLevelStyleConfig h5, HeaderLevelStyleConfig h6) {
        this.h1 = h1;
        this.h2 = h2;
        this.h3 = h3;
        this.h4 = h4;
        this.h5 = h5;
        this.h6 = h6;
    }

    public HeaderLevelStyleConfig getH1() {
        return h1;
    }

    public HeaderLevelStyleConfig getH2() {
        return h2;
    }

    public HeaderLevelStyleConfig getH3() {
        return h3;
    }

    public HeaderLevelStyleConfig getH4() {
        return h4;
    }

    public HeaderLevelStyleConfig getH5() {
        return h5;
    }

    public HeaderLevelStyleConfig getH6() {
        return h6;
    }
}
