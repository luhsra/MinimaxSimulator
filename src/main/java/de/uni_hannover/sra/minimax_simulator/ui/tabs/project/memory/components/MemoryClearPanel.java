package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;

public class MemoryClearPanel extends JPanel
{
	private class ClearAction extends AbstractAction
	{
		ClearAction(String label, Icon icon)
		{
			super(label, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int result = JOptionPane.showConfirmDialog(Application.getMainWindow(),
				_res.get("confirm.message"),
				_res.get("confirm.title"), JOptionPane.OK_CANCEL_OPTION);

			if (result != JOptionPane.OK_OPTION)
				return;

			_memory.getMemoryState().zero();
		}
	}

	private final MachineMemory	_memory;

	private final Action		_clearAction;

	private final TextResource	_res;

	private LayoutManager createLayout()
	{
		String cols = "[min!,left]";
		String rows = "[min!]";
		return new MigLayout("", cols, rows);
	}

	public MemoryClearPanel(MachineMemory memory)
	{
		_memory = memory;

		_res = Application.getTextResource("project").using("memory.clear");

		setLayout(createLayout());

		Icons icons = Icons.getInstance();
		_clearAction = new ClearAction(_res.get("button.label"),
			icons.get(_res.get("button.icon")));

		add(new JButton(_clearAction), "cell 0 0");

		setBorder(UIUtil.createGroupBorder(_res.get("title")));
	}
}