package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.Graphics2D;

import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Memory;

public class MemorySprite extends CircuitSprite
{
	private final Memory _memory;

	public MemorySprite(Memory memory)
	{
		_memory = checkNotNull(memory);
	}

	@Override
	public void paint(Graphics2D g)
	{
		Bounds b = _memory.getBounds();

		g.drawRect(b.x, b.y, b.w, b.h);

		String name = "HS";

		int textWidth = g.getFontMetrics().stringWidth(name);
		int textHeight = g.getFontMetrics().getHeight();

		int textX = b.x + b.w / 2 - textWidth / 2;
		int textY = b.y + b.h / 2 + textHeight / 4;

		g.drawString(name, textX, textY);

		debugPin(g, _memory.getDataIn());
		debugPin(g, _memory.getDataOut());
		debugPin(g, _memory.getAdr());
		debugPin(g, _memory.getCs());
		debugPin(g, _memory.getRw());
	}
}