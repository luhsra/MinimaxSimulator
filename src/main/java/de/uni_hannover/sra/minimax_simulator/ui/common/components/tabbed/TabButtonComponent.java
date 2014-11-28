/*
 * See: http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabComponentsDemoProject/src/components/ButtonTabComponent.java 
 * 
 * 
 * 
 */

package de.uni_hannover.sra.minimax_simulator.ui.common.components.tabbed;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class TabButtonComponent extends JPanel
{
	private final JCloseableTabPanel _pane;

	private boolean _isClosingEnabled = true;
	private TabButton _button;

	public TabButtonComponent(final JCloseableTabPanel pane, final Tab theTab)
	{
		// unset default FlowLayout' gaps
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		if (pane == null)
		{
			throw new NullPointerException("TabbedPane is null");
		}
		this._pane = pane;
		setOpaque(false);

		// make JLabel read titles from JTabbedPane
		JLabel label = new JLabel()
		{
			@Override
			public String getText()
			{
				return theTab.getTitle();
			}

			@Override
			public Icon getIcon()
			{
				return theTab.getIcon();
			}
		};

		add(label);
		// add more space between the label and the button
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		// tab button
		_button = new TabButton();
		add(_button);
		// add more space to the top of the component
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}

	private class TabButton extends JButton implements ActionListener
	{
		public TabButton()
		{
			int size = 17;
			setPreferredSize(new Dimension(size, size));
			//setToolTipText("close this tab");
			// Make the button looks the same for all Laf's
			setUI(new BasicButtonUI());
			// Make it transparent
			setContentAreaFilled(false);
			// No need to be focusable
			setFocusable(false);
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			setBorderPainted(false);
			// Making nice rollover effect
			// we use the same listener for all buttons
			addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			// Close the proper tab by clicking the button
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int i = _pane.indexOfTabComponent(TabButtonComponent.this);
			if (i != -1)
			{
				_pane.closeTab(i);
			}
		}

		// we don't want to update UI for this button
		@Override
		public void updateUI()
		{
		}

		// paint the cross
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			// shift the image for pressed buttons
			if (getModel().isPressed())
			{
				g2.translate(1, 1);
			}
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.GRAY);
			if (getModel().isRollover())
			{
				g2.setColor(Color.BLACK);
			}
			int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2.dispose();
		}
	}

	private final MouseListener	buttonMouseListener	= new MouseAdapter()
	{
		@Override
		public void mouseEntered(MouseEvent e)
		{
			if (_isClosingEnabled && !_button.isBorderPainted())
				_button.setBorderPainted(true);
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			if (_button.isBorderPainted())
				_button.setBorderPainted(false);
		}
	};

	public boolean isClosingEnabled()
	{
		return _isClosingEnabled;
	}

	public void setClosingEnabled(boolean isClosingEnabled)
	{
		_isClosingEnabled = isClosingEnabled;
	}
}
