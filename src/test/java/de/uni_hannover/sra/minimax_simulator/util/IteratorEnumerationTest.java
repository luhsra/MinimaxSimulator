package de.uni_hannover.sra.minimax_simulator.util;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link IteratorEnumeration}.
 *
 * @author Philipp Rohde
 */
public class IteratorEnumerationTest {

    /**
     * Tests the implementation using a {@code Collection}.
     */
    @Test
    public void testCollection() {
        final Map<String, String> map = new LinkedHashMap<>();
        map.put("key A", "value A");
        map.put("key B", "value B");
        map.put("key C", "value C");

        final IteratorEnumeration<String> iterEnum = new IteratorEnumeration<>(map.keySet());
        assertTrue("has more elements", iterEnum.hasMoreElements());
        assertEquals("first element", "key A", iterEnum.nextElement());

        assertTrue("has more elements", iterEnum.hasMoreElements());
        assertEquals("second element", "key B", iterEnum.nextElement());

        assertTrue("has more elements", iterEnum.hasMoreElements());
        assertEquals("third element", "key C", iterEnum.nextElement());

        assertFalse("has no more elements", iterEnum.hasMoreElements());
    }

    /**
     * Tests the implementation using an {@code Iterator}.
     */
    @Test
    public void testIterator() {
        final Collection<String> set = new LinkedHashSet<>();
        Collections.addAll(set, "key A", "key B", "key C");

        final IteratorEnumeration<String> iterEnum = new IteratorEnumeration<>(set.iterator());
        assertTrue("has more elements", iterEnum.hasMoreElements());
        assertEquals("first element", "key A", iterEnum.nextElement());

        assertTrue("has more elements", iterEnum.hasMoreElements());
        assertEquals("second element", "key B", iterEnum.nextElement());

        assertTrue("has more elements", iterEnum.hasMoreElements());
        assertEquals("third element", "key C", iterEnum.nextElement());

        assertFalse("has no more elements", iterEnum.hasMoreElements());
    }
}
