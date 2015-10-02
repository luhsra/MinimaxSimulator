package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;
import javafx.scene.canvas.GraphicsContext;

import com.sun.javafx.tk.*;

/**
 * The ALU sprite.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class AluSprite extends CircuitSprite {

	private final static int[][] POINTS = new int[][] {
			// x    y
			{ -9,   0},
			{-34,  12},
			{-34,  44},
			{ 34,  10},
			{ 34, -10},
			{-34, -44},
			{-34, -12},
			{ -9,   0}
	};

	private final static String NAME = "ALU";
	private final static String PIN_A = "A";
	private final static String PIN_B = "B";

	private final Alu _alu;

	/**
	 * Initializes the {@code AluSprite}.
	 *
	 * @param alu
	 *          the {@link Alu} this sprite will represent
	 */
	public AluSprite(Alu alu) {
		_alu = checkNotNull(alu);
	}

	@Override
	public void paint(Graphics2D g)
	{
		debugBounds(g, _alu.getBounds());

		int xCenter = _alu.getBounds().x + _alu.getBounds().w / 2;
		int yCenter = _alu.getBounds().y + _alu.getBounds().h / 2;

		for (int i = 1; i < POINTS.length; i++)
			g.drawLine(POINTS[i - 1][0] + xCenter, POINTS[i - 1][1] + yCenter,
				POINTS[i][0] + xCenter, POINTS[i][1] + yCenter);

		FontMetrics fm = g.getFontMetrics();
		
		int xTextAlu = xCenter - fm.stringWidth(NAME) / 2 + 16;
		int yTextAlu = yCenter + fm.getHeight() / 4;

		int xTextA = xCenter - fm.stringWidth(PIN_A) / 2 - 25;
		int yTextA = yCenter + fm.getHeight() / 4 - 23;

		int xTextB = xTextA;
		int yTextB = yCenter + fm.getHeight() / 4 + 23;

		g.drawString(NAME, xTextAlu, yTextAlu);
		g.drawString(PIN_A, xTextA, yTextA);
		g.drawString(PIN_B, xTextB, yTextB);

		debugPin(g, _alu.getInA());
		debugPin(g, _alu.getInB());
		debugPin(g, _alu.getInCtrl());
		debugPin(g, _alu.getOutData());
		debugPin(g, _alu.getOutZero());
	}

	@Override
	public void paint(GraphicsContext gc) {
		debugBounds(gc, _alu.getBounds());

		int xCenter = _alu.getBounds().x + _alu.getBounds().w / 2;
		int yCenter = _alu.getBounds().y + _alu.getBounds().h / 2;

		for (int i = 1; i < POINTS.length; i++) {
			gc.strokeLine(POINTS[i - 1][0] + xCenter, POINTS[i - 1][1] + yCenter, POINTS[i][0] + xCenter, POINTS[i][1] + yCenter);
		}

		com.sun.javafx.tk.FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont());

		double xTextAlu = xCenter - fm.computeStringWidth(NAME) / 2 + 16;
		double yTextAlu = yCenter + fm.getLineHeight() / 4;

		double xTextA = xCenter - fm.computeStringWidth(PIN_A) / 2 - 25;
		double yTextA = yCenter + fm.getLineHeight() / 4 - 23;

		double xTextB = xTextA;
		double yTextB = yCenter + fm.getLineHeight() / 4 + 23;

		gc.fillText(NAME, xTextAlu, yTextAlu);
		gc.fillText(PIN_A, xTextA, yTextA);
		gc.fillText(PIN_B, xTextB, yTextB);

		debugPin(gc, _alu.getInA());
		debugPin(gc, _alu.getInB());
		debugPin(gc, _alu.getInCtrl());
		debugPin(gc, _alu.getOutData());
		debugPin(gc, _alu.getOutZero());
	}

}