package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.CuLabelSprite;

/**
 * Implementation of the CU as {@link Label}.<br>
 * <br>
 * That is because the functionality of the CU is done by the simulation.
 *
 * @author Martin L&uuml;ck
 */
public class CuLabel extends Label {

	/**
	 * Constructs a new instance of the {@code CuLabel}.
	 */
	public CuLabel() {
		super("CU");
	}

	@Override
	public Sprite createSprite() {
		return new CuLabelSprite(this);
	}
}