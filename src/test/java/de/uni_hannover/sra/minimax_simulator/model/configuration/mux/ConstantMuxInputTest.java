package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of a constant multiplexer input.
 *
 * @see ConstantMuxInput
 *
 * @author Philipp Rohde
 */
public class ConstantMuxInputTest {

    private static final int TWELVE = 12;
    private static final int THIRTEEN = 13;

    /**
     * Tests the constant multiplexer input.
     */
    @Test
    public void testConstantMuxInput() {
        ConstantMuxInput cmi = new ConstantMuxInput(TWELVE);

        assertEquals("Constant Mux Input Value", 12, cmi.getConstant());
        assertEquals("Constant Mux Input Name", "12", cmi.getName());

        // test equals
        assertTrue("equals: self comparison", cmi.equals(cmi));
        ConstantMuxInput equal = new ConstantMuxInput(TWELVE);
        assertTrue("equals: same constant value", cmi.equals(equal));
        ConstantMuxInput notEqual = new ConstantMuxInput(THIRTEEN);
        assertFalse("equals: different constant values", cmi.equals(notEqual));
        assertFalse("equals: null comparison", cmi.equals(null));
        assertFalse("equals: different classes", cmi.equals(NullMuxInput.INSTANCE));
    }
}
