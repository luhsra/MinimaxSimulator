package de.uni_hannover.sra.minimax_simulator.io.importer.xml;

import java.io.InputStream;

import de.uni_hannover.sra.minimax_simulator.io.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.model.user.ProjectConfiguration;

class UserXmlImporter extends Importer
{
	ProjectConfiguration loadProjectConfiguration(InputStream stream) throws ProjectImportException
	{
//		Element root = rootOf(parseAndValidate(inputStream), "project");
		return new ProjectConfiguration();
	}

//	private ProjectConfiguration readProjectConfiguration(Element projectElement) throws Exception
//	{
//		Element tabs = projectElement.getChild("tabs");
//		if (tabs == null)
//			throw new Exception("Missing <tabs> entry found in <project>");
//
//		ProjectConfiguration config = new ProjectConfiguration();
//
//		String selectedTab = tabs.getAttributeValue("selectedTab");
//		if (selectedTab == null)
//			throw new Exception("Missing \"selectedTab\" attribute in <tabs> entry");
//
//		config.setSelectedTabKey(selectedTab);
//
//		for (Element tab : tabs.getChildren("tab"))
//		{
//			String tabKey = tab.getAttributeValue("key");
//			if (tabKey == null)
//				throw new Exception("Missing \"key\" attribute in <tab> entry");
//
//			config.getOpenTabKeys().add(tabKey);
//		}
//		
//		return config;	
//	}
}