package de.uni_hannover.sra.minimax_simulator.ui.layout;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link Insets}.
 *
 * @author Philipp Rohde
 */
public class InsetsTest {

    private static final Insets IN_1 = new Insets(10, 10, 10, 10);
    private static final Insets IN_2 = new Insets(0, 5, 12, 12);
    private static final Insets IN_3 = new Insets(2, 0, 5, 10);
    private static final Insets IN_4 = new Insets(4, 8, 0, 16);
    private static final Insets IN_5 = new Insets(3, 9, 27, 0);
    private static final Insets IN_6 = new Insets(0, 0, 0, 0);

    /**
     * Tests the implementation of {@link Insets#equals(Object)}.
     */
    @Test
    public void testEquals() {
        assertTrue("self comparison", IN_1.equals(IN_1));
        assertFalse("null comparison", IN_1.equals(null));
        assertFalse("other class comparison", IN_1.equals(new Bounds(10, 10, 10, 10)));

        assertFalse("different top", IN_1.equals(new Insets(0, 10, 10, 10)));
        assertFalse("different bottom", IN_2.equals(new Insets(0, 30, 12, 12)));
        assertFalse("different left", IN_3.equals(new Insets(2, 0, 10, 10)));
        assertFalse("different right", IN_4.equals(new Insets(4, 8, 0, 32)));
        assertTrue("same values", IN_5.equals(new Insets(3, 9, 27, 0)));
    }

    /**
     * Tests the implementation of {@link Insets#hashCode()}.
     */
    @Test
    public void testHash() {
        assertEquals("hash of IN_1", calculateHash(10, 10, 10, 10), IN_1.hashCode());
        assertEquals("hash of IN_2", calculateHash(0, 5, 12, 12), IN_2.hashCode());
        assertEquals("hash of IN_3", calculateHash(2, 0, 5, 10), IN_3.hashCode());
        assertEquals("hash of IN_4", calculateHash(4, 8, 0, 16), IN_4.hashCode());
        assertEquals("hash of IN_5", calculateHash(3, 9, 27, 0), IN_5. hashCode());
        assertEquals("hash of IN_6", 31*31*31*31, IN_6.hashCode());
    }

    /**
     * Calculates the expected hash for the specified values.
     *
     * @param b
     *         the bottom inset of the object to calculate the hash for
     * @param l
     *         the left inset of the object to calculate the hash for
     * @param r
     *         the right inset of the object to calculate the hash for
     * @param t
     *         the top inset of the object to calculate the hash for
     * @return
     *         the expected hash
     */
    private int calculateHash(int t, int b, int l, int r) {
        return 31 * (31 * (31 * (31 + b) + l) + r) + t;
    }

    /**
     * Tests the implementation of {@link Insets#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("string of IN_1", "[10,10,10,10]", IN_1.toString());
        assertEquals("string of IN_2", "[0,5,12,12]", IN_2.toString());
        assertEquals("string of IN_3", "[2,0,5,10]", IN_3.toString());
        assertEquals("string of IN_4", "[4,8,0,16]", IN_4.toString());
        assertEquals("string of IN_5", "[3,9,27,0]", IN_5.toString());
        assertEquals("string of IN_6", "[0,0,0,0]", IN_6.toString());
    }
}
