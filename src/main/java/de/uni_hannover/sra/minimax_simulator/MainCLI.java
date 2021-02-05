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
        @Option(names = {"-e", "--export-file"}, description = "Path to the file the machine memory should be exported to.", required = true)
        protected String pathMemExpFile;

        @Option(names = {"-ef", "--export-from"}, description = "First address of the memory to be included in the dump. If unspecified, it defaults to address 0.", defaultValue = "0")
        protected int memExpFrom;

        @Option(names = {"-et", "--export-to"}, description = "Last memory address to be included in the bump. If unspecified, it defaults to 0xFFFFFF.", defaultValue = "16777215")  // 0xFFFFFF is 16777215
        protected int memExpTo;
    }

    @ArgGroup(exclusive = false, multiplicity = "0..*")
    private MemoryImport[] memImport;

    /** Dependent argument group for importing memory dumps. */
    private static class MemoryImport {
        @Option(names = {"-i", "--import-file"}, description = "Path to the file that will be imported into the machine memory.", required = true)
        protected String pathMemImpFile;

        @Option(names = {"-if", "--import-from"}, description = "First address in the machine memory to which the file will be imported. If no value is specified, it is either the first memory address or the first address after a previous import.", defaultValue = "-1")
        protected int memImpFrom;

        @Option(names = {"-ib", "--import-bytes"}, description = "Number of bytes to import. If left unspecified, the whole file will be imported.")
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
                int last_start = -1;
                int last_end = -1;
                for (MemoryImport memImpArg : memImport) {
                    File memImpFile = new File(memImpArg.pathMemImpFile);

                    if (last_start == -1 && memImpArg.memImpFrom == -1) {
                        last_start = 0;
                    }
                    else if (memImpArg.memImpFrom == -1) {
                        last_start = last_end;
                    }
                    else {
                        last_start = memImpArg.memImpFrom;
                    }

                    int bytes = (memImpArg.memImpBytes > 0) ? (int) Math.min(memImpArg.memImpBytes, memImpFile.length()) : (int) Math.min(Integer.MAX_VALUE, memImpFile.length());
                    last_end = last_start + (int) Math.ceil( ((double) bytes) / 4);

                    MemoryImporter memImp = new MemoryImporter(
                            ws.getProject().getMachine().getMemory(),
                            last_start,
                            bytes,
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
