package de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Trackable;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

/**
 * The {@code RegisterUpdateDialog} is basically an {@link ValueUpdateDialog}.
 * It prompts for which register the value will be changed. On confirmation the new value will be stored.
 *
 * @author Philipp Rohde
 */
public class RegisterUpdateDialog extends ValueUpdateDialog {

	private final Trackable<Integer>	_value;

	public RegisterUpdateDialog(String register, Trackable<Integer> value) {
		super(value.get());

		_value = value;

		TextResource res = Main.getTextResource("debugger").using("register.update");

		_messageLabel.setText(res.format("message", register));
	}

	@Override
	protected void setValue(int value) {
		_value.set(value);
	}
}