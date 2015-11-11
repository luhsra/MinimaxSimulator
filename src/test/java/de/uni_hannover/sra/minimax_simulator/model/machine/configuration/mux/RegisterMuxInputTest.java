package de.uni_hannover.sra.minimax_simulator.model.machine.configuration.mux;

import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.NullMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegisterMuxInputTest {

    @Test
    public void testRegisterMuxInput() {
        String register = "PC";

        RegisterMuxInput rmi = new RegisterMuxInput(register);
        assertEquals("register name", register, rmi.getRegisterName());
        assertEquals("display name", register, rmi.getName());

        RegisterMuxInput ir = new RegisterMuxInput("IR", "AT");
        assertEquals("register name", "IR", ir.getRegisterName());
        assertEquals("display name", "AT", ir.getName());

        // test equals
        assertEquals("equals: self comparison", true, rmi.equals(rmi));
        RegisterMuxInput equal = new RegisterMuxInput(register);
        assertEquals("equals: same register", true, rmi.equals(equal));
        assertEquals("equals: different registers", false, rmi.equals(ir));
        assertEquals("equals: null comparison", false, rmi.equals(null));
        assertEquals("equals: different classes", false, rmi.equals(NullMuxInput.INSTANCE));
    }
}
