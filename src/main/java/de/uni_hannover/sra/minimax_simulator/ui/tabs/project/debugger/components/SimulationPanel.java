package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.components;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationState;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UI;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;

@Deprecated
public class SimulationPanel extends JPanel implements Disposable, SimulationListener
{
	private class InitAction extends AbstractAction
	{
		public InitAction(Icon icon)
		{
			super(null, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			_simulation.init();
		}
	}

	private class ResetAction extends AbstractAction
	{
		public ResetAction(Icon icon)
		{
			super(null, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			_simulation.reset();
		}
	}

	private class StopAction extends AbstractAction
	{
		public StopAction(Icon icon)
		{
			super(null, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			_simulation.stop();
		}
	}

	private class StepAction extends AbstractAction
	{
		public StepAction(Icon icon)
		{
			super(null, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			_simulation.step();
		}
	}

	private class RunAction extends AbstractAction
	{

		public RunAction(Icon icon)
		{
			super(null, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			UIUtil.executeWorker(new Runnable()
			{
				// background task
				@Override
				public void run()
				{
					_simulation.run();
				}
			}, _res.get("simulation.wait.title"), _res.get("simulation.wait.message"),
				new Runnable()
				{
					@Override
					public void run()
					{
						// on cancel
						_simulation.pause();
					}
				});
		}
	}

	private final TextResource	_res				= Application.getTextResource("debugger");

	private final Action		_initAction;
	private final Action		_resetAction;
	private final Action		_stopAction;
	private final Action		_stepAction;
	private final Action		_runAction;

	private final JButton		_initResetButton;

	private final JLabel		_cyclesLabel;
	private final MessageFormat	_cyclesFormatHalted;
	private final MessageFormat	_cyclesFormatRead;
	private final MessageFormat	_cyclesFormatWrite;
	private final Object[]		_cyclesFormatParam	= new Object[1];

	private final Simulation	_simulation;

	private final ProgramPanel	_programPanel;

	private MigLayout createLayout()
	{
		String cols = "[]25px[]";
		String rows = "[min!]unrel[]unrel[]";

		return new MigLayout("left, top", cols, rows);
	}

	public SimulationPanel(Simulation simulation, SignalTable signalTable)
	{
		_simulation = simulation;
		_simulation.addSimulationListener(this);

		Icons icons = Icons.getInstance();
		TextResource res = Application.getTextResource("debugger");

		_initAction = new InitAction(icons.get(res.get("action.init.icon")));
		_resetAction = new ResetAction(icons.get(res.get("action.reset.icon")));
		_stopAction = new StopAction(icons.get(res.get("action.stop.icon")));
		_stepAction = new StepAction(icons.get(res.get("action.step.icon")));
		_runAction = new RunAction(icons.get(res.get("action.run.icon")));

		_cyclesFormatHalted = res.createFormat("cycles.label");
		_cyclesFormatRead = res.createFormat("cycles.read.label");
		_cyclesFormatWrite = res.createFormat("cycles.write.label");
		_cyclesLabel = new JLabel();
		_cyclesLabel.setFont(_cyclesLabel.getFont().deriveFont(15f));

		_programPanel = new ProgramPanel(_simulation, signalTable);

		setLayout(createLayout());

		_initResetButton = new JButton(_initAction);

		_initAction.putValue(Action.SHORT_DESCRIPTION, res.get("action.init.tip"));
		_resetAction.putValue(Action.SHORT_DESCRIPTION, res.get("action.reset.tip"));
		_stopAction.putValue(Action.SHORT_DESCRIPTION, res.get("action.stop.tip"));
		_stepAction.putValue(Action.SHORT_DESCRIPTION, res.get("action.step.tip"));
		_runAction.putValue(Action.SHORT_DESCRIPTION, res.get("action.run.tip"));

		// button row
		add(_initResetButton, "cell 0 0");
		add(new JButton(_stopAction), "cell 0 0");
		add(new JButton(_stepAction), "cell 1 0");
		add(new JButton(_runAction), "cell 1 0");

		// info row
		add(_cyclesLabel, "cell 0 1 2 1");

		// table row
		add(_programPanel, "cell 0 2 2 1");

		setBorder(UIUtil.createGroupBorder(res.get("simulation.title")));

		stateChangedEDT();
	}

	private void updateCyclesText()
	{
		String text;
		if (_simulation.getState() == SimulationState.OFF)
		{
			_cyclesFormatParam[0] = "---";
			text = _cyclesFormatHalted.format(_cyclesFormatParam);
		}
		else
		{
			MessageFormat format = _simulation.isResolved() ? _cyclesFormatWrite : _cyclesFormatRead;
			_cyclesFormatParam[0] = Integer.valueOf(_simulation.getCyclesCount());
			text = format.format(_cyclesFormatParam);
		}
		_cyclesLabel.setText(text);
	}

	@Override
	public void dispose()
	{
		_simulation.removeSimulationListener(this);
		_programPanel.dispose();
	}

	private void stateChangedEDT()
	{
		switch (_simulation.getState())
		{
			case IDLE:
				_resetAction.setEnabled(true);
				_initResetButton.setAction(_resetAction);
				_initAction.setEnabled(false);
				_stopAction.setEnabled(true);

				if (_simulation.isHalted())
				{
					_stepAction.setEnabled(false);
					_runAction.setEnabled(false);
				}
				else
				{
					_stepAction.setEnabled(true);
					_runAction.setEnabled(true);
				}
				break;

			case OFF:
				_initAction.setEnabled(true);
				_initResetButton.setAction(_initAction);
				_resetAction.setEnabled(false);
				_stopAction.setEnabled(false);
				_stepAction.setEnabled(false);
				_runAction.setEnabled(false);
				_initResetButton.requestFocusInWindow();
				break;

			case RUNNING:
//				_initAction.setEnabled(false);
//				_resetAction.setEnabled(false);
//				_stopAction.setEnabled(false);
//				_stepAction.setEnabled(false);
//				_runAction.setEnabled(false);
				break;

			default:
				throw new AssertionError();
		}
		updateCyclesText();
	}

	@Override
	public void stateChanged(SimulationState state)
	{
		UI.invokeInEDT(new Runnable()
		{
			@Override
			public void run()
			{
				stateChangedEDT();
			}
		});
	}
}