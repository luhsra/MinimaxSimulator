package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.components;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.ValueUpdateDialog;

public abstract class RegisterUpdateDialog extends ValueUpdateDialog
{
	public RegisterUpdateDialog(RegisterExtension register, int value)
	{
		super(value);

		TextResource res = Application.getTextResource("debugger").using("register.update");

		_messageLabel.setText(res.format("message", register.getName()));
		_swapMode.setIcon(Icons.getInstance().get(res.get("swapmode.icon")));
		_okButton.setText(res.get("ok"));
		_cancelButton.setText(res.get("cancel"));

		pack();
		setLocationRelativeTo(null);
	}
}