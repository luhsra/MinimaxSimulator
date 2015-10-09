/* Adapted from:
 * http://stackoverflow.com/questions/60269/how-to-implement-draggable-tab-using-java-swing Original
 * author: Tom Martin StackOverflow.com contents is Creative Commons Share-Alike (Attribution
 * Required) license */

package de.uni_hannover.sra.minimax_simulator.ui.common.components.tabbed;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

@Deprecated
public class JDraggableTabPanel extends JTabbedPane
{
	private boolean	_dragging				= false;
	private Image	_tabImage				= null;
	private Point	_currentMouseLocation	= null;

	private int		_draggedTabIndex		= -1;

	public JDraggableTabPanel()
	{
		addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				if (!_dragging)
				{
					// Gets the tab index based on the mouse position
					int tabNumber = getUI().tabForCoordinate(
						JDraggableTabPanel.this, e.getX(), e.getY());

					if (tabNumber == -1)
						return;

					onDragStart(tabNumber);
					initiateDrag(tabNumber);
				}
				else
				{
					_currentMouseLocation = e.getPoint();

					// Need to repaint
					repaint();
				}
			}
		});

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				try
				{
					if (_dragging)
					{
						int tabNumber = getUI().tabForCoordinate(
							JDraggableTabPanel.this, e.getX(), 10);

						onDragStop(_draggedTabIndex, tabNumber);

						if (tabNumber != -1 && _draggedTabIndex != tabNumber)
						{
							moveTab(_draggedTabIndex, tabNumber);
						}
					}
				}
				finally
				{
					if (_dragging)
					{
						repaint();
					}
					_dragging = false;
					_tabImage = null;
					_draggedTabIndex = -1;
				}
			}
		});
	}

	private void initiateDrag(int tabNumber)
	{
		_draggedTabIndex = tabNumber;

		_tabImage = paintTabbedPanelImage(tabNumber);

		_dragging = true;
		repaint();
	}

	private Image paintTabbedPanelImage(int tabNumber)
	{
		Rectangle bounds = getUI().getTabBounds(JDraggableTabPanel.this,
			tabNumber);

		// Paint the tabbed pane to a buffer
		Image totalImage = new BufferedImage(getWidth(), getHeight(),
			BufferedImage.TYPE_INT_ARGB);
		Graphics totalGraphics = totalImage.getGraphics();
		totalGraphics.setClip(bounds);
		// Don't be double buffered when painting to a static image.
		setDoubleBuffered(false);
		paintComponent(totalGraphics);
		totalGraphics.dispose();

		// Paint just the dragged tab to the buffer
		Image image = new BufferedImage(bounds.width, bounds.height,
			BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = image.getGraphics();
		graphics.drawImage(totalImage, 0, 0, bounds.width, bounds.height,
			bounds.x, bounds.y, bounds.x + bounds.width, bounds.y
				+ bounds.height, JDraggableTabPanel.this);

		if (getTabComponentAt(tabNumber) != null)
		{
			JComponent tabComponent = (JComponent) getTabComponentAt(tabNumber);
			tabComponent.paint(graphics);
		}

		graphics.dispose();

		
		return image;
	}

	protected void moveTab(int fromTabNumber, int toTabNumber)
	{
		Icon icon = getIconAt(fromTabNumber);
		Component tab = getTabComponentAt(fromTabNumber);
		Component comp = getComponentAt(fromTabNumber);
		String title = getTitleAt(fromTabNumber);
		String tip = getToolTipTextAt(fromTabNumber);

		// Prevent hierarchy listeners from firing (since the tabs are only moved)
		final HierarchyListener[] hierarchyListeners = comp.getListeners(HierarchyListener.class);
		for (HierarchyListener l : hierarchyListeners)
			comp.removeHierarchyListener(l);

		removeTabAt(fromTabNumber);
		insertTab(title, icon, comp, tip, toTabNumber);
		setTabComponentAt(toTabNumber, tab);

		// re-add listeners
		for (HierarchyListener l : hierarchyListeners)
			comp.addHierarchyListener(l);

		setSelectedIndex(toTabNumber);
	}

	protected void onDragStop(int fromTabNumber, int toTabNumber)
	{
	}

	protected void onDragStart(int fromTabNumber)
	{
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Are we dragging?
		if (_dragging && _currentMouseLocation != null && _tabImage != null)
		{
			// Draw the dragged tab
			g.drawImage(_tabImage, _currentMouseLocation.x,
				_currentMouseLocation.y, this);
		}
	}
}