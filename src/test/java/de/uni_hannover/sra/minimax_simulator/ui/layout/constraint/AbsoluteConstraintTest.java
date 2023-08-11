package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link AbsoluteConstraint}.
 *
 * @author Philipp Rohde
 */
public class AbsoluteConstraintTest {

    private static final AbsoluteConstraint AC1 = new AbsoluteConstraint(-5);
    private static final AbsoluteConstraint AC2 = new AbsoluteConstraint(0);
    private static final AbsoluteConstraint AC3 = new AbsoluteConstraint(10);

    /**
     * Tests the implementation of {@link AbsoluteConstraint#equals(Object)}.
     */
    @Test
    public void testEquals() {
        assertTrue("self comparison", AC1.equals(AC1));
        assertFalse("null comparison", AC1.equals(null));
        assertFalse("other class comparison", AC1.equals(new ConstrainedArea("AC1")));
        assertFalse("different value", AC1.equals(AC2));
        assertTrue("same vallue", AC1.equals(new AbsoluteConstraint(-5)));
    }

    /**
     * Tests the implementation of {@link AbsoluteConstraint#hashCode()}.
     */
    @Test
    public void testHash() {
        assertEquals("hash of AC1", 26, AC1.hashCode());
        assertEquals("hash of AC2", 31, AC2.hashCode());
        assertEquals("hash of AC3", 41, AC3.hashCode());
    }

    /**
     * Tests the implementation of {@link AbsoluteConstraint#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("string of AC1", "abs(-5)", AC1.toString());
        assertEquals("string of AC2", "abs(0)", AC2.toString());
        assertEquals("string of AC3", "abs(10)", AC3.toString());
    }
}
