package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link AbstractAttributeOwner}.
 *
 * @author Philipp Rohde
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AbstractAttributeOwnerTest {

    private static final String EXPECTED_EXCEPTION = "expected to throw exception";
    private static final AbstractAttributeOwner aao = new ConstrainedArea("aao");
    private static final Constraint CONSTRAINT = new RelativeConstraint(new Attribute("this", AttributeType.LEFT), 10);

    /**
     * Tests the instance creation.
     */
    @Test
    public void testConstructor() {
        try {
            new ConstrainedArea(null);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("IllegalArgumentException", "Name must not be null", e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link AbstractAttributeOwner#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("", new ConstrainedArea("").toString());
        assertEquals("aao", aao.toString());
    }

    /**
     * Tests the implementation of {@link AbstractAttributeOwner#set(AttributeType, int)}.
     */
    @Test
    public void test01set() {
        AttributeType attribute = AttributeType.HORIZONTAL_CENTER;

        // add attribute
        aao.set(attribute, 0);
        aao.set(AttributeType.BOTTOM, 2);
        assertTrue(aao.hasSet(attribute));
        assertTrue(aao.hasSet(AttributeType.BOTTOM));

        // set null attribute
        try {
            aao.set(null, 1);
            fail(EXPECTED_EXCEPTION);
        } catch (NullPointerException e) {
            assertEquals("NullPointerException", "aao: attribute is null", e.getMessage());
        }

        // re-add attribute
        try {
            aao.set(attribute, 1);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalStateException e) {
            assertEquals("IllegalStateException", "aao: Value for " + attribute + " already set", e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link AbstractAttributeOwner#hasSet(AttributeType)}.
     */
    @Test
    public void test02hasSet() {
        assertTrue("existing attribute: BOTTOM", aao.hasSet(AttributeType.BOTTOM));
        assertFalse("non-existing attribute: HEIGHT", aao.hasSet(AttributeType.HEIGHT));

        // null attribute
        try {
            aao.hasSet(null);
            fail(EXPECTED_EXCEPTION);
        } catch (NullPointerException e) {
            assertEquals("NullPointerException", "aao: attribute is null", e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link AbstractAttributeOwner#get(AttributeType)}.
     */
    @Test
    public void test03get() {
        // existing attributes
        assertEquals("existing attribute: HORIZONTAL_CENTER", 0, aao.get(AttributeType.HORIZONTAL_CENTER));
        assertEquals("existing attribute: BOTTOM", 2, aao.get(AttributeType.BOTTOM));

        // non-existing attribute
        try {
            aao.get(AttributeType.HEIGHT);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalStateException e) {
            assertEquals("IllegalStateException", "aao: Unresolved attribute " + AttributeType.HEIGHT, e.getMessage());
        }

        // null attribute
        try {
            aao.get(null);
            fail(EXPECTED_EXCEPTION);
        } catch (NullPointerException e) {
            assertEquals("NullPointerException", "aao: attribute is null", e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link AbstractAttributeOwner#setAttributeConstraint(AttributeType, Constraint)}.
     */
    @Test
    public void test04setAttributeConstraint() {
        aao.setAttributeConstraint(AttributeType.LEFT, CONSTRAINT);

        // null attribute
        try {
            aao.setAttributeConstraint(null, CONSTRAINT);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("IllegalArgumentException", "aao: Attribute type must not be null", e.getMessage());
        }

        // null constraint
        try {
            aao.setAttributeConstraint(AttributeType.LEFT, null);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("IllegalArgumentException", "aao: Constraint must not be null", e.getMessage());
        }

        // add a second and then try to add a third constraint for the horizontal axis
        Constraint right = new RelativeConstraint(new Attribute("this", AttributeType.RIGHT), 10);
        aao.setAttributeConstraint(AttributeType.RIGHT, right);
        Constraint width = new RelativeConstraint(new Attribute("this", AttributeType.WIDTH), 1000);
        try {
            aao.setAttributeConstraint(AttributeType.WIDTH, width);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalStateException e) {
            assertEquals("IllegalStateException", "aao: At most 2 constraints on the HORIZONTAL axis allowed", e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link AbstractAttributeOwner#getAttributeConstraint(AttributeType)}.
     */
    @Test
    public void test07getAttributeConstraint() {
        // existing attribute
        assertEquals("existing attribute", CONSTRAINT, aao.getAttributeConstraint(AttributeType.LEFT));

        // non-existing attribute
        assertNull("non-existing attribute", aao.getAttributeConstraint(AttributeType.TOP));

        // null attribute
        try {
            aao.getAttributeConstraint(null);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("IllegalArgumentException", "aao: Attribute type must not be null", e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link AbstractAttributeOwner#removeAttributeConstraint(AttributeType)}.
     */
    @Test
    public void test06removeAttributeConstraint() {
        aao.removeAttributeConstraint(AttributeType.WIDTH);
        assertNull(aao.getAttributeConstraint(AttributeType.WIDTH));
        aao.removeAttributeConstraint(AttributeType.RIGHT);
        assertNull(aao.getAttributeConstraint(AttributeType.RIGHT));

        try {
            aao.removeAttributeConstraint(null);
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("IllegalArgumentException", "aao: Attribute type must not be null", e.getMessage());
        }
    }

    /**
     * Tests the uncovered part of the implementation of {@link AbstractAttributeOwner#validateConstraints()}.
     */
    @Test
    public void test07ValidateConstraints() {
        AbstractAttributeOwner test = new ConstrainedArea("test");

        // empty x axis
        try {
            test.validateConstraints();
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalStateException e) {
            assertEquals("empty x axis", "test: underconstrained in x axis", e.getMessage());
        }

        // only width
        test.setAttributeConstraint(AttributeType.WIDTH, new RelativeConstraint("this", AttributeType.WIDTH, 100));
        try {
            test.validateConstraints();
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalStateException e) {
            assertEquals("only width", "test: underconstrained in x axis", e.getMessage());
        }

        // empty y axis
        test.setAttributeConstraint(AttributeType.LEFT, new RelativeConstraint("this", AttributeType.LEFT, 10));
        try {
            test.validateConstraints();
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalStateException e) {
            assertEquals("empty y axis", "test: underconstrained in y axis", e.getMessage());
        }

        // only height
        test.setAttributeConstraint(AttributeType.HEIGHT, new RelativeConstraint("this", AttributeType.HEIGHT, 300));
        try {
            test.validateConstraints();
            fail(EXPECTED_EXCEPTION);
        } catch (IllegalStateException e) {
            assertEquals("only height", "test: underconstrained in y axis", e.getMessage());
        }
    }

    /**
     * Tests the uncovered part of the implementation of {@link AbstractAttributeOwner#computeAttribute(AttributeType, AttributeSource)}.
     */
    @Test
    public void test08ComputeAttribute() {
        try {
            aao.computeAttribute(AttributeType.LEFT, null);
            fail(EXPECTED_EXCEPTION);
        } catch (NullPointerException e) {
            assertEquals("NullPointerException", "aao: source is null", e.getMessage());
        }
    }
}
