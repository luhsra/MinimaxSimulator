package de.uni_hannover.sra.minimax_simulator.model.signal;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;

public class DefaultSignalTable extends AbstractSignalTable
{
	private final List<SignalRow>		_rows;

	public DefaultSignalTable()
	{
		_rows = new ArrayList<SignalRow>();
	}

	@Override
	public int getRowCount()
	{
		return _rows.size();
	}

	@Override
	public SignalRow getRow(int index)
	{
		return _rows.get(index);
	}

	@Override
	public ImmutableList<SignalRow> getRows()
	{
		return ImmutableList.copyOf(_rows);
	}

	@Override
	public void addSignalRow(int index, SignalRow row)
	{
		_rows.add(index, row);
		fireRowAdded(index, row);
	}

	@Override
	public void removeSignalRow(int index) {
		// the following code updates the JumpTargets
		// TODO: should this be active? should the user be asked?
/*		for (int i = 0; i < _rows.size(); i++) {
			Jump j = _rows.get(i).getJump();
			if ( (j != null) && (j instanceof UnconditionalJump) ) {
				UnconditionalJump uj = (UnconditionalJump) j;
				if (uj.getTargetRow() > index) {
					int newTarget = uj.getTargetRow() - 1;
					setRowJump(i, new UnconditionalJump(newTarget));
				}
			}
			else if ( (j != null) && (j instanceof ConditionalJump) ) {
				ConditionalJump cj = (ConditionalJump) j;
				if ( (cj.getTargetRow(1) > index) || (cj.getTargetRow(0) > index) ) {
					int target0 = cj.getTargetRow(0);
					int target1 = cj.getTargetRow(1);

					int newTarget0 = (target0 > index) ? target0 - 1 : target0;
					int newTarget1 = (target1 > index) ? target1 - 1 : target1;

					setRowJump(i, new ConditionalJump(newTarget0, newTarget1));
				}
			}
		}		*/

		_rows.remove(index);
		fireRowRemoved(index);
	}

	@Override
	public void exchangeSignalRows(int index1, int index2)
	{
		Collections.swap(_rows, index1, index2);
		fireRowsExchanged(index1, index2);
	}

	@Override
	public void addSignalRow(SignalRow row)
	{
		_rows.add(row);
		int index = getRowCount() - 1;
		fireRowAdded(index, row);
	}

	// TODO: make SignalRow immutable?
	@Override
	public void setRowSignal(int index, String signal, SignalValue value)
	{
		SignalRow row = _rows.get(index);
		row.setSignal(signal, value);
		fireRowReplaced(index, row);
	}

	@Override
	public void setRowJump(int index, Jump jump)
	{
		SignalRow row = _rows.get(index);
		row.setJump(jump);
		fireRowReplaced(index, row);
	}

	@Override
	public void setSignalRow(int index, SignalRow row)
	{
		_rows.set(index, row);
		fireRowReplaced(index, row);
	}

	@Override
	public DescriptionFactory getDescriptionFactory() {
		return null;
	}

	@Override
	public void moveSignalRows(int firstIndex, int lastIndex, int direction)
	{
		checkArgument(firstIndex >= 0 && lastIndex < getRowCount() && lastIndex >= firstIndex);
		
		// move down
		if (direction == 1)
		{
			checkArgument(lastIndex < getRowCount() - 1);
			for (int i = lastIndex; i >= firstIndex; i--)
			{
				Collections.swap(_rows, i, i + 1);
			}
			fireRowsUpdated(firstIndex, lastIndex + 1);
		}
		// move up
		else if (direction == -1)
		{
			checkArgument(firstIndex > 0);
			for (int i = firstIndex; i <= lastIndex; i++)
			{
				Collections.swap(_rows, i - 1, i);
			}
			fireRowsUpdated(firstIndex - 1, lastIndex);
		}
		else
		{
			throw new IllegalArgumentException("direction == -1 or 1");
		}
	}
}