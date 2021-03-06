package de.uni_hannover.sra.minimax_simulator.io.importer.json;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.io.exporter.json.ProjectZipExporter;
import de.uni_hannover.sra.minimax_simulator.io.importer.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.io.importer.ProjectImporter;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@link ProjectImporter} that reads a project from a {@link File}. <br>
 * <br>
 * This importer can parse files that are generated by a {@link ProjectZipExporter}. However, this
 * only can be guaranteed for files that are generated by an exporter that is of the same
 * application version as this importer.
 * 
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class ProjectZipImporter implements ProjectImporter {

    private final File file;

    /**
     * Prepares a new importer that will read a project as a zip archive from the given file. <br>
     * However, the file is not checked for existence and read accessibility until the actual import
     * process.
     *
     * @param file
     *            the (non-null) file that will be read
     */
    public ProjectZipImporter(File file) {
        this.file = checkNotNull(file, "Invalid Null argument: file");
    }

    @Override
    public Project importProject() throws ProjectImportException {
        ZipFile zip = null;
        try {
            zip = new ZipFile(file);

            MachineConfiguration machineConfiguration = getMachineConfiguration(zip);
            SignalTable signalTable = getSignalTable(zip);

            return new Project(machineConfiguration, signalTable);
        } catch (ZipException e) {
            throw new ProjectImportException("Invalid zip file: " + file.getPath(), e);
        } catch (IOException e) {
            throw new ProjectImportException("I/O error while reading zip file: " + file.getPath(), e);
        } catch (JSONException e) {
            throw new ProjectImportException("Error while parsing JSON string!", e);
        }
        finally {
            IOUtils.closeQuietly(zip);
        }
    }

    /**
     * Gets the {@link MachineConfiguration} from {@link MachineJsonImporter}.
     *
     * @param zip
     *            the ZIP containing the {@code machine.json}
     * @return
     *            the imported {@code MachineConfiguration}
     * @throws IOException
     *            thrown if the {@code machine.json} can not be found
     * @throws JSONException
     *            thrown if there is an error during parsing the JSON string
     * @throws ProjectImportException
     *            thrown if there is any other error during import
     */
    private static MachineConfiguration getMachineConfiguration(ZipFile zip) throws IOException, ProjectImportException {
        ZipEntry machineEntry = zip.getEntry("machine.json");
        if (machineEntry == null) {
            throw new FileNotFoundException("Missing machine.json in project archive");
        }

        InputStream machineStream = zip.getInputStream(machineEntry);
        try {
            return new MachineJsonImporter().loadMachine(IOUtils.getStringFromInputStream(machineStream));
        } finally {
            IOUtils.closeQuietly(machineStream);
        }
    }

    /**
     * Gets the {@link SignalTable} from {@link SignalJsonImporter}.
     *
     * @param zip
     *            the ZIP containing the {@code signal.json}
     * @return
     *            the imported {@code SignalTable}
     * @throws IOException
     *            thrown if the {@code signal.json} can not be found
     * @throws JSONException
     *            thrown if there is an error during parsing the JSON string
     */
    private static SignalTable getSignalTable(ZipFile zip) throws IOException {
        ZipEntry signalEntry = zip.getEntry("signal.json");
        if (signalEntry == null) {
            throw new FileNotFoundException("Missing signal.json in project archive");
        }

        InputStream signalStream = zip.getInputStream(signalEntry);
        try {
            return new SignalJsonImporter().loadSignalTable(IOUtils.getStringFromInputStream(signalStream));
        } finally {
            IOUtils.closeQuietly(signalStream);
        }
    }

}