package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link ConditionalJump}.
 *
 * @author Philipp Rohde
 */
public class ConditionalJumpTest {

    private static final Jump zero5one6 = new ConditionalJump(5, 6);
    private static final Jump zero5one8 = new ConditionalJump(5, 8);
    private static final Jump zero9one6 = new ConditionalJump(9, 6);
    private static final Jump zero3one4 = new ConditionalJump(3, 4);

    /**
     * Tests the self comparison.
     */
    @Test
    public void testEqualsSelf() {
        assertTrue("equals: self comparison", zero5one6.equals(zero5one6));
    }

    /**
     * Tests the comparison with another object with the same values.
     */
    @Test
    public void testEqualsSame() {
        Jump jmp = new ConditionalJump(5, 6);
        assertTrue("equals: same values", zero5one6.equals(jmp));
    }

    /**
     * Tests the comparison with different condition zero targets.
     */
    @Test
    public void testEqualsDiffZero() {
        assertFalse("equals: different condition zero target", zero5one6.equals(zero9one6));
    }

    /**
     * Tests the comparison with different condition one targets.
     */
    @Test
    public void testEqualsDiffOne()  {
        assertFalse("equals: different condition one target", zero5one6.equals(zero5one8));
    }

    /**
     * Tests the comparison with different condition zero and one targets.
     */
    @Test
    public void testEqualsDiffBoth() {
        assertFalse("equals: completely different", zero5one6.equals(zero3one4));
    }

    /**
     * Tests the {@code null} comparison.
     */
    @Test
    public void testEqualsNull() {
        assertFalse("equals: null comparison", zero5one6.equals(null));
    }

    /**
     * Tests the comparison with objects of other classes.
     */
    @Test
    public void testEqualsDiffClass() {
        Jump uj = new UnconditionalJump(5);
        Jump dj = DefaultJump.INSTANCE;
        assertFalse("equals: comparison with UnconditionalJump", zero5one6.equals(uj));
        assertFalse("equals: comparison with DefaultJump", zero5one6.equals(dj));
    }
}
