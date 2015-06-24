package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FxValueUpdateDialog;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.scene.image.ImageView;

public class MemoryUpdateDialog extends FxValueUpdateDialog
{
	private final MachineMemory	_memory;
	private final int			_address;

	public MemoryUpdateDialog(int address, MachineMemory memory)
	{
		super(memory.getMemoryState().getInt(address));

		TextResource res = Application.getTextResource("project").using("memory.update");

		_memory = memory;
		_address = address;

		_messageLabel.setText(res.format("message",
			Util.toHex(address, memory.getAddressWidth(), true)));

//		_swapMode.setIcon(Icons.getInstance().get(res.get("swapmode.icon")));
		_swapMode.setGraphic(new ImageView("./images/"+res.get("swapmode.icon")));
		_okButton.setText(res.get("ok"));
//		_cancelButton.setText(res.get("cancel"));

//		pack();
//		setLocationRelativeTo(null);
	}

	@Override
	protected void setValue(int value)
	{
		_memory.getMemoryState().setInt(_address, value);
	}
}