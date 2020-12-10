package de.uni_hannover.sra.minimax_simulator.io.exporter.json;

import de.uni_hannover.sra.minimax_simulator.io.exporter.ProjectExportException;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Writer;
import java.util.Map.Entry;

/**
 * An Exporter that writes a {@link SignalTable} as a JSON string.
 *
 * @author Philipp Rohde
 */
class SignalJsonExporter {

    /**
     * Writes the {@link SignalTable} as a JSON string using the given {@link Writer}.
     *
     * @param wr
     *             the writer to use for output
     * @param table
     *              the SignalTable to export
     * @throws ProjectExportException
     *              thrown if there is an I/O error during export
     */
    void write(Writer wr, SignalTable table) throws ProjectExportException {

        JSONObject root = new JSONObject();
        JSONObject signaltable = new JSONObject();

        int rowCount = table.getRowCount();
        if (rowCount == 0) {
            signaltable.put("row", new JSONArray());                // add empty JSONArray if there is no signal row
        }
        else {
            for (int i = 0; i < rowCount; i++) {
                signaltable.accumulate("row", createRowElement(table.getRow(i)));
            }
        }
        root.put("signaltable", signaltable);

        try {
            wr.write(root.toString(2));
            wr.flush();
        } catch (IOException e) {
            throw new ProjectExportException("Cannot export due to I/O error: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a {@link JSONObject} of a {@link SignalRow}.
     *
     * @param row
     *              the SignalRow to process
     * @return
     *              the SignalRow as JSONObject
     */
    private JSONObject createRowElement(SignalRow row) {
        JSONObject rowObj = new JSONObject();

        if (row.getLabel() != null) {
            rowObj.put("label", row.getLabel());
        }
        if (row.isBreakpoint()) {
            rowObj.put("breakpoint", true);
        }

        rowObj.put("signal", new JSONArray());                // add empty JSONArray as base for row signals
        for (Entry<String, SignalValue> entry : row.getSignalValues().entrySet()) {
            SignalValue value = entry.getValue();

            JSONObject signal = new JSONObject();
            signal.put("name", entry.getKey());
            signal.put("value", Integer.toString(value.intValue()));
            if (value.isDontCare()) {
                signal.put("dontcare", true);
            }
            rowObj.accumulate("signal", signal);
        }

        Jump j = row.getJump();
        if (j instanceof UnconditionalJump) {
            String target = Integer.toString(((UnconditionalJump) j).getTargetRow());
            JSONObject uncondJmp = new JSONObject();
            uncondJmp.put("target", target);
            rowObj.put("unconditional-jump", uncondJmp);
        }
        else if (j instanceof ConditionalJump) {
            ConditionalJump cj = (ConditionalJump) j;
            String target0 = Integer.toString(cj.getTargetRow(0));
            String target1 = Integer.toString(cj.getTargetRow(1));
            JSONObject condJmp = new JSONObject();
            condJmp.put("cond0-target", target0);
            condJmp.put("cond1-target", target1);
            rowObj.put("conditional-jump", condJmp);
        }

        return rowObj;
    }
}
