package de.uni_hannover.sra.minimax_simulator.io.exporter.csv;

import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.common.base.Strings;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalType;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;

public class SignalCsvExporter
{
	private final File	_file;

	public SignalCsvExporter(File file)
	{
		_file = checkNotNull(file, "Invalid Null argument: file");
	}

	private String binaryToString(int val, SignalType signal)
	{
		return Strings.padStart(Integer.toBinaryString(val), signal.getBitWidth(), '0');
	}

	private String getShortDescription(SignalValue value, SignalType signal)
	{
		if (value.isDontCare())
			return "-";
		return binaryToString(value.intValue(), signal);
	}

	public void exportSignalTable(SignalTable table, SignalConfiguration config)
			throws IOException
	{
		String lineSeparator = System.getProperty("line.separator");
		if (lineSeparator == null || lineSeparator.isEmpty())
			lineSeparator = "\n";

		// Write the table to disk
		Writer wr = null;
		try
		{
			wr = IOUtils.toBufferedWriter(new FileWriter(_file));

			// Header line
			wr.append("#,Label");
			for (SignalType signal : config.getSignalTypes())
			{
				wr.append(',').append(signal.getId());
			}
			wr.append(",Jump0,Jump1,Description");
			wr.append(lineSeparator);

			// Value lines
			for (int i = 0, n = table.getRowCount(); i < n; i++)
			{
				SignalRow row = table.getRow(i);
				wr.append(Integer.toString(i)).append(',');
				if (row.getLabel() != null)
					wr.append(row.getLabel());
				wr.append(',');
				for (SignalType signal : config.getSignalTypes())
				{
					SignalValue value = row.getSignal(signal);
					wr.append(getShortDescription(value, signal)).append(',');
				}
				wr.append(Integer.toString(row.getJump().getTargetRow(i, 0))).append(',');
				wr.append(Integer.toString(row.getJump().getTargetRow(i, 1))).append(',');
				wr.append(row.getDescription().replaceAll("\n", " : "));
				wr.append(lineSeparator);
			}
		}
		finally
		{
			IOUtils.closeQuietly(wr);
		}
	}
}