package de.uni_hannover.sra.minimax_simulator.model.machine;

import com.google.common.collect.ImmutableList;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.signal.*;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Decorator for a {@link SignalTable} that calculates the descriptions of the {@link SignalRow}s
 * and updates the ALUSelect codes, ALUOperation codes and JumpTargets.
 * 
 * @author Martin L&uml;ck
 * @author Philipp Rohde
 */
public class MachineSignalTable implements SignalTable, MachineConfigListener {

	private final SignalTable theTable;
	private final DescriptionFactory descriptionFactory;
	private final MachineConfiguration machineConfig;
	private final SignalConfiguration signalConfig;

	/** Enum used for the recalculation of the jump targets. */
	private enum Action {
		ADDED,
		MOVED {
			private int direction = 0;

			@Override
			public void setDirection(int value) {
				direction = value;
			}

			@Override
			public int getDirection() {
				return direction;
			}
		},
		REMOVED;

		/**
		 * Gets the direction of moving.
		 *
		 * @return
		 *          {@code -1} for up and {@code 1} for down
		 */
		public int getDirection() { return 0; }

		/**
		 * Sets the direction to the specified value.
		 *
		 * @param value
		 *          the new value
		 */
		public void setDirection(int value) {}
	}

	/**
	 * Constructs a new {@code MachineSignalTable}.
	 *
	 * @param table
	 *          the {@link SignalTable} of the machine
	 * @param machineConfig
	 *          the {@link MachineConfiguration} of the machine
	 * @param descriptionFactory
	 *          the {@link DescriptionFactory} used for generating the row description
	 * @param signalConfig
	 *          the {@link SignalConfiguration} of the machine
	 */
	public MachineSignalTable(SignalTable table, MachineConfiguration machineConfig,
			DescriptionFactory descriptionFactory, SignalConfiguration	signalConfig) {
		theTable = checkNotNull(table);
		this.machineConfig = checkNotNull(machineConfig);
		this.descriptionFactory = checkNotNull(descriptionFactory);
		this.signalConfig = checkNotNull(signalConfig);

		this.machineConfig.addMachineConfigListener(this);

		updateAllDescriptions();
	}

	/**
	 * Updates the description of the specified {@link SignalRow}.
	 *
	 * @param rowIndex
	 *          the index of the row
	 * @param row
	 *          the {@code SignalRow}
	 */
	private void updateDescription(int rowIndex, SignalRow row) {
		row.setDescription(descriptionFactory.createDescription(rowIndex, row));
	}

	/**
	 * Updates the description of all {@link SignalRow}s.
	 */
	private void updateAllDescriptions() {
		for (int i = 0, n = theTable.getRowCount(); i < n; i++) {
			updateDescription(i, theTable.getRow(i));
		}
	}

	@Override
	public int getRowCount() {
		return theTable.getRowCount();
	}

	@Override
	public SignalRow getRow(int index) {
		return theTable.getRow(index);
	}

	@Override
	public void addSignalTableListener(SignalTableListener l) {
		theTable.addSignalTableListener(l);
	}

	@Override
	public void removeSignalTableListener(SignalTableListener l) {
		theTable.removeSignalTableListener(l);
	}

	@Override
	public void addSignalRow(SignalRow row) {
		updateDescription(theTable.getRowCount(), row);
		theTable.addSignalRow(row);
	}

	@Override
	public void addSignalRow(int index, SignalRow row) {
		updateJumpTargets(index, Action.ADDED);
		updateDescription(index, row);
		theTable.addSignalRow(index, row);
	}

	@Override
	public void removeSignalRow(int index) {
		updateJumpTargets(index, Action.REMOVED);
		theTable.removeSignalRow(index);
	}

	/**
	 * Updates the jump targets after a {@code SignalRow} has been added, removed or moved.
	 *
	 * @param index
	 *          the index of the row that has been changed
	 * @param action
	 *          the action that triggered the update
	 */
	private void updateJumpTargets(int index, Action action) {
		List<SignalRow> rows = theTable.getRows();

		for (int i = 0; i < rows.size(); i++) {
			Jump j = rows.get(i).getJump();

			if (j == null) {
				break;
			}
			if (j instanceof UnconditionalJump ) {
				UnconditionalJump uj = (UnconditionalJump) j;

				int oldTarget = uj.getTargetRow();
				int newTarget = calculateJumpTarget(index, oldTarget, action);

				if (oldTarget != newTarget) {
					setRowJump(i, new UnconditionalJump(newTarget));
				}
			}
			else if (j instanceof ConditionalJump) {
				ConditionalJump cj = (ConditionalJump) j;

				int oldTarget0 = cj.getTargetRow(0);
				int newTarget0 = calculateJumpTarget(index, oldTarget0, action);

				int oldTarget1 = cj.getTargetRow(1);
				int newTarget1 = calculateJumpTarget(index, oldTarget1, action);

				if ( (oldTarget0 != newTarget0) || (oldTarget1 != newTarget1) ) {
					setRowJump(i, new ConditionalJump(newTarget0, newTarget1));
				}

			}
		}
	}

