package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of a null multiplexer input.
 *
 * @see NullMuxInput
 *
 * @author Philipp Rohde
 */
public class NullMuxInputTest {

    private static MuxInput nmi = NullMuxInput.INSTANCE;

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
        assertEquals("equals self", true, nmi.equals(nmi));
        assertEquals("equals instance", true, nmi.equals(NullMuxInput.INSTANCE));
        assertEquals("equals constant mux input", false, nmi.equals(new ConstantMuxInput(42)));
        assertEquals("equals register mux input", false, nmi.equals(new RegisterMuxInput("ACCU")));
    }

    /**
     * Tests {@link NullMuxInput#hashCode()}.
     */
    @Test
    public void testHashCode() {
        assertEquals("hash code", 31, nmi.hashCode());
    }
}
