package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public abstract class TwoSidedListSelectionListener implements ListSelectionListener
{
	private int	_myLastSelectionMin	= -1;
	private int	_myLastSelectionMax	= -1;

	private final ListSelectionModel _myModel;
	private final ListSelectionModel _otherModel;

	public TwoSidedListSelectionListener(ListSelectionModel myModel, ListSelectionModel otherModel)
	{
		_myModel = myModel;
		_otherModel = otherModel;
	}

	protected abstract void onSelection(int minIndex, int maxIndex);
	protected abstract void onEmptySelection();

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting())
		{
			int myNewSelectionMin = _myModel.getMinSelectionIndex();
			int myNewSelectionMax = _myModel.getMaxSelectionIndex();
			if (_myLastSelectionMin != myNewSelectionMin
				|| _myLastSelectionMax != myNewSelectionMax)
			{
				_myLastSelectionMin = myNewSelectionMin;
				_myLastSelectionMax = myNewSelectionMax;

				if (!_myModel.isSelectionEmpty())
				{
					if (!_otherModel.isSelectionEmpty())
						_otherModel.clearSelection();

					onSelection(myNewSelectionMin, myNewSelectionMax);

				}
				else
				{
					onEmptySelection();
				}
			}
		}
	}
}