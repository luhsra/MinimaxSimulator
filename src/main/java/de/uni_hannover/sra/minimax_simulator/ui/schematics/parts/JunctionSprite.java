package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import javafx.scene.canvas.GraphicsContext;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The sprite of a {@link Junction}.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class JunctionSprite implements Sprite {

	private final Junction junction;

	/**
	 * Initializes the  {@code JunctionSprite}.
	 *
	 * @param junction
	 *          the {@code Junction} this sprite will represent
	 */
	public JunctionSprite(Junction junction) {
		this.junction = checkNotNull(junction);
	}

	@Override
	public void paint(GraphicsContext gc, RenderEnvironment env) {
		// draw only junction with 0, 2 or more outgoing wires
		if (junction.getDataOuts().size() == 1) {
			return;
		}

		int x = junction.getBounds().x;
		int y = junction.getBounds().y;

		gc.fillOval(x - 2 + 0.5, y - 2 + 0.5, 5, 5);
	}
}