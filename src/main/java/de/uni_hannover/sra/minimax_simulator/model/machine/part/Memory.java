package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.SynchronousCircuit;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.MemorySprite;

/**
 * Implementation of the memory as component part of a machine.
 *
 * @author Martin L&uuml;ck
 */
public class Memory extends SimplePart implements SpriteOwner, SynchronousCircuit {

	private final MachineMemory	_memory;

	private final IngoingPin	_dataIn;
	private final IngoingPin	_adr;
	private final IngoingPin	_cs;
	private final IngoingPin	_rw;

	/**
	 * Constructs a new {@code Memory} related to the specified {@link MachineMemory}.
	 *
	 * @param memory
	 *          the memory of the machine
	 */
	public Memory(MachineMemory memory) {
		_memory = memory;

		_dataIn = new IngoingPin(this);
		_adr = new IngoingPin(this);
		_cs = new IngoingPin(this);
		_rw = new IngoingPin(this);
	}

	/**
	 * Gets the data {@link IngoingPin}.
	 *
	 * @return
	 *          the data input pin
	 */
	public IngoingPin getDataIn() {
		return _dataIn;
	}

	/**
	 * Gets the address {@link IngoingPin}.
	 *
	 * @return
	 *          the address input pin
	 */
	public IngoingPin getAdr() {
		return _adr;
	}

	/**
	 * Gets the {@link IngoingPin} of the {@code HS.CS} signal.
	 *
	 * @return
	 *          the {@code HS.CS IngoingPin}
	 */
	public IngoingPin getCs() {
		return _cs;
	}

	/**
	 * Gets the {@link IngoingPin} of the {code HS.RW} signal.
	 *
	 * @return
	 *          the {@code HS.RW IngoingPin}
	 */
	public IngoingPin getRw() {
		return _rw;
	}

	@Override
	public void update() {
		if (_cs.read() != 0 && _rw.read() != 0) {
			// read
			int value = _memory.getMemoryState().getInt(_adr.read());
			getDataOut().write(value);
		}
		else {
			getDataOut().write(0);
		}
	}

	@Override
	public void nextCycle() {
		// write only on cycle transition
		if (_cs.read() != 0 && _rw.read() == 0) {
			// write
			int value = _dataIn.read();
			_memory.getMemoryState().setInt(_adr.read(), value);
		}
	}

	@Override
	public Sprite createSprite() {
		return new MemorySprite(this);
	}
}