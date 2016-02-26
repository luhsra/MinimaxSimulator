package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.PagedArrayMemory;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests if a {@link AddressFormatter} creates the correct values using its converter.<br>
 * Testing the change filter programmatically is not possible.
 *
 * @author Philipp Rohde
 */
public class AddressFormatterTest {

    /**
     * Actually runs the test.
     */
    @Test
    public void test() {
        MachineMemory memory = new PagedArrayMemory(24, 12);
        AddressFormatter formatter = new AddressFormatter(memory);

        HexStringConverter converter = new HexStringConverter(0, 16777215, Util.createHexFormatString(24, false));

        assertEquals(converter.fromString("12A"), formatter.getValueConverter().fromString("12A"));
        assertEquals(converter.fromString("16777215"), formatter.getValueConverter().fromString("16777215"));
        assertEquals(converter.fromString("16777219"), formatter.getValueConverter().fromString("16777219"));
        assertEquals(converter.fromString("0"), formatter.getValueConverter().fromString("0"));
        assertEquals(converter.fromString("80000000"), formatter.getValueConverter().fromString("80000000"));
    }
}
