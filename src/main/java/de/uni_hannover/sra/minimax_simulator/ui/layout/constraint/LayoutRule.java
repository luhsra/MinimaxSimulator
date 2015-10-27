package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Set;

/**
 * A bundle of {@link Layout}s.
 *
 * @author Martin L&uuml;ck
 */
// TODO: remove because unused?
public interface LayoutRule {

	/**
	 * Gets the {@link Layout}s belonging to the {@code LayoutRule}.
	 *
	 * @return
	 *          a set of the {@code Layout}s
	 */
	public Set<Layout> getLayouts();

	/**
	 * Gets the name of the target component of the specified {@link Layout}
	 *
	 * @param layout
	 *          the {@code Layout}
	 * @return
	 *          the name of the target component
	 */
	public String getTarget(Layout layout);
}