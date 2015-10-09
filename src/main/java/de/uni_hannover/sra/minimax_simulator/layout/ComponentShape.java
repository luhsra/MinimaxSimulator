package de.uni_hannover.sra.minimax_simulator.layout;

/**
 * A {@code ComponentShape} is the shape of a {@link Component} of the machine.
 *
 * @author Martin L&uuml;ck
 */
public interface ComponentShape {

	/**
	 * Updates the shape of the {@code Component}.
	 *
	 * @param component
	 *          the {@code Component} for which the shape will be updated
	 */
	public void updateShape(Component component);

	/**
	 * Creates the layout of the {@code Component}.
	 *
	 * @param component
	 *          the {@code Component} for which the layout will be created
	 */
	public void layout(Component component);
}