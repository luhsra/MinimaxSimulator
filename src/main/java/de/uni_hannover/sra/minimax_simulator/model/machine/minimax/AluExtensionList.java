package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.ExtensionList;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;

import java.util.Collection;
import java.util.Collections;

/**
 * An {@link ExtensionList} for the machine's {@link AluOperation}s.
 *
 * @author Martin L&uuml;ck
 */
class AluExtensionList implements ExtensionList<AluOperation> {

	private final Alu alu;

	/**
	 * Constructs a new {@code AluExtensionList} for the specified {@link Alu}.
	 *
	 * @param alu
	 *          the {@code Alu}
	 */
	public AluExtensionList(Alu alu) {
		this.alu = alu;
	}

	@Override
	public void add(AluOperation operation) {
		alu.getAluOperations().add(operation);
	}

	@Override
	public void addAll(Collection<? extends AluOperation> elements) {
		alu.getAluOperations().addAll(elements);
	}

	@Override
	public void remove(int index) {
		alu.getAluOperations().remove(index);
	}

	@Override
	public void swap(int index1, int index2) {
		Collections.swap(alu.getAluOperations(), index1, index2);
	}

	@Override
	public void set(int index, AluOperation element) {
		// cannot happen with ALU operations
		throw new AssertionError();
	}
}