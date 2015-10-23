package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.JunctionSprite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A {@code Junction} is the junction of several {@link Wire}s and the related {@link Pin}s.
 *
 * @author Martin L&uuml;ck
 */
public class Junction extends Part implements SpriteOwner {

	private final ArrayList<OutgoingPin> _dataOuts;

	private final IngoingPin _dataIn;

	/**
	 * Constructs a new {@code Junction} without any pin.
	 */
	public Junction() {
		this(0);
	}

	/**
	 * Constructs a new {@code Junction} with the specified amount of pins.
	 *
	 * @param pinCount
	 *          the amount of pins crossing
	 */
	public Junction(int pinCount) {
		_dataIn = new IngoingPin(this);

		_dataOuts = new ArrayList<OutgoingPin>(pinCount);
		for (int i = 0; i < pinCount; i++) {
			_dataOuts.add(new OutgoingPin(this));
		}
	}

	/**
	 * Gets all {@link OutgoingPin}s of the {@code Junction}.
	 *
	 * @return
	 *          a list of the {@code OutgoingPin}s
	 */
	public List<OutgoingPin> getDataOuts() {
		return _dataOuts;
	}

	/**
	 * Gets all {@link IngoingPin}s of the {@code Junction}.
	 *
	 * @return
	 *          a list of the {@code IngoingPin}s
	 */
	public IngoingPin getDataIn() {
		return _dataIn;
	}

	@Override
	public void update() {
		int value = _dataIn.read();
		for (OutgoingPin out : _dataOuts)
			out.write(value);
	}

	@Override
	public Set<? extends Circuit> getSuccessors() {
		Set<Circuit> successors = new HashSet<Circuit>();
		for (OutgoingPin pin : _dataOuts) {
			successors.addAll(pin.getSuccessors());
		}
		return successors;
	}

	@Override
	public Sprite createSprite() {
		return new JunctionSprite(this);
	}

	@Override
	public void reset() {

	}
}
