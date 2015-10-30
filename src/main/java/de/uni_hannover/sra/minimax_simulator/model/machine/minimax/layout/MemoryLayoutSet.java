package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.AttributeType;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

/**
 * Container for the {@link de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Layout}s
 * of the components of the memory.
 *
 * @author Martin L&uuml;ck
 */
public class MemoryLayoutSet extends DefaultLayoutSet {

	/**
	 * Constructs a new {@code MemoryLayoutSet}.
	 */
	public MemoryLayoutSet() {
		ConstraintBuilder cb = new ConstraintBuilder();

		addLayout(Parts.MEMORY_CS + Parts._PORT, cb.above(Parts.MEMORY_CS, 20).alignHorizontally(Parts.MEMORY_CS));
		addLayout(Parts.MEMORY_RW + Parts._PORT, cb.above(Parts.MEMORY_RW, 20).alignHorizontally(Parts.MEMORY_RW));

		addLayout(Parts.MEMORY_CS + Parts._LABEL, cb.above(Parts.MEMORY_CS + Parts._PORT).alignHorizontally(Parts.MEMORY_CS + Parts._PORT));
		addLayout(Parts.MEMORY_RW + Parts._LABEL, cb.above(Parts.MEMORY_RW + Parts._PORT).alignHorizontally(Parts.MEMORY_RW + Parts._PORT));
		addLayout(Parts.MEMORY_DI + Parts._LABEL, cb.right(Parts.MEMORY_DI, 3).alignVertically(Parts.MEMORY_DI));
		addLayout(Parts.MEMORY_ADR + Parts._LABEL, cb.right(Parts.MEMORY_ADR, 3).alignVertically(Parts.MEMORY_ADR));
		addLayout(Parts.MEMORY_DO + Parts._LABEL, cb.left(Parts.MEMORY_DO, 3).alignVertically(Parts.MEMORY_DO));

		addLayout(Parts.MEMORY_CS + Parts._WIRE_PORT + ".0", cb.align(Parts.MEMORY_CS + Parts._PORT));
		addLayout(Parts.MEMORY_CS + Parts._WIRE_PORT + ".1", cb.align(Parts.MEMORY_CS));

		addLayout(Parts.MEMORY_RW + Parts._WIRE_PORT + ".0", cb.align(Parts.MEMORY_RW + Parts._PORT));
		addLayout(Parts.MEMORY_RW + Parts._WIRE_PORT + ".1", cb.align(Parts.MEMORY_RW));

		addLayout(Parts.MEMORY_DO + Parts._WIRE + ".0", cb.align(Parts.MEMORY_DO));
		addLayout(Parts.MEMORY_DO + Parts._WIRE + ".1", cb.alignVertically(Parts.MEMORY_DO).relative(AttributeType.HORIZONTAL_CENTER, Parts.ALU_LINE, 40));
		addLayout(Parts.MEMORY_DO + Parts._WIRE + ".2", cb.alignHorizontally(Parts.MEMORY_DO + Parts._WIRE + ".1").below(Parts.MDR_SELECT, -10));
		addLayout(Parts.MEMORY_DO + Parts._WIRE + ".3", cb.alignVertically(Parts.MEMORY_DO + Parts._WIRE + ".2").right(Parts.MDR_SELECT));

		addLayout(Parts.MEMORY_ADR + Parts._WIRE + ".0", cb.alignVertically(Parts.MAR).left(Parts.MAR));
		addLayout(Parts.MEMORY_ADR + Parts._WIRE + ".1", cb.left(Parts.GROUP_BASE_REGISTERS, 50).alignVertically(Parts.MAR));
		addLayout(Parts.MEMORY_ADR + Parts._WIRE + ".2", cb.alignHorizontally(Parts.MEMORY_ADR + Parts._WIRE + ".1").alignVertically(Parts.MEMORY_ADR));
		addLayout(Parts.MEMORY_ADR + "_WIRE.3", cb.align(Parts.MEMORY_ADR));
	}
}