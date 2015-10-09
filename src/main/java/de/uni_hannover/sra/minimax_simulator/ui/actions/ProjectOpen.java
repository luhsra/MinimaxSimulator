package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.io.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UI;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;

@Deprecated
public class ProjectOpen extends AbstractAction
{
	private final static TextResource	res	= Application.getTextResource("application");

	private final static Logger			log	= Logger.getLogger(ProjectOpen.class.getName());

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (!UIUtil.confirmCloseProject())
			return;

		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setFileFilter(new FileNameExtensionFilter(
			res.get("project.filedescription"), "zip"));

		File lastFolder = Application.getWorkspace().getLastProjectFolder();
		if (lastFolder != null && lastFolder.exists())
		{
			chooser.setCurrentDirectory(lastFolder);	
		}

		int selection = chooser.showOpenDialog(Application.getMainWindow());
		if (selection != JFileChooser.APPROVE_OPTION)
			return;

		final File file = chooser.getSelectedFile();

		UIUtil.executeWorker(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Application.getWorkspace().openProject(file);

					UI.invokeNow(new Runnable()
					{
						@Override
						public void run()
						{
							Application.getMainWindow().getWorkspacePanel().openDefaultTabs();
						}
					});
				}
				catch (ProjectImportException e)
				{
					Application.getWorkspace().closeProject();
					JOptionPane.showMessageDialog(Application.getMainWindow(),
						res.get("load-error.message"), res.get("load-error.title"),
						JOptionPane.ERROR_MESSAGE);
					log.log(Level.WARNING, e.getMessage(), e);
				}
			}
		}, res.get("wait.title"), res.format("wait.project.load", file.getName()));
	}
}