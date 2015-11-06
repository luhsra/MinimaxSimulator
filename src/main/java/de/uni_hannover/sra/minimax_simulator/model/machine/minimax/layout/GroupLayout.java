package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.*;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Groups the {@link Layout}s of the members of a group.
 *
 * @author Martin L&uuml;ck
 */
public class GroupLayout implements Layout {

	private final static Set<AttributeType> ATTRIBUTES = EnumSet.of(AttributeType.LEFT,
															AttributeType.RIGHT,
															AttributeType.TOP,
															AttributeType.BOTTOM);

	private final Constraint top;
	private final Constraint bottom;
	private final Constraint left;
	private final Constraint right;

	/**
	 * Constructs a new {@code GroupLayout} with the specified {@code Collection} of group members.
	 *
	 * @param groupMembers
	 *          the members of the group the {@code GroupLayout} belongs to
	 */
	public GroupLayout(Collection<String> groupMembers) {
		this(new HashSet<String>(groupMembers));
	}

	/**
	 * Constructs a new {@code GroupLayout} with the specified set of group members.
	 *
	 * @param groupMembers
	 *          the members of the group the {@code GroupLayout} belongs to
	 */
	public GroupLayout(Set<String> groupMembers) {
		top = new RelativeMinConstraint(groupMembers, AttributeType.TOP, 0);
		bottom = new RelativeMaxConstraint(groupMembers, AttributeType.BOTTOM, 0);
		left = new RelativeMinConstraint(groupMembers, AttributeType.LEFT, 0);
		right = new RelativeMaxConstraint(groupMembers, AttributeType.RIGHT, 0);
	}

	@Override
	public Constraint getConstraint(AttributeType attribute) {
		switch (attribute) {
			case TOP:
				return top;
			case BOTTOM:
				return bottom;
			case LEFT:
				return left;
			case RIGHT:
				return right;

			default:
				// never happens
				throw new AssertionError();
		}
	}

	@Override
	public Set<AttributeType> getConstrainedAttributes() {
		return ATTRIBUTES;
	}
}