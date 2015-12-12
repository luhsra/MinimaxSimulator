package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals("equals: self comparison", true, j5.equals(j5));
    }

    /**
     * Tests the comparison with another object with the same values.
     */
    @Test
    public void testEqualsSame() {
        Jump jmp = new UnconditionalJump(5);
        assertEquals("equals: same values", true, j5.equals(jmp));
    }

    /**
     * Tests the comparison with different targets.
     */
    @Test
    public void testEqualsDiff() {
        assertEquals("equals: different targets", false, j5.equals(j9));
    }

    /**
     * Tests the {@code null} comparison.
     */
    @Test
    public void testEqualsNull() {
        assertEquals("equals: null comparison", false, j5.equals(null));
    }

    /**
     * Tests the comparison with objects of other classes.
     */
    @Test
    public void testEqualsDiffClass() {
        Jump cj = new ConditionalJump(5, 6);
        Jump dj = DefaultJump.INSTANCE;
        assertEquals("equals: comparison with ConditionalJump", false, j5.equals(cj));
        assertEquals("equals: comparison with DefaultJump", false, j5.equals(dj));
    }
}
