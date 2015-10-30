package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.layout.AbstractComponent;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.LabelSprite;

/**
 * A {@code Label} is a component of the machine that has no functionality implemented.
 * Its functionality is done by the simulation of the machine. However it needs a visual representation.
 *
 * @author Martin L&uuml;ck
 */
public class Label extends AbstractComponent implements SpriteOwner {

	private final String message;

	/**
	 * Constructs a new {@code Label} with the specified message.
	 *
	 * @param message
	 *          the message of the {@code Label}
	 */
	public Label(String message) {
		this.message = message;
	}

	/**
	 * Gets the message of the {@code Label}.
	 *
	 * @return
	 *          the message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public Sprite createSprite() {
		return new LabelSprite(this);
	}
}