package de.uni_hannover.sra.minimax_simulator.model.signal;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link DefaultSignalType}.
 *
 * @see SignalType
 *
 * @author Philipp Rohde
 */
public class DefaultSignalTypeTest {

    /**
     * Tests {@code DefaultSignalType} without {@code don't care}.
     */
    @Test
    public void testCare() {
        SignalType signalType = new DefaultSignalType("test", "Test", 10, false);
        assertEquals("bit width", 4, signalType.getBitWidth());
        for (int i = 0; i < signalType.getValues().size(); i++) {
            assertEquals("signal value", i, signalType.getValues().get(i).intValue());
            assertFalse("is don't care", signalType.getValues().get(i).isDontCare());
        }
    }

    /**
     * Tests {@code DefaultSignalType} with {@code don't care}.
     */
    @Test
    public void testDontCare() {
        SignalType signalType = new DefaultSignalType("dont", "don't care", 100, true);
        assertEquals("bit width", 7, signalType.getBitWidth());
        assertEquals("signal value", 0, signalType.getValues().get(0).intValue());
        assertTrue("is don't care", signalType.getValues().get(0).isDontCare());
        for (int i = 1; i < signalType.getValues().size(); i++) {
            assertEquals("signal value", i-1, signalType.getValues().get(i).intValue());
            assertFalse("is don't care", signalType.getValues().get(i).isDontCare());
        }
    }
}
