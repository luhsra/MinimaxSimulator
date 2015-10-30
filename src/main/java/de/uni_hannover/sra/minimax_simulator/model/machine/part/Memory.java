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

	private final MachineMemory memory;

	private final IngoingPin dataIn;
	private final IngoingPin adr;
	private final IngoingPin cs;
	private final IngoingPin rw;

	/**
	 * Constructs a new {@code Memory} related to the specified {@link MachineMemory}.
	 *
	 * @param memory
	 *          the memory of the machine
	 */
	public Memory(MachineMemory memory) {
		this.memory = memory;

		dataIn = new IngoingPin(this);
		adr = new IngoingPin(this);
		cs = new IngoingPin(this);
		rw = new IngoingPin(this);
	}

	/**
	 * Gets the data {@link IngoingPin}.
	 *
	 * @return
	 *          the data input pin
	 */
	public IngoingPin getDataIn() {
		return dataIn;
	}

	/**
	 * Gets the address {@link IngoingPin}.
	 *
	 * @return
	 *          the address input pin
	 */
	public IngoingPin getAdr() {
		return adr;
	}

	/**
	 * Gets the {@link IngoingPin} of the {@code HS.CS} signal.
	 *
	 * @return
	 *          the {@code HS.CS IngoingPin}
	 */
	public IngoingPin getCs() {
		return cs;
	}

	/**
	 * Gets the {@link IngoingPin} of the {code HS.RW} signal.
	 *
	 * @return
	 *          the {@code HS.RW IngoingPin}
	 */
	public IngoingPin getRw() {
		return rw;
	}

	@Override
	public void update() {
		if (cs.read() != 0 && rw.read() != 0) {
			// read
			int value = memory.getMemoryState().getInt(adr.read());
			getDataOut().write(value);
		}
		else {
			getDataOut().write(0);
		}
	}

	@Override
	public void nextCycle() {
		// write only on cycle transition
		if (cs.read() != 0 && rw.read() == 0) {
			// write
			int value = dataIn.read();
			memory.getMemoryState().setInt(adr.read(), value);
		}
	}

	@Override
	public Sprite createSprite() {
		return new MemorySprite(this);
	}
}