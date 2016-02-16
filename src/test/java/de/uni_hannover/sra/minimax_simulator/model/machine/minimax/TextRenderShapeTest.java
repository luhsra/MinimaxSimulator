package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.machine.shape.LabelShape;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.TextRenderShape;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.render.DummyRenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.render.RenderEnvironment;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * Runs some tests on {@link TextRenderShape}.
 *
 * @author Philipp Rohde
 */
public class TextRenderShapeTest {

    private static final MinimaxDisplay DISPLAY = new MinimaxDisplay();
    private static Method getStringDimension;

    /**
     * Sets up the test instance.
     *
     * @throws Exception
     *          thrown if something with the reflections went wrong
     */
    @BeforeClass
    public static void initialize() throws Exception {
        getStringDimension = TextRenderShape.class.getDeclaredMethod("getStringDimension", String.class);
        getStringDimension.setAccessible(true);
    }

    /**
     * Tests creating an instance of {@code TextRenderShape} without the {@code FontMetricsProvider}.
     */
    @Test
    public void testConstructor() {
        try {
            new LabelShape(null);               // LabelShape extends TextRenderShape
        } catch (NullPointerException e) {
            assertEquals("no font provider exception", "FontMetricsProvider must not be null", e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link TextRenderShape#getStringDimension(String)}.
     *
     * @throws Exception
     *          thrown if something with the reflections went wrong
     */
    @Test
    public void testGetStringDimension() throws Exception {
        TextRenderShape shape = new LabelShape(DISPLAY);
        //shape.updateShape(new Label("Test"));

        Dimension dim = (Dimension) getStringDimension.invoke(shape, "Test");
        assertEquals("dimension of \"Test\"", new Dimension(36, 13), dim);

        // run again to cover cached dimension
        dim = (Dimension) getStringDimension.invoke(shape, "Test");
        assertEquals("dimension of \"Test\"", new Dimension(36, 13), dim);

        // set fontMetrics of provider to null and test again
        Field renderEnvironment = DISPLAY.getClass().getDeclaredField("renderEnvironment");
        renderEnvironment.setAccessible(true);
        Field ownFont = TextRenderShape.class.getDeclaredField("font");
        ownFont.setAccessible(true);
        Field ownMetrics = TextRenderShape.class.getDeclaredField("fontMetrics");
        ownMetrics.setAccessible(true);

        RenderEnvironment noMetrics = new DummyRenderEnvironment();
        Field metrics = noMetrics.getClass().getDeclaredField("fontMetrics");
        metrics.setAccessible(true);
        metrics.set(noMetrics, null);
        renderEnvironment.set(DISPLAY, noMetrics);
        ownFont.set(shape, noMetrics.getFont());
        try {
            getStringDimension.invoke(shape, "Test");
        } catch (Exception e) {
            assertEquals("no font metrics exception", "FontMetrics is null in layout of " + LabelShape.class.getSimpleName(), e.getCause().getMessage());
        }

        // set font of provider to null and test again
        RenderEnvironment noFont = new DummyRenderEnvironment();
        Field font = noFont.getClass().getDeclaredField("font");
        font.setAccessible(true);
        font.set(noFont, null);
        renderEnvironment.set(DISPLAY, noFont);
        ownMetrics.set(shape, noFont.getFontMetrics());
        try {
            getStringDimension.invoke(shape, "Test");
        } catch (Exception e) {
            assertEquals("no font exception", "Font is null in layout of " + LabelShape.class.getSimpleName(), e.getCause().getMessage());
        }

        // reset the font provider
        renderEnvironment.set(DISPLAY, new DummyRenderEnvironment());

        // set font and metrics to null and test again
        ownFont.set(shape, null);
        ownMetrics.set(shape, null);
        dim = (Dimension) getStringDimension.invoke(shape, "Test");
        assertEquals("dimension of \"Test\"", new Dimension(36, 13), dim);
    }
}
