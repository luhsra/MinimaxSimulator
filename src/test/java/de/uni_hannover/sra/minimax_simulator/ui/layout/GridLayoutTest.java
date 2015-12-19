package de.uni_hannover.sra.minimax_simulator.ui.layout;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the layout created by {@link GridContainer}.
 *
 * @author Martin L&uuml;ck
 */
public class GridLayoutTest {

    /**
     * Tests the {@code GridContainer} with two rows with a different amount of cells.
     */
    @Test
    public void test() {
        GridContainer c = new GridContainer(3, 2);

        // first row
        AbstractComponent c11 = new Label("11");
        c11.setDimension(new Dimension(100, 10));
        AbstractComponent c21 = new Label("21");
        c21.setDimension(new Dimension(50, 10));
        AbstractComponent c31 = new Label("31");
        c31.setDimension(new Dimension(10, 120));

        // second row
        AbstractComponent c12 = new Label("12");
        c12.setDimension(new Dimension(50, 20));
        AbstractComponent c22 = new Label("22");
        c22.setDimension(new Dimension(60, 10));
        AbstractComponent c32 = new Label("32");
        c32.setDimension(new Dimension(20, 50));

        c.addComponent(c11, null);
        c.addComponent(c21, null);
        c.addComponent(c31, null);
        c.addComponent(c12, null);
        c.addComponent(c22, null);
        c.addComponent(c32, null);

        c.updateSize();

        Dimension dim = c.getDimension();

        assertEquals(180, dim.w);
        assertEquals(170, dim.h);
    }
}
