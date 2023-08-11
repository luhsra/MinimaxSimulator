package de.uni_hannover.sra.minimax_simulator.model.signal;

import de.uni_hannover.sra.minimax_simulator.model.user.NewProjectBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link SignalRow}.
 *
 * @author Philipp Rohde
 */
public class SignalRowTest {

    private static SignalRow row;

    /**
     * Initializes the test instance with the default microprogram.
     */
    @BeforeClass
    public static void initialize() {
        row = new NewProjectBuilder().buildProject().getSignalTable().getRow(1);
    }

    /**
     * Tests {@link SignalRow#getSignalValue(String)}.
     */
    @Test
    public void testValue() {
        // value of not existing signal
        assertFalse("MDR.Sel", row.getSignalValues().containsKey("MDR_SEL"));
        assertEquals("MDR.Sel value", 0, row.getSignalValue("MDR_SEL"));

        // value of existing signal
        assertTrue("ALU_SELECT_B", row.getSignalValues().containsKey("ALU_SELECT_B"));
        assertEquals("ALU_SELECT_B value", 3, row.getSignalValue("ALU_SELECT_B"));
    }

    /**
     * Tests {@link SignalRow#getSignal(String, SignalValue)}.
     */
    @Test
    public void testSignal() {
        // value of not existing signal
        assertFalse("MDR.Sel", row.getSignalValues().containsKey("MDR_SEL"));
        assertEquals("MDR.Sel value", SignalValue.valueOf(5), row.getSignal("MDR_SEL", SignalValue.valueOf(5)));

        // value of existing signal
        assertTrue("ALU_SELECT_B", row.getSignalValues().containsKey("ALU_SELECT_B"));
        assertEquals("ALU_SELECT_B value", SignalValue.valueOf(3), row.getSignal("ALU_SELECT_B", SignalValue.valueOf(5)));
    }

}
