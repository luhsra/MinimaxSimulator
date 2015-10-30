package de.uni_hannover.sra.minimax_simulator.model.machine.base.display;

import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;

/**
 * A {@code MachineDisplayListener} is a class that needs to react to changes
 * of the {@link MachineDisplay}.
 *
 * @author Martin L&uuml;ck
 */
public interface MachineDisplayListener {

	/**
	 * Notifies the listener about a change of the machine's size.
	 */
	public void machineSizeChanged();

	/**
	 * Notifies the listener about a change of the {@link MachineDisplay}.
	 */
	public void machineDisplayChanged();

	/**
	 * Notifies the listener about the addition of a {@link SpriteOwner}.
	 *
	 * @param spriteOwner
	 *          the added {@code SpriteOwner}
	 */
	public void onSpriteOwnerAdded(SpriteOwner spriteOwner);

	/**
	 * Notifies the listener about the deletion of a {@link SpriteOwner}.
	 *
	 * @param spriteOwner
	 *          the removed {@code SpriteOwner}
	 */
	public void onSpriteOwnerRemoved(SpriteOwner spriteOwner);

	/**
	 * Notifies the listener about the change of a {@link SpriteOwner}.
	 *
	 * @param spriteOwner
	 *          the changed {@code SpriteOwner}
	 */
	public void onSpriteOwnerChanged(SpriteOwner spriteOwner);
}