package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.overview.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.tabbed.JPersistentTabPanel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register.RegisterExtensionsTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register.models.MachineRegisterTableModel;

@Deprecated
class RegisterTable extends JTable implements Disposable
{
	private final TextResource				res	= Application.getTextResource("machine");

	private final MachineConfiguration		_conf;
	private final MachineRegisterTableModel	_model;

	public RegisterTable(MachineConfiguration machine)
	{
		_conf = machine;
		_model = new MachineRegisterTableModel(machine);
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
						projectPanel.selectTab("register", true);

						int row = rowAtPoint(e.getPoint());
						if (row == -1)
							return;

						RegisterExtensionsTab tab = (RegisterExtensionsTab) projectPanel.getTab("register");
						tab.getComponent().setSelectedExtendedRegister(row);
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
			return res.get("register.description.none");
		return tip;
	}

	@Override
	public void dispose()
	{
		_conf.removeMachineConfigListener(_model);
	}
}
