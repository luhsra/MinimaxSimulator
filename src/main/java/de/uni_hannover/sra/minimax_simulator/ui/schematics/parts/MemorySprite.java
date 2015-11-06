package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Memory;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Bounds;
import javafx.scene.canvas.GraphicsContext;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The sprite of a {@link Memory}.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class MemorySprite extends CircuitSprite {

	private final Memory memory;

	/**
	 * Initializes the {@code MemorySprite}.
	 *
	 * @param memory
	 *          the {@code Memory} this sprite will represent
	 */
	public MemorySprite(Memory memory) {
		this.memory = checkNotNull(memory);
	}

	@Override
	public void paint(GraphicsContext gc) {
		Bounds b = memory.getBounds();

		gc.strokeRect(b.x + 0.5, b.y + 0.5 , b.w, b.h);

		String name = "HS";

		FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont());

		double textWidth = fm.computeStringWidth(name);
		double textHeight = fm.getLineHeight();

		double textX = b.x + b.w / 2 - textWidth / 2;
		double textY = b.y + b.h / 2 + textHeight / 4;

		gc.fillText(name, textX, textY);

		debugPin(gc, memory.getDataIn());
		debugPin(gc, memory.getDataOut());
		debugPin(gc, memory.getAdr());
		debugPin(gc, memory.getCs());
		debugPin(gc, memory.getRw());
	}
}