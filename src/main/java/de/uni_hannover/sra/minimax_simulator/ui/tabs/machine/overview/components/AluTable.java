package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.overview.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.tabbed.JPersistentTabPanel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.alu.AluTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.overview.model.AluTableModel;

class AluTable extends JTable implements Disposable
{
	private MachineConfiguration	_conf;
	private AluTableModel			_model;

	public AluTable(MachineConfiguration machine)
	{
		_conf = machine;
		_model = new AluTableModel(machine, Application.getTextResource("alu"));
		setModel(_model);
		_conf.addMachineConfigListener(_model);

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
				{
					JPersistentTabPanel projectPanel = Application.getMainWindow().getWorkspacePanel().getProjectPanel();
					if (projectPanel != null)
					{
						// switch to alu editor tab
						projectPanel.selectTab("alu", true);

						int row = rowAtPoint(e.getPoint());
						if (row == -1)
							return;

						AluTab aluTab = (AluTab) projectPanel.getTab("alu");
						aluTab.getComponent().setSelectedAddedOperation(row);
					}
				}
			}
		});
	}

	@Override
	public String getToolTipText(MouseEvent m)
	{
		// TODO: no tooltip for empty string
		int row = rowAtPoint(m.getPoint());
		if (row == -1)
			return null;
		String tip = _model.getTooltip(row);
		if (tip == null || tip.isEmpty())
			return null;
		return tip;
	}

	@Override
	public void dispose()
	{
		_conf.removeMachineConfigListener(_model);
	}
}
