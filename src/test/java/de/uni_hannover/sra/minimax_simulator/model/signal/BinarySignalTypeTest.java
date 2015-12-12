package de.uni_hannover.sra.minimax_simulator.model.signal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of {@link BinarySignalType}.
 *
 * @see SignalType
 *
 * @author Philipp Rohde
 */
public class BinarySignalTypeTest {

    /**
     * Tests {@code BinarySignalType} without {@code don't care}.
     */
    @Test
    public void testCare() {
        SignalType signalType = new BinarySignalType("test", "Test", false);
        assertEquals("bit width", 1, signalType.getBitWidth());
        for (int i = 0; i < signalType.getValues().size(); i++) {
            assertEquals("signal value", i, signalType.getValues().get(i).intValue());
            assertEquals("is don't care", false, signalType.getValues().get(i).isDontCare());
        }
    }

    /**
     * Tests {@code BinarySignalType} with {@code don't care}.
     */
    @Test
    public void testDontCare() {
        SignalType signalType = new BinarySignalType("dont", "don't care", true);
        assertEquals("bit width", 1, signalType.getBitWidth());
        assertEquals("signal value", 0, signalType.getValues().get(0).intValue());
        assertEquals("is don't care", true, signalType.getValues().get(0).isDontCare());
        for (int i = 1; i < signalType.getValues().size(); i++) {
            assertEquals("signal value", i-1, signalType.getValues().get(i).intValue());
            assertEquals("is don't care", false, signalType.getValues().get(i).isDontCare());
        }
    }
}
