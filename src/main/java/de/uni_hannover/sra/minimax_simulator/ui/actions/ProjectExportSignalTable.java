package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.io.exporter.csv.SignalCsvExporter;
import de.uni_hannover.sra.minimax_simulator.io.exporter.csv.SignalHtmlExporter;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UI;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXDialog;
import javafx.scene.control.Alert;

@Deprecated
public class ProjectExportSignalTable extends ProjectAction {
	
	private final TextResource	res	= Main.getTextResource("application");

	@Override @Deprecated
	public void actionPerformed(ActionEvent e)
	{
		final Project project = Main.getWorkspace().getProject();

		// Let the user select a file
		final File file = chooseFile(new File(res.get("project.export.file")));
		if (file == null)
			return;

		if (file.exists() && !UIUtil.confirmOverwriteFile(file.getName()))
			return;

		UIUtil.executeWorker(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					SignalTable table = project.getSignalTable();
					SignalConfiguration config = project.getSignalConfiguration();
					if (file.getName().endsWith(".csv"))
					{
						new SignalCsvExporter(file).exportSignalTable(table, config);
					}
					else if (file.getName().endsWith(".html"))
					{
						new SignalHtmlExporter(file).exportSignalTable(table, config);
					}
					else
					{
						error(file.getPath(), res.get("project.export.error.message.wrongformat"));
					}
				}
				catch (IOException e1)
				{
					// (almost) ignore
					e1.printStackTrace();

					error(file.getPath(), res.get("project.export.error.message.ioex"));
				}
			}

		}, res.get("wait.title"), res.get("wait.signal-export"));
	}

	private void error(String filename, String reason) {
		String error = res.format("project.export.error.message", filename, reason);
		String title = res.get("project.export.error.title");
		//JOptionPane.showMessageDialog(Application.getMainWindow(), error, title,
		//		JOptionPane.ERROR_MESSAGE);
		UI.invokeInFAT(new Runnable() {
			@Override
			public void run() {
				new FXDialog(Alert.AlertType.ERROR, title, error).showAndWait();
			}
		});
	}

	@Deprecated
	protected File chooseFile(File defaultFile)
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileNameExtensionFilter(
			res.get("project.signalfile.description"), "csv", "html"));

		if (defaultFile != null)
			chooser.setCurrentDirectory(defaultFile.getParentFile());

		int button = chooser.showSaveDialog(Application.getMainWindow());
		if (button != JFileChooser.APPROVE_OPTION)
			return null;

		File selectedFile = chooser.getSelectedFile();
		if (selectedFile != null && selectedFile.getName().lastIndexOf('.') == -1)
		{
			// append ending
			selectedFile = new File(selectedFile.getPath() + ".html");
		}

		return selectedFile;
	}

	public void exportSignalTable(File f) {
		final Project project = Main.getWorkspace().getProject();

		if (f == null) {
			return;
		}

		if (f.getName().lastIndexOf(".") == -1) {
			// append ending
			f = new File(f.getPath() + ".html");
		}

		final File file = f;

		UIUtil.executeWorker(new Runnable() {
			@Override
			public void run() {
				try {
					SignalTable table = project.getSignalTable();
					SignalConfiguration config = project.getSignalConfiguration();
					if (file.getName().endsWith(".csv")) {
						new SignalCsvExporter(file).exportSignalTable(table, config);
					}
					else if (file.getName().endsWith(".html")) {
						new SignalHtmlExporter(file).exportSignalTable(table, config);
					}
					else {
						error(file.getPath(), res.get("project.export.error.message.wrongformat"));
					}
				} catch (IOException e1) {
					// (almost) ignore
					e1.printStackTrace();

					error(file.getPath(), res.get("project.export.error.message.ioex"));
				}
			}

		}, res.get("wait.title"), res.get("wait.signal-export"));
	}
}
