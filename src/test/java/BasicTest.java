import dev.dediamondpro.minemark.LayoutStyle;
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

    /*private final MineMarkCore<LayoutStyle, Object> core = MineMarkCore.builder()
            .addExtension(StrikethroughExtension.create())
            .addElement(Elements.TEXT, DummyTextElement::new)
            .build();

    private final LayoutStyle config = new LayoutStyle(5, new LayoutStyle.SpacingConfig(1f, 2f, 5f), new LayoutStyle.HeadingConfig(6, 5, 4, 3, 2, 1));

    @Test
    public void test() throws IOException, SAXException {
        MineMarkElement<LayoutStyle, Object> element = core.parse(config, "> Test");
        //element.beforeDraw(0f, 0f, 25f, 0f, 0f, new Object());
        System.out.println(element.getTree());
    }

    @Test
    public void testFile() throws IOException, SAXException {
        MineMarkElement<LayoutStyle, Object> element = core.parse(config, readFromInputStream(getClass().getResourceAsStream("test.md")));
        System.out.println(element.getTree());
    }*/

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
