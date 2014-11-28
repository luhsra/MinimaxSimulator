package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;

public class AluSprite extends CircuitSprite
{
	private final static int[][] POINTS = new int[][] {
			// x  y
			{-9, 0 },
			{-34, 12},
			{-34, 44},
			{34, 10},
			{34, -10},
			{-34, -44},
			{-34, -12},
			{-9, 0}
	};

	private final static String NAME = "ALU";
	private final static String PIN_A = "A";
	private final static String PIN_B = "B";

	private final Alu _alu;

	public AluSprite(Alu alu)
	{
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
}