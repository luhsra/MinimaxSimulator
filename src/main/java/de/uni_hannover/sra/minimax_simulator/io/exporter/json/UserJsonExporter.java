package de.uni_hannover.sra.minimax_simulator.io.exporter.json;

import de.uni_hannover.sra.minimax_simulator.model.user.ProjectConfiguration;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Writer;

/**
 * An Exporter that exports a {@link ProjectConfiguration} as a JSON string.
 *
 * @author Philipp Rohde
 */
class UserJsonExporter {

    /**
     * Writes the {@link ProjectConfiguration} as a JSON string using the given {@link Writer}.
     *
     * @param wr
     *             the writer to use for output
     * @param projectConfig
     *             the ProjectConfiguration to export
     * @throws IOException
     *             thrown if there is an I/O error during export
     */
    void write(Writer wr, ProjectConfiguration projectConfig) throws IOException {
        JSONObject root = new JSONObject();
        JSONObject project = new JSONObject();

        root.put("project", project);

        wr.write(root.toString(2));
        wr.flush();
    }

//  private Element buildTabsElement(ProjectConfiguration config)
//  {
//      Element tabs = new Element("tabs");
//      {
//          String selectedTab = config.getSelectedTabKey();
//          tabs.setAttribute("selectedTab", selectedTab);
//
//          for (String openTab : config.getOpenTabKeys())
//          {
//              Element tab = new Element("tab");
//              tab.setAttribute("key", openTab);
//              tabs.addContent(tab);
//          }
//      }
//      return tabs;
//  }
}
