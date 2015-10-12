package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXValueUpdateDialog;
import de.uni_hannover.sra.minimax_simulator.util.Util;

/**
 * The MemoryUpdateDialog is basically an {@link FXValueUpdateDialog}.
 * It prompts for which memory address the value will be changed. On confirmation the new value will be stored in the {@link MachineMemory}.
 *
 * @author Philipp Rohde
 */
public class MemoryUpdateDialog extends FXValueUpdateDialog
{
	private final MachineMemory	_memory;
	private final int			_address;

	public MemoryUpdateDialog(int address, MachineMemory memory)
	{
		super(memory.getMemoryState().getInt(address));

		_memory = memory;
		_address = address;

		_messageLabel.setText(_res.format("message",
			Util.toHex(address, memory.getAddressWidth(), true)));
	}

	/**
	 * Sets the new value to the memory address for which the dialog was opened.
	 *
	 * @param value
	 * 			the new value
	 */
	@Override
	protected void setValue(int value)
	{
		_memory.getMemoryState().setInt(_address, value);
	}
}