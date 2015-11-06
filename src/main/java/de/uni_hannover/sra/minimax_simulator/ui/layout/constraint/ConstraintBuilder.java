package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@code ConstraintBuilder} is used for building {@link Constraint}s.
 *
 * @author Martin L&uuml;ck
 */
public class ConstraintBuilder {

	private final Map<AttributeType, Constraint> constraints;

	/**
	 * Constructs a new {@code ConstrainBuilder} without {@link Constraint}s.
	 */
	public ConstraintBuilder() {
		constraints = new EnumMap<AttributeType, Constraint>(AttributeType.class);
	}

	/**
	 * Gets the {@link Constraint}s built by the instance of {@code ConstraintBuilder}.
	 *
	 * @return
	 *          a map of the built {@code Constraint}s
	 */
	public Map<AttributeType, Constraint> constraints() {
		return constraints;
	}

	/**
	 * Clears the already built {@link Constraint}s of the instance of {@code ConstraintBuilder}.
	 *
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder clear() {
		constraints.clear();
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} for the specified {@link AttributeType}.
	 *
	 * @param targetAttr
	 *          the {@code AttributeType}
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @param sourceAttr
	 *          the source {@code AttributeType} of the {@code RelativeConstraint}
	 * @param offset
	 *          the offset of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder relative(AttributeType targetAttr, String source, AttributeType sourceAttr, int offset) {
		constraints.put(targetAttr, new RelativeConstraint(source, sourceAttr, offset));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} for the specified {@link AttributeType}.
	 *
	 * @param targetAttr
	 *          the {@code AttributeType}
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @param sourceAttr
	 *          the source {@code AttributeType} of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder relative(AttributeType targetAttr, String source, AttributeType sourceAttr) {
		constraints.put(targetAttr, new RelativeConstraint(source, sourceAttr));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} for the specified {@link AttributeType}.
	 *
	 * @param attr
	 *          the {@code AttributeType}
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @param offset
	 *          the offset of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder relative(AttributeType attr, String source, int offset) {
		constraints.put(attr, new RelativeConstraint(source, attr, offset));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} for the specified {@link AttributeType}.
	 *
	 * @param attr
	 *          the {@code AttributeType}
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder relative(AttributeType attr, String source) {
		constraints.put(attr, new RelativeConstraint(source, attr));
		return this;
	}

	/**
	 * Builds an {@link AbsoluteConstraint} for the specified target {@link AttributeType}.
	 *
	 * @param targetAttr
	 *          the {@code AttributeType}
	 * @param offset
	 *          the offset of the {@code AbsoluteConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder absolute(AttributeType targetAttr, int offset) {
		constraints.put(targetAttr, new AbsoluteConstraint(offset));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} to arrange things below the specified source.
	 *
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @param offset
	 *          the offset of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder below(String source, int offset) {
		constraints.put(AttributeType.TOP, new RelativeConstraint(source, AttributeType.BOTTOM, offset));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} to arrange things below the specified source.
	 *
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder below(String source) {
		constraints.put(AttributeType.TOP, new RelativeConstraint(source, AttributeType.BOTTOM, 0));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} to arrange things above the specified source.
	 *
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @param offset
	 *          the offset of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder above(String source, int offset) {
		constraints.put(AttributeType.BOTTOM, new RelativeConstraint(source, AttributeType.TOP, -offset));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} to arrange things above the specified source.
	 *
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder above(String source) {
		constraints.put(AttributeType.BOTTOM, new RelativeConstraint(source, AttributeType.TOP, 0));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} to arrange things left of the specified source.
	 *
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @param offset
	 *          the offset of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder left(String source, int offset) {
		constraints.put(AttributeType.RIGHT, new RelativeConstraint(source, AttributeType.LEFT, -offset));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} to arrange things left of the specified source.
	 *
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder left(String source) {
		constraints.put(AttributeType.RIGHT, new RelativeConstraint(source, AttributeType.LEFT, 0));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} to arrange things right of the specified source.
	 *
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @param offset
	 *          the offset of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder right(String source, int offset) {
		constraints.put(AttributeType.LEFT, new RelativeConstraint(source, AttributeType.RIGHT, offset));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} to arrange things right of the specified source.
	 *
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder right(String source) {
		constraints.put(AttributeType.LEFT, new RelativeConstraint(source, AttributeType.RIGHT, 0));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} to align things horizontally with the specified source.
	 *
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder alignHorizontally(String source) {
		constraints.put(AttributeType.HORIZONTAL_CENTER, new RelativeConstraint(source, AttributeType.HORIZONTAL_CENTER));
		return this;
	}

	/**
	 * Builds a {@link RelativeConstraint} to align things vertically with the specified source.
	 *
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder alignVertically(String source) {
		constraints.put(AttributeType.VERTICAL_CENTER, new RelativeConstraint(source, AttributeType.VERTICAL_CENTER));
		return this;
	}

	/**
	 * Builds two {@link RelativeConstraint}s to align things vertically and horizontally with the specified source.
	 *
	 * @param source
	 *          the source of the {@code RelativeConstraint}
	 * @return
	 *          the instance
	 */
	public ConstraintBuilder align(String source) {
		alignHorizontally(source);
		return alignVertically(source);
	}
}