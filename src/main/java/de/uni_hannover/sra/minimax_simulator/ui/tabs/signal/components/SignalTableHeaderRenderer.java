package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;

import de.uni_hannover.sra.minimax_simulator.ui.common.renderer.VerticalTableHeaderCellRenderer;

@Deprecated
public class SignalTableHeaderRenderer extends VerticalTableHeaderCellRenderer
{
//	private final static Painter	_backgroundPainter;
//
//	static
//	{
//		Object o = UIManager.get("TableHeader:\"TableHeader.renderer\"[Enabled+Sorted].backgroundPainter");
//
//		Painter painter;
//		try
//		{
//			painter = new JdkPainter<JComponent>(o);
//		}
//		catch (Exception e)
//		{
//			System.err.println("No JDK table header painter supported: " + e.toString());
//			e.printStackTrace();
//			painter = new DummyPainter<JComponent>();
//		}
//
//		_backgroundPainter = painter;
//	}
//
//	private static interface Painter<T>
//	{
//		public void paint(Graphics2D g2d, T object, int height, int width);
//	}
//
//	private static class JdkPainter<T> implements Painter<T>
//	{
//		private final Method	_paintMethod;
//		private final Object	_painter;
//
//		public JdkPainter(Object painter) throws Exception
//		{
//			_painter = checkNotNull(painter);
//
//			Class<?> painterClass = Class.forName("javax.swing.Painter");
//			checkArgument(painterClass.isInstance(painter));
//
//			_paintMethod = painterClass.getMethod("paint", Graphics2D.class,
//				Object.class, int.class, int.class);
//		}
//
//		@Override
//		public void paint(Graphics2D g2d, T object, int height, int width)
//		{
//			try
//			{
//				_paintMethod.invoke(_painter, g2d, object, height, width);
//			}
//			catch (Exception e)
//			{
//				throw Throwables.propagate(e);
//			}
//		}
//	}
//
//	private static class DummyPainter<T> implements Painter<T>
//	{
//		@Override
//		public void paint(Graphics2D g2d, T object, int height, int width)
//		{
//		}
//	}

//	private final Border	_border	= BorderFactory.createCompoundBorder(new LineBorder(
//										Color.BLACK, 1), new EmptyBorder(4, 4, 4, 4));

	private final Color		_backgnd		= new Color(231, 232, 239);
	private final Color		_borderColor	= new Color(68, 89, 108);
	private final Color		_foregnd		= new Color(30, 30, 76);
	private final Font		_font			= new Font("SansSerif", Font.PLAIN, 13);
//	private final Font		_boldFont		= _font.deriveFont(Font.BOLD);
	private final Border	_border			= BorderFactory.createCompoundBorder(
												BorderFactory.createLineBorder(
													_borderColor, 1),
												BorderFactory.createEmptyBorder(3, 3, 5,
													3));

	public SignalTableHeaderRenderer()
	{
//		setHorizontalAlignment(LEFT);
//		setHorizontalTextPosition(CENTER);
//		setVerticalAlignment(CENTER);
//		setVerticalTextPosition(TOP);
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
//		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
//			column);

//		if (column == 0)
//			setFont(_boldFont);
//		else
		setFont(_font);

		setText(value.toString());
		setBackground(_backgnd);
		setForeground(_foregnd);
		setBorder(_border);

		return this;
	}

	@Override
	public void paintComponent(Graphics g)
	{
//		if (g instanceof Graphics2D)
//		{
//			Graphics2D g2 = (Graphics2D) g.create();
//			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//			super.paintComponent(g2);
//			g2.dispose();
//		}
//		else
		super.paintComponent(g);
	}
}
