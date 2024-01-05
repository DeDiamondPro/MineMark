package dev.dediamondpro.minemark.style;

public class HeadingStyleConfig {
    private final HeadingLevelStyleConfig h1;
    private final HeadingLevelStyleConfig h2;
    private final HeadingLevelStyleConfig h3;
    private final HeadingLevelStyleConfig h4;
    private final HeadingLevelStyleConfig h5;
    private final HeadingLevelStyleConfig h6;

    public HeadingStyleConfig(HeadingLevelStyleConfig h1, HeadingLevelStyleConfig h2, HeadingLevelStyleConfig h3, HeadingLevelStyleConfig h4, HeadingLevelStyleConfig h5, HeadingLevelStyleConfig h6) {
        this.h1 = h1;
        this.h2 = h2;
        this.h3 = h3;
        this.h4 = h4;
        this.h5 = h5;
        this.h6 = h6;
    }

    public HeadingLevelStyleConfig getH1() {
        return h1;
    }

    public HeadingLevelStyleConfig getH2() {
        return h2;
    }

    public HeadingLevelStyleConfig getH3() {
        return h3;
    }

    public HeadingLevelStyleConfig getH4() {
        return h4;
    }

    public HeadingLevelStyleConfig getH5() {
        return h5;
    }

    public HeadingLevelStyleConfig getH6() {
        return h6;
    }
}
