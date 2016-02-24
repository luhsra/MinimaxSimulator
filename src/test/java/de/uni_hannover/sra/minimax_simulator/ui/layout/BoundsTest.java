package de.uni_hannover.sra.minimax_simulator.ui.layout;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests the implementation of {@link Bounds}.
 *
 * @author Philipp Rohde
 */
public class BoundsTest {

    private static final String EXPECTED_EXCEPTION = "expected to throw exception";

    /**
     * Tests the implementation of {@link Bounds#Bounds(int, int, int, int)}.
     */
    @Test
    public void testMainConstructor() {
        Bounds b1 = new Bounds(-1, 1, 0, 3);
        assertEquals("b1.x", -1, b1.x);
        assertEquals("b1.y", 1, b1.y);
        assertEquals("b1.w", 0, b1.w);
        assertEquals("b1.h", 3, b1.h);

        // negative width
        int width = -3;
        try {
            new Bounds(0, 1, width, 0);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("negative width", "width < 0: " + width, e.getMessage());
        }

        // negative height
        int height = -3;
        try {
            new Bounds(0, 1, 2, height);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("negative height", "height < 0: " + height, e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link Bounds#Bounds(Bounds)}.
     *
     * @throws IllegalAccessException
     *          thrown if the field for width or height could not be accessed
     * @throws NoSuchFieldException
     *          thrown if the field for width or height could not be found
     */
    @Test
    public void testCopyConstructor() throws IllegalAccessException, NoSuchFieldException {
        Bounds b1 = new Bounds(-1, 1, 0, 3);
        Bounds copy = new Bounds(b1);
        assertEquals("copy.x", -1, copy.x);
        assertEquals("copy.y", 1, copy.y);
        assertEquals("copy.w", 0, copy.w);
        assertEquals("copy.h", 3, copy.h);

        // negative width
        Bounds b2 = new Bounds();
        Field bw = Bounds.class.getDeclaredField("w");
        bw.setAccessible(true);
        int width = -1;
        bw.set(b2, width);
        try {
            new Bounds(b2);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("negative width", "width < 0: " + width, e.getMessage());
        }

        // negative height
        Bounds b3 = new Bounds();
        Field bh = Bounds.class.getDeclaredField("h");
        bh.setAccessible(true);
        int height = -2;
        bh.set(b3, height);
        try {
            new Bounds(b3);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("negative height", "height < 0: " + height, e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link Bounds#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("[0,0 / 0,0]", new Bounds().toString());
        assertEquals("[-2,3 / 5,3]", new Bounds(-2, 3, 5, 3).toString());
    }
}
