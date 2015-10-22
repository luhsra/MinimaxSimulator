package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import com.sun.javafx.tk.FontMetrics;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplay;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplayListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;
import de.uni_hannover.sra.minimax_simulator.ui.render.DummyRenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the {@link MachineDisplay} for a Minimax machine.
 *
 * @author Martin L&uuml;ck
 */
class MinimaxDisplay implements MachineDisplay, FontMetricsProvider {

	private final List<MachineDisplayListener>	_listeners;

	private final Set<SpriteOwner>				_spriteOwners;

	private RenderEnvironment					_renderEnvironment;
	private Dimension							_dimension;

	/**
	 * Constructs a new and empty {@code MinimaxDisplay}.
	 */
	public MinimaxDisplay() {
		_listeners = new ArrayList<MachineDisplayListener>(1);
		_spriteOwners = new HashSet<SpriteOwner>();

		_renderEnvironment = new DummyRenderEnvironment();
		_dimension = Dimension.ZERO;
	}

	@Override
	public RenderEnvironment getRenderEnvironment() {
		return _renderEnvironment;
	}

	@Override
	public void setRenderEnvironment(RenderEnvironment env) {
		_renderEnvironment = env;

		for (MachineDisplayListener l : _listeners) {
			l.machineDisplayChanged();
		}
	}

	@Override
	public void addSpriteOwner(SpriteOwner spriteOwner) {
		_spriteOwners.add(spriteOwner);
		fireSpriteOwnerAdded(spriteOwner);
	}

	@Override
	public void removeSpriteOwner(SpriteOwner spriteOwner) {
		_spriteOwners.remove(spriteOwner);
		fireSpriteOwnerRemoved(spriteOwner);
	}

	/**
	 * Updates a {@link SpriteOwner}.
	 *
	 * @param spriteOwner
	 *          the updated {@code SpriteOwner}
	 */
	public void updateSpriteOwner(SpriteOwner spriteOwner) {
		fireSpriteOwnerChanged(spriteOwner);
	}

	/**
	 * Adds all {@link SpriteOwner}s of the specified {@link Group}.
	 *
	 * @param group
	 *          the {@code Group} whose {@code SpriteOwner}s will be added
	 */
	public void addGroup(Group group) {
		for (SpriteOwner sprite : group.getSpriteOwners()) {
			addSpriteOwner(sprite);
		}
	}

	/**
	 * Removes all {@link SpriteOwner}s of the specified {@link Group}.
	 *
	 * @param group
	 *          the {@code Group} whose {@code SpriteOwner}s will be removed
	 */
	public void removeGroup(Group group) {
		for (SpriteOwner sprite : group.getSpriteOwners()) {
			removeSpriteOwner(sprite);
		}
	}

	@Override
	public void addMachineDisplayListener(MachineDisplayListener l) {
		_listeners.add(l);
	}

	@Override
	public void removeMachineDisplayListener(MachineDisplayListener l) {
		_listeners.add(l);
	}

	/**
	 * Sets the {@link Dimension} of the {@code MinimaxDisplay} and notifies
	 * the {@link MachineDisplayListener}s about it.
	 *
	 * @param dimension
	 *          the new {@code Dimension}
	 */
	public void setDimension(Dimension dimension) {
		_dimension = dimension;

		for (MachineDisplayListener l : _listeners) {
			l.machineSizeChanged();
		}
	}

	@Override
	public Dimension getDimension() {
		return _dimension;
	}

	@Override
	public Set<SpriteOwner> getAllSpriteOwners() {
		return _spriteOwners;
	}

	/**
	 * Notifies the {@link MachineDisplayListener}s of the addition of a {@link SpriteOwner}.
	 *
	 * @param spriteOwner
	 *          the added {@code SpriteOwner}
	 */
	protected void fireSpriteOwnerAdded(SpriteOwner spriteOwner) {
		for (MachineDisplayListener l : _listeners) {
			l.onSpriteOwnerAdded(spriteOwner);
		}
	}

	/**
	 * Notifies the {@link MachineDisplayListener}s of the removal of a {@link SpriteOwner}.
	 *
	 * @param spriteOwner
	 *          the removed {@code SpriteOwner}
	 */
	protected void fireSpriteOwnerRemoved(SpriteOwner spriteOwner) {
		for (MachineDisplayListener l : _listeners) {
			l.onSpriteOwnerRemoved(spriteOwner);
		}
	}

	/**
	 * Notifies the {@link MachineDisplayListener}s of the change of a {@link SpriteOwner}.
	 *
	 * @param spriteOwner
	 *          the changed {@code SpriteOwner}
	 */
	protected void fireSpriteOwnerChanged(SpriteOwner spriteOwner) {
		for (MachineDisplayListener l : _listeners) {
			l.onSpriteOwnerChanged(spriteOwner);
		}
	}

	@Override
	public Font getFont() {
		return _renderEnvironment.getFont();
	}

	@Override
	public FontMetrics getFontMetrics() {
		return _renderEnvironment.getFontMetrics();
	}
}