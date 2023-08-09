package de.uni_hannover.sra.minimax_simulator.util;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.MemoryExportWorker;

import java.io.*;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The {@code MemoryExporter} writes the memory image to file.
 * An error message will be logged if the export fails.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class MemoryExporter {

    private static final Logger LOG = Logger.getLogger(MemoryExportWorker.class.getName());

    private final MachineMemory memory;

    protected final File file;
    protected final TextResource res;

    protected final int fromAddress;
    protected final int toAddress;

    /**
     * Constructs a new {@code MemoryExporter} instance.
     *
     * @param memory
     *          the machine's memory
     * @param fromAddress
     *          the start address
     * @param toAddress
     *          the end address
     * @param file
     *          the file to write to
     * @param res
     *          the {@link TextResource} for getting localized texts
     */
    public MemoryExporter(MachineMemory memory, int fromAddress, int toAddress, File file, TextResource res) {
        this.memory = memory;

        this.file = file;
        this.res = res;

        this.fromAddress = fromAddress;
        this.toAddress = toAddress;

        checkArgument(fromAddress >= memory.getMinAddress());
        checkArgument(fromAddress <= toAddress);
        checkArgument(toAddress <= memory.getMaxAddress());
    }

    /**
     * Converts the memory image to little-endian and writes it to file.
     *
     * @throws IOException
     *          thrown if the memory image could not be written
     */
    public void doExport() throws IOException {
        BufferedOutputStream bos = null;
        try {
            bos = IOUtils.toBufferedStream(new FileOutputStream(file));

            MemoryState state = memory.getMemoryState();

            byte[] intBytes = new byte[4];

            for (int i = fromAddress, n = toAddress; i <= n; i++) {
                int value = state.getInt(i);

                // convert integer to little-endian byte-array
                intBytes[0] = (byte) (value & 0xFF);
                value >>>= 8;
                intBytes[1] = (byte) (value & 0xFF);
                value >>>= 8;
                intBytes[2] = (byte) (value & 0xFF);
                value >>>= 8;
                intBytes[3] = (byte) (value & 0xFF);

                bos.write(intBytes);
            }
            LOG.fine(((toAddress - fromAddress) << 2) + " bytes / " + (toAddress - fromAddress) + " words exported to " + file.getPath());
        } finally {
            IOUtils.closeQuietly(bos);
        }
    }
}
