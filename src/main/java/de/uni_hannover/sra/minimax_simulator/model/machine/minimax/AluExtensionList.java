package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.ExtensionList;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;

import java.util.Collection;
import java.util.Collections;

class AluExtensionList implements ExtensionList<AluOperation>
{
	private final Alu	_alu;

	public AluExtensionList(Alu alu)
	{
		_alu = alu;
	}

	@Override
	public void add(AluOperation operation)
	{
		_alu.getAluOperations().add(operation);
	}

	@Override
	public void addAll(Collection<? extends AluOperation> elements)
	{
		_alu.getAluOperations().addAll(elements);
	}

	@Override
	public void remove(int index)
	{
		_alu.getAluOperations().remove(index);
	}

	@Override
	public void swap(int index1, int index2)
	{
		Collections.swap(_alu.getAluOperations(), index1, index2);
	}

	@Override
	public void set(int index, AluOperation element)
	{
		// Cannot happen with ALU operations
		throw new AssertionError();
	}
}