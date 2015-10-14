package de.uni_hannover.sra.minimax_simulator.io.importer.json;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.io.importer.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfigurationBuilder;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.*;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.resources.ResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An Importer that imports a {@link MachineConfiguration} from a JSON string.
 *
 * @author Philipp Rohde
 */
class MachineJsonImporter extends Importer {

	/**
	 * Imports the {@link MachineConfiguration} from a JSON string.
     *
	 * @param input
	 *            the JSON string containing the saved MachineConfiguration
	 * @return
	 *            the imported MachineConfiguration
     * @throws JSONException
     *            thrown if there is an error during parsing the JSON string
	 * @throws ProjectImportException
	 *            thrown if there is an error during import
	 */
	MachineConfiguration loadMachine(String input) throws JSONException, ProjectImportException {
		JSONObject root = new JSONObject(input);
		JSONObject machine = root.getJSONObject("machine");

		JSONObject alu = machine.getJSONObject("alu");

		JSONObject registers = machine.getJSONObject("registers");

		// TODO: remove global access?
		ResourceBundleLoader resourceLoader = Main.getResourceLoader();
		TextResource registerTextResource = resourceLoader.getTextResource("register");

		MachineConfigurationBuilder mb = new MachineConfigurationBuilder();
		mb.addDefaultBaseRegisters(registerTextResource);
		mb.getAluOperations().addAll(loadAlu(alu));
		mb.getRegisterExtensions().addAll(loadRegisters(registers));

		JSONArray muxInputList = machine.getJSONArray("muxInputs");
		for (int i = 0; i < muxInputList.length(); i++) {
			MuxType type = get(MuxType.class, muxInputList.getJSONObject(i).getString("muxType"));
			mb.getSelectedMuxInputs(type).addAll(loadMuxInputs(muxInputList.getJSONObject(i).getJSONArray("input"), mb.getAllowedMuxInputs()));
		}

		return mb.build();
	}

	/**
	 * Creates a list of all ALU operations.
	 *
	 * @param aluElem
	 *            the ALU as JSONObject
	 * @return
	 *            a list of all saved ALU operations
     * @throws JSONException
     *            thrown if there is an error during parsing the JSON string
	 * @throws ProjectImportException
	 *            thrown if creation of object of type {@code AluOperation} fails
	 */
	private List<AluOperation> loadAlu(JSONObject aluElem) throws JSONException, ProjectImportException {
		List<AluOperation> ops = new ArrayList<>();

		JSONArray savedOPs = aluElem.getJSONArray("operation");
		for (int i = 0; i < savedOPs.length(); i++) {
			ops.add(get(AluOperation.class, savedOPs.getString(i)));
		}

		return ops;
	}

	/**
	 * Creates a list of all extended registers.
	 *
	 * @param registerList
	 *            the registers as JSON object
	 * @return
	 *            a list of all saved extended registers
     * @throws JSONException
     *            thrown if there is an error during parsing the JSON string
	 * @throws ProjectImportException
	 *            thrown if creation of object of type {@code RegisterSize} fails
	 */
	private List<RegisterExtension> loadRegisters(JSONObject registerList) throws JSONException, ProjectImportException {
		List<RegisterExtension> registers = new ArrayList<>();
		JSONArray savedRegisters = registerList.getJSONArray("register");
		for (int i = 0; i < savedRegisters.length(); i++) {
			JSONObject reg = savedRegisters.getJSONObject(i);
			String name = reg.getString("name");
			RegisterSize size = get(RegisterSize.class, reg.getString("size"));
			String description = reg.getString("description");

			registers.add(new RegisterExtension(name, size, description, true));
		}

		return registers;
	}

	/**
	 * Creates a list of the MuxInputs.
	 *
	 * @param muxInputList
	 *            the JSONArray containing the MuxInputs
	 * @param available
	 *            a list of all available MuxInputs (not used)
	 * @return
	 *            a list of all saved MuxInputs
     * @throws JSONException
     *            thrown if there is an error during parsing the JSON string
	 */
	private List<MuxInput> loadMuxInputs(JSONArray muxInputList, List<MuxInput> available) throws JSONException {
		ArrayList<MuxInput> result = new ArrayList<>();

		for (int i = 0; i < muxInputList.length(); i++) {
			JSONObject input = muxInputList.getJSONObject(i);
			String type = input.getString("type");
			String value = input.getString("value");
			if ("constant".equals(type)) {
				result.add(new ConstantMuxInput(Integer.parseInt(value)));
			}
			else if ("register".equals(type)) {
				result.add(new RegisterMuxInput(value));
			}
			else if ("null".equals(type)) {
				result.add(NullMuxInput.INSTANCE);
			}

			// TODO: remove old element names that are clashing with stored registers of the machine
//			else if ("constant".equals(elem.getName()))
//			{
//				result.add(new ConstantMuxInput(getInt(elem)));
//			}
//			else if ("register".equals(elem.getName()))
//			{
//				result.add(new RegisterMuxInput(getNonEmptyString(elem)));
//			}
//			else if ("null".equals(elem.getName()))
//			{
//				result.add(NullMuxInput.INSTANCE);
//			}
		}
		return result;
	}

}