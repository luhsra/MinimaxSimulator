package de.uni_hannover.sra.minimax_simulator.ui.common;

import javax.swing.Icon;
import javax.swing.JComponent;

import de.uni_hannover.sra.minimax_simulator.ui.common.components.tabbed.CloseableTab;

@Deprecated
public class AbstractTab<T extends JComponent & Disposable> implements CloseableTab
{
	private String	_title;
	private Icon	_icon;
	private T		_panel;

	public AbstractTab()
	{
	}

	public AbstractTab(String title, Icon icon, T panel)
	{
		_title = title;
		_icon = icon;
		_panel = panel;
	}

	@Override
	public String getTitle()
	{
		return _title;
	}

	@Override
	public Icon getIcon()
	{
		return _icon;
	}

	@Override
	public T getComponent()
	{
		return _panel;
	}

	protected void setComponent(T panel)
	{
		_panel = panel;
	}

	protected void setTitle(String title)
	{
		_title = title;
	}

	protected void setIcon(Icon icon)
	{
		_icon = icon;
	}

	@Override
	public void onClose()
	{
		_panel.dispose();
	}
}