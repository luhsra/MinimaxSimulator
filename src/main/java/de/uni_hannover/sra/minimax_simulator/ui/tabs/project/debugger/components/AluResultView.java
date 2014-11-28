package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.components;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.model.AluResultViewModel;

public class AluResultView extends JPanel implements Disposable
{
	private final AluResultViewModel _model;
	private final JTable _table;

	public AluResultView(Simulation simulation)
	{
		_model = new AluResultViewModel(simulation);

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
		_table.setFillsViewportHeight(true);
		_table.getTableHeader().setReorderingAllowed(false);

		setLayout(createLayout());

		TextResource res = Application.getTextResource("debugger");

		add(UIUtil.wrapInTitledGroupScroller(_table, res.get("alu.title")));
	}

	private LayoutManager createLayout()
	{
		String cols = "[]";
		String rows = "[p!]";
		return new MigLayout("", cols, rows);
	}

	@Override
	public void dispose()
	{
		_model.dispose();
	}
}