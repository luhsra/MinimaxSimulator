package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.SynchronousCircuit;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.MemorySprite;

public class Memory extends SimplePart implements SpriteOwner, SynchronousCircuit
{
	private final MachineMemory	_memory;

	private final IngoingPin	_dataIn;
	private final IngoingPin	_adr;
	private final IngoingPin	_cs;
	private final IngoingPin	_rw;

	public Memory(MachineMemory memory)
	{
		_memory = memory;

		_dataIn = new IngoingPin(this);
		_adr = new IngoingPin(this);
		_cs = new IngoingPin(this);
		_rw = new IngoingPin(this);
	}

	public IngoingPin getDataIn()
	{
		return _dataIn;
	}

	public IngoingPin getAdr()
	{
		return _adr;
	}

	public IngoingPin getCs()
	{
		return _cs;
	}

	public IngoingPin getRw()
	{
		return _rw;
	}

	@Override
	public void update()
	{
		if (_cs.read() != 0 && _rw.read() != 0)
		{
			// read
			int value = _memory.getMemoryState().getInt(_adr.read());
			getDataOut().write(value);
		}
		else
		{
			getDataOut().write(0);
		}
	}

	@Override
	public void nextCycle()
	{
		// Write only on cycle transition
		if (_cs.read() != 0 && _rw.read() == 0)
		{
			// write
			int value = _dataIn.read();
			_memory.getMemoryState().setInt(_adr.read(), value);
		}
	}

	@Override
	public Sprite createSprite()
	{
		return new MemorySprite(this);
	}
}