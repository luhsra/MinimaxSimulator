package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.io.File;

import com.google.common.base.Throwables;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;

public class ProjectSaveTo {

	private final TextResource	res;

	public ProjectSaveTo() {
		res	= Main.getTextResource("application");
	}

	public boolean save(File file) {
		if (file == null) {
			return false;
		}

		if (file.getName().lastIndexOf('.') == -1) {
			// append ending
			file = new File(file.getPath() + ".zip");
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
					Main.getWorkspace().closeProject();		// TODO: really closing project if saving didn't work?
					throw Throwables.propagate(e);
				}
			}
		}, res.get("wait.title"), res.format("wait.project.save", file.getName()));
		return true;
	}

}