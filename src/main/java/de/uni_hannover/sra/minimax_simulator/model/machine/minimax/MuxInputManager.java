package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import java.util.Collection;
import java.util.List;

import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.ExtensionList;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;

interface MuxInputManager extends ExtensionList<MuxInput>
{
	public class InputEntry
	{
		public MuxInput		input;
		public IngoingPin	pin;
		public String		pinId;

		@Override
		public String toString()
		{
			return pinId + "<-" + input;
		}
	}

	public MuxType getMuxType();

	public List<InputEntry> getMuxInputs();

	@Override
	public void add(MuxInput input);

	@Override
	public void addAll(Collection<? extends MuxInput> inputs);

	@Override
	public void remove(int index);

	@Override
	public void swap(int index1, int index2);

	@Override
	public void set(int index, MuxInput input);

	public void registerGroupManager(MuxInputGroupManager inputGroupManager);
}