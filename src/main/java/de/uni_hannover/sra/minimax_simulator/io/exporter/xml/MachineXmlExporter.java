package de.uni_hannover.sra.minimax_simulator.io.exporter.xml;

import java.io.IOException;
import java.io.Writer;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.uni_hannover.sra.minimax_simulator.io.ProjectExportException;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.NullMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

class MachineXmlExporter
{
	void write(Writer wr, MachineConfiguration machine) throws ProjectExportException
	{
		Document doc = new Document();
		DocType dc = new DocType("machine", "machine.dtd");
		doc.setDocType(dc);
		{
			Element machineElem = new Element("machine");
			doc.setRootElement(machineElem);
			{
				Element aluElem = new Element("alu");
				machineElem.addContent(aluElem);
				{
					for (AluOperation op : machine.getAluOperations())
					{
						Element opElem = new Element("operation");
						opElem.setText(op.name());
						aluElem.addContent(opElem);
					}
				}

				Element registersElem = new Element("registers");
				machineElem.addContent(registersElem);
				{
					for (RegisterExtension register : machine.getRegisterExtensions())
					{
						if (!register.isExtended())
							continue;

						Element registerElem = new Element("register");
						registerElem.setAttribute("name", register.getName());
						registerElem.setAttribute("size", register.getSize().name());
						registerElem.addContent(register.getDescription());
						registersElem.addContent(registerElem);
					}
				}

				for (MuxType mux : MuxType.values())
				{
					Element multiplexElem = new Element("muxInputs");
					multiplexElem.setAttribute("muxType", mux.name());

					for (MuxInput input : machine.getMuxSources(mux))
					{
						Element inputElem = new Element("input");
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
						else
							throw new ProjectExportException("Cannot export an instance of "
								+ input.getClass());

						inputElem.setAttribute("type", type);
						inputElem.setText(content);
						multiplexElem.addContent(inputElem);
					}

					machineElem.addContent(multiplexElem);
				}
			}
		}

		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
		try
		{
			output.output(doc, wr);
		}
		catch (IOException e)
		{
			throw new ProjectExportException(
				"Cannot export due to I/O error: " + e.getMessage(), e);
		}
	}
}
