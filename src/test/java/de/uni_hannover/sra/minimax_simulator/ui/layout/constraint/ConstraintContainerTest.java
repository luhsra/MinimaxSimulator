package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import de.uni_hannover.sra.minimax_simulator.ui.layout.PointComponent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests parts of the implementation of {@link ConstraintContainer} that are not covered by other tests.
 *
 * @author Philipp Rohde
 */
public class ConstraintContainerTest {

    private static final String EXPECTED_EXCEPTION = "expected to throw exception";

    /**
     * Actually runs the test.
     */
    @Test
    public void test() {
        ConstraintContainer cc1 = new ConstraintContainer();
        cc1.updateSize();

        // add virtual component
        try {
            cc1.addVirtualComponent(null);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("add virtual null component", "Component id must not be null", e.getMessage());
        }

        cc1.addVirtualComponent("virtual");

        // add component
        PointComponent point = new PointComponent();
        point.set(0, 0);

        try {
            cc1.addComponent(point, 100);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("adding component with non-string constraint", "Component id must be a string", e.getMessage());
        }

        cc1.addComponent(point, "point");

        try {
            cc1.addComponent(point, "again");
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("try adding same component twice", "Component again already existing in layout under name: point", e.getMessage());
        }

        // remove component
        cc1.removeComponent(new PointComponent());

        // clear constraints
        try {
            cc1.clearConstraints("noone");
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalStateException e) {
            assertEquals("clearing constraints of non-existing owner", "Attribute owner not existing: noone", e.getMessage());
        }

        // set constraint
        try {
            cc1.setConstraint("point", AttributeType.LEFT, null);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("setting null constraint", "Null constraint not allowed", e.getMessage());
        }

        try {
            cc1.setConstraint("point", null, new AbsoluteConstraint(0));
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("setting null attribute", "Null attribute not allowed", e.getMessage());
        }

        try {
            cc1.setConstraint("noone", AttributeType.LEFT, new AbsoluteConstraint(0));
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("setting constraint of non-existing owner", "Attribute owner not found: noone", e.getMessage());
        }

        // add constraint
        AttributeType aType = AttributeType.LEFT;
        Constraint constraint = new AbsoluteConstraint(0);

        try {
            cc1.addConstraint("point", aType, null);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("adding null constraint", "Null constraint not allowed", e.getMessage());
        }

        try {
            cc1.addConstraint("point", null, constraint);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("adding null attribute", "Null attribute not allowed", e.getMessage());
        }

        try {
            cc1.addConstraint("noone", aType, constraint);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("adding constraint to non-existing owner", "Attribute owner not found: noone", e.getMessage());
        }

        cc1.addConstraint("point", aType, constraint);

        try {
            cc1.addConstraint("point", aType, new AbsoluteConstraint(10));
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("adding existing constraint", "point is already constrained in the LEFT attribute", e.getMessage());
        }

        // remove constraint
        try {
            cc1.removeConstraint("point", null);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("removing null constraint", "Null attribute not allowed", e.getMessage());
        }

        try {
            cc1.removeConstraint("noone", aType);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("removing constraint of non-existing owner", "Attribute owner not found: noone", e.getMessage());
        }

        cc1.removeConstraint("point", aType);
    }
}
