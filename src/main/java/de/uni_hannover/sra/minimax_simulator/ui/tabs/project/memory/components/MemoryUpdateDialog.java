package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXValueUpdateDialog;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.scene.image.ImageView;

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

		_swapMode.setGraphic(new ImageView("images/"+_res.get("swapmode.icon")));
	}

	@Override
	protected void setValue(int value)
	{
		_memory.getMemoryState().setInt(_address, value);
	}
}