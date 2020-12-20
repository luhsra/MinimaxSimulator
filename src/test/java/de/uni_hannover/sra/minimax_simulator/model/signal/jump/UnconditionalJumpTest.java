package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link UnconditionalJump}.
 *
 * @author Philipp Rohde
 */
public class UnconditionalJumpTest {

    private static final Jump j5 = new UnconditionalJump(5);
    private static final Jump j9 = new UnconditionalJump(9);

    /**
     * Tests the self comparison.
     */
    @Test
    public void testEqualsSelf() {
        assertTrue("equals: self comparison", j5.equals(j5));
    }

    /**
     * Tests the comparison with another object with the same values.
     */
    @Test
    public void testEqualsSame() {
        Jump jmp = new UnconditionalJump(5);
        assertTrue("equals: same values", j5.equals(jmp));
    }

    /**
     * Tests the comparison with different targets.
     */
    @Test
    public void testEqualsDiff() {
        assertFalse("equals: different targets", j5.equals(j9));
    }

    /**
     * Tests the {@code null} comparison.
     */
    @Test
    public void testEqualsNull() {
        assertFalse("equals: null comparison", j5.equals(null));
    }

    /**
     * Tests the comparison with objects of other classes.
     */
    @Test
    public void testEqualsDiffClass() {
        Jump cj = new ConditionalJump(5, 6);
        Jump dj = DefaultJump.INSTANCE;
        assertFalse("equals: comparison with ConditionalJump", j5.equals(cj));
        assertFalse("equals: comparison with DefaultJump", j5.equals(dj));
    }
}
