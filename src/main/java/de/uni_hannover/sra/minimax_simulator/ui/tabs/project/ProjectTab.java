package de.uni_hannover.sra.minimax_simulator.ui.tabs.project;

import javax.swing.JComponent;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.AbstractTab;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;

@Deprecated
public class ProjectTab<T extends JComponent & Disposable> extends AbstractTab<T>
{
	public ProjectTab(String key, T panel)
	{
		TextResource res = Application.getTextResource("project");
		String title = res.get("tab." + key + ".title");
		String icon = res.get("tab." + key + ".icon");

		setTitle(title);
		if (!icon.isEmpty())
			setIcon(Icons.getInstance().get(icon));

		setComponent(panel);
	}
}
