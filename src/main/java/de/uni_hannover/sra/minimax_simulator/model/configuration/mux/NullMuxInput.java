package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

/**
 * The {@code NullMuxInput} is an empty {@link MuxInput}.<br>
 * It is not instantiable because it uses the Singleton pattern. Use the instance instead.
 *
 * @author Martin L&uuml;ck
 */
public class NullMuxInput implements MuxInput {

	/** The {@code NullMuxInput} instance. */
	public static final MuxInput INSTANCE = new NullMuxInput();

	/**
	 * Constructs the the instance of the {@code NullMuxInput}.
	 */
	private NullMuxInput() {

	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public boolean equals(Object o) {
		return this == o;
	}

	@Override
	public int hashCode() {
		return 31;
	}
}