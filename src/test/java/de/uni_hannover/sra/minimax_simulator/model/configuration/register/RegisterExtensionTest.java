package de.uni_hannover.sra.minimax_simulator.model.configuration.register;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of a register.
 *
 * @see RegisterExtension
 *
 * @author Philipp Rohde
 */
public class RegisterExtensionTest {

    private static final RegisterSize SIZE24 = RegisterSize.BITS_24;
    private static final RegisterSize SIZE32 = RegisterSize.BITS_32;
    private static final String ACCU = "ACCU";
    private static final String ACCU_DESC = "The ACCU...";
    private static final String MDR = "MDR";
    private static final String MDR_DESC = "The MDR...";

    /**
     * Tests the register.
     */
    @Test
    public void testRegisterExtension() {
        RegisterExtension accu = new RegisterExtension(ACCU, SIZE32, ACCU_DESC, false);
        RegisterExtension mdr = new RegisterExtension(MDR, SIZE24, MDR_DESC, false);

        // test equals
        assertEquals("equals: self comparison", true, accu.equals(accu));
        RegisterExtension equal = new RegisterExtension(ACCU, SIZE32, ACCU_DESC, false);
        assertEquals("equals: same register", true, accu.equals(equal));

        assertEquals("equals: different registers", false, accu.equals(mdr));

        assertEquals("equals: null comparison", false, accu.equals(null));

        RegisterExtension diffName = new RegisterExtension(MDR, SIZE32, ACCU_DESC, false);
        assertEquals("equals: different names", false, accu.equals(diffName));

        RegisterExtension diffSize = new RegisterExtension(ACCU, SIZE24, ACCU_DESC, false);
        assertEquals("equals: different sizes", false, accu.equals(diffSize));

        RegisterExtension diffDesc = new RegisterExtension(ACCU, SIZE32, MDR_DESC, false);
        assertEquals("equals: different descriptions", false, accu.equals(diffDesc));

        RegisterExtension diffExtended = new RegisterExtension(ACCU, SIZE32, ACCU_DESC, true);
        assertEquals("equals: different extended value", false, accu.equals(diffExtended));
    }
}
