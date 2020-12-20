package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of a register multiplexer input.
 *
 * @see RegisterMuxInput
 *
 * @author Philipp Rohde
 */
public class RegisterMuxInputTest {

    private static final String PC = "PC";
    private static final String IR = "IR";
    private static final String AT = "AT";

    /**
     * Tests the register multiplexer input.
     */
    @Test
    public void testRegisterMuxInput() {
        RegisterMuxInput rmi = new RegisterMuxInput(PC);
        assertEquals("register name", PC, rmi.getRegisterName());
        assertEquals("display name", PC, rmi.getName());

        RegisterMuxInput ir = new RegisterMuxInput(IR, AT);
        assertEquals("register name", IR, ir.getRegisterName());
        assertEquals("display name", AT, ir.getName());

        // test equals
        assertTrue("equals: self comparison", rmi.equals(rmi));
        RegisterMuxInput equal = new RegisterMuxInput(PC);
        assertTrue("equals: same register", rmi.equals(equal));
        assertFalse("equals: different registers", rmi.equals(ir));
        assertFalse("equals: null comparison", rmi.equals(null));
        assertFalse("equals: different classes", rmi.equals(NullMuxInput.INSTANCE));
    }
}
