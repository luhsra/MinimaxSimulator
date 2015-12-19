package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of {@link DefaultJump}.
 *
 * @author Philipp Rohde
 */
public class DefaultJumpTest {

    private static final Jump jmp = DefaultJump.INSTANCE;

    /**
     * Tests the self comparison.
     */
    @Test
    public void testEqualsSelf() {
        assertEquals("equals: self comparison", true, jmp.equals(jmp));
    }

    /**
     * Tests the comparison with another object with the same values.
     */
    @Test
    public void testEqualsSame() {
        Jump jmp2 = DefaultJump.INSTANCE;
        assertEquals("equals: same values", true, jmp.equals(jmp2));
    }

    /**
     * Tests the {@code null} comparison.
     */
    @Test
    public void testEqualsNull() {
        assertEquals("equals: null comparison", false, jmp.equals(null));
    }

    /**
     * Tests the comparison with objects of other classes.
     */
    @Test
    public void testEqualsDiffClass() {
        Jump uj = new UnconditionalJump(42);
        Jump cj = new ConditionalJump(21, 42);
        assertEquals("equals: comparison with UnconditionalJump", false, jmp.equals(uj));
        assertEquals("equals: comparison with ConditionalJump", false, jmp.equals(cj));
    }
}
