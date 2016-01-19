package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.FXDialog;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The {@code MemoryExportWorker} is a {@link Runnable} that writes the memory image to file.
 * An error dialog will be shown if the export fails.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class MemoryExportWorker implements Runnable {

    private static final Logger LOG = Logger.getLogger(MemoryExportWorker.class.getName());

    private final MachineMemory memory;

    private final File file;
    private final TextResource res;

    private final int fromAddress;
    private final int toAddress;

    /**
     * Constructs a new {@code MemoryExportWorker} instance.
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
    public MemoryExportWorker(MachineMemory memory, int fromAddress, int toAddress, File file, TextResource res) {
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
     * Tries to write the memory image to file. If the export fails an error dialog will be shown.
     */
    @Override
    public void run() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            doExport(fos);
        } catch (IOException ioe) {
            UIUtil.invokeInFAT(new Runnable() {
                @Override
                public void run() {
                    FXDialog fnw = new FXDialog(AlertType.ERROR, res.get("memory.export.error"), res.format("memory.export.write-error", file.getPath()));
                    // FIXME: delete if issue with long texts in linux is resolved
                    fnw.setResizable(true);

                    fnw.showAndWait();
                }
            });
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    /**
     * Converts the memory image to little-endian and writes it to file.
     *
     * @param os
     *          the {@link OutputStream} to use for writing
     * @throws IOException
     *          thrown if the memory image could not be written
     */
    private void doExport(OutputStream os) throws IOException {
        MemoryState state = memory.getMemoryState();

        BufferedOutputStream bos = IOUtils.toBufferedStream(os);

        byte[] intBytes = new byte[4];

        try {
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
        } finally {
            IOUtils.closeQuietly(bos);
        }

        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine(((toAddress - fromAddress) << 2) + " bytes / " + (toAddress - fromAddress) + " words exported to " + file.getPath());
        }
    }
}
