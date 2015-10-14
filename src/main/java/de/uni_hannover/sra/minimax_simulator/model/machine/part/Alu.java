package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import com.google.common.collect.Sets;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.AluSprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Alu extends Part implements SpriteOwner
{
	private final static Logger			_log	= Logger.getLogger(Alu.class.getName());

	private final List<AluOperation>	_aluOperations;

	private final IngoingPin			_inCtrl;
	private final IngoingPin			_inA;
	private final IngoingPin			_inB;
	private final OutgoingPin			_outData;
	private final OutgoingPin			_outZero;

	private int							_result;

	public Alu()
	{
		_aluOperations = new ArrayList<AluOperation>();

		_inCtrl = new IngoingPin(this);
		_inA = new IngoingPin(this);
		_inB = new IngoingPin(this);
		_outData = new OutgoingPin(this);
		_outZero = new OutgoingPin(this);

		// _successors = ImmutableSet.of(_outData, _outZero);

		_result = 0;
	}

	@Override
	public void update()
	{
		int mode = _inCtrl.read();
		if (mode < _aluOperations.size())
		{
			AluOperation op = _aluOperations.get(mode);
			_result = op.execute(_inA.read(), _inB.read());

			if (_log.isLoggable(Level.FINER))
				_log.log(Level.FINER, "Applying ALU operation " + op + " to " + _inA.read()
					+ " and " + _inB.read() + " with result " + _result);
		}
		else
		{
			_result = 0;

			if (_log.isLoggable(Level.FINER))
				_log.log(Level.FINER, "Unknown ALU operation: " + mode + ", writing 0");
		}

		_outZero.write(_result == 0 ? 1 : 0);
		_outData.write(_result);
	}

	@Override
	public Set<? extends Circuit> getSuccessors()
	{
		return Sets.union(_outData.getSuccessors(), _outZero.getSuccessors());
	}

	public List<AluOperation> getAluOperations()
	{
		return _aluOperations;
	}

	public IngoingPin getInCtrl()
	{
		return _inCtrl;
	}

	public IngoingPin getInA()
	{
		return _inA;
	}

	public IngoingPin getInB()
	{
		return _inB;
	}

	public OutgoingPin getOutData()
	{
		return _outData;
	}

	public OutgoingPin getOutZero()
	{
		return _outZero;
	}

	public int getResult()
	{
		return _result;
	}

	@Override
	public Sprite createSprite()
	{
		return new AluSprite(this);
	}

	@Override
	public void reset()
	{
		_result = 0;
	}
}