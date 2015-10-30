package de.uni_hannover.sra.minimax_simulator.io.exporter.csv;

import com.google.common.base.Strings;
import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.signal.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The {@code SignalCsvExporter} exports the {@link SignalTable} to a csv file.
 *
 * @author Martin L&uuml;ck
 */
// TODO: create abstract parent class SignalExporter
public class SignalCsvExporter {

	private final File file;

	/**
	 * Creates a new instance of the {@code SignalCsvExporter} and sets the file.
	 *
	 * @param file
	 *          the {@code File} to save to
	 */
	public SignalCsvExporter(File file) {
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
	private String binaryToString(int val, SignalType signal) {
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
	private String getShortDescription(SignalValue value, SignalType signal) {
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