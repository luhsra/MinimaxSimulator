package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.scene.control.SpinnerValueFactory;

/**
 * The {@code MemorySpinnerValueFactory} is the {@link SpinnerValueFactory} for the {@link javafx.scene.control.Spinner}s
 * from the memory {@link javafx.scene.control.Tab}.<br>
 * It sets the range from the minimal address to the maximal address of the {@link MachineMemory}.
 * The addresses are rendered as hexadecimal String.
 *
 * @author Philipp Rohde
 */
public class MemorySpinnerValueFactory extends HexSpinnerValueFactory {

    public MemorySpinnerValueFactory(MachineMemory mMemory) {
        super();
        this.setMin(mMemory.getMinAddress());
        this.setMax(mMemory.getMaxAddress());

        // render addresses as hexadecimal String
        final String addressFormatString = Util.createHexFormatString(mMemory.getAddressWidth(), false);
        this.setConverter(new HexStringConverter(getMin(), getMax(), addressFormatString));
    }

}
