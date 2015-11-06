package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.AbsoluteConstraint;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.AttributeType;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Constraint;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Layout;

import java.util.EnumSet;
import java.util.Set;

/**
 * The {@code OriginLayout} forces the component having this layout to the origin.
 *
 * @author Martin L&uuml;ck
 */
public class OriginLayout implements Layout {

	private static final Set<AttributeType> ATTRIBUTES = EnumSet.of(AttributeType.HORIZONTAL_CENTER,
															AttributeType.VERTICAL_CENTER);

	private static final Constraint ZERO = new AbsoluteConstraint(0);

	@Override
	public Constraint getConstraint(AttributeType attribute) {
		return ZERO;
	}

	@Override
	public Set<AttributeType> getConstrainedAttributes() {
		return ATTRIBUTES;
	}
}