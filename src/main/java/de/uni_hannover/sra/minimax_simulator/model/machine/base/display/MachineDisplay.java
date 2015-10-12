package de.uni_hannover.sra.minimax_simulator.model.machine.base.display;

import de.uni_hannover.sra.minimax_simulator.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;

import java.util.Set;

public interface MachineDisplay
{
	public Set<SpriteOwner> getAllSpriteOwners();

	public void addSpriteOwner(SpriteOwner spriteOwner);

	public void removeSpriteOwner(SpriteOwner spriteOwner);

	public Dimension getDimension();

	public RenderEnvironment getRenderEnvironment();

	public void setRenderEnvironment(RenderEnvironment env);

	public void addMachineDisplayListener(MachineDisplayListener l);

	public void removeMachineDisplayListener(MachineDisplayListener l);
}