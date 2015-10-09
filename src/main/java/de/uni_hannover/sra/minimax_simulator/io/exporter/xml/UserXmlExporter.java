package de.uni_hannover.sra.minimax_simulator.io.exporter.xml;

import java.io.IOException;
import java.io.Writer;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.uni_hannover.sra.minimax_simulator.model.user.ProjectConfiguration;

@Deprecated
class UserXmlExporter
{
	void write(Writer wr, ProjectConfiguration projectConfig) throws IOException
	{
		Document doc = new Document();
		{
			Element root = new Element("project");
			doc.setRootElement(root);

			//Element tabs = buildTabsElement(projectConfig);
			//root.addContent(tabs);
		}

		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
		output.output(doc, wr);
	}

//	private Element buildTabsElement(ProjectConfiguration config)
//	{
//		Element tabs = new Element("tabs");
//		{
//			String selectedTab = config.getSelectedTabKey();
//			tabs.setAttribute("selectedTab", selectedTab);
//
//			for (String openTab : config.getOpenTabKeys())
//			{
//				Element tab = new Element("tab");
//				tab.setAttribute("key", openTab);
//				tabs.addContent(tab);
//			}
//		}
//		return tabs;
//	}
}
