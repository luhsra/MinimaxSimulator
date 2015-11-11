package de.uni_hannover.sra.minimax_simulator.model.machine.configuration.mux;

import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.NullMuxInput;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstantMuxInputTest {

    private static final int CONSTANT = 12;

    @Test
    public void testConstantMuxInput() {
        ConstantMuxInput cmi = new ConstantMuxInput(CONSTANT);

        assertEquals("Constant Mux Input Value", 12, cmi.getConstant());
        assertEquals("Constant Mux Input Name", "12", cmi.getName());

        // test equals
        assertEquals("equals: self comparison", true, cmi.equals(cmi));
        ConstantMuxInput equal = new ConstantMuxInput(CONSTANT);
        assertEquals("equals: same constant value", true, cmi.equals(equal));
        ConstantMuxInput notequal = new ConstantMuxInput(13);
        assertEquals("equals: different constant values", false, cmi.equals(notequal));
        assertEquals("equals: null comparison", false, cmi.equals(null));
        assertEquals("equals: different classes", false, cmi.equals(NullMuxInput.INSTANCE));
    }
}
