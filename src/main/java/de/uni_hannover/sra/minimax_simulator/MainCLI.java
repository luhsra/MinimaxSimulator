package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.io.importer.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.util.MemoryExporter;
import de.uni_hannover.sra.minimax_simulator.util.MemoryImporter;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@Command(name = "Minimax Simulator CLI")
public class MainCLI implements Callable<Integer> {

    @Parameters(index = "0", description = "The project file to use.")
    private File file;

    @ArgGroup(exclusive = false, multiplicity = "0..*")
    private MemoryExport[] memExport;

    /** Dependent argument group for exporting memory dumps. */
    private static class MemoryExport {
        @Option(names = {"-e", "--export-file"}, description = "File path to be used for exporting the machine memory", required = true)
        protected String pathMemExpFile;

        @Option(names = {"-ef", "--export-from"}, description = "Starting address for the memory export.", required = true)
        protected int memExpFrom;

        @Option(names = {"-et", "--export-to"}, description = "End address for the memory export.", required = true)
        protected int memExpTo;
    }

    @ArgGroup(exclusive = false, multiplicity = "0..*")
    private MemoryImport[] memImport;

    /** Dependent argument group for importing memory dumps. */
    private static class MemoryImport {
        @Option(names = {"-i", "--import-file"}, description = "File path to be used for importing the machine memory", required = true)
        protected String pathMemImpFile;

        @Option(names = {"-if", "--import-from"}, description = "Starting address for the memory import.", required = true)
        protected int memImpFrom;

        @Option(names = {"-ib", "--import-bytes"}, description = "Number of bytes to import.")
        protected int memImpBytes;
    }

    /**
     * Runs the CLI version of the Minimax Simulator using the command line arguments parsed by picocli.
     *
     * @return
     *          exit status of the application
     * @throws Exception
     *          thrown if an error occurred during the execution
     */
    @Override
    public Integer call() throws Exception {
        Main.LOG.info("Loading project file...");
        try {
            Workspace ws = Main.getWorkspace();
            ws.openProject(file);

            // disable breakpoints in project file so that the complete program runs
            for (SignalRow signalRow : ws.getProject().getSignalTable().getRows()) {
                signalRow.setBreakpoint(false);
            }

            Main.LOG.info("Project loaded.");

            if (memImport != null) {
                Main.LOG.info("Importing memory dump...");
                for (MemoryImport memImpArg : memImport) {
                    File memImpFile = new File(memImpArg.pathMemImpFile);
                    MemoryImporter memImp = new MemoryImporter(
                            ws.getProject().getMachine().getMemory(),
                            memImpArg.memImpFrom,
                            (memImpArg.memImpBytes > 0) ? memImpArg.memImpBytes : (int) Math.min(Integer.MAX_VALUE, memImpFile.length()),
                            memImpFile,
                            Main.getTextResource("project"));
                    try {
                        memImp.doImport();

                    } catch (IOException ioe) {
                        Main.LOG.severe(ioe.getMessage());
                        return -1;
                    }
                }
                Main.LOG.info("Memory dump imported.");
            }

            Main.LOG.info("Running simulation...");
            ws.getProject().getSimulation().init();
            ws.getProject().getSimulation().run();
            Main.LOG.info("Simulation complete. It took " + ws.getProject().getSimulation().getCyclesCount() + " cycle(s).");
            Main.LOG.info("Blub " + ws.getProject().getSimulation().getMemoryState().getMemoryState().getInt(0));

            if (memExport != null) {
                Main.LOG.info("Exporting memory dump...");
                for (MemoryExport memExpArg : memExport) {
                    File memExpFile = new File(memExpArg.pathMemExpFile);
                    MemoryExporter memExp = new MemoryExporter(
                            ws.getProject().getMachine().getMemory(),
                            memExpArg.memExpFrom,
                            memExpArg.memExpTo,
                            memExpFile,
                            Main.getTextResource("project"));
                    try {
                        memExp.doExport();
                    } catch (IOException ioe) {
                        Main.LOG.severe(ioe.getMessage());
                        return -1;
                    }
                }
                Main.LOG.info("Memory dump exported.");
            }
        } catch (ProjectImportException e) {
            Main.LOG.severe(e.getMessage());
            return -1;
        }
        return 0;
    }

    /**
     * Wraps the CLI version of the Minimax Simulator into picocli for
     * command line argument parsing and executes it.
     *
     * @param args
     *          command line arguments
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new MainCLI()).execute(args);
        System.exit(exitCode);
    }

}
