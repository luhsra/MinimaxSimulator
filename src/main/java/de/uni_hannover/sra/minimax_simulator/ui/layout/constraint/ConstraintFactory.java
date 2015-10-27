package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides methods to add and remove {@link Constraint}s to attributes.
 *
 * @author Martin L&uuml;ck
 */
public abstract class ConstraintFactory {

	/** The align mode. */
	public static enum AlignMode {
		/** align horizontally */
		HORIZONTALLY,
		/** align vertically */
		VERTICALLY,
		/** align horizontally and vertically */
		HORIZONTALLY_VERTICALLY
	}

	/**
	 * Gets the {@link ConstraintsManager} of the {@code ConstraintFactory}.
	 *
	 * @return
	 *          the manager
	 */
	protected abstract ConstraintsManager getManager();

	/**
	 * Sets the specified {@link Constraint} to the specified {@link AttributeType}.
	 *
	 * @param target
	 *          the name of the attribute
	 * @param targetAttr
	 *          the {@code AttributeType}
	 * @param constraint
	 *          the {@code Constraint} to add
	 */
	private void setConstraint(String target, AttributeType targetAttr, Constraint constraint) {
		getManager().setConstraint(target, targetAttr, constraint);
	}

	/**
	 * Clears all {@link Constraint}s of the specified attribute.
	 *
	 * @param target
	 *          the name of the attribute
	 */
	private void clearConstraints(String target) {
		getManager().clearConstraints(target);
	}

	/**
	 * Sets a {@link RelativeConstraint} with the specified arguments to the specified attribute.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param targetAttr
	 *          the {@code AttributeType} of the target
	 * @param source
	 *          the name of the source attribute
	 * @param sourceAttr
	 *          the {@code AttributeType} of the source
	 * @param offset
	 *          the offset of the {@code RelativeConstraint}
	 */
	public void relative(String target, AttributeType targetAttr, String source, AttributeType sourceAttr, int offset) {
		setConstraint(target, targetAttr, new RelativeConstraint(source, sourceAttr, offset));
	}

	/**
	 * Sets a {@link RelativeConstraint} with the specified arguments to the specified attribute.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param targetAttr
	 *          the {@code AttributeType} of the target
	 * @param source
	 *          the name of the source attribute
	 * @param sourceAttr
	 *          the {@code AttributeType} of the source
	 */
	public void relative(String target, AttributeType targetAttr, String source, AttributeType sourceAttr) {
		setConstraint(target, targetAttr, new RelativeConstraint(source, sourceAttr));
	}

	/**
	 * Sets a {@link RelativeConstraint} with the specified arguments to the specified attribute.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param attr
	 *          the {@code AttributeType}
	 * @param source
	 *          the name of the source attribute
	 * @param offset
	 *          the offset of the {@code RelativeConstraint}
	 */
	public void relative(String target, AttributeType attr, String source, int offset) {
		setConstraint(target, attr, new RelativeConstraint(source, attr, offset));
	}

	/**
	 * Sets a {@link RelativeConstraint} with the specified arguments to the specified attribute.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param attr
	 *          the {@code AttributeType}
	 * @param source
	 *          the name of the source attribute
	 */
	public void relative(String target, AttributeType attr, String source) {
		setConstraint(target, attr, new RelativeConstraint(source, attr));
	}

	/**
	 * Sets an {@link AbsoluteConstraint} with the specified offset to the specified attribute.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param targetAttr
	 *          the {@code AttributeType} of the target
	 * @param offset
	 *          the offset of the {@code AbsoluteConstraint}
	 */
	public void absolute(String target, AttributeType targetAttr, int offset) {
		setConstraint(target, targetAttr, new AbsoluteConstraint(offset));
	}

	/**
	 * Sets a {@link RelativeConstraint} to the specified attribute for arranging things below.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 * @param offset
	 *          the offset of the {@link RelativeConstraint}
	 */
	public void below(String target, String source, int offset) {
		setConstraint(target, AttributeType.TOP, new RelativeConstraint(source, AttributeType.BOTTOM, offset));
	}

	/**
	 * Sets a {@link RelativeConstraint} to the specified attribute for arranging things below.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 */
	public void below(String target, String source) {
		setConstraint(target, AttributeType.TOP, new RelativeConstraint(source, AttributeType.BOTTOM, 0));
	}

