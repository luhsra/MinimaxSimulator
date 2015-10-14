package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import com.sun.javafx.tk.Toolkit;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The sprite of the control unit (CU).
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class CuLabelSprite extends CircuitSprite {

	private final Label					_label;

	/**
	 * Initializes the {@code CuLabelSprite}.
	 *
	 * @param label
	 *          the CU's label
	 */
	public CuLabelSprite(Label label) {
		_label = checkNotNull(label);
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
		gc.save();

		// change parameters and stroke rectangle
		gc.setLineWidth(1.0f);
		gc.setLineCap(StrokeLineCap.BUTT);
		gc.setLineJoin(StrokeLineJoin.MITER);
		gc.setMiterLimit(10.0f);
		gc.setLineDashes(10.0f);
		gc.setLineDashOffset(0.0f);
		gc.strokeRect(b.x + 0.5, b.y + 0.5, b.w, b.h);

		// restore old parameters
		gc.restore();
	}
}