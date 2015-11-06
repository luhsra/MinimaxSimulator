package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

/**
 * A traceable {@code Integer}.
 *
 * @see Traceable
 *
 * @author Martin L&uuml;ck
 */
public class TraceableInteger extends AbstractTraceable<Integer> {

	/** The value of the {@code TraceableInteger}. */
	private Integer value;

	@Override
	public Integer get() {
		return value;
	}

	@Override
	public void set(Integer value) {
		this.value = value;
		fireValueChanged();
	}
}