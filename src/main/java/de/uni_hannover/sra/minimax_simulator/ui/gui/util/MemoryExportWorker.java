package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.FXDialog;
import de.uni_hannover.sra.minimax_simulator.util.MemoryExporter;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.IOException;

/**
 * The {@code MemoryExportWorker} is a {@link Runnable} that writes the memory image to file.
 * It does so by extending {@link MemoryExporter} and wrapping it into an FX thread.
 * An error dialog will be shown if the export fails.
 *
 * @author Philipp Rohde
 */
public class MemoryExportWorker extends MemoryExporter implements Runnable {

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
        super(memory, fromAddress, toAddress, file, res);
    }

    /**
     * Tries to write the memory image to file. If the export fails an error dialog will be shown.
     */
    @Override
    public void run() {
        try {
            doExport();
        } catch (IOException ioe) {
            UIUtil.invokeInFAT(() -> {
                FXDialog fnw = new FXDialog(AlertType.ERROR, res.get("memory.export.error"), res.format("memory.export.write-error", file.getPath()));
                // FIXME: delete if issue with long texts in linux is resolved
                fnw.setResizable(true);

                fnw.showAndWait();
            });
        }
    }
}
