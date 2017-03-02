package de.uni_hannover.sra.minimax_simulator.io.exporter.csv;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.signal.*;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;

import java.io.*;

/**
 * The {@code SignalHtmlExporter} exports the {@link SignalTable} to an HTML file.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
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
            // read template file
            InputStream is = this.getClass().getResourceAsStream("/html/signaltable.html");
            String template = IOUtils.getStringFromInputStream(is);

            StringBuilder sb = new StringBuilder();
            // header line
            sb.append("<table><tr><th>#</th><th>Label</th>");
            for (SignalType signal : config.getSignalTypes()) {
                sb.append("<th>").append(signal.getId()).append("</th>");
            }
            sb.append("<th>Alu == 0?</th><th>Jump</th><th>Description</th></tr>");

            // value lines
            for (int i = 0, n = table.getRowCount(); i < n; i++) {
                SignalRow row = table.getRow(i);
                sb.append("<tr><td>").append(Integer.toString(i)).append("</td>");
                if (row.getLabel() != null) {
                    sb.append("<td>").append(row.getLabel()).append("</td>");
                }
                else {
                    sb.append("<td></td>");
                }

                for (SignalType signal : config.getSignalTypes()) {
                    SignalValue value = row.getSignal(signal);
                    sb.append("<td>").append(getShortDescription(value, signal)).append("</td>");
                }

                String targets;
                String cond;
                String openingTag = "<td>";
                Jump j = row.getJump();

                int target0 = j.getTargetRow(i, 0);
                int target1 = j.getTargetRow(i, 1);

                if (target0 == target1) {
                    cond = "";
                    targets = Integer.toString(target0);
                    if (target0 == i + 1) {
                        openingTag = "<td class=\"defaultJump\">";
                    }
                }
                else {
                    cond = "1<br>0";
                    targets = target1 + "<br>" + target0;
                }

                sb.append("<td>").append(cond).append("</td>").append(openingTag).append(targets).append("</td><td class=\"description\">");
                sb.append(row.getDescription().replaceAll("\n", "<br>"));
                sb.append("</td></tr>");
            }
            sb.append("</table></body></html>");
            String signalTable = sb.toString();

            wr = IOUtils.toBufferedWriter(new FileWriter(file));
            // replace placeholder with content
            wr.append(template.replace("$table", signalTable));
        } finally {
            IOUtils.closeQuietly(wr);
        }
    }
}