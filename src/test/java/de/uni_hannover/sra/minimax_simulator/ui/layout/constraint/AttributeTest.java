package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link Attribute}.
 *
 * @author Philipp Rohde
 */
public class AttributeTest {

    /**
     * Tests the error handling of the constructor.
     */
    @Test
    public void testConstructor() {
        String expected_exception = "expected to throw exception";
        // null owner
        try {
            new Attribute(null, AttributeType.HEIGHT);
            fail(expected_exception);
        } catch (NullPointerException e) {
            assertEquals("null owner", "owner is null", e.getMessage());
        }
        // null type
        try {
            new Attribute("owner", null);
            fail(expected_exception);
        } catch (NullPointerException e) {
            assertEquals("null type", "type is null", e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link Attribute#equals(Object)}.
     */
    @Test
    public void testEquals() {
        Attribute a1 = new Attribute("owner", AttributeType.BOTTOM);
        assertTrue("self comparison", a1.equals(a1));
        assertFalse("null comparison", a1.equals(null));
        assertFalse("other class comparison", a1.equals(AttributeType.BOTTOM));
        assertFalse("different owner", a1.equals(new Attribute("other", AttributeType.BOTTOM)));
        assertFalse("different type", a1.equals(new Attribute("owner", AttributeType.TOP)));
        assertTrue("same values", a1.equals(new Attribute("owner", AttributeType.BOTTOM)));
    }
}
