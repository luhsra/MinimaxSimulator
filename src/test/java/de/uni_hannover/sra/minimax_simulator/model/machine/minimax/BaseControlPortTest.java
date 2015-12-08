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
     * Tests {@link BaseControlPort#ALU_SELECT_A}.
     */
    @Test
    public void testAluSelectA() {
        BaseControlPort bcp = BaseControlPort.ALU_SELECT_A;
        assertEquals("Port of ALU_SELECT_A", "ALU_SELECT_A", bcp.port().getName());
    }

    /**
     * Tests {@link BaseControlPort#ALU_SELECT_B}.
     */
    @Test
    public void testAluSelectB() {
        BaseControlPort bcp = BaseControlPort.ALU_SELECT_B;
        assertEquals("Port of ALU_SELECT_B", "ALU_SELECT_B", bcp.port().getName());
    }

    /**
     * Tests {@link BaseControlPort#MDR_SEL}.
     */
    @Test
    public void testMdrSel() {
        BaseControlPort bcp = BaseControlPort.MDR_SEL;
        assertEquals("Port of MDR_SEL", "MDR_SEL", bcp.port().getName());
    }

    /**
     * Tests {@link BaseControlPort#MEM_CS}.
     */
    @Test
    public void testMemCS() {
        BaseControlPort bcp = BaseControlPort.MEM_CS;
        assertEquals("Port of MEM_CS", "MEM_CS", bcp.port().getName());
    }

    /**
     * Tests {@link BaseControlPort#MEM_RW}.
     */
    @Test
    public void testMemRW() {
        BaseControlPort bcp = BaseControlPort.MEM_RW;
        assertEquals("Port of MEM_RW", "MEM_RW", bcp.port().getName());
    }

    /**
     * Tests {@link BaseControlPort#ALU_CTRL}.
     */
    @Test
    public void testAluCtrl() {
        BaseControlPort bcp = BaseControlPort.ALU_CTRL;
        assertEquals("Port of ALU_CTRL", "ALU_CTRL", bcp.port().getName());
    }

}
