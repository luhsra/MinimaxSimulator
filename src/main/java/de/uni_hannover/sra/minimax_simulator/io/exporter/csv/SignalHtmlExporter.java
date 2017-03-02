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
            sb.append("<table>\n      <tr>\n        <th>#</th>\n        <th>Label</th>\n");
            for (SignalType signal : config.getSignalTypes()) {
                sb.append("        <th>").append(signal.getId()).append("</th>\n");
            }
            sb.append("        <th>Alu == 0?</th>\n        <th>Jump</th>\n        <th>Description</th>\n      </tr>\n");

            // value lines
            for (int i = 0, n = table.getRowCount(); i < n; i++) {
                SignalRow row = table.getRow(i);
                sb.append("      <tr>\n        <td>").append(Integer.toString(i)).append("</td>\n");
                if (row.getLabel() != null) {
                    sb.append("        <td class=\"label\">").append(row.getLabel()).append("</td>\n");
                }
                else {
                    sb.append("        <td></td>\n");
                }

                for (SignalType signal : config.getSignalTypes()) {
                    SignalValue value = row.getSignal(signal);
                    sb.append("        <td>").append(getShortDescription(value, signal)).append("</td>\n");
                }

                String targets;
                String cond;
                String openingTag = "        <td>";
                Jump j = row.getJump();

                int target0 = j.getTargetRow(i, 0);
                int target1 = j.getTargetRow(i, 1);

                if (target0 == target1) {
                    cond = "";
                    targets = Integer.toString(target0);
                    if (target0 == i + 1) {
                        openingTag = "        <td class=\"defaultJump\">";
                    }
                }
                else {
                    cond = "1<br>0";
                    targets = target1 + "<br>" + target0;
                }

                sb.append("        <td>").append(cond).append("</td>\n").append(openingTag).append(targets).append("</td>\n        <td class=\"description\">");
                sb.append(row.getDescription().replaceAll("\n", "<br>"));
                sb.append("</td>\n      </tr>\n");
            }
            sb.append("    </table>");
            String signalTable = sb.toString();

            wr = IOUtils.toBufferedWriter(new FileWriter(file));
            // replace placeholder with content
            wr.append(template.replace("$table", signalTable));
        } finally {
            IOUtils.closeQuietly(wr);
        }
    }
}