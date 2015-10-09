package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components;

import static com.google.common.base.Preconditions.*;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.AbstractDocumentListener;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.common.DoubleClickListener;
import de.uni_hannover.sra.minimax_simulator.ui.common.FillLayout;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.model.MemoryTableModel;

@Deprecated
public class MemoryView extends JPanel implements Disposable, ListSelectionListener
{
	private class FirstPageAction extends AbstractAction
	{
		FirstPageAction(String title, Icon icon)
		{
			super(title, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			_model.setPage(0);
			checkActions();
			updateAddressText();
		}
	}

	private class PrevPageAction extends AbstractAction
	{
		PrevPageAction(String title, Icon icon)
		{
			super(title, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			_model.setPage(_model.getPage() - 1);
			checkActions();
			updateAddressText();
		}
	}

	private class NextPageAction extends AbstractAction
	{
		NextPageAction(String title, Icon icon)
		{
			super(title, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			_model.setPage(_model.getPage() + 1);
			checkActions();
			updateAddressText();
		}
	}

	private class LastPageAction extends AbstractAction
	{
		LastPageAction(String title, Icon icon)
		{
			super(title, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			_model.setPage(_model.getPageCount() - 1);
			checkActions();
			updateAddressText();
		}
	}

	private class WheelListener implements MouseWheelListener
	{
		@Override
		public void mouseWheelMoved(MouseWheelEvent e)
		{
			int pageInc = e.getWheelRotation();
			int newPage = _model.getPage() + pageInc;

			newPage = Math.max(newPage, 0);
			newPage = Math.min(newPage, _model.getPageCount() - 1);

			_model.setPage(newPage);
			checkActions();
			updateAddressText();
		}
	}

	private final Action						_firstPage;
	private final Action						_prevPage;
	private final Action						_nextPage;
	private final Action						_lastPage;

	private final JTable						_table;
	private final JTextField					_addressField;
	private boolean								_observeSelections;
	private boolean								_observeTextField;

	private final MemoryTableModel				_model;

	private final List<MemorySelectionListener>	_listeners	= new ArrayList<MemorySelectionListener>(
																2);

	public MemoryView(final MachineMemory memory)
	{
		checkNotNull(memory);

		setLayout(createLayout());

		TextResource res = Application.getTextResource("project");

		_model = new MemoryTableModel(memory);
		_table = new JTable(_model)
		{
			@Override
			public Dimension getPreferredScrollableViewportSize()
			{
				Dimension size = super.getPreferredScrollableViewportSize();
				return new Dimension(size.width, Math.min(getPreferredSize().height,
					size.height));
			}
		};
		_table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_table.getSelectionModel().addListSelectionListener(this);
		_table.addMouseWheelListener(new WheelListener());
		_table.getTableHeader().setReorderingAllowed(false);

		_observeSelections = true;
		_observeTextField = true;

		Icons icons = Icons.getInstance();

		_firstPage = new FirstPageAction(null, icons.get(res.get("memtable.first.icon")));
		_prevPage = new PrevPageAction(null, icons.get(res.get("memtable.previous.icon")));
		_nextPage = new NextPageAction(null, icons.get(res.get("memtable.next.icon")));
		_lastPage = new LastPageAction(null, icons.get(res.get("memtable.last.icon")));

		_addressField = new JTextField();
		_addressField.setToolTipText(res.get("memtable.address.tip"));

		_firstPage.putValue(Action.SHORT_DESCRIPTION, res.get("memtable.first.tip"));
		_prevPage.putValue(Action.SHORT_DESCRIPTION, res.get("memtable.previous.tip"));
		_nextPage.putValue(Action.SHORT_DESCRIPTION, res.get("memtable.next.tip"));
		_lastPage.putValue(Action.SHORT_DESCRIPTION, res.get("memtable.last.tip"));

		add(new JButton(_firstPage), "cell 0 0");
		add(new JButton(_prevPage), "cell 1 0");
		add(_addressField, "cell 2 0");
		add(new JButton(_nextPage), "cell 3 0");
		add(new JButton(_lastPage), "cell 4 0");

//		add(UIUtils.wrapInTitledGroupScroller(_table, res.get("memtable.title")),
//			"cell 0 1 5 1, growx");

		JScrollPane scroller = new JScrollPane(_table);
		JPanel panel = new JPanel();
		// panel.setBorder(UIUtils.createGroupBorder(res.get("memtable.title")));
		panel.setLayout(FillLayout.INSTANCE);
		panel.add(scroller);

		add(panel, "cell 0 1 5 1, growx");

		setBorder(UIUtil.createGroupBorder(res.get("memtable.title")));
		// setLayout(FillLayout.INSTANCE);

		checkActions();

		_addressField.getDocument().addDocumentListener(new AbstractDocumentListener()
		{
			@Override
			public void documentChanged(DocumentEvent e)
			{
				if (!_observeTextField)
					return;

				String text = _addressField.getText().trim();
				if (text.isEmpty())
					return;

				if (text.startsWith("0x"))
					text = text.substring(2);

				try
				{
					int value = Integer.parseInt(text, 16);

					_observeSelections = false;
					selectAddress(value);
					_observeSelections = true;
				}
				catch (NumberFormatException nfe)
				{
					// Ignore malformed input
				}
			}
		});

		_table.addMouseListener(new DoubleClickListener()
		{
			@Override
			public void doubleClicked(MouseEvent e)
			{
				int row = _table.getSelectedRow();
				if (row == -1)
					return;

				// open edit dialog
				//new MemoryUpdateDialog(_model.rowToAddress(row), memory).setVisible(true);
			}
		});
	}

