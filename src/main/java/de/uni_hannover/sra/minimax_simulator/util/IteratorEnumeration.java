package de.uni_hannover.sra.minimax_simulator.util;

import java.util.Enumeration;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class wraps a single {@link Iterator} of type {@code E} into
 * an {@link Enumeration} of type {@code E}.
 *
 * @param <E>
 *          the class of the {@code Iterator} to wrap
 *
 * @author Martin L&uuml;ck
 */
public class IteratorEnumeration<E> implements Enumeration<E> {

	private final Iterator<E> iterator;

	/**
	 * Constructs a new {@code IteratorEnumeration} to wrap the specified {@link Iterator}.
	 *
	 * @param iterator
	 *          the {@code Iterator} to wrap
	 */
	public IteratorEnumeration(Iterator<E> iterator) {
		this.iterator = checkNotNull(iterator, "Iterator cannot be null");
	}

	/**
	 * Constructs a new {@code IteratorEnumeration} to wrap the {@link Iterator}
	 * of the specified {@link Iterable}.
	 *
	 * @param collection
	 *          the {@code Iterable} whose {@code Iterator} will be wrapped
	 */
	public IteratorEnumeration(Iterable<E> collection) {
		this(collection.iterator());
	}

	@Override
	public boolean hasMoreElements() {
		return iterator.hasNext();
	}

	@Override
	public E nextElement() {
		return iterator.next();
	}
}