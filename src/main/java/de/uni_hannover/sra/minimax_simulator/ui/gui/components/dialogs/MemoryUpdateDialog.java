package de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.util.Util;

/**
 * The {@code MemoryUpdateDialog} is basically an {@link ValueUpdateDialog}.<br>
 * It prompts for which memory address the value will be changed. On confirmation the new value will be stored in the {@link MachineMemory}.
 *
 * @author Philipp Rohde
 */
public class MemoryUpdateDialog extends ValueUpdateDialog {

    private final MachineMemory memory;
    private final int address;

    /**
     * Constructs a new {@code MemoryUpdateDialog} for the specified address of the specified {@link MachineMemory}.
     *
     * @param address
     *          the address to change its value
     * @param memory
     *          the machine's memory
     */
    public MemoryUpdateDialog(int address, MachineMemory memory) {
        super(memory.getMemoryState().getInt(address));

        this.memory = memory;
        this.address = address;

        messageLabel.setText(res.format("message",
            Util.toHex(address, memory.getAddressWidth(), true)));
    }

    /**
     * Sets the new value to the memory address for which the dialog was opened.
     *
     * @param value
     *          the new value
     */
    @Override
    protected void setValue(int value) {
        memory.getMemoryState().setInt(address, value);
    }
}