package de.uni_hannover.sra.minimax_simulator.io.importer.xml;

import java.io.InputStream;

import org.jdom2.Element;

import de.uni_hannover.sra.minimax_simulator.io.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.model.signal.DefaultSignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.DefaultJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;

class SignalXmlImporter extends Importer
{
	SignalTable loadSignalTable(InputStream inputStream) throws ProjectImportException
	{
		SignalTable table = new DefaultSignalTable();

		Element tableElem = rootOf(parseAndValidate(inputStream), "signaltable");
		for (Element rowElem : tableElem.getChildren("row"))
		{
			SignalRow row = new SignalRow();

			String label = rowElem.getAttributeValue("label", (String) null);
			row.setLabel(label);

			String breakPoint = rowElem.getAttributeValue("breakpoint", "false");
			row.setBreakpoint(Boolean.valueOf(breakPoint));

			for (Element signalElem : rowElem.getChildren("signal"))
			{
				int intValue = getInt(signalElem, "value");
				boolean dontcare = Boolean.parseBoolean(signalElem.getAttributeValue(
					"dontcare", "false"));
				SignalValue value = dontcare ? SignalValue.DONT_CARE
						: SignalValue.valueOf(intValue);
				row.setSignal(getNonEmptyString(signalElem, "name"), value);
			}

			Jump jump;
			Element uncondJumpElem = rowElem.getChild("unconditional-jump");
			if (uncondJumpElem != null)
			{
				jump = new UnconditionalJump(getInt(uncondJumpElem, "target"));
			}
			else
			{
				Element condJumpElem = rowElem.getChild("conditional-jump");
				if (condJumpElem != null)
				{
					int target0 = getInt(condJumpElem, "cond0-target");
					int target1 = getInt(condJumpElem, "cond1-target");
					jump = new ConditionalJump(target0, target1);
				}
				else
				{
					jump = DefaultJump.INSTANCE;
				}
			}
			row.setJump(jump);

			table.addSignalRow(row);
		}

		return table;
	}
}
