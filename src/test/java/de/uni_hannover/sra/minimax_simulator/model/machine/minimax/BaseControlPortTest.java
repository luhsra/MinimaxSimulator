package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link BaseControlPort}.
 *
 * @author Philipp Rohde
 */
public class BaseControlPortTest {

    /**
     * Tests all values of {@code BaseControlPort}.
     */
    @Test
    public void test() {
        for (BaseControlPort bcp : BaseControlPort.values()) {
            assertEquals("Port of " + bcp.name(), bcp.name(), bcp.port().getName());
        }
    }
}
