package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.overview.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.JPanel;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.Config;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.JCollapsablePanel;

@Deprecated
class MachineSidePanel extends JPanel implements Disposable
{
	private final RegisterTable	_registerTable;
	private final AluTable		_aluTable;
	//private final MemoryView	_memoryView;

	public MachineSidePanel(MachineConfiguration config, MachineMemory memory)
	{
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		// c.insets = new Insets(4, 4, 4, 4);
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;

		_registerTable = new RegisterTable(config);
		_aluTable = new AluTable(config);
		//_memoryView = new MemoryView(memory);

		TextResource res = Application.getTextResource("project");
		Icon collapsedIcon = Icons.getInstance().get(res.get("overview.panel.icon.collapsed"));
		Icon expandedIcon = Icons.getInstance().get(res.get("overview.panel.icon.expanded"));

		add(new JCollapsablePanel(res.get("overview.panel.alu"), collapsedIcon, expandedIcon,
			_aluTable, Config.EDITOR_ALU_EXPANDED), c);
		add(new JCollapsablePanel(res.get("overview.panel.register"), collapsedIcon, expandedIcon,
			_registerTable, Config.EDITOR_REGISTER_EXPANDED), c);
//		add(new JCollapsablePanel(res.get("panel.memory"), collapsedIcon, expandedIcon,
//			_memoryView, Config.EDITOR_MEMORY_EXPANDED), c);

		// Bottom Spacer
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		add(new JPanel(), c);
	}

	@Override
	public void dispose()
	{
		_registerTable.dispose();
		_aluTable.dispose();
	}
}