package de.uni_hannover.sra.minimax_simulator.model.machine.base.display;

import de.uni_hannover.sra.minimax_simulator.ui.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;

import java.util.Set;

/**
 * A {@code MachineDisplay} is the visual representation of a register machine.
 *
 * @author Martin L&uuml;ck
 */
public interface MachineDisplay {

	/**
	 * Gets all {@link SpriteOwner}s of the {@code MachineDisplay}.
	 *
	 * @return
	 *          a set of all {@code SpriteOwner}s
	 */
	public Set<SpriteOwner> getAllSpriteOwners();

	/**
	 * Adds a new {@link SpriteOwner} to the {@code MachineDisplay}.
	 *
	 * @param spriteOwner
	 *          the {@code SpriteOwner} to add
	 */
	public void addSpriteOwner(SpriteOwner spriteOwner);

	/**
	 * Removes a {@link SpriteOwner} from the {@code MachineDisplay}.
	 *
	 * @param spriteOwner
	 *          the {@code SpriteOwner} to remove
	 */
	public void removeSpriteOwner(SpriteOwner spriteOwner);

	/**
	 * Gets the {@link Dimension} of the {@code MachineDisplay}.
	 *
	 * @return
	 *          the {@code Dimension} of the {@code MachineDisplay}
	 */
	public Dimension getDimension();

	/**
	 * Gets the {@link RenderEnvironment} used for rendering of the {@code MachineDisplay}.
	 *
	 * @return
	 *          the {@code RenderEnvironment} used for rendering
	 */
	public RenderEnvironment getRenderEnvironment();

	/**
	 * Sets the {@link RenderEnvironment} of the {@code MachineDisplay}.
	 *
	 * @param env
	 *          the new {@code RenderEnvironment}
	 */
	public void setRenderEnvironment(RenderEnvironment env);

	/**
	 * Registers a new {@link MachineDisplayListener}.
	 *
	 * @param l
	 *          the {@code MachineDisplayListener} to register
	 */
	public void addMachineDisplayListener(MachineDisplayListener l);

	/**
	 * Removes a {@link MachineDisplayListener}.
	 *
	 * @param l
	 *          the {@code MachineDisplayListener} to remove.
	 */
	public void removeMachineDisplayListener(MachineDisplayListener l);
}