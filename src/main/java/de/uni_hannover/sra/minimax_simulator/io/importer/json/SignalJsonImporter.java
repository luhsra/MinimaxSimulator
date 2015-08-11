package de.uni_hannover.sra.minimax_simulator.io.importer.json;

import de.uni_hannover.sra.minimax_simulator.io.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.model.signal.DefaultSignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.DefaultJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;
import org.json.JSONArray;
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
	 * @throws ProjectImportException
	 *            thrown if there is an error during import
	 */
	SignalTable loadSignalTable(String input) throws ProjectImportException {
		SignalTable table = new DefaultSignalTable();

		System.out.println("DEBUG: creating root object");
		JSONObject root = new JSONObject(input);
		System.out.println("DEBUG: creating signaltable object");
		JSONObject signaltable = root.getJSONObject("signaltable");
		System.out.println("DEBUG: creating rows array");
		JSONArray rows = signaltable.getJSONArray("row");
		for (int i = 0; i < rows.length(); i++) {
			SignalRow row = new SignalRow();

			System.out.println("DEBUG: creating current row object");
			JSONObject currentRow = rows.getJSONObject(i);
			//System.out.println("DEBUG: result: " + currentRow.toString());
			System.out.println("DEBUG: check if row has label");
			if (currentRow.has("-label")) {
				String label = currentRow.getString("-label");
				row.setLabel(label);
			}
			System.out.println("DEBUG: check if row has breakpoint");
			if (currentRow.has("breakpoint")) {
				boolean breakpoint = currentRow.getBoolean("breakpoint");
				row.setBreakpoint(breakpoint);
			}

			System.out.println("DEBUG: get signal array");
			JSONArray signals = currentRow.getJSONArray("signal");
			System.out.println("DEBUG: length of signal array: " + signals.length());
			for (int j = 0; j < signals.length(); j++) {
				System.out.println("DEBUG: get the " + j + "th signal");
				JSONObject currentSignal = signals.getJSONObject(j);
				System.out.println("DEBUG: get intValue");
				//int intValue = currentSignal.getInt("-value");
				int intValue = Integer.valueOf(currentSignal.getString("-value"));
				boolean dontcare = false;
				System.out.println("DEBUG: check if signal has dontcare");
				if (currentSignal.has("dontcare")) {
					dontcare = currentSignal.getBoolean("dontcare");
				}
				System.out.println("DEBUG: create SignalValue");
				SignalValue value = dontcare ? SignalValue.DONT_CARE : SignalValue.valueOf(intValue);
				System.out.println("DEBUG: set SignalValue to row");
				row.setSignal(currentSignal.getString("-name"), value);
			}

			Jump jump;

			System.out.println("DEBUG: check if row has uncond jump or cond jump");
			if (currentRow.has("unconditional-jump")) {
				System.out.println("DEBUG: uncond jump detected");
				JSONObject jmp = currentRow.getJSONObject("unconditional-jump");
				jump = new UnconditionalJump(jmp.getInt("-target"));
			}
			else if (currentRow.has("conditional-jump")) {
				System.out.println("DEBUG: cond jump detected");
				JSONObject jmp = currentRow.getJSONObject("conditional-jump");
				int cond0 = jmp.getInt("-cond0-target");
				int cond1 = jmp.getInt("-cond1-target");
				jump = new ConditionalJump(cond0, cond1);
			}
			else {
				System.out.println("DEBUG: no jump detected");
				jump = DefaultJump.INSTANCE;
			}
			row.setJump(jump);

			table.addSignalRow(row);
		}

		return table;
	}
}
