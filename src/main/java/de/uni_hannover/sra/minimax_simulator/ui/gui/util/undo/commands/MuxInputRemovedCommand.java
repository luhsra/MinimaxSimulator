package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;

import java.util.ArrayList;
import java.util.List;

/**
 * This {@link Command} is used for deleting {@link MuxInput}s from the multiplexers.
 *
 * @author Philipp Rohde
 */
public class MuxInputRemovedCommand extends Command {

    private final MuxType mux;
    private final int index;
    private final MachineConfiguration config;
    private final List<MuxInput> muxInputs;
    private final List<SignalRow> signalRows;
    private final SignalTable signalTable;

    /**
     * Creates the {@code MuxInputRemovedCommand} instance for the change.
     *
     * @param mux
     *         the multiplexer where the input should be deleted
     * @param index
     *         the index of the input to delete
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public MuxInputRemovedCommand(MuxType mux, int index, MachineConfiguration config) {
        this.mux = mux;
        this.index = index;
        this.config = config;
        this.muxInputs = new ArrayList<>();
        this.signalRows = new ArrayList<>();
        this.signalTable = Main.getWorkspace().getProject().getSignalTable();

        for (SignalRow row : signalTable.getRows()) {
            signalRows.add(new SignalRow(row));
        }

        for (MuxInput input : config.getMuxSources(mux)) {
            muxInputs.add(input);
        }
    }

    @Override
    public void execute() {
        config.removeMuxSource(mux, index);
    }

    @Override
    public void undo() {
        for (int i = config.getMuxSources(mux).size() - 1; i >= 0; i--) {
            config.removeMuxSource(mux, i);
        }
        for (MuxInput input : muxInputs) {
            config.addMuxSource(mux, input);
        }

        for (int j = signalTable.getRowCount() - 1; j >= 0; j--) {
            signalTable.removeSignalRow(j);
        }
        for (SignalRow row : signalRows) {
            signalTable.addSignalRow(row);
        }
    }

    @Override
    public void redo() {
        this.execute();
    }
}
