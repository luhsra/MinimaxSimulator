package de.uni_hannover.sra.minimax_simulator.io.importer.json;

import de.uni_hannover.sra.minimax_simulator.io.importer.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.model.user.ProjectConfiguration;
import org.json.JSONException;

/**
 * An Importer that imports a {@link ProjectConfiguration} from a JSON string.
 *
 * @author Martin L&uuml;ck
 */
class UserJsonImporter extends Importer {

    /**
     * Imports the {@link ProjectConfiguration} from a JSON string.
     *
     * @param input
     *            the JSON string containing the saved ProjectConfiguration
     * @return
     *            the imported ProjectConfiguration
     * @throws JSONException
     *            thrown if there is an error during parsing the JSON string
     * @throws ProjectImportException
     *            thrown if there is an error during import
     */
    ProjectConfiguration loadProjectConfiguration(String input) throws ProjectImportException {
//      Element root = rootOf(parseAndValidate(inputStream), "project");
        return new ProjectConfiguration();
    }

//  private ProjectConfiguration readProjectConfiguration(Element projectElement) throws Exception
//  {
//      Element tabs = projectElement.getChild("tabs");
//      if (tabs == null)
//          throw new Exception("Missing <tabs> entry found in <project>");
//
//      ProjectConfiguration config = new ProjectConfiguration();
//
//      String selectedTab = tabs.getAttributeValue("selectedTab");
//      if (selectedTab == null)
//          throw new Exception("Missing \"selectedTab\" attribute in <tabs> entry");
//
//      config.setSelectedTabKey(selectedTab);
//
//      for (Element tab : tabs.getChildren("tab"))
//      {
//          String tabKey = tab.getAttributeValue("key");
//          if (tabKey == null)
//              throw new Exception("Missing \"key\" attribute in <tab> entry");
//
//          config.getOpenTabKeys().add(tabKey);
//      }
//
//      return config;
//  }
}