package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.common.base.Throwables;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.model.user.WorkspaceListener;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;

public class ProjectSaveTo extends AbstractAction implements WorkspaceListener
{
	private final TextResource	res	= Main.getTextResource("application");

	public ProjectSaveTo()
	{
		setEnabled(false);
		Main.getWorkspace().addListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		save(null);
	}

	public boolean save(File file)
	{
		if (file == null)
		{
			file = chooseFile(Main.getWorkspace().getCurrentProjectFile());

			if (file == null)
				return false;

			if (file.exists() && !UIUtil.confirmOverwriteFile(file.getName()))
				return false;
		}

		final File fileToSave = file;

		UIUtil.executeWorker(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Main.getWorkspace().saveProject(fileToSave);
				}
				catch (Exception e)
				{
					Main.getWorkspace().closeProject();
					throw Throwables.propagate(e);
				}
			}
		}, res.get("wait.title"), res.format("wait.project.save", file.getName()));
		return true;
	}

	protected File chooseFile(File defaultFile)
	{
/*		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setFileFilter(new FileNameExtensionFilter(
			res.get("project.filedescription"), "zip"));

		if (defaultFile != null)
		{
			File folder = defaultFile.getParentFile();
			if (folder != null && folder.exists())
			{
				chooser.setCurrentDirectory(folder);	
			}
		}

		int button = chooser.showSaveDialog(Main.getMainWindow());
		if (button != JFileChooser.APPROVE_OPTION)
			return null;

		File selectedFile = chooser.getSelectedFile();
		if (selectedFile != null && selectedFile.getName().lastIndexOf('.') == -1)
		{
			// append ending
			selectedFile = new File(selectedFile.getPath() + ".zip");
		}

		return selectedFile;*/
		return defaultFile;
	}

	@Override
	public void onProjectOpened(Project project)
	{
		setEnabled(true);
	}

	@Override
	public void onProjectSaved(Project project)
	{
	}

	@Override
	public void onProjectClosed(Project project)
	{
		setEnabled(false);
	}

	@Override
	public void onProjectDirty(Project project)
	{
	}
}