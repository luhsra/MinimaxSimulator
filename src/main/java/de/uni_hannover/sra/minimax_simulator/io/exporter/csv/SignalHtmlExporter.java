package de.uni_hannover.sra.minimax_simulator.io.exporter.csv;

import com.google.common.base.Strings;
import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.signal.*;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static com.google.common.base.Preconditions.checkNotNull;

public class SignalHtmlExporter
{
	private final File	_file;

	public SignalHtmlExporter(File file)
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
		// Write the table to disk
		Writer wr = null;
		try
		{
			wr = IOUtils.toBufferedWriter(new FileWriter(_file));

			wr.append("<!doctype html>\n" +
					"<html>" +
					"<head><meta charset=\"utf-8\"></head><body><table>\n");

			// Header line
			wr.append("<tr><th>#</th><th>Label</th>");
			for (SignalType signal : config.getSignalTypes())
			{
				wr.append("<th>").append(signal.getId()).append("</th>");
			}
			wr.append("<th>Alu == 0?</th><th>Jump</th><th>Description</th></tr>");

			// Value lines
			for (int i = 0, n = table.getRowCount(); i < n; i++)
			{
				SignalRow row = table.getRow(i);
				wr.append("<tr><td>") .append(Integer.toString(i)).append("</td>");
				if (row.getLabel() != null)
					wr.append("<td>").append(row.getLabel()).append("</td>");
				else
					wr.append("<td></td>");

				for (SignalType signal : config.getSignalTypes())
				{
					SignalValue value = row.getSignal(signal);
					wr.append("<td>").append(getShortDescription(value, signal)).append("</td>");
				}

				String targets;
				String cond;
				Jump j = row.getJump();

				if (j.getTargetRow(i, 0) == j.getTargetRow(i, 1))
				{
					cond = "";
				}
				else
				{
					cond = "1<br/>0";
				}


				int target0 = j.getTargetRow(i, 0);
				int target1 = j.getTargetRow(i, 1);
				if (target0 == target1)
				{
					targets = Integer.toString(target0);
					if (target0 == i + 1)
						targets = "<span style=\"color:#777777;\">" + targets + "</span>";
				}
				else
				{
					targets = target1 + "<br/>" + target0;
				}

				wr.append("<td>").append(cond).append("</td><td>").append(targets).append("</td><td>");
				wr.append(row.getDescription().replaceAll("\n", "<br/>"));
				wr.append("</td></tr>");
			}
			wr.append("</table></body></html>");
		}
		finally
		{
			IOUtils.closeQuietly(wr);
		}
	}
}