package de.uni_hannover.sra.minimax_simulator.util;

import org.junit.Test;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests the implementation of {@link EnumerationIterator}.
 *
 * @author Philipp Rohde
 */
public class EnumerationIteratorTest {

    private static final String EXPECTED_EXCEPTION = "expected to throw exception";

    /**
     * Tests the implementation of the non-static methods.
     */
    @Test
    public void test() {
        Enumeration<String> enumeration = new TestEnumeration();
        EnumerationIterator<String> enumIter = new EnumerationIterator<>(enumeration);

        assertEquals("has more elements", true, enumIter.hasNext());
        assertEquals("first element", "one", enumIter.next());
        assertEquals("has more elements", true, enumIter.hasNext());
        assertEquals("second element", "two", enumIter.next());
        assertEquals("has more elements", true, enumIter.hasNext());
        assertEquals("third element", "three", enumIter.next());
        assertEquals("has no more elements", false, enumIter.hasNext());

        // try to get more elements than there actually are
        try {
            String next = enumIter.next();
            fail(EXPECTED_EXCEPTION);
        } catch (Exception e) {
            assertEquals("NoSuchElementException", true, e instanceof NoSuchElementException);
        }

        // null enumeration
        try {
            new EnumerationIterator<>(null);
            fail(EXPECTED_EXCEPTION);
        } catch (NullPointerException e) {
            assertEquals("NullPointerException", "Enumeration cannot be null", e.getMessage());
        }

        // try to call remove
        try {
            enumIter.remove();
            fail(EXPECTED_EXCEPTION);
        } catch (Exception e) {
            assertEquals("UnsupportedOperationException", true, e instanceof UnsupportedOperationException);
        }
    }

    /**
     * Tests the implementation of {@link EnumerationIterator#iterate(Enumeration)}.
     */
    @Test
    public void testIterate() {
        Enumeration<String> enumeration = new TestEnumeration();
        Iterable<String> iterable = EnumerationIterator.iterate(enumeration);
        Iterator iter = iterable.iterator();

        assertEquals("has more elements", true, iter.hasNext());
        assertEquals("first element", "one", iter.next());
        assertEquals("has more elements", true, iter.hasNext());
        assertEquals("second element", "two", iter.next());
        assertEquals("has more elements", true, iter.hasNext());
        assertEquals("third element", "three", iter.next());
        assertEquals("has no more elements", false, iter.hasNext());

        // try to get more elements than there actually are
        try {
            String next = (String) iter.next();
            fail(EXPECTED_EXCEPTION);
        } catch (Exception e) {
            assertEquals("NoSuchElementException", true, e instanceof NoSuchElementException);
        }

        // try to call remove
        try {
            iter.remove();
            fail(EXPECTED_EXCEPTION);
        } catch (Exception e) {
            assertEquals("UnsupportedOperationException", true, e instanceof UnsupportedOperationException);
        }
    }

    /**
     * Lightweight {@link Enumeration} for testing purposes.
     */
    private class TestEnumeration implements Enumeration<String> {

        private final String[] elements = {"one", "two", "three"};
        private int current = 0;

        @Override
        public boolean hasMoreElements() {
            return current < elements.length;
        }

        @Override
        public String nextElement() {
            current++;
            return elements[current-1];
        }
    }
}
