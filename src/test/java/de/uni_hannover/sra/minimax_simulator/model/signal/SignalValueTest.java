package de.uni_hannover.sra.minimax_simulator.model.signal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals("equals: self comparison", true, v4.equals(v4));
        assertEquals("equals: self comparison", true, dont.equals(dont));
    }

    /**
     * Tests the comparison with another object with the same values.
     */
    @Test
    public void testEqualsSame() {
        SignalValue sv = SignalValue.valueOf(4);
        assertEquals("equals: same value", true, v4.equals(sv));

        SignalValue dc = SignalValue.DONT_CARE;
        assertEquals("equals: same don't care", true, dont.equals(dc));
    }

    /**
     * Tests the comparison with different don't care.
     */
    @Test
    public void testEqualsDiffDontCare() {
        assertEquals("equals: different don't care", false, v0.equals(dont));
    }

    /**
     * Tests the comparison with different values.
     */
    @Test
    public void testEqualsDiffValue() {
        assertEquals("equals: different value", false, v4.equals(v0));
    }

    /**
     * Tests the comparison with different don't care and values.
     */
    @Test
    public void testEqualsDiff() {
        assertEquals("equals: completely different", false, v4.equals(dont));
    }

    /**
     * Tests the {@code null} comparison.
     */
    @Test
    public void testEqualsNull() {
        assertEquals("equals: null comparison", false, v4.equals(null));
    }

}
