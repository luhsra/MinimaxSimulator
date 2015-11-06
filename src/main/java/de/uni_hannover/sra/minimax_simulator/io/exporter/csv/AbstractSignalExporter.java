package de.uni_hannover.sra.minimax_simulator.io.exporter.csv;

import com.google.common.base.Strings;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalType;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base class for the {@link SignalTable} exporter.<br>
 * It provides methods for converting the value of a {@link SignalValue} to {@code String.}
 *
 * @author Philipp Rohde
 */
public abstract class AbstractSignalExporter {

    protected final File file;

    protected AbstractSignalExporter(File file) {
        this.file = checkNotNull(file, "Invalid Null argument: file");
    }

    /**
     * Converts an {@code int} to a {@code String} with the length of
     * {@link SignalType#getBitWidth()} belonging to the value.
     *
     * @param val
     *          the {@code int} to convert
     * @param signal
     *          the {@code SignalType} the value belongs to
     * @return
     *          a binary {@code String} representation of the value
     */
    protected String binaryToString(int val, SignalType signal) {
        return Strings.padStart(Integer.toBinaryString(val), signal.getBitWidth(), '0');
    }

    /**
     * Converts the value of a {@link SignalValue} to a {@code String} with
     * the length of {@link SignalType#getBitWidth()} belonging to the value.
     *
     * @param value
     *          the {@code SignalValue} to convert
     * @param signal
     *          the {@code SignalType} the value belongs to
     * @return
     *          a binary {@code String} representation of the value
     */
    protected String getShortDescription(SignalValue value, SignalType signal) {
        if (value.isDontCare()) {
            return "-";
        }
        return binaryToString(value.intValue(), signal);
    }

    /**
     * Exports the {@code SignalTable} to the file set via the constructor.
     *
     * @param table
     *          the {@code SignalTable} to export
     * @param config
     *          the {@code SignalConfiguration} belonging to the {@code SignalTable}
     * @throws IOException
     *          thrown if the file was not writable
     */
    public abstract void exportSignalTable(SignalTable table, SignalConfiguration config) throws IOException;
}
