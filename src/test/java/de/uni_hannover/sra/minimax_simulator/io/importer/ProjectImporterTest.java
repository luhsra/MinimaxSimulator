package de.uni_hannover.sra.minimax_simulator.io.importer;

import de.uni_hannover.sra.minimax_simulator.io.exporter.ProjectExportException;
import de.uni_hannover.sra.minimax_simulator.io.exporter.json.ProjectZipExporter;
import de.uni_hannover.sra.minimax_simulator.io.importer.json.ProjectZipImporter;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalType;
import de.uni_hannover.sra.minimax_simulator.model.user.NewProjectBuilder;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of the {@code Project} importer.
 *
 * @author Philipp Rohde
 */
public class ProjectImporterTest {

    /** temporary folder */
    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    private static Project project;

    /**
     * Initializes the test instance.
     */
    @BeforeClass
    public static void initialize() {
        project = new NewProjectBuilder().buildProject();
    }

    /**
     * Tests the actual implementation of {@link ProjectZipImporter}.
     *
     * @throws IOException
     *          thrown if the project file could not be created
     * @throws ProjectExportException
     *          thrown if there was an error during export
     * @throws ProjectImportException
     *          thrown if there was an error during import
     */
    @Test
    public void testImport() throws IOException, ProjectExportException, ProjectImportException {
        File saved = tmpDir.newFile("project.zip");
        new ProjectZipExporter(saved).exportProject(project);

        Project loaded = new ProjectZipImporter(saved).importProject();

        // signal table and rows
        SignalTable savedTable = project.getSignalTable();
        SignalTable loadedTable = loaded.getSignalTable();

        for (int i = 0; i < savedTable.getRowCount(); i++) {
            SignalRow savedRow = savedTable.getRow(i);
            SignalRow loadedRow = loadedTable.getRow(i);

            assertEquals("signal row: " + i, savedRow.toString(), loadedRow.toString());
        }

        // signal types
        List<SignalType> savedSignals = project.getSignalConfiguration().getSignalTypes();
        List<SignalType> loadedSignals = loaded.getSignalConfiguration().getSignalTypes();
        for (int i = 0; i < savedSignals.size(); i++) {
            SignalType savedType = savedSignals.get(i);
            SignalType loadedType = loadedSignals.get(i);
            assertEquals("signal type: " + i, savedType.toString(), loadedType.toString());
        }

        // ALU operations
        List<AluOperation> savedOps = project.getMachineConfiguration().getAluOperations();
        List<AluOperation> loadedOps = loaded.getMachineConfiguration().getAluOperations();
        assertEquals("ALU operations", savedOps, loadedOps);

        // multiplexer A
        List<MuxInput> savedMuxA = project.getMachineConfiguration().getMuxSources(MuxType.A);
        List<MuxInput> loadedMuxA = loaded.getMachineConfiguration().getMuxSources(MuxType.A);
        assertEquals("MUX A", savedMuxA, loadedMuxA);

        // multiplexer B
        List<MuxInput> savedMuxB = project.getMachineConfiguration().getMuxSources(MuxType.B);
        List<MuxInput> loadedMuxB = loaded.getMachineConfiguration().getMuxSources(MuxType.B);
        assertEquals("MUX B", savedMuxB, loadedMuxB);

        // base registers
        List<RegisterExtension> savedBase = project.getMachineConfiguration().getBaseRegisters();
        List<RegisterExtension> loadedBase = loaded.getMachineConfiguration().getBaseRegisters();
        assertEquals("base registers", savedBase, loadedBase);

        // extended registers
        List<RegisterExtension> savedExtended = project.getMachineConfiguration().getRegisterExtensions();
        List<RegisterExtension> loadedExtended = loaded.getMachineConfiguration().getRegisterExtensions();
        assertEquals("extended registers", savedExtended, loadedExtended);
    }
}
