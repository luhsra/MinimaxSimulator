package de.uni_hannover.sra.minimax_simulator.model.configuration.register;

import org.junit.Test;

import static org.junit.Assert.*;

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
        assertTrue("equals: self comparison", accu.equals(accu));
        RegisterExtension equal = new RegisterExtension(ACCU, SIZE32, ACCU_DESC, false);
        assertTrue("equals: same register", accu.equals(equal));

        assertFalse("equals: different registers", accu.equals(mdr));

        assertFalse("equals: null comparison", accu.equals(null));

        RegisterExtension diffName = new RegisterExtension(MDR, SIZE32, ACCU_DESC, false);
        assertFalse("equals: different names", accu.equals(diffName));

        RegisterExtension diffSize = new RegisterExtension(ACCU, SIZE24, ACCU_DESC, false);
        assertFalse("equals: different sizes", accu.equals(diffSize));

        RegisterExtension diffDesc = new RegisterExtension(ACCU, SIZE32, MDR_DESC, false);
        assertFalse("equals: different descriptions", accu.equals(diffDesc));

        RegisterExtension diffExtended = new RegisterExtension(ACCU, SIZE32, ACCU_DESC, true);
        assertFalse("equals: different extended value", accu.equals(diffExtended));
    }
}
