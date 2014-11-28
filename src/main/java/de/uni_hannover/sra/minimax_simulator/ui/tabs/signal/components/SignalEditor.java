package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.components;

import static com.google.common.base.Preconditions.*;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellEditor;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.JWideComboBox;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.SignalColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.SignalTableModel;

public class SignalEditor extends AbstractCellEditor implements TableCellEditor
{
	// When dropping Java 6 support, change to JComboBox<SignalValue>
	private final JComboBox			_box;
	private final SignalTableModel	_signalTableModel;

	public SignalEditor(SignalTableModel model)
	{
		_signalTableModel = checkNotNull(model);

		_box = new JWideComboBox()
		{
//			@Override
//			public void processFocusEvent(FocusEvent fe)
//			{
//				super.processFocusEvent(fe);
//				Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
//
//				if (isDisplayable() && fe.getID() == FocusEvent.FOCUS_GAINED
//					&& focusOwner == this && !isPopupVisible())
//				{
//					showPopup();
//				}
//			}
		};
		_box.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
		_box.setBorder(BorderFactory.createEmptyBorder());
		_box.addPopupMenuListener(new PopupMenuListener()
		{
			@Override
			public void popupMenuCanceled(PopupMenuEvent e)
			{
				fireEditingCanceled();
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
			{
				fireEditingStopped();
			}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e)
			{
			}
		});
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent)
	{
		if (anEvent instanceof MouseEvent)
		{
			MouseEvent e = (MouseEvent) anEvent;
			return e.getID() != MouseEvent.MOUSE_DRAGGED;
		}
		return true;
	}

	@Override
	public boolean isCellEditable(EventObject anEvent)
	{
		if (anEvent instanceof MouseEvent)
		{
			return ((MouseEvent) anEvent).getClickCount() >= 2;
		}
		// Keyboard events
		return true;
	}

	@Override
	public Object getCellEditorValue()
	{
		return _box.getSelectedItem();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		SignalColumn col = (SignalColumn) _signalTableModel.getColumn(column);

		List<SignalValue> valueList = col.getSignal().getValues();
		SignalValue selected = (SignalValue) value;

		_box.setRenderer(new SignalEditorRenderer(col));
		_box.setModel(new SignalIntegerComboBoxModel(valueList, selected));
		return _box;
	}

	private class SignalIntegerComboBoxModel extends AbstractListModel implements
			ComboBoxModel
	{
		private List<SignalValue>	_values;
		private SignalValue			_selected;

		SignalIntegerComboBoxModel(List<SignalValue> values, SignalValue selected)
		{
			_values = values;
			_selected = selected;
		}

		@Override
		public int getSize()
		{
			return _values.size();
		}

		@Override
		public Object getElementAt(int index)
		{
			return _values.get(index);
		}

		@Override
		public void setSelectedItem(Object anItem)
		{
			_selected = (SignalValue) anItem;
			fireContentsChanged(this, -1, -1);
		}

		@Override
		public Object getSelectedItem()
		{
			return _selected;
		}
	}
}