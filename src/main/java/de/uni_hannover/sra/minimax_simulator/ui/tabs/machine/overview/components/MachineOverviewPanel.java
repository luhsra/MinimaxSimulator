package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.overview.components;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.Machine;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.common.FillLayout;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.JFastScrollPane;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.JScrollablePanel;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.JScrollablePanel.ScrollableSizeHint;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.MachineSchematics;

public class MachineOverviewPanel extends JPanel implements Disposable
{
	private final Machine _machine;
	private final MachineSchematics _machineView;

//	private final MachineSidePanel _sidePanel;

	public MachineOverviewPanel(final Project project)
	{
		this(project.getMachineConfiguration(), project.getMachine());
	}

	public MachineOverviewPanel(final MachineConfiguration config, final Machine machine)
	{
		_machine = machine;
		
		// Create render component first
		_machineView = new MachineSchematics(_machine);
		//_machineView.setBorder(BorderFactory.createLoweredBevelBorder());

		// Update the render environment to the better environment for font measuring etc.
		_machine.getDisplay().setRenderEnvironment(_machineView.getRenderEnvironment());
		_machine.getDisplay().addMachineDisplayListener(_machineView);

//		_sidePanel = new MachineSidePanel(config, _machine.getMemory());
//		JScrollPane sidePanelScroller = new JFastScrollPane(_sidePanel);

//		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, sidePanelScroller, machineScroller);
//		split.setDividerLocation(Config.EDITOR_SLIDER_POSITION);
		JScrollablePanel panel = new JScrollablePanel();
		panel.setScrollableWidth(ScrollableSizeHint.STRETCH);
		panel.setScrollableHeight(ScrollableSizeHint.STRETCH);
		panel.setLayout(FillLayout.INSTANCE);
		panel.add(_machineView);
//		add(split);
		JScrollPane machineScroller = new JFastScrollPane(panel);
		panel.setLayout(new MigLayout("center", "[]", "[]"));

		setLayout(FillLayout.INSTANCE);
		add(machineScroller);
	}

	@Override
	public void dispose()
	{
		_machine.getDisplay().removeMachineDisplayListener(_machineView);
//		_sidePanel.dispose();
	}
}