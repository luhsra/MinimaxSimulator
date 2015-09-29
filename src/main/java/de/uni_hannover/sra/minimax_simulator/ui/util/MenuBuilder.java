package de.uni_hannover.sra.minimax_simulator.ui.util;

import static com.google.common.base.Preconditions.*;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.JCustomMenuItem;

@Deprecated
public class MenuBuilder
{
	private final TextResource _res;

	public MenuBuilder(TextResource res)
	{
		_res = checkNotNull(res);
	}

	public class MenuAppender
	{
		private JMenu _menu;

		protected MenuAppender(JMenu menu)
		{
			_menu = menu;
		}

		public MenuAppender appendSeparator()
		{
			_menu.addSeparator();
			return this;
		}

		public MenuAppender appendItem(String key, Action action)
		{
			_menu.add(buildItem(key, action));
			return this;
		}

		public MenuAppender appendItem(JMenuItem item)
		{
			_menu.add(item);
			return this;
		}

		public JMenu menu()
		{
			return _menu;
		}
	}

	private void setupMenu(JMenu menu, String key)
	{
		String label = _res.get(key);
		String mnemonic = _res.get(key + ".mne");
		String icon = _res.get(key + ".icon");

		if (!label.isEmpty())
			menu.setText(label);
		if (!mnemonic.isEmpty())
			menu.setMnemonic(mnemonic.charAt(0));
		if (!icon.isEmpty())
			menu.setIcon(Icons.getInstance().get(icon));
	}

	public MenuAppender buildMenu(String key)
	{
		JMenu menu = new JMenu();
		setupMenu(menu, key);
		return new MenuAppender(menu);
	}

	public MenuAppender buildMenu(String key, Action action)
	{
		JMenu menu = new JMenu();
		menu.setAction(action);
		setupMenu(menu, key);
		return new MenuAppender(menu);
	}

	public JMenuItem buildItem(String key, Action action)
	{
		String label = _res.get(key);
		String mnemonic = _res.get(key + ".mne");
		String accelerator = _res.get(key + ".acc");
		String icon = _res.get(key + ".icon");

		JMenuItem item = new JCustomMenuItem(action);
		item.setText(label);
		if (!mnemonic.isEmpty())
			item.setMnemonic(mnemonic.charAt(0));
		if (!accelerator.isEmpty())
			item.setAccelerator(KeyStroke.getKeyStroke(accelerator));
		if (!icon.isEmpty())
			item.setIcon(Icons.getInstance().get(icon));

		return item;
	}

	public MenuAppender appendSeparatorTo(JMenu menu)
	{
		return new MenuAppender(menu).appendSeparator();
	}

	public MenuAppender appendItemTo(JMenu menu, String key, Action action)
	{
		return new MenuAppender(menu).appendItem(key, action);
	}

	public MenuAppender appendItemTo(JMenu menu, JMenuItem item)
	{
		return new MenuAppender(menu).appendItem(item);
	}
}
