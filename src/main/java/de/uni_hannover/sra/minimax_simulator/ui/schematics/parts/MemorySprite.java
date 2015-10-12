package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import com.sun.javafx.tk.Toolkit;
import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Memory;
import javafx.scene.canvas.GraphicsContext;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The sprite of a {@link Memory}.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class MemorySprite extends CircuitSprite {

	private final Memory _memory;

	/**
	 * Initializes the {@code MemorySprite}.
	 *
	 * @param memory
	 *          the {@code Memory} this sprite will represent
	 */
	public MemorySprite(Memory memory) {
		_memory = checkNotNull(memory);
	}

	@Override
	public void paint(GraphicsContext gc) {
		Bounds b = _memory.getBounds();

		gc.strokeRect(b.x + 0.5, b.y + 0.5 , b.w, b.h);

		String name = "HS";

		com.sun.javafx.tk.FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont());

		double textWidth = fm.computeStringWidth(name);
		double textHeight = fm.getLineHeight();

		double textX = b.x + b.w / 2 - textWidth / 2;
		double textY = b.y + b.h / 2 + textHeight / 4;

		gc.fillText(name, textX, textY);

		debugPin(gc, _memory.getDataIn());
		debugPin(gc, _memory.getDataOut());
		debugPin(gc, _memory.getAdr());
		debugPin(gc, _memory.getCs());
		debugPin(gc, _memory.getRw());
	}
}