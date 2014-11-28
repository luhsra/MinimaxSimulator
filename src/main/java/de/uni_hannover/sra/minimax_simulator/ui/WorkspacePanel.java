package de.uni_hannover.sra.minimax_simulator.ui;

import static com.google.common.base.Preconditions.*;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.model.user.WorkspaceListener;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.common.FillLayout;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.tabbed.JPersistentTabPanel;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.tabbed.Tab;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.tabbed.TabFactory;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.alu.AluTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux.MuxTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.overview.OverviewTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register.RegisterExtensionsTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.DebuggerTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.MemoryTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.SignalTab;

public class WorkspacePanel extends JPanel implements WorkspaceListener, Disposable
{
	private final Workspace		_workspace;

	private final CardLayout	_cardLayout;
	private final JPanel		_editorContainer;

	private JPersistentTabPanel	_projectPanel;

	public WorkspacePanel(Workspace ws)
	{
		_workspace = checkNotNull(ws);
		ws.addListener(this);

		// TODO: list last opened projects

		_cardLayout = new CardLayout();
		setLayout(_cardLayout);

		_editorContainer = new JPanel();
		_editorContainer.setLayout(FillLayout.INSTANCE);

		LastOpenProjectsView his = new LastOpenProjectsView();

		add(_editorContainer, "editor");
		add(his, "history");

		_cardLayout.show(this, "editor");
	}

	public JPersistentTabPanel getProjectPanel()
	{
		return _projectPanel;
	}

	@Override
	public void onProjectOpened(final Project project)
	{
		UI.invokeInEDT(new Runnable()
		{
			@Override
			public void run()
			{
				_projectPanel = buildPanel(project);
				_editorContainer.removeAll();
				_editorContainer.add(_projectPanel);
				_editorContainer.revalidate();

				_cardLayout.show(WorkspacePanel.this, "editor");
			}
		});
	}

	@Override
	public void onProjectSaved(Project project)
	{
	}

	@Override
	public void onProjectClosed(Project project)
	{
		UI.invokeInEDT(new Runnable()
		{
			@Override
			public void run()
			{
				_cardLayout.show(WorkspacePanel.this, "history");
				_editorContainer.removeAll();
				_projectPanel = null;
			}
		});
	}

	@Override
	public void onProjectDirty(Project project)
	{
	}

	private JPersistentTabPanel buildPanel(final Project project)
	{
		JPersistentTabPanel panel = new JPersistentTabPanel();

		// Bug: Nimbus doesn't draw the bottom border with this tab layout policy.
		//panel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		panel.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

		panel.registerTabFactory("overview", new TabFactory()
		{
			@Override
			public Tab buildTab()
			{
				return new OverviewTab(project);
			}
		});
		panel.registerTabFactory("alu", new TabFactory()
		{
			@Override
			public Tab buildTab()
			{
				return new AluTab(project);
			}
		});
		panel.registerTabFactory("register", new TabFactory()
		{
			@Override
			public Tab buildTab()
			{
				return new RegisterExtensionsTab(project);
			}
		});
		panel.registerTabFactory("mux", new TabFactory()
		{
			@Override
			public Tab buildTab()
			{
				return new MuxTab(project);
			}
		});
		panel.registerTabFactory("signal", new TabFactory()
		{
			@Override
			public Tab buildTab()
			{
				return new SignalTab(project);
			}
		});
		panel.registerTabFactory("debugger", new TabFactory()
		{
			@Override
			public Tab buildTab()
			{
				return new DebuggerTab(project);
			}
		});
		panel.registerTabFactory("memory", new TabFactory()
		{
			@Override
			public Tab buildTab()
			{
				return new MemoryTab(project);
			}
		});

		return panel;
	}

	public void openDefaultTabs()
	{
		_projectPanel.addTab("overview", true);
		//_projectPanel.addTab("alu", false);
		//_projectPanel.addTab("register", false);
		//_projectPanel.addTab("mux", false);
		_projectPanel.addTab("signal", false);
		_projectPanel.addTab("memory", false);
		_projectPanel.addTab("debugger", false);
	}

	@Override
	public void dispose()
	{
		_workspace.removeListener(this);
	}
}