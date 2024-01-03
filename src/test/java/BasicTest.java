import dev.dediamondpro.minemark.LayoutConfig;
import dev.dediamondpro.minemark.MineMarkCore;
import dev.dediamondpro.minemark.elements.Elements;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BasicTest {

    private final MineMarkCore<LayoutConfig, Object> core = MineMarkCore.builder()
            .addExtension(StrikethroughExtension.create())
            .addElement(Elements.TEXT, DummyTextElement::new)
            .build();

    private final LayoutConfig config = new LayoutConfig(5, new LayoutConfig.SpacingConfig(1f, 2f, 2f, 5f), new LayoutConfig.HeadingConfig(6, 5, 4, 3, 2, 1));

    @Test
    public void test() throws IOException, SAXException {
        MineMarkElement<LayoutConfig, Object> element = core.parse(config, "```\ntest\n```\nHello<br>World");
        //element.beforeDraw(0f, 0f, 25f, 0f, 0f, new Object());
        System.out.println(element.getTree());
    }

    @Test
    public void testFile() throws IOException, SAXException {
        MineMarkElement<LayoutConfig, Object> element = core.parse(config, readFromInputStream(getClass().getResourceAsStream("test.md")));
        System.out.println(element.getTree());
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
