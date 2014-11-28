package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Multiplexer;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Pin;

public class MultiplexerSprite extends CircuitSprite
{
	private final Multiplexer _mux;

	private final static ArrayList<String> addressNumberCache = new ArrayList<String>();

	private static String intToStr(int i)
	{
		if (addressNumberCache.size() <= i)
			for (int c = addressNumberCache.size(); c <= i; c++)
				addressNumberCache.add(Integer.toString(c));
		return addressNumberCache.get(i);
	}

	public MultiplexerSprite(Multiplexer mux)
	{
		_mux = checkNotNull(mux);
	}

	@Override
	public void paint(Graphics2D g)
	{
		debugBounds(g, _mux.getBounds());

		Bounds b = _mux.getBounds();

		int textHeight = g.getFontMetrics().getAscent() - 3;

		// Upper/lower arc
		g.drawArc(b.x, b.y, b.w, b.w, 0, 180);
		g.drawArc(b.x, b.y + b.h - b.w, b.w, b.w, 180, 180);

		// Left/right line
		g.drawLine(b.x, b.y + b.w / 2, b.x, b.y + b.h - b.w / 2);
		g.drawLine(b.x + b.w, b.y + b.w / 2, b.x + b.w, b.y + b.h - b.w / 2);

		// Pin addresses
		//int availableHeight = b.h - 24;
		//int paY = b.y + (b.h - availableHeight) / 2;
		int labelX = b.x + b.w / 2;
		int labelY = 0;

		List<IngoingPin> pins = _mux.getDataInputs();

		if (!pins.isEmpty())
		{
			int addr = 0;
			for (Pin pin : _mux.getDataInputs())
			{
				String nrStr = intToStr(addr);
				int textWidth = g.getFontMetrics().stringWidth(nrStr);

				labelY = pin.getY();
				
				g.drawString(intToStr(addr), labelX - textWidth / 2, labelY + textHeight / 2);
				addr++;
			}	
		}

		for (Pin pin : _mux.getDataInputs())
		{
			debugPin(g, pin);
		}
		debugPin(g, _mux.getDataOut());
		debugPin(g, _mux.getSelectPin());
	}
}