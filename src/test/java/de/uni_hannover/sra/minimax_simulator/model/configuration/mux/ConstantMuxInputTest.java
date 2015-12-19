package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals("equals: self comparison", true, cmi.equals(cmi));
        ConstantMuxInput equal = new ConstantMuxInput(TWELVE);
        assertEquals("equals: same constant value", true, cmi.equals(equal));
        ConstantMuxInput notEqual = new ConstantMuxInput(THIRTEEN);
        assertEquals("equals: different constant values", false, cmi.equals(notEqual));
        assertEquals("equals: null comparison", false, cmi.equals(null));
        assertEquals("equals: different classes", false, cmi.equals(NullMuxInput.INSTANCE));
    }
}
