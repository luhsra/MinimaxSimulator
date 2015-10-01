package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.sun.javafx.tk.Toolkit;
import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class CuLabelSprite extends CircuitSprite
{
	private final Label					_label;

	private final static float			dash[]		= { 10.0f };
	private final static BasicStroke	dashStroke	= new BasicStroke(1.0f,
														BasicStroke.CAP_BUTT,
														BasicStroke.JOIN_MITER, 10.0f,
														dash, 0.0f);

	public CuLabelSprite(Label label)
	{
		_label = checkNotNull(label);
	}

	@Override
	public void paint(Graphics2D g, RenderEnvironment env)
	{
		Bounds b = _label.getBounds();

		debugBounds(g, b);

		String message = _label.getMessage();

		int textWidth = g.getFontMetrics().stringWidth(message);
		int textHeight = g.getFontMetrics().getHeight();

		int textX = b.x + b.w / 2 - textWidth / 2;
		int textY = b.y + b.h / 2 + textHeight / 4 + 1;

		g.drawString(message, textX, textY);

		Stroke stroke = g.getStroke();
		g.setStroke(dashStroke);
		g.drawRect(b.x, b.y, b.w, b.h);
		g.setStroke(stroke);
	}

	@Override
	public void paint(GraphicsContext gc, RenderEnvironment env) {
		Bounds b = _label.getBounds();
		debugBounds(gc, b);

		String message = _label.getMessage();

		com.sun.javafx.tk.FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont());
		double textWidth = fm.computeStringWidth(message);
		double textHeight = fm.getLineHeight();

		double textX = b.x + b.w / 2 - textWidth / 2;
		double textY = b.y + b.h / 2 + textHeight / 4 + 1;

		gc.fillText(message, textX, textY);

		// save parameters
		double lineWidth = gc.getLineWidth();
		StrokeLineCap lineCap = gc.getLineCap();
		StrokeLineJoin lineJoin = gc.getLineJoin();
		double miterLimit = gc.getMiterLimit();
		double[] lineDashes = gc.getLineDashes();
		double lineDashOffset = gc.getLineDashOffset();

		// change parameters and stroke rectangle
		gc.setLineWidth(1.0f);
		gc.setLineCap(StrokeLineCap.BUTT);
		gc.setLineJoin(StrokeLineJoin.MITER);
		gc.setMiterLimit(10.0f);
		gc.setLineDashes(dash[0]);
		gc.setLineDashOffset(0.0f);
		gc.strokeRect(b.x, b.y, b.w, b.h);

		// restore old parameters
		gc.setLineWidth(lineWidth);
		gc.setLineCap(lineCap);
		gc.setLineJoin(lineJoin);
		gc.setMiterLimit(miterLimit);
		gc.setLineDashes(lineDashes);
		gc.setLineDashOffset(lineDashOffset);
	}
}