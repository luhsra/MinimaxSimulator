package de.uni_hannover.sra.minimax_simulator.io.exporter.json;

import de.uni_hannover.sra.minimax_simulator.io.ProjectExportException;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.*;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Writer;

/**
 * An Exporter that writes a {@link MachineConfiguration} as a JSON string.
 *
 * @author Philipp Rohde
 */
class MachineJsonExporter {

	/**
	 * Writes the {@link MachineConfiguration} as a JSON string using the given {@link Writer}.
	 *
	 * @param wr
	 *             the writer to use for output
	 * @param machineConf
	 *             the MachineConfiguration to export
	 * @throws ProjectExportException
	 *             thrown if there is an I/O error during export
	 */
	void write(Writer wr, MachineConfiguration machineConf) throws ProjectExportException {
		JSONObject root = new JSONObject();
		JSONObject machine = new JSONObject();

		JSONObject alu = new JSONObject();
		for (AluOperation op : machineConf.getAluOperations()) {
			alu.accumulate("operation", op.name());
		}
		machine.put("alu", alu);

		JSONObject registers = new JSONObject();
		for (RegisterExtension register : machineConf.getRegisterExtensions())
		{
			if (!register.isExtended()) {
				continue;
			}

			JSONObject currentRegister = new JSONObject();
			currentRegister.put("-name", register.getName());
			currentRegister.put("-size", register.getSize().name());
			currentRegister.put("#text", register.getDescription());
			registers.accumulate("register", currentRegister);
		}
		machine.put("registers", registers);

		for (MuxType mux : MuxType.values()) {
			JSONObject currentMux = new JSONObject();
			currentMux.put("-muxType", mux.name());

			for (MuxInput input : machineConf.getMuxSources(mux)) {
				JSONObject muxInput = new JSONObject();
				String type;
				String content;
				if (input instanceof ConstantMuxInput)
				{
					content = Integer.toString(((ConstantMuxInput) input)
							.getConstant());
					type = "constant";
				}
				else if (input instanceof RegisterMuxInput)
				{
					content = ((RegisterMuxInput) input).getRegisterName();
					type = "register";
				}
				else if (input instanceof NullMuxInput)
				{
					content = "";
					type = "null";
				}
				else {
					throw new ProjectExportException("Cannot export an instance of " + input.getClass());
				}
				muxInput.put("-type", type);
				muxInput.put("#text", content);
				currentMux.accumulate("input", muxInput);
			}
			machine.accumulate("muxInputs", currentMux);
		}
		root.put("machine", machine);

		try {
			wr.write(root.toString(2));
			wr.flush();
		} catch (IOException e) {
			throw new ProjectExportException("Cannot export due to I/O error: " + e.getMessage(), e);
		}
	}
}
