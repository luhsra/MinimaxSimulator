package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

/**
 * A {@code ConstrainedArea} is an area with {@link Constraint}s.
 *
 * @author Martin L&uuml;ck
 */
class ConstrainedArea extends AbstractAttributeOwner {

	/**
	 * Constructs a new {@code ConstrainedArea} with the specified name.
	 *
	 * @param name
	 *          the name of the {@code ConstrainedArea}
	 */
	public ConstrainedArea(String name) {
		super(name);
	}

	@Override
	public int getPreferredWidth() {
		return 0;
	}

	@Override
	public int getPreferredHeight() {
		return 0;
	}
}