	void checkActions()
	{
		int page = _model.getPage();
		boolean first = page == 0;
		boolean last = page == _model.getPageCount() - 1;

		_firstPage.setEnabled(!first);
		_prevPage.setEnabled(!first);
		_nextPage.setEnabled(!last);
		_lastPage.setEnabled(!last);
	}

	private LayoutManager createLayout()
	{
		String cols = "[min!]rel[min!]unrel[min:pref:max,grow,fill]unrel[min!]rel[min!]";
		String rows = "[min!]unrel[p!]";
		return new MigLayout("", cols, rows);
	}

//
//	public int getSelectedAddress()
//	{
//		int row = _table.getSelectedRow();
//		if (row == -1)
//			return -1;
//
//		return _model.getPage() * _model.getPageSize() + row;
//	}

	void selectAddress(int address)
	{
		int page;
		int row;
		if (address < 0)
		{
			page = 0;
			row = 0;
		}
		else
		{
			if (address > _model.getMemory().getMaxAddress())
			{
				address = _model.getMemory().getMaxAddress();
			}

			page = address / _model.getPageSize();
			if (page >= _model.getPageCount())
			{
				page = _model.getPageCount() - 1;
				row = _model.getPageSize() - 1;
			}
			else
			{
				row = address % _model.getPageSize();
			}
		}

		_model.setPage(page);
		checkActions();

		if (row != _table.getSelectedRow())
			_table.setRowSelectionInterval(row, row);
	}

	@Override
	public void dispose()
	{
		_model.dispose();
		_listeners.clear();
	}

	void updateAddressText()
	{
		// Write selected address in text field -- maybe later

//		_observeTextField = false;
//
//		int row = _table.getSelectedRow();
//		if (row == -1)
//		{
//			_addressField.setText("");
//		}
//		else
//		{
//			int address = row + _model.getPage() * _model.getPageSize();
//
//			// check for selection of row that is not an address (if addresses are not multiples of
//			// page size)
//			if (address >= _model.getMemory().getMinAddress()
//				&& address <= _model.getMemory().getMaxAddress())
//			{
//				String text = _addressField.getText();
//				String newText = _model.formatHexAddress(address,
//					text.startsWith("0x") || text.startsWith("0X"));
//				if (!text.equals(newText))
//					_addressField.setText(newText);
//			}
//		}
//
//		_observeTextField = true;
	}

	// ListSelectionListener.valueChanged()
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		int row = _table.getSelectedRow();
		if (row != -1)
			fireMemoryAddressSelected(_model.rowToAddress(row));

		if (!_observeSelections || e.getValueIsAdjusting())
			return;

		updateAddressText();
	}

	public void addMemorySelectionListener(MemorySelectionListener listener)
	{
		if (!_listeners.contains(listener))
			_listeners.add(listener);
	}

	public void removeMemorySelectionListener(MemorySelectionListener listener)
	{
		_listeners.remove(listener);
	}

	private void fireMemoryAddressSelected(int address)
	{
		for (MemorySelectionListener listener : _listeners)
			listener.onAddressSelection(address);
	}
}