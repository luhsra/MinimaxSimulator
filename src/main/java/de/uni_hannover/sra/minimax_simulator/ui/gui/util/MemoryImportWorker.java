package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.FXDialog;
import de.uni_hannover.sra.minimax_simulator.util.MemoryImporter;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.IOException;

/**
 * The {@code MemoryImportWorker} is a {@link Runnable} that loads a memory image from file.
 * It does so by extending {@link MemoryImporter} and wrapping it into an FX thread.
 * An error dialog will be shown if the import fails.
 *
 * @author Philipp Rohde
 */
public class MemoryImportWorker extends MemoryImporter implements Runnable {

    /**
     * Constructs a new {@code MemoryImportWorker} instance.
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
    public MemoryImportWorker(MachineMemory memory, int addressStart, int byteCount, File file, TextResource res) {
        super(memory, addressStart, byteCount, file, res);
    }

    /**
     * Tries to load the memory image from file. An error dialog will be shown if the import fails.
     */
    @Override
    public void run() {
        try {
            int bytesTruncated = doImport();

            if (bytesTruncated > 0) {
                int maxAddress = memory.getMaxAddress();
                int width = memory.getAddressWidth();

                UIUtil.invokeInFAT(() -> {
                    FXDialog trunc = new FXDialog(Alert.AlertType.WARNING, res.get("memory.import.warning"), res.format("memory.import.bytes-truncated", Util.toHex(maxAddress, width, true), bytesTruncated));
                    // FIXME: delete if issue with long texts in linux is resolved
                    trunc.setResizable(true);

                    trunc.showAndWait();
                });
            }
        } catch (IOException ioe) {
            UIUtil.invokeInFAT(() -> {
                FXDialog fne = new FXDialog(AlertType.ERROR, res.get("memory.import.error"), res.format("memory.import.file-not-existing", file.getPath()));
                // FIXME: delete if issue with long texts in linux is resolved
                fne.setResizable(true);

                fne.showAndWait();
            });
        }
    }
}
