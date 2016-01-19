package de.uni_hannover.sra.minimax_simulator.io.exporter.csv;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.signal.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * The {@code SignalCsvExporter} exports the {@link SignalTable} to a csv file.
 *
 * @author Martin L&uuml;ck
 */
public class SignalCsvExporter extends AbstractSignalExporter {

    /**
     * Creates a new instance of the {@code SignalCsvExporter} and sets the file.
     *
     * @param file
     *          the {@code File} to save to
     */
    public SignalCsvExporter(File file) {
        super(file);
    }

    @Override
    public void exportSignalTable(SignalTable table, SignalConfiguration config) throws IOException {
        String lineSeparator = System.getProperty("line.separator");
        if (lineSeparator == null || lineSeparator.isEmpty()) {
            lineSeparator = "\n";
        }

        // write the table to disk
        Writer wr = null;
        try {
            wr = IOUtils.toBufferedWriter(new FileWriter(file));

            // header line
            wr.append("#,Label");
            for (SignalType signal : config.getSignalTypes()) {
                wr.append(',').append(signal.getId());
            }
            wr.append(",Jump0,Jump1,Description");
            wr.append(lineSeparator);

            // value lines
            for (int i = 0, n = table.getRowCount(); i < n; i++) {
                SignalRow row = table.getRow(i);
                wr.append(Integer.toString(i)).append(',');
                if (row.getLabel() != null) {
                    wr.append(row.getLabel());
                }
                wr.append(',');
                for (SignalType signal : config.getSignalTypes()) {
                    SignalValue value = row.getSignal(signal);
                    wr.append(getShortDescription(value, signal)).append(',');
                }
                wr.append(Integer.toString(row.getJump().getTargetRow(i, 0))).append(',');
                wr.append(Integer.toString(row.getJump().getTargetRow(i, 1))).append(',');
                wr.append(row.getDescription().replaceAll("\n", " : "));
                wr.append(lineSeparator);
            }
        } finally {
            IOUtils.closeQuietly(wr);
        }
    }
}