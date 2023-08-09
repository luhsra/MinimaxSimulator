package de.uni_hannover.sra.minimax_simulator.util;

import com.google.common.io.ByteStreams;
import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.MemoryImportWorker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The {@code MemoryImporter} loads a memory image from file.
 * An error message will be logged if the import fails.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class MemoryImporter {

    private static final Logger LOG = Logger.getLogger(MemoryImportWorker.class.getName());

    protected final MachineMemory memory;
    private final int addressStart;

    private final int byteCount;
    private final int effectiveByteCount;

    protected final File file;
    protected final TextResource res;

    /**
     * Constructs a new {@code MemoryImporter} instance.
     *
     * @param memory
     *          the machine's memory
     * @param addressStart
     *          the start address
     * @param byteCount
     *          the number of bytes to import
     * @param file
     *          the file to read from
     * @param res
     *          the {@link TextResource} for getting localized texts
     */
    public MemoryImporter(MachineMemory memory, int addressStart, int byteCount, File file, TextResource res) {
        this.memory = memory;
        this.addressStart = addressStart;

        this.byteCount = byteCount;

        long addressStartInBytes = ((long) addressStart) << 2;
        long endAddressInBytes = ((long) (memory.getMaxAddress() + 1)) << 2;
        effectiveByteCount = (int) (Math.min(endAddressInBytes, addressStartInBytes + byteCount) - addressStartInBytes);

        checkArgument(effectiveByteCount >= 0, "Negative number of bytes to import: " + effectiveByteCount);
        checkArgument(effectiveByteCount <= Integer.MAX_VALUE, "Too many bytes to import: " + effectiveByteCount);

        this.file = file;
        this.res = res;
    }

    /**
     * Loads a memory image from file. The input is treated as little-endian.
     *
     * @return
     *          the number of bytes truncated during import
     * @throws IOException
     *          thrown if the file is not readable or does not exist
     */
    public int doImport() throws IOException {
        FileInputStream fis = null;
        boolean memoryNotifiesListeners = memory.getNotifiesListeners();

        try {
            fis = new FileInputStream(file);
            memory.setNotifiesListeners(false);

            byte[] bytes = new byte[effectiveByteCount];
            ByteStreams.readFully(fis, bytes, 0, effectiveByteCount);

            MemoryState state = memory.getMemoryState();

            // Convert byte array to ints
            // divide length by 4 (rounding up)
            int intCount = ((effectiveByteCount - 1) >> 2) + 1;

            for (int i = 0, a = addressStart; i < intCount; i++, a++) {
                // multiply by 4
                int byteNum = i << 2;

//          // treat 4 bytes as big-endian integer
//          int value = bytes[byteNum] << 24;
//          if (byteNum + 1 < effectiveByteCount)
//              value |= bytes[byteNum + 1] << 16;
//          if (byteNum + 2 < effectiveByteCount)
//              value |= bytes[byteNum + 2] << 8;
//          if (byteNum + 3 < effectiveByteCount)
//              value |= bytes[byteNum + 3];

                // treat 4 bytes as little-endian integer
                // & 0xFF is needed because Java handles all Bytes as signed
                int value = bytes[byteNum] & 0xFF;
                if (byteNum + 1 < effectiveByteCount)
                    value |= (bytes[byteNum + 1] & 0xFF) << 8;
                if (byteNum + 2 < effectiveByteCount)
                    value |= (bytes[byteNum + 2] & 0xFF) << 16;
                if (byteNum + 3 < effectiveByteCount)
                    value |= (bytes[byteNum + 3] & 0xFF) << 24;
                state.setInt(a, value);
            }

            int truncated = byteCount - effectiveByteCount;
            LOG.fine(bytes.length + " bytes / " + intCount + " words imported, " + truncated + " bytes truncated from " + file.getPath());
            return truncated;
        } finally {
            memory.setNotifiesListeners(memoryNotifiesListeners);
            IOUtils.closeQuietly(fis);
        }
    }
}
