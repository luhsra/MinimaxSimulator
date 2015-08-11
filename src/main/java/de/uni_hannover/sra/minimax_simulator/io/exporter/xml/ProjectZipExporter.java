package de.uni_hannover.sra.minimax_simulator.io.exporter.xml;

import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.io.ProjectExportException;
import de.uni_hannover.sra.minimax_simulator.io.ProjectExporter;
import de.uni_hannover.sra.minimax_simulator.io.importer.xml.ProjectZipImporter;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;

/**
 * A {@link ProjectExporter} that writes a project to a {@link File}. <br>
 * <br>
 * After a successful export, the written file will be a zip file containing several definitions in
 * form of XML files. <br>
 * <br>
 * The files produced by this class can be parsed by a {@link ProjectZipImporter} again, however
 * this only is guaranteed for files that are parsed by an importer that is of the same application
 * version as this exporter.
 * 
 * @author Martin
 * 
 */
@Deprecated
public class ProjectZipExporter implements ProjectExporter
{
	private final static Charset	charset	= Charset.forName("UTF-8");

	private final File				_file;

	/**
	 * Prepares a new exporter that will write an exported project as a zip archive to the given
	 * file.
	 * 
	 * @param file
	 *            the (non-null) file that will be (over-)written
	 */
	public ProjectZipExporter(File file)
	{
		_file = checkNotNull(file, "Invalid Null argument: file");
	}

	@Override
	public void exportProject(Project project) throws ProjectExportException
	{
		checkNotNull(project);

		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(_file);

			ZipOutputStream zos = new ZipOutputStream(fos);
			try
			{
				ZipEntry machineFile = new ZipEntry("machine.xml");
				zos.putNextEntry(machineFile);
				new MachineXmlExporter().write(new OutputStreamWriter(zos, charset),
					project.getMachineConfiguration());

				zos.closeEntry();

				ZipEntry userFile = new ZipEntry("user.xml");
				zos.putNextEntry(userFile);
				new UserXmlExporter().write(new OutputStreamWriter(zos, charset),
					project.getProjectConfiguration());

				zos.closeEntry();

				ZipEntry signalTableEntry = new ZipEntry("signal.xml");
				zos.putNextEntry(signalTableEntry);
				new SignalXmlExporter().write(new OutputStreamWriter(zos, charset),
					project.getSignalTable());
			}
			catch (IOException ioe)
			{
				throw new ProjectExportException(
					"I/O Error while exporting project into file: " + _file.getPath(),
					ioe);
			}
			finally
			{
				IOUtils.closeQuietly(zos);
			}
		}
		catch (FileNotFoundException e)
		{
			throw new ProjectExportException(
				"Target file for export of project cannot be opened: " + _file.getPath(),
				e);
		}
		finally
		{
			IOUtils.closeQuietly(fos);
		}
	}
}