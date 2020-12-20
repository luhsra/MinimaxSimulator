package de.uni_hannover.sra.minimax_simulator.model.signal;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link SignalValue}.
 *
 * @author Philipp Rohde
 */
public class SignalValueTest {

    private static final SignalValue v0 = SignalValue.valueOf(0);
    private static final SignalValue v4 = SignalValue.valueOf(4);
    private static final SignalValue dont = SignalValue.DONT_CARE;

    /**
     * Tests the self comparison.
     */
    @Test
    public void testEqualsSelf() {
        assertTrue("equals: self comparison", v4.equals(v4));
        assertTrue("equals: self comparison", dont.equals(dont));
    }

    /**
     * Tests the comparison with another object with the same values.
     */
    @Test
    public void testEqualsSame() {
        SignalValue sv = SignalValue.valueOf(4);
        assertTrue("equals: same value", v4.equals(sv));

        SignalValue dc = SignalValue.DONT_CARE;
        assertTrue("equals: same don't care", dont.equals(dc));
    }

    /**
     * Tests the comparison with different don't care.
     */
    @Test
    public void testEqualsDiffDontCare() {
        assertFalse("equals: different don't care", v0.equals(dont));
    }

    /**
     * Tests the comparison with different values.
     */
    @Test
    public void testEqualsDiffValue() {
        assertFalse("equals: different value", v4.equals(v0));
    }

    /**
     * Tests the comparison with different don't care and values.
     */
    @Test
    public void testEqualsDiff() {
        assertFalse("equals: completely different", v4.equals(dont));
    }

    /**
     * Tests the {@code null} comparison.
     */
    @Test
    public void testEqualsNull() {
        assertFalse("equals: null comparison", v4.equals(null));
    }

}
