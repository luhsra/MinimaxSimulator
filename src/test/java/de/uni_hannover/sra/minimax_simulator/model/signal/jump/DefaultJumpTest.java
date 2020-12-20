package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

import org.junit.Test;

import static org.junit.Assert.*;

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
        assertTrue("equals: self comparison", jmp.equals(jmp));
    }

    /**
     * Tests the comparison with another object with the same values.
     */
    @Test
    public void testEqualsSame() {
        Jump jmp2 = DefaultJump.INSTANCE;
        assertTrue("equals: same values", jmp.equals(jmp2));
    }

    /**
     * Tests the {@code null} comparison.
     */
    @Test
    public void testEqualsNull() {
        assertFalse("equals: null comparison", jmp.equals(null));
    }

    /**
     * Tests the comparison with objects of other classes.
     */
    @Test
    public void testEqualsDiffClass() {
        Jump uj = new UnconditionalJump(42);
        Jump cj = new ConditionalJump(21, 42);
        assertFalse("equals: comparison with UnconditionalJump", jmp.equals(uj));
        assertFalse("equals: comparison with ConditionalJump", jmp.equals(cj));
    }
}
