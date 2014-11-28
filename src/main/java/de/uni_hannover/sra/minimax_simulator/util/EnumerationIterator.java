package de.uni_hannover.sra.minimax_simulator.util;

import static com.google.common.base.Preconditions.*;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * This class wraps a single Enumeration of type E into an Iterator of type E.
 * 
 * @author Martin
 */
public class EnumerationIterator<E> implements Iterator<E>
{
	private final Enumeration<E>	_enumeration;

	public EnumerationIterator(Enumeration<E> enumeration)
	{
		_enumeration = checkNotNull(enumeration, "Enumeration cannot be null");
	}

	@Override
	public boolean hasNext()
	{
		return _enumeration.hasMoreElements();
	}

	@Override
	public E next()
	{
		return _enumeration.nextElement();
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Create an {@link Iterable} wrapping the given {@link Enumeration} which maintains a single
	 * Iterator. Every request to the iterator of the created iterable will return the same iterator
	 * object, since an {@link Enumeration} also can only be traversed once.
	 */
	public static <E> Iterable<E> iterate(Enumeration<E> enumeration)
	{
		final Iterator<E> iterator = new EnumerationIterator<E>(enumeration);
		return new Iterable<E>()
		{
			@Override
			public Iterator<E> iterator()
			{
				return iterator;
			}
		};
	}
}