package de.uni_hannover.sra.minimax_simulator.model.machine.base;

import java.util.Collection;

public interface ExtensionList<T>
{
	public void add(T element);

	public void addAll(Collection<? extends T> elements);

	public void remove(int index);

	public void swap(int index1, int index2);

	public void set(int index, T element);
}