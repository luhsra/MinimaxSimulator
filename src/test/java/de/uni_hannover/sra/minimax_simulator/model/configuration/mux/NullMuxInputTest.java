package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the implementation of a null multiplexer input.
 *
 * @see NullMuxInput
 *
 * @author Philipp Rohde
 */
public class NullMuxInputTest {

    private static final MuxInput nmi = NullMuxInput.INSTANCE;

    /**
     * Tests {@link NullMuxInput#getName()}.
     */
    @Test
    public void testGetName() {
        assertEquals("getName()", "", nmi.getName());
    }

    /**
     * Tests {@link NullMuxInput#equals(Object)}.
     */
    @Test
    public void testEquals() {
        assertTrue("equals self", nmi.equals(nmi));
        assertTrue("equals instance", nmi.equals(NullMuxInput.INSTANCE));
        assertFalse("equals constant mux input", nmi.equals(new ConstantMuxInput(42)));
        assertFalse("equals register mux input", nmi.equals(new RegisterMuxInput("ACCU")));
    }

    /**
     * Tests {@link NullMuxInput#hashCode()}.
     */
    @Test
    public void testHashCode() {
        assertEquals("hash code", 31, nmi.hashCode());
    }
}
