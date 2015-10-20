package de.uni_hannover.sra.minimax_simulator.model.machine.base;

import java.util.Collection;

/**
 * The {@code ExtensionList} provides all needed methods for lists of the machine's extensions.
 *
 * @param <T>
 *          the class of the machine's extension
 *
 * @author Martin L&uuml;ck
 */
public interface ExtensionList<T> {

	/**
	 * Adds an extension to the list.
	 *
	 * @param element
	 *          the extension to add
	 */
	public void add(T element);

	/**
	 * Adds a {@code Collection} of extensions to the list.
	 *
	 * @param elements
	 *          the {@code Collection} of extensions to add
	 */
	public void addAll(Collection<? extends T> elements);

	/**
	 * Removes the extension at the specified index from the list.
	 *
	 * @param index
	 *          the index of the extension to remove
	 */
	public void remove(int index);

	/**
	 * Swaps to entries of the list.
	 *
	 * @param index1
	 *          the index of the first extension
	 * @param index2
	 *          the index of the second extension
	 */
	public void swap(int index1, int index2);

	/**
	 * Adds the specified extension at the specified index.
	 *
	 * @param index
	 *          the index where the extension should be added
	 * @param element
	 *          the extension to add
	 */
	public void set(int index, T element);
}