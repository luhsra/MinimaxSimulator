package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import java.util.ArrayList;
import java.util.List;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTableListener;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalType;

public class SignalTableModel extends AbstractProgramTableModel implements SignalTableListener,
		SignalConfigListener
{
	private final SignalTable			_signalTable;
	private final SignalConfiguration	_signalConfig;

	public SignalTableModel(SignalTable signalTable, SignalConfiguration signalConfig)
	{
		super(Main.getTextResource("signal"), signalTable);

		_signalTable = signalTable;
		_signalConfig = signalConfig;

		updateColumns();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return getColumn(columnIndex) instanceof EditableColumn;
	}

	@Override
	public void onStructureChanged()
	{
		updateColumns();
	}

	@Override
	public void onRowAdded(int index, SignalRow row)
	{
		fireTableRowsInserted(index, index);
	}

	@Override
	public void onRowRemoved(int index)
	{
		fireTableRowsDeleted(index, index);
	}

	@Override
	public void onRowsExchanged(int index1, int index2)
	{
		fireTableRowsUpdated(index1, index1);
		fireTableRowsUpdated(index2, index2);
	}

	@Override
	public void onRowReplaced(int index, SignalRow row)
	{
		fireTableRowsUpdated(index, index);
	}

	@Override
	public void onRowsUpdated(int fromIndex, int toIndex)
	{
		fireTableRowsUpdated(fromIndex, toIndex);
	}

	@Override
	public void signalStructureChanged()
	{
		// TODO: don't auto-generate columns, the size set by the adjuster is lost
		updateColumns();
	}

	@Override
	protected List<Column> createColumns()
	{
		List<Column> cols = new ArrayList<Column>();
		cols.add(new BreakpointColumn());

		cols.add(new LabelColumn());
		cols.add(new AddressColumn());

		for (SignalType signal : _signalConfig.getSignalTypes())
			cols.add(new SignalColumn(_signalTable, signal));

		cols.add(new ConditionColumn());
		cols.add(new JumpTargetColumn());
		cols.add(new DescriptionColumn());
		return cols;
	}
}