	/**
	 * Recalculates the jump target.
	 *
	 * @param index
	 *          the index of the row that has been changed
	 * @param oldTarget
	 *          the index of old target row
	 * @param action
	 *          the action that triggered the update
	 * @return
	 *          the recalculated jump target
	 */
	private int calculateJumpTarget(int index, int oldTarget, Action action) {
		int newTarget = oldTarget;
		switch (action) {
			case ADDED:
				if (oldTarget >= index) {
					newTarget = oldTarget + 1;
				}
				break;

			case REMOVED:
				if (oldTarget == index) {
					newTarget = -1;
				}
				else if (oldTarget > index) {
					newTarget = oldTarget - 1;
				}
				break;

			case MOVED:
				int direction = action.getDirection();
				if (oldTarget == index + direction) {
					newTarget = oldTarget - direction;
				}
				else if (oldTarget == index) {
					newTarget = oldTarget + direction;
				}
				break;
		}
		return newTarget;
	}

	@Override
	public void exchangeSignalRows(int index1, int index2) {
		theTable.exchangeSignalRows(index1, index2);
	}

	@Override
	public void setRowSignal(int index, String signal, SignalValue value) {
		SignalRow row = theTable.getRow(index);
		row.setSignal(signal, value);
		updateDescription(index, row);
		theTable.setRowSignal(index, signal, value);
	}

	@Override
	public void setRowJump(int index, Jump jump) {
		SignalRow row = theTable.getRow(index);
		row.setJump(jump);
		updateDescription(index, row);
		theTable.setRowJump(index, jump);
	}

	@Override
	public void processEvent(MachineConfigEvent event) {
		// update the ALUSelect codes
		if (event instanceof MachineConfigListEvent.MachineConfigMuxEvent) {
			MachineConfigListEvent.MachineConfigMuxEvent muxEvent = (MachineConfigListEvent.MachineConfigMuxEvent) event;
			if (muxEvent.type == MachineConfigListEvent.EventType.ELEMENT_REMOVED) {
				updateAluSelectCodesRemoved(muxEvent.mux, muxEvent.index);
			}
			else if (muxEvent.type == MachineConfigListEvent.EventType.ELEMENTS_EXCHANGED) {
				updateAluSelectCodesExchanged(muxEvent.mux, muxEvent.index, muxEvent.index2);
			}
		}
		// update the ALUOp codes
		else if (event instanceof MachineConfigListEvent.MachineConfigAluEvent) {
			MachineConfigListEvent.MachineConfigAluEvent aluEvent = (MachineConfigListEvent.MachineConfigAluEvent) event;
			if (aluEvent.type == MachineConfigListEvent.EventType.ELEMENT_REMOVED) {
				updateAluOpCodesRemoved(aluEvent.index);
			}
			else if (aluEvent.type == MachineConfigListEvent.EventType.ELEMENTS_EXCHANGED) {
				updateAluOpCodesExchanged(aluEvent.index, aluEvent.index2);
			}
		}

		// any signal may now be invalid
		replaceInvalidSignals();

		// any signal row can have another description now
		updateAllDescriptions();
	}

	/**
	 * Replaces invalid signals with {@link SignalValue#DONT_CARE}.
	 */
	private void replaceInvalidSignals() {
		ImmutableList<SignalRow> rows = getRows();
		ListIterator<SignalRow> iter = rows.listIterator();
		while (iter.hasNext()) {
			int index = iter.nextIndex();
			SignalRow row = iter.next();
			for (SignalType signalType : signalConfig.getSignalTypes()) {
				SignalValue currentRowValue = row.getSignal(signalType);
				if (!signalType.getValues().contains(currentRowValue)) {
					row.setSignal(signalType, SignalValue.DONT_CARE);
					theTable.setRowSignal(index, signalType.getName(), SignalValue.DONT_CARE);
				}
			}
		}
	}

