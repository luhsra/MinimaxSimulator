package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link TraceableInteger}.
 *
 * @author Philipp Rohde
 */
public class TraceableIntegerTest {

    /**
     * Actually runs the test.
     */
    @Test
    public void test() {
        Traceable<Integer> tInt = new TraceableInteger();
        Listener listenerOne = new Listener();
        Listener listenerTwo = new Listener();

        tInt.addChangeListener(listenerOne);
        tInt.set(10);
        assertTrue("listener one should have been notified", listenerOne.fired);
        assertEquals("own value should be 10", 10, (int) tInt.get());
        assertEquals("listener one's value should be 10", 10, (int) listenerOne.value);
        assertFalse("listener two should not have been notified", listenerTwo.fired);

        tInt.addChangeListener(listenerTwo);
        tInt.removeChangeListener(listenerOne);
        tInt.set(42);
        assertTrue("listener two should have been notified", listenerTwo.fired);
        assertEquals("own value should be 42", 42, (int) tInt.get());
        assertEquals("listener one's value should still be 10", 10, (int) listenerOne.value);
        assertEquals("listener two's value should be 42", 42, (int) listenerTwo.value);

        tInt.addChangeListener(listenerOne);
        tInt.addChangeListener(listenerOne);        // try to add same listener twice
        tInt.clearListeners();
        tInt.set(30);
        assertEquals("own value should be 30", 30, (int) tInt.get());
        assertEquals("listener one's value should still be 10", 10, (int) listenerOne.value);
        assertEquals("listener two's value should still be 42", 42, (int) listenerTwo.value);

    }

    /**
     * Simple {@link TraceableChangeListener} for testing purposes.
     */
    private static class Listener implements TraceableChangeListener<Integer> {

        public boolean fired = false;
        public Integer value;

        @Override
        public void onValueChanged(Integer value) {
            this.fired = true;
            this.value = value;
        }
    }
}