	/**
	 * Sets a {@link RelativeConstraint} to the specified attribute for arranging things above.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 * @param offset
	 *          the offset of the {@link RelativeConstraint}
	 */
	public void above(String target, String source, int offset) {
		setConstraint(target, AttributeType.BOTTOM, new RelativeConstraint(source, AttributeType.TOP, -offset));
	}

	/**
	 * Sets a {@link RelativeConstraint} to the specified attribute for arranging things above.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 */
	public void above(String target, String source) {
		setConstraint(target, AttributeType.BOTTOM, new RelativeConstraint(source, AttributeType.TOP, 0));
	}

	/**
	 * Sets a {@link RelativeConstraint} to the specified attribute for arranging things left.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 * @param offset
	 *          the offset of the {@link RelativeConstraint}
	 */
	// TODO: rename all to left / right
	public void left(String target, String source, int offset) {
		setConstraint(target, AttributeType.RIGHT, new RelativeConstraint(source, AttributeType.LEFT, -offset));
	}

	/**
	 * Sets a {@link RelativeConstraint} to the specified attribute for arranging things left.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 */
	public void leftTo(String target, String source) {
		setConstraint(target, AttributeType.RIGHT, new RelativeConstraint(source, AttributeType.LEFT, 0));
	}

	/**
	 * Sets a {@link RelativeConstraint} to the specified attribute for arranging things right.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 * @param offset
	 *          the offset of the {@link RelativeConstraint}
	 */
	public void rightTo(String target, String source, int offset) {
		setConstraint(target, AttributeType.LEFT, new RelativeConstraint(source, AttributeType.RIGHT, offset));
	}

	/**
	 * Sets a {@link RelativeConstraint} to the specified attribute for arranging things right.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 */
	public void right(String target, String source) {
		setConstraint(target, AttributeType.LEFT, new RelativeConstraint(source, AttributeType.RIGHT, 0));
	}

	/**
	 * Sets a {@link RelativeConstraint} to the specified attribute for horizontal alignment.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 */
	public void alignHorizontally(String target, String source) {
		setConstraint(target, AttributeType.HORIZONTAL_CENTER, new RelativeConstraint(source, AttributeType.HORIZONTAL_CENTER));
	}

	/**
	 * Sets a {@link RelativeConstraint} to the specified attribute for vertical alignment.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 */
	public void alignVertically(String target, String source) {
		setConstraint(target, AttributeType.VERTICAL_CENTER, new RelativeConstraint(source, AttributeType.VERTICAL_CENTER));
	}

	/**
	 * Sets two {@link RelativeConstraint}s to the specified attribute for horizontal and vertical alignment.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 */
	public void align(String target, String source) {
		alignHorizontally(target, source);
		alignVertically(target, source);
	}

	/**
	 * Sets a {@link RelativeConstraint} to the specified attribute according to the specified {@link AlignMode}.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param source
	 *          the name of the source attribute
	 * @param mode
	 *          the alignment mode
	 */
	public void align(String target, String source, AlignMode mode) {
		switch (mode) {
			case HORIZONTALLY:
				alignHorizontally(target, source);
				break;
			case VERTICALLY:
				alignVertically(target, source);
				break;
			case HORIZONTALLY_VERTICALLY:
				align(target, source);
				break;
		}
	}

	/**
	 * Groups the specified attribute with the specified anchors.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param anchors
	 *          the names of the anchors
	 */
	public void group(String target, String... anchors) {
		group(target, new HashSet<String>(Arrays.asList(anchors)));
	}

	/**
	 * Groups the specified attribute with the specified anchors.
	 *
	 * @param target
	 *          the name of the target attribute
	 * @param anchors
	 *          a set of the names of the anchors
	 */
	public void group(String target, Set<String> anchors) {
		// Attributes to minimize
		setConstraint(target, AttributeType.LEFT, new RelativeMinConstraint(anchors, AttributeType.LEFT, 0));
		setConstraint(target, AttributeType.TOP, new RelativeMinConstraint(anchors, AttributeType.TOP, 0));

		// Attributes to maximize
		setConstraint(target, AttributeType.RIGHT, new RelativeMaxConstraint(anchors, AttributeType.RIGHT, 0));
		setConstraint(target, AttributeType.BOTTOM, new RelativeMaxConstraint(anchors, AttributeType.BOTTOM, 0));
	}

	/**
	 * Clears all {@link Constraint}s of the specified attribute.
	 *
	 * @param target
	 *          the name of the attribute
	 */
	public void clear(String target) {
		clearConstraints(target);
	}
}