package de.uni_hannover.sra.minimax_simulator.io.importer.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.uni_hannover.sra.minimax_simulator.Main;
import org.jdom2.Element;

import de.uni_hannover.sra.minimax_simulator.io.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfigurationBuilder;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.NullMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.resources.ResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

@Deprecated
class MachineXmlImporter extends Importer {

	MachineConfiguration loadMachine(InputStream inputStream) throws ProjectImportException	{
		Element machineElem = rootOf(parseAndValidate(inputStream), "machine");

		Element alu = machineElem.getChild("alu");
		if (alu == null)
			throw new ProjectImportException("No <alu> entry found in <machine>");
		Element registers = machineElem.getChild("registers");
		if (registers == null)
			throw new ProjectImportException("No <registers> entry found in <machine>");

		// TODO: remove global access?
		ResourceBundleLoader resourceLoader = Main.getResourceLoader();
		TextResource registerTextResource = resourceLoader.getTextResource("register");

		MachineConfigurationBuilder mb = new MachineConfigurationBuilder();
		mb.addDefaultBaseRegisters(registerTextResource);
		mb.getAluOperations().addAll(loadAlu(alu));
		mb.getRegisterExtensions().addAll(loadRegisters(registers));

		for (Element muxInputList : machineElem.getChildren("muxInputs"))
		{
			MuxType type = get(MuxType.class, muxInputList, "muxType");
			mb.getSelectedMuxInputs(type).addAll(
				loadMuxInputs(muxInputList, mb.getAllowedMuxInputs()));
		}

		return mb.build();
	}

	private List<AluOperation> loadAlu(Element aluElem) throws ProjectImportException
	{
		List<AluOperation> ops = new ArrayList<AluOperation>();
		for (Element op : aluElem.getChildren("operation"))
			ops.add(get(AluOperation.class, op));

		return ops;
	}

	private List<RegisterExtension> loadRegisters(Element registerList)
			throws ProjectImportException
	{
		List<RegisterExtension> registers = new ArrayList<RegisterExtension>();
		for (Element registerElem : registerList.getChildren("register"))
		{
			String name = getNonEmptyString(registerElem, "name");
			RegisterSize size = get(RegisterSize.class, registerElem, "size");
			String description = registerElem.getText();

			registers.add(new RegisterExtension(name, size, description, true));
		}
		return registers;
	}

	private List<MuxInput> loadMuxInputs(Element muxInputList, List<MuxInput> available)
			throws ProjectImportException
	{
		ArrayList<MuxInput> result = new ArrayList<MuxInput>();
		for (Element elem : muxInputList.getChildren())
		{
			if ("input".equals(elem.getName()))
			{
				String type = elem.getAttributeValue("type");
				if ("constant".equals(type))
				{
					result.add(new ConstantMuxInput(getInt(elem)));
				}
				else if ("register".equals(type))
				{
					result.add(new RegisterMuxInput(getNonEmptyString(elem)));
				}
				else if ("null".equals(type))
				{
					result.add(NullMuxInput.INSTANCE);
				}
			}
			// TODO: remove old element names that are clashing with stored registers of
			// the machine
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