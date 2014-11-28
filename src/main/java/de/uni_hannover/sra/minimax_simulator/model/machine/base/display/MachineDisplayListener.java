package de.uni_hannover.sra.minimax_simulator.model.machine.base.display;

import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;

public interface MachineDisplayListener
{
	public void machineSizeChanged();

	public void machineDisplayChanged();

	public void onSpriteOwnerAdded(SpriteOwner spriteOwner);

	public void onSpriteOwnerRemoved(SpriteOwner spriteOwner);

	public void onSpriteOwnerChanged(SpriteOwner spriteOwner);
}