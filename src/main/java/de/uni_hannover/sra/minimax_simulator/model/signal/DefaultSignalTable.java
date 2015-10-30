package de.uni_hannover.sra.minimax_simulator.model.signal;

import com.google.common.collect.ImmutableList;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Default implementation of a {@link SignalTable}.
 *
 * @author Martin L&uuml;ck
 */
public class DefaultSignalTable extends AbstractSignalTable {

	private final List<SignalRow> rows;

	/**
	 * Constructs a new and empty {@code DefaultSignalTable}.
	 */
	public DefaultSignalTable() {
		rows = new ArrayList<SignalRow>();
	}

	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public SignalRow getRow(int index) {
		return rows.get(index);
	}

	@Override
	public ImmutableList<SignalRow> getRows() {
		return ImmutableList.copyOf(rows);
	}

	@Override
	public void addSignalRow(int index, SignalRow row) {
		rows.add(index, row);
		fireRowAdded(index, row);
	}

	@Override
	public void removeSignalRow(int index) {
		rows.remove(index);
		fireRowRemoved(index);
	}

	@Override
	public void exchangeSignalRows(int index1, int index2) {
		Collections.swap(rows, index1, index2);
		fireRowsExchanged(index1, index2);
	}

	@Override
	public void addSignalRow(SignalRow row) {
		rows.add(row);
		int index = getRowCount() - 1;
		fireRowAdded(index, row);
	}

	// TODO: make SignalRow immutable?
	@Override
	public void setRowSignal(int index, String signal, SignalValue value) {
		SignalRow row = rows.get(index);
		row.setSignal(signal, value);
		fireRowReplaced(index, row);
	}

	@Override
	public void setRowJump(int index, Jump jump) {
		SignalRow row = rows.get(index);
		row.setJump(jump);
		fireRowReplaced(index, row);
	}

	@Override
	public void setSignalRow(int index, SignalRow row) {
		rows.set(index, row);
		fireRowReplaced(index, row);
	}

	@Override
	public DescriptionFactory getDescriptionFactory() {
		return null;
	}

	@Override
	public void moveSignalRows(int firstIndex, int lastIndex, int direction) {
		checkArgument(firstIndex >= 0 && lastIndex < getRowCount() && lastIndex >= firstIndex);
		
		// move down
		if (direction == 1) {
			checkArgument(lastIndex < getRowCount() - 1);
			for (int i = lastIndex; i >= firstIndex; i--) {
				Collections.swap(rows, i, i + 1);
			}
			fireRowsUpdated(firstIndex, lastIndex + 1);
		}
		// move up
		else if (direction == -1) {
			checkArgument(firstIndex > 0);
			for (int i = firstIndex; i <= lastIndex; i++) {
				Collections.swap(rows, i - 1, i);
			}
			fireRowsUpdated(firstIndex - 1, lastIndex);
		}
		else {
			throw new IllegalArgumentException("direction == -1 or 1");
		}
	}
}