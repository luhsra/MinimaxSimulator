package de.uni_hannover.sra.minimax_simulator.io.exporter.csv;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.signal.*;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * The {@code SignalHtmlExporter} exports the {@link SignalTable} to an HTML file.
 *
 * @author Martin L&uuml;ck
 */
// TODO: upgrade to HTML5 standards
public class SignalHtmlExporter extends AbstractSignalExporter {

	/**
	 * Creates a new instance of the {@code SignalHtmlExporter} and sets the file.
	 *
	 * @param file
	 *          the {@code File} to save to
	 */
	public SignalHtmlExporter(File file) {
		super(file);
	}

	@Override
	public void exportSignalTable(SignalTable table, SignalConfiguration config) throws IOException {
		// write the table to disk
		Writer wr = null;

		try {
			wr = IOUtils.toBufferedWriter(new FileWriter(file));

			wr.append("<!doctype html>\n" +
					"<html>" +
					"<head><meta charset=\"utf-8\"></head><body><table>\n");

			// header line
			wr.append("<tr><th>#</th><th>Label</th>");
			for (SignalType signal : config.getSignalTypes()) {
				wr.append("<th>").append(signal.getId()).append("</th>");
			}
			wr.append("<th>Alu == 0?</th><th>Jump</th><th>Description</th></tr>");

			// value lines
			for (int i = 0, n = table.getRowCount(); i < n; i++) {
				SignalRow row = table.getRow(i);
				wr.append("<tr><td>") .append(Integer.toString(i)).append("</td>");
				if (row.getLabel() != null) {
					wr.append("<td>").append(row.getLabel()).append("</td>");
				}
				else {
					wr.append("<td></td>");
				}

				for (SignalType signal : config.getSignalTypes()) {
					SignalValue value = row.getSignal(signal);
					wr.append("<td>").append(getShortDescription(value, signal)).append("</td>");
				}

				String targets;
				String cond;
				Jump j = row.getJump();

				if (j.getTargetRow(i, 0) == j.getTargetRow(i, 1)) {
					cond = "";
				}
				else {
					cond = "1<br/>0";
				}


				int target0 = j.getTargetRow(i, 0);
				int target1 = j.getTargetRow(i, 1);
				if (target0 == target1) {
					targets = Integer.toString(target0);
					if (target0 == i + 1) {
						targets = "<span style=\"color:#777777;\">" + targets + "</span>";
					}
				}
				else {
					targets = target1 + "<br/>" + target0;
				}

				wr.append("<td>").append(cond).append("</td><td>").append(targets).append("</td><td>");
				wr.append(row.getDescription().replaceAll("\n", "<br/>"));
				wr.append("</td></tr>");
			}
			wr.append("</table></body></html>");
		} finally {
			IOUtils.closeQuietly(wr);
		}
	}
}