package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.components;

import java.awt.LayoutManager;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.Machine;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.common.DoubleClickListener;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.model.RegisterViewModel;

@Deprecated
public class RegisterView extends JPanel implements Disposable
{
	// private final MachineConfiguration _config;
	private final RegisterViewModel	_model;
	private final JTable			_table;

	public RegisterView(Machine machine, MachineConfiguration config,
			final Simulation simulation)
	{
		// _config = config;
		_model = new RegisterViewModel(config, simulation);

		_table = new JTable(_model);
		_table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// _table.setShowGrid(true);
		// _table.setBorder(BorderFactory.createEtchedBorder());
		_table.setFillsViewportHeight(true);
		_table.getTableHeader().setReorderingAllowed(false);

		setLayout(createLayout());

		// TODO: get this from constructor
		TextResource res = Application.getTextResource("debugger");

		add(UIUtil.wrapInTitledGroupScroller(_table, res.get("register.title")));
/*
		_table.addMouseListener(new DoubleClickListener()
		{
			@Override
			public void doubleClicked(MouseEvent e)
			{
				if (simulation.getState() != SimulationState.IDLE)
					return;

				final int row = _table.getSelectedRow();
				if (row == -1)
					return;

				RegisterExtension register = _model.getRegister(row);
				new RegisterUpdateDialog(register, _model.getValue(row))
				{
					@Override
					protected void setValue(int value)
					{
						_model.setValue(row, value);
					}
				}.setVisible(true);
			}
		}); */
	}

	private LayoutManager createLayout()
	{
		String cols = "[]";
		String rows = "[]";
		return new MigLayout("", cols, rows);
	}

	@Override
	public void dispose()
	{
		_model.dispose();
	}
}