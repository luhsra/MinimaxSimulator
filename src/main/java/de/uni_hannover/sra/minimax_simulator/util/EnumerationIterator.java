package de.uni_hannover.sra.minimax_simulator.util;

import java.util.Enumeration;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class wraps a single {@link Enumeration} of type {@code E} into
 * an {@link Iterator} of type {@code E}.
 *
 * @param <E>
 *          the class of the {@code Enumeration} to wrap
 *
 * @author Martin L&uuml;ck
 */
public class EnumerationIterator<E> implements Iterator<E> {

	private final Enumeration<E>	_enumeration;

	/**
	 * Constructs a new {@code EnumerationIterator} to wrap the specified {@link Enumeration}.
	 *
	 * @param enumeration
	 *          the {@code Enumeration} to wrap
	 */
	public EnumerationIterator(Enumeration<E> enumeration) {
		_enumeration = checkNotNull(enumeration, "Enumeration cannot be null");
	}

	@Override
	public boolean hasNext() {
		return _enumeration.hasMoreElements();
	}

	@Override
	public E next() {
		return _enumeration.nextElement();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Create an {@link Iterable} wrapping the specified {@link Enumeration} which maintains a single
	 * {@link Iterator}. Every request to the iterator of the created iterable will return the same {@code Iterator}
	 * object, since an {@code Enumeration} also can only be traversed once.
	 *
	 * @param enumeration
	 *          the {@code Enumeration} to wrap
	 * @param <E>
	 *          the class of the {@code Enumeration}
	 * @return
	 *          an {@code Iterable} wrapping the {@code Enumeration}
	 */
	public static <E> Iterable<E> iterate(Enumeration<E> enumeration) {
		final Iterator<E> iterator = new EnumerationIterator<E>(enumeration);
		return new Iterable<E>() {
			@Override
			public Iterator<E> iterator() {
				return iterator;
			}
		};
	}
}