package de.uni_hannover.sra.minimax_simulator.io.importer.json;

import de.uni_hannover.sra.minimax_simulator.model.signal.DefaultSignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.DefaultJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An Importer that imports a {@link SignalTable} from a JSON string.
 *
 * @author Philipp Rohde
 */
class SignalJsonImporter extends Importer {

	/**
	 * Imports the {@link SignalTable} from a JSON string.
	 *
	 * @param input
	 *            the JSON string containing the SignalTable
	 * @return
	 *            the imported SignalTable
	 * @throws JSONException
	 *            thrown if there is an error during parsing the JSON string
	 */
	SignalTable loadSignalTable(String input) throws JSONException {
		SignalTable table = new DefaultSignalTable();

		JSONObject root = new JSONObject(input);
		JSONObject signaltable = root.getJSONObject("signaltable");
		JSONArray rows = signaltable.getJSONArray("row");
		for (int i = 0; i < rows.length(); i++) {
			SignalRow row = new SignalRow();

			JSONObject currentRow = rows.getJSONObject(i);
			if (currentRow.has("label")) {
				String label = currentRow.getString("label");
				row.setLabel(label);
			}
			if (currentRow.has("breakpoint")) {
				boolean breakpoint = currentRow.getBoolean("breakpoint");
				row.setBreakpoint(breakpoint);
			}

			if (currentRow.has("signal")) {
				JSONArray signals = currentRow.getJSONArray("signal");
				for (int j = 0; j < signals.length(); j++) {
					JSONObject currentSignal = signals.getJSONObject(j);
					int intValue = Integer.valueOf(currentSignal.getString("value"));
					boolean dontcare = false;
					if (currentSignal.has("dontcare")) {
						dontcare = currentSignal.getBoolean("dontcare");
					}
					SignalValue value = dontcare ? SignalValue.DONT_CARE : SignalValue.valueOf(intValue);
					row.setSignal(currentSignal.getString("name"), value);
				}
			}
			
			Jump jump;

			if (currentRow.has("unconditional-jump")) {
				JSONObject jmp = currentRow.getJSONObject("unconditional-jump");
				jump = new UnconditionalJump(jmp.getInt("target"));
			}
			else if (currentRow.has("conditional-jump")) {
				JSONObject jmp = currentRow.getJSONObject("conditional-jump");
				int cond0 = jmp.getInt("cond0-target");
				int cond1 = jmp.getInt("cond1-target");
				jump = new ConditionalJump(cond0, cond1);
			}
			else {
				jump = DefaultJump.INSTANCE;
			}
			row.setJump(jump);

			table.addSignalRow(row);
		}

		return table;
	}
}
