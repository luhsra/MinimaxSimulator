package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.components;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXValueUpdateDialog;

/**
 * The MemoryUpdateDialog is basically an {@link FXValueUpdateDialog}.
 * It prompts for which register the value will be changed. On confirmation the new value will be stored.
 *
 * @author Philipp Rohde
 */
public abstract class RegisterUpdateDialog extends FXValueUpdateDialog {

	public RegisterUpdateDialog(RegisterExtension register, int value) {
		super(value);

		TextResource res = Main.getTextResource("debugger").using("register.update");

		_messageLabel.setText(res.format("message", register.getName()));
	}

}