	/**
	 * Updates the ALUSelect codes after deletion of a muxInput.
	 *
	 * @param mux
	 *          the multiplexer that was changed
	 * @param index
	 *          the index of the removed muxInput
	 */
	private void updateAluSelectCodesRemoved(MuxType mux, int index) {
		String lookUp = (mux == MuxType.A) ? "ALU_SELECT_A" : "ALU_SELECT_B";

		for (SignalRow signalRow : theTable.getRows()) {
			Map<String, SignalValue> signalValues = signalRow.getSignalValues();

			if (signalValues.containsKey(lookUp)) {
				SignalValue value = signalValues.get(lookUp);
				if (!value.isDontCare() && value.intValue() > index) {
					int newIndex = value.intValue() - 1;
					signalRow.setSignalValue(lookUp, newIndex);
				}
				else if (!value.isDontCare() && value.intValue() == index) {
					signalRow.setSignal(lookUp, null);
				}
			}
		}
	}

	/**
	 * Updates the ALUSelect codes after exchanging two muxInputs.
	 *
	 * @param mux
	 *          the multiplexer that was changed
	 * @param index1
	 *          the first muxInput
	 * @param index2
	 *          the second muxInput
	 */
	private void updateAluSelectCodesExchanged(MuxType mux, int index1, int index2) {
		String lookUp = (mux == MuxType.A) ? "ALU_SELECT_A" : "ALU_SELECT_B";

		for (SignalRow signalRow : theTable.getRows()) {
			Map<String, SignalValue> signalValues = signalRow.getSignalValues();

			if (signalValues.containsKey(lookUp)) {
				SignalValue value = signalValues.get(lookUp);
				if (!value.isDontCare() && value.intValue() == index1) {
					signalRow.setSignalValue(lookUp, index2);
				}
				else if (!value.isDontCare() && value.intValue() == index2) {
					signalRow.setSignalValue(lookUp, index1);
				}
			}
		}
	}

	/**
	 * Updates the ALUOp codes after deletion of an ALUOperation.
	 *
	 * @param index
	 *          the index of the removed muxInput
	 */
	private void updateAluOpCodesRemoved(int index) {
		String lookUp = "ALU_CTRL";

		for (SignalRow signalRow : theTable.getRows()) {
			Map<String, SignalValue> signalValues = signalRow.getSignalValues();

			if (signalValues.containsKey(lookUp)) {
				SignalValue value = signalValues.get(lookUp);
				if (!value.isDontCare() && value.intValue() > index) {
					int newIndex = value.intValue() - 1;
					signalRow.setSignalValue(lookUp, newIndex);
				}
				else if (!value.isDontCare() && value.intValue() == index) {
					signalRow.setSignal(lookUp, null);
				}
			}
		}
	}

	/**
	 * Updates the ALUOp codes after exchanging two ALUOperations.
	 *
	 * @param index1
	 *          the first muxInput
	 * @param index2
	 *          the second muxInput
	 */
	private void updateAluOpCodesExchanged(int index1, int index2) {
		String lookUp = "ALU_CTRL";

		for (SignalRow signalRow : theTable.getRows()) {
			Map<String, SignalValue> signalValues = signalRow.getSignalValues();

			if (signalValues.containsKey(lookUp)) {
				SignalValue value = signalValues.get(lookUp);
				if (!value.isDontCare() && value.intValue() == index1) {
					signalRow.setSignalValue(lookUp, index2);
				}
				else if (!value.isDontCare() && value.intValue() == index2) {
					signalRow.setSignalValue(lookUp, index1);
				}
			}
		}
	}

	@Override
	public void setSignalRow(int index, SignalRow row) {
		updateDescription(index, row);
		theTable.setSignalRow(index, row);
	}

	@Override
	public DescriptionFactory getDescriptionFactory() {
		return descriptionFactory;
	}

	@Override
	public void moveSignalRows(int firstIndex, int lastIndex, int direction) {
		Action action = Action.MOVED;
		action.setDirection(direction);
		for (int i = lastIndex; i >= firstIndex; i--) {
			updateJumpTargets(i, action);
		}
		theTable.moveSignalRows(firstIndex, lastIndex, direction);
	}

	@Override
	public ImmutableList<SignalRow> getRows() {
		return theTable.getRows();
	}
}