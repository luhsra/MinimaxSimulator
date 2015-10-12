package de.uni_hannover.sra.minimax_simulator.ui;

import static com.google.common.base.Preconditions.*;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.model.user.WorkspaceListener;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.util.MenuBuilder;
import de.uni_hannover.sra.minimax_simulator.ui.util.MenuBuilder.MenuAppender;
import javafx.application.Platform;

@Deprecated
public class MainWindow extends JFrame implements WorkspaceListener
{
	private final TextResource		_res;
	private final Workspace			_ws;
	private final WorkspacePanel	_wsPanel;

	private final String			_versionString;

	public MainWindow(Workspace ws, TextResource appRes, TextResource menuRes)
	{
		_res = checkNotNull(appRes, "application text resource is null");
		_ws = checkNotNull(ws, "workspace is null");

		_versionString = Main.getVersionString();

		List<Image> images = new ArrayList<Image>(2);
		images.add(Icons.getInstance().get(appRes.get("application.icon-big")).getImage());
		images.add(Icons.getInstance().get(appRes.get("application.icon-small")).getImage());

		setTitle(appRes.format("title", _versionString));
		setIconImages(images);

		// Prevent automatic window closing in the case of unsaved data,
		// instead handle the request with a WindowListener
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setLayout(new BorderLayout());

		// set up menu
		initMenu(menuRes);

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				Platform.exit();
			}

		    @Override
			public void windowClosed(WindowEvent e)
		    {
				_wsPanel.dispose();
		    }
		});

		_wsPanel = new WorkspacePanel(ws);
		add(_wsPanel, BorderLayout.CENTER);

		ws.addListener(this);

		// Set initial size -- since there is no content in the beginning,
		// the window would shrink to nothing
		setSize(1024, 700);
//		pack();

		// center on screen
		setLocationRelativeTo(null);
	}

	public WorkspacePanel getWorkspacePanel()
	{
		return _wsPanel;
	}

	private void initMenu(TextResource menuRes)
	{
		JMenuBar bar = new JMenuBar();

		MenuBuilder m = new MenuBuilder(menuRes);

		MenuAppender ma = m.buildMenu("project");
		ma.appendSeparator();
		bar.add(ma.menu());

		JMenu machineMenu = ma.menu();

		ma = m.buildMenu("view");
		ma.appendSeparator();
		ma.appendItem(machineMenu);
		ma.appendSeparator();
		bar.add(ma.menu());

		add(bar, BorderLayout.NORTH);
	}

	private String getProjectName()
	{
		File file = _ws.getCurrentProjectFile();
		if (file == null)
		{
			return _res.get("project.unnamed");
		}
		return file.getName();
	}

	private void setTitleInEDT(final String title)
	{
		UI.invokeInEDT(new Runnable()
		{
			@Override
			public void run()
			{
				setTitle(title);
			}
		});
	}

	@Override
	public void onProjectOpened(Project project)
	{
		setTitleInEDT(_res.format("title.open-project", _versionString, getProjectName()));
	}

	@Override
	public void onProjectSaved(Project project)
	{
		setTitleInEDT(_res.format("title.open-project", _versionString, getProjectName()));
	}

	@Override
	public void onProjectClosed(Project project)
	{
		setTitleInEDT(_res.format("title", _versionString));
	}

	@Override
	public void onProjectDirty(Project project)
	{
		setTitleInEDT(_res.format("title.open-unsaved-project", _versionString,
			getProjectName()));
	}
}
