package de.uni_hannover.sra.minimax_simulator.model.configuration.event;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

public abstract class MachineConfigListEvent<T> extends MachineConfigEvent
{
	// Add: the added element
	// Remove: the removed element
	// Update: the new element
	// Exchange: the first element
	public T			element;

	// Update: the old element
	// Exchange: the second element
	public T			element2;

	// Add: the index of the new element
	// Remove: the index that was removed
	// Update: the index of the updated element
	// Exchange: the first index to swap
	public int			index;

	// Exchange: the second index to swap
	public int			index2;

	public EventType	type;

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "; " + type + "; " + element
			+ (element2 != null ? "/" + element2 : "") + ", " + index
			+ (index2 != -1 ? "/" + index2 : "");
	}

	public static enum EventType
	{
		ELEMENT_ADDED,
		ELEMENT_REMOVED,
		ELEMENT_REPLACED,
		ELEMENTS_EXCHANGED;
	}

	private MachineConfigListEvent()
	{
	}

	public static <T, U extends MachineConfigListEvent<T>> U eventAdded(T o, int index,
			U e)
	{
		e.element = o;
		e.index = index;
		e.type = EventType.ELEMENT_ADDED;
		return e;
	}

	public static <T, U extends MachineConfigListEvent<T>> U eventRemoved(T o, int index,
			U e)
	{
		e.element = o;
		e.index = index;
		e.type = EventType.ELEMENT_REMOVED;
		return e;
	}

	public static <T, U extends MachineConfigListEvent<T>> U eventReplaced(T oldElem,
			T newElem, int index, U e)
	{
		e.element = newElem;
		e.element2 = oldElem;
		e.index = index;
		e.type = EventType.ELEMENT_REPLACED;
		return e;
	}

	public static <T, U extends MachineConfigListEvent<T>> U eventExchanged(T elem1,
			T elem2, int index1, int index2, U e)
	{
		e.element = elem1;
		e.element2 = elem2;
		e.index = index1;
		e.index2 = index2;
		e.type = EventType.ELEMENTS_EXCHANGED;
		return e;
	}

	public static class MachineConfigAluEvent extends
			MachineConfigListEvent<AluOperation>
	{
		public static MachineConfigAluEvent eventAdded(AluOperation aluOp, int index)
		{
			MachineConfigAluEvent e = new MachineConfigAluEvent();
			return eventAdded(aluOp, index, e);
		}

		public static MachineConfigAluEvent eventRemoved(AluOperation aluOp, int index)
		{
			MachineConfigAluEvent e = new MachineConfigAluEvent();
			return eventRemoved(aluOp, index, e);
		}

		public static MachineConfigAluEvent eventExchanged(AluOperation alu1,
				AluOperation alu2, int index1, int index2)
		{
			MachineConfigAluEvent e = new MachineConfigAluEvent();
			return eventExchanged(alu1, alu2, index1, index2, e);
		}
	}

	public static class MachineConfigRegisterEvent extends
			MachineConfigListEvent<RegisterExtension>
	{
		public static MachineConfigRegisterEvent eventAdded(RegisterExtension register,
				int index)
		{
			MachineConfigRegisterEvent e = new MachineConfigRegisterEvent();
			return eventAdded(register, index, e);
		}

		public static MachineConfigRegisterEvent eventRemoved(RegisterExtension register,
				int index)
		{
			MachineConfigRegisterEvent e = new MachineConfigRegisterEvent();
			return eventRemoved(register, index, e);
		}

		public static MachineConfigRegisterEvent eventReplaced(
				RegisterExtension oldRegister, RegisterExtension newRegister, int index)
		{
			MachineConfigRegisterEvent e = new MachineConfigRegisterEvent();
			return eventReplaced(oldRegister, newRegister, index, e);
		}

		public static MachineConfigRegisterEvent eventExchanged(
				RegisterExtension register1, RegisterExtension register2, int index1,
				int index2)
		{
			MachineConfigRegisterEvent e = new MachineConfigRegisterEvent();
			return eventExchanged(register1, register2, index1, index2, e);
		}
	}

	public static class MachineConfigMuxEvent extends MachineConfigListEvent<MuxInput>
	{
		public MuxType	mux;

		public static MachineConfigMuxEvent eventAdded(MuxType mux, MuxInput source,
				int index)
		{
			MachineConfigMuxEvent e = new MachineConfigMuxEvent();
			e.mux = mux;
			return eventAdded(source, index, e);
		}

		public static MachineConfigMuxEvent eventRemoved(MuxType mux, MuxInput source,
				int index)
		{
			MachineConfigMuxEvent e = new MachineConfigMuxEvent();
			e.mux = mux;
			return eventRemoved(source, index, e);
		}

		public static MachineConfigMuxEvent eventReplaced(MuxType mux, MuxInput oldSource,
				MuxInput newSource, int index)
		{
			MachineConfigMuxEvent e = new MachineConfigMuxEvent();
			e.mux = mux;
			return eventReplaced(oldSource, newSource, index, e);
		}

		public static MachineConfigMuxEvent eventExchanged(MuxType mux, MuxInput source1,
				MuxInput source2, int index1, int index2)
		{
			MachineConfigMuxEvent e = new MachineConfigMuxEvent();
			e.mux = mux;
			return eventExchanged(source1, source2, index1, index2, e);
		}

		@Override
		public String toString()
		{
			return super.toString() + "; mux=" + mux;
		}
	}
}