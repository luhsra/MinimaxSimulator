package de.uni_hannover.sra.minimax_simulator.util;

import static com.google.common.base.Preconditions.*;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * This class wraps a single Iterator of type E into an Enumeration of type E.
 * 
 * @author Martin
 */
public class IteratorEnumeration<E> implements Enumeration<E>
{
	private final Iterator<E>	_iterator;

	public IteratorEnumeration(Iterator<E> iterator)
	{
		_iterator = checkNotNull(iterator, "Iterator cannot be null");
	}

	public IteratorEnumeration(Iterable<E> collection)
	{
		this(collection.iterator());
	}

	@Override
	public boolean hasMoreElements()
	{
		return _iterator.hasNext();
	}

	@Override
	public E nextElement()
	{
		return _iterator.next();
	}
}