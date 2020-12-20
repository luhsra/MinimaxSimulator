package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link RelativeConstraint}.
 *
 * @author Philipp Rohde
 */
public class RelativeConstraintTest {

    private static final RelativeConstraint REL_ONE = new RelativeConstraint("THIS", AttributeType.HEIGHT);
    private static final RelativeConstraint REL_TWO = new RelativeConstraint("THIS", AttributeType.WIDTH);

    /**
     * Tests the error handling of the constructor.
     */
    @Test
    public void testConstructor() {
        try {
            new RelativeConstraint(null);
            fail("expected to throw exception");
        } catch (NullPointerException e) {
            assertEquals("NullPointerException", "anchor is null", e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link RelativeConstraint#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("string of REL_ONE", "rel(THIS.HEIGHT + 0)", REL_ONE.toString());
        assertEquals("string of REL_TWO", "rel(THIS.WIDTH + 0)", REL_TWO.toString());
    }

    /**
     * Tests the implementation of {@link RelativeConstraint#equals(Object)}.
     */
    @Test
    public void testEquals() {
        assertTrue("self comparison", REL_ONE.equals(REL_ONE));
        assertFalse("null comparison", REL_ONE.equals(null));
        assertFalse("other class comparison", REL_ONE.equals(new ConstrainedArea("Area")));
        assertFalse("different anchor name", REL_ONE.equals(new RelativeConstraint("OTHER", AttributeType.HEIGHT)));
        assertFalse("different anchor attribute type", REL_ONE.equals(new RelativeConstraint("THIS", AttributeType.WIDTH)));
        assertFalse("different anchor", REL_ONE.equals(new RelativeConstraint("OTHER", AttributeType.WIDTH)));
        assertFalse("different offset", REL_ONE.equals(new RelativeConstraint("THIS", AttributeType.HEIGHT, 10)));
        assertTrue("same values", REL_ONE.equals(new RelativeConstraint("THIS", AttributeType.HEIGHT)));
    }
}

