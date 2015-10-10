package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UI;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXDialog;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.MachineSchematics;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;

@Deprecated
public class ProjectExportSchematics extends ProjectAction
{
	private final TextResource	res	= Main.getTextResource("application");

	@Override @Deprecated
	public void actionPerformed(ActionEvent e)
	{
/*		// Let the user select an image file
		final File imageFile = chooseFile(new File(res.get("project.export.file")));
		if (imageFile == null)
			return;

		if (imageFile.exists() && !UIUtil.confirmOverwriteFile(imageFile.getName()))
			return;

		int dot = imageFile.getName().lastIndexOf('.');
		final String ending = imageFile.getName().substring(dot + 1);

		// Create a display of the existing machine
		Project project = Main.getWorkspace().getProject();
		MachineSchematics schematics = new MachineSchematics(project.getMachine());

		// Paint it to a buffered image
		Dimension dim = schematics.getPreferredSize();
		schematics.setSize(dim);
		final BufferedImage image = new BufferedImage(dim.width, dim.height,
			BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		schematics.paint(g);
		g.dispose();

		UIUtil.executeWorker(new Runnable()
		{
			@Override
			public void run()
			{
				// Write the image to disk
				try
				{
					if (!ImageIO.write(image, ending, imageFile))
					{
						error(imageFile.getPath());
						return;
					}
				}
				catch (IOException e1)
				{
					// (almost) ignore
					e1.printStackTrace();
					error(imageFile.getPath());
					return;
				}

				try
				{
					Desktop.getDesktop().open(imageFile);
				}
				catch (Exception e)
				{
					// (almost) ignore
					e.printStackTrace();
				}
			}

		}, res.get("wait.title"), res.get("wait.image-export"));	*/
	}

	private void error(String filename) {
		String error = res.format("project.export.error.message", filename, res.get("project.export.error.message.ioex"));
		String title = res.get("project.export.error.title");
		//JOptionPane.showMessageDialog(Application.getMainWindow(), error, title,
		//	JOptionPane.ERROR_MESSAGE);
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
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setFileFilter(new FileNameExtensionFilter(
			res.get("project.imagefile.description"), "png", "jpg"));

		if (defaultFile != null)
			chooser.setCurrentDirectory(defaultFile.getParentFile());

		int button = -1; //chooser.showSaveDialog(Application.getMainWindow());
		if (button != JFileChooser.APPROVE_OPTION)
			return null;

		File selectedFile = chooser.getSelectedFile();
		if (selectedFile != null && selectedFile.getName().lastIndexOf('.') == -1)
		{
			// append ending
			selectedFile = new File(selectedFile.getPath() + ".png");
		}

		return selectedFile;
	}

	public void export(File f) {
		if (f == null) {
			return;
		}

		if (f.getName().lastIndexOf(".") == -1) {
			f = new File(f.getPath() + ".png");
		}

		final File imageFile = f;

		int dot = imageFile.getName().lastIndexOf('.');
		final String ending = imageFile.getName().substring(dot + 1);

		// Create a display of the existing machine
		Project project = Main.getWorkspace().getProject();
		MachineSchematics schematics = new MachineSchematics(project.getMachine());

		// Paint it to a buffered image
		//Dimension dim = new Dimension( (int) schematics.getWidth(), (int) schematics.getHeight()); //schematics.getPreferredSize();
		//schematics.setSize(dim);
		//final BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
		final WritableImage image = schematics.snapshot(null, null);  //new WritableImage(dim.width, dim.height);
		//Graphics g = image.createGraphics();
		//schematics.paint(g);
		//g.dispose();

		UIUtil.executeWorker(new Runnable() {
			@Override
			public void run() {
				// Write the image to disk
				try {
					// TODO: pure JavaFX
					if (!ImageIO.write(SwingFXUtils.fromFXImage(image, null), ending, imageFile)) {
						error(imageFile.getPath());
						return;
					}
				} catch (IOException e1) {
					// (almost) ignore
					e1.printStackTrace();
					error(imageFile.getPath());
					return;
				}

				try {
					Desktop.getDesktop().open(imageFile);
				} catch (Exception e) {
					// (almost) ignore
					e.printStackTrace();
				}
			}
		}, res.get("wait.title"), res.get("wait.image-export"));
	}
}
