package de.uni_hannover.sra.minimax_simulator.io.exporter.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Map.Entry;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.uni_hannover.sra.minimax_simulator.io.ProjectExportException;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;

class SignalXmlExporter
{
	void write(Writer wr, SignalTable table) throws ProjectExportException
	{
		Document doc = new Document();
		DocType dc = new DocType("signaltable", "signal.dtd");
		doc.setDocType(dc);
		{
			Element tableElem = new Element("signaltable");
			doc.setRootElement(tableElem);

			for (int i = 0, n = table.getRowCount(); i < n; i++)
			{
				tableElem.addContent(createRowElement(table.getRow(i)));
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

	private Element createRowElement(SignalRow row)
	{
		Element rowElem = new Element("row");
		if (row.getLabel() != null)
			rowElem.setAttribute("label", row.getLabel());

		if (row.isBreakpoint())
			rowElem.setAttribute("breakpoint", "true");

		for (Entry<String, SignalValue> entry : row.getSignalValues().entrySet())
		{
			SignalValue value = entry.getValue();
			Element signalElem = new Element("signal");
			signalElem.setAttribute("name", entry.getKey());
			signalElem.setAttribute("value", Integer.toString(value.intValue()));
			if (value.isDontCare())
				signalElem.setAttribute("dontcare", "true");
			rowElem.addContent(signalElem);
		}

		Jump j = row.getJump();
		if (j instanceof UnconditionalJump)
		{
			String target = Integer.toString(((UnconditionalJump) j).getTargetRow());
			Element jumpElem = new Element("unconditional-jump");
			jumpElem.setAttribute("target", target);
			rowElem.addContent(jumpElem);
		}
		else if (j instanceof ConditionalJump)
		{
			ConditionalJump cj = (ConditionalJump) j;
			String target0 = Integer.toString(cj.getTargetRow(0));
			String target1 = Integer.toString(cj.getTargetRow(1));
			Element jumpElem = new Element("conditional-jump");
			jumpElem.setAttribute("cond0-target", target0);
			jumpElem.setAttribute("cond1-target", target1);
			rowElem.addContent(jumpElem);
		}

		return rowElem;
	}
}
