package de.uni_hannover.sra.minimax_simulator.gui.util;

import de.uni_hannover.sra.minimax_simulator.gui.common.HexStringConverter;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

/**
 * The AddressFormatter is a {@link TextFormatter} for editing the {@link javafx.scene.control.Spinner}s of the {@link de.uni_hannover.sra.minimax_simulator.gui.MemoryView}.
 *
 * @author Philipp Rohde
 */
public class AddressFormatter extends TextFormatter<Integer> {

    /**
     * The constructor creates a {@link TextFormatter} with an {@link HexStringConverter} and a filter for limiting the input.
     *
     * @param mMemory
     * 			the MachineMemory this AddressFormatter is used for
     */
    public AddressFormatter(MachineMemory mMemory) {
        super(  new HexStringConverter(mMemory.getMinAddress(), mMemory.getMaxAddress(), Util.createHexFormatString(mMemory.getAddressWidth(), false)),
                0,
                new UnaryOperator<Change>() {
                    @Override
                    public Change apply(Change change) {
                        if (change.isContentChange()) {
                            String newValue = change.getControlNewText();
                            if (!newValue.matches("-?[0-9a-fA-F]+") || newValue.length() > Long.toHexString((long) mMemory.getMaxAddress()).length()) {
                                return null;
                            }
                        }
                        return change;
                    }
                });
    }
}
