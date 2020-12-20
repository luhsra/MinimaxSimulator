package de.uni_hannover.sra.minimax_simulator.ui.layout;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link Dimension}.
 *
 * @author Philipp Rohde
 */
public class DimensionTest {

    private static final String EXPECTED_EXCEPTION = "expected to throw exception";

    /**
     * Tests the error handling of the constructor.
     */
    @Test
    public void testConstructor() {
        // negative width
        int width = -1;
        try {
            new Dimension(width, 10);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("negative width", "width < 0: " + width, e.getMessage());
        }

        // negative height
        int height = -3;
        try {
            new Dimension(20, height);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("negative height", "height < 0: " + height, e.getMessage());
        }

        // get width and height of ZERO instance
        assertEquals("width", 0, Dimension.ZERO.getWidth());
        assertEquals("height", 0, Dimension.ZERO.getHeight());
    }

    /**
     * Tests the implementation of {@link Dimension#hashCode()}.
     */
    @Test
    public void testHash() {
        assertEquals(calculateHash(0, 0), new Dimension(0, 0).hashCode());
        assertEquals(calculateHash(0, 1), new Dimension(0, 1).hashCode());
        assertEquals(calculateHash(3, 0), new Dimension(3, 0).hashCode());
        assertEquals(calculateHash(12, 9), new Dimension(12, 9).hashCode());
    }

    /**
     * Calculates the expected hash with the specified width and height.
     *
     * @param w
     *         the width of the object to calculate the hash for
     * @param h
     *         the height of the object to calculate the hash for
     * @return
     *         the hash of the object
     */
    private int calculateHash(int w, int h) {
        return 31 * (31 + h) + w;
    }

    /**
     * Tests the implementation of {@link Dimension#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("[0,0]", Dimension.ZERO.toString());
        assertEquals("[100,200]", new Dimension(100, 200).toString());
    }

    /**
     * Tests the implementation of {@link Dimension#equals(Object)}.
     */
    @Test
    public void testEquals() {
        Dimension d1 = new Dimension(10, 10);
        assertTrue("self comparison", d1.equals(d1));
        assertFalse("null comparison", d1.equals(null));
        assertFalse("other class comparison", d1.equals(new Bounds(10, 10, 10, 10)));
        assertFalse("different height", d1.equals(new Dimension(10, 11)));
        assertFalse("different width", d1.equals(new Dimension(11, 10)));
        assertTrue("same values", d1.equals(new Dimension(10, 10)));
    }
}
