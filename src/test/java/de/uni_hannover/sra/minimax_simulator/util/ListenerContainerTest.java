package de.uni_hannover.sra.minimax_simulator.util;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfigListener;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of {@link ListenerContainer}.
 *
 * @author Philipp Rohde
 */
public class ListenerContainerTest {

    /**
     * Actually runs the test.
     */
    @Test
    public void test() {
        ListenerContainer<SignalConfigListener> container = new ListenerContainer<>();
        Listener listenerOne = new Listener();
        Listener listenerTwo = new Listener();

        container.addListener(listenerOne);
        assertEquals("contains listener one", true, container.getListeners().contains(listenerOne));

        // re-add
        container.addListener(listenerOne);
        assertEquals("contains listener one", true, container.getListeners().contains(listenerOne));

        container.addListener(listenerTwo);
        assertEquals("contains listener two", true, container.getListeners().contains(listenerTwo));

        container.removeListener(listenerOne);
        assertEquals("does not longer contain listener one", false, container.getListeners().contains(listenerOne));
        assertEquals("still contains listener two", true, container.getListeners().contains(listenerTwo));

        container.removeListener(listenerTwo);
        assertEquals("empty container", true, container.getListeners().isEmpty());
    }

    /**
     * Lightweight {@code SignalConfigListener} for testing purposes.
     */
    private class Listener implements SignalConfigListener {

        @Override
        public void signalStructureChanged() {
            // do nothing
        }
    }
}
