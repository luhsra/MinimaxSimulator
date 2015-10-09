package de.uni_hannover.sra.minimax_simulator.ui.common.components;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

@Deprecated
public class JCollapsablePanel extends JPanel
{
	private final Icon			_expandedIcon;
	private final Icon			_collapsedIcon;

	private final TitlePanel	_titlePanel;
	private final JComponent	_content;

	private boolean				_expanded;

	public JCollapsablePanel(String title, Icon collapsedIcon, Icon expandedIcon,
			JComponent content)
	{
		this(title, collapsedIcon, expandedIcon, content, true);
	}

	public JCollapsablePanel(String title, Icon collapsedIcon, Icon expandedIcon,
			JComponent content, boolean isExpanded)
	{
		_expanded = isExpanded;
		_content = content;

		_expandedIcon = expandedIcon;
		_collapsedIcon = collapsedIcon;

		_titlePanel = new TitlePanel(title);

		setLayout(new BorderLayout());
		add(_content, BorderLayout.CENTER);
		add(_titlePanel, BorderLayout.NORTH);

		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		if (!_expanded)
			_content.setVisible(false);
	}

	public void setExpanded(boolean expanded)
	{
		if (expanded == _expanded)
			return;

		_expanded = expanded;
		_content.setVisible(_expanded);
		_titlePanel.updateIcon();

		revalidate();
	}

	private class TitlePanel extends JPanel implements MouseListener
	{
		private final String	_title;
		private final JLabel	_iconLabel;

		public TitlePanel(String title)
		{
			_title = title;

			_iconLabel = new JLabel(_expanded ? _expandedIcon : _collapsedIcon);
			_iconLabel.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 3));

			Border innerBorder = BorderFactory.createEmptyBorder(2, 4, 2, 4);
			Border outerBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

			setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
			setLayout(new BorderLayout());
			add(new JLabel(_title), BorderLayout.WEST);
			add(_iconLabel, BorderLayout.EAST);

			addMouseListener(this);
		}

		public void updateIcon()
		{
			_iconLabel.setIcon(_expanded ? _expandedIcon : _collapsedIcon);
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			setExpanded(!_expanded);
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
		}
	}
}
