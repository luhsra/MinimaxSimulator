package de.uni_hannover.sra.minimax_simulator.model.user;

/**
 * A {@code ProjectBuilder} is capable of building a {@link Project}.
 *
 * @author Martin L&uuml;ck
 */
public interface ProjectBuilder {

	/**
	 * Builds a {@link Project}.
	 *
	 * @return
	 *          the built {@code Project}
	 */
	public Project buildProject();
}