package de.uni_hannover.sra.minimax_simulator.util;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfigListener;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertTrue("contains listener one", container.getListeners().contains(listenerOne));

        // re-add
        container.addListener(listenerOne);
        assertTrue("contains listener one", container.getListeners().contains(listenerOne));

        container.addListener(listenerTwo);
        assertTrue("contains listener two", container.getListeners().contains(listenerTwo));

        container.removeListener(listenerOne);
        assertFalse("does not longer contain listener one", container.getListeners().contains(listenerOne));
        assertTrue("still contains listener two", container.getListeners().contains(listenerTwo));

        container.removeListener(listenerTwo);
        assertTrue("empty container", container.getListeners().isEmpty());
    }

    /**
     * Lightweight {@code SignalConfigListener} for testing purposes.
     */
    private static class Listener implements SignalConfigListener {

        @Override
        public void signalStructureChanged() {
            // do nothing
        }
    }
}
