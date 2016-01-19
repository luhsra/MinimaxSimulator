package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

/**
 * The {@code MachineMemory} is the basis of all types of memory used by register machines.
 *
 * @author Martin L&uuml;ck
 */
public interface MachineMemory extends AddressRange {

    /**
     * Gets the {@link MemoryState} of the {@code MachineMemory}.
     *
     * @return
     *          the {@code MemoryState} of the {@code MachineMemory}
     */
    public MemoryState getMemoryState();

//  public void pushMemoryState();
//
//  public void popMemoryState();

    /**
     * Marks the {@link MemoryState} of the {@code MachineMemory}.
     */
    public void markMemoryState();

    /**
     * Resets the {@link MemoryState} of the {@code MachineMemory}.
     */
    public void resetMemoryState();

    /**
     * Gets the value of the {@code notify listeners} property.
     *
     * @return
     *          {@code true} if listeners will be notified, {@code false} otherwise
     */
    public boolean getNotifiesListeners();

    /**
     * Sets the {@code notify listeners} property to the specific value.
     *
     * @param notify
     *          the new value of the {@code notify listeners} property
     */
    public void setNotifiesListeners(boolean notify);

    /**
     * Registers a new {@link MemoryAccessListener}.
     *
     * @param l
     *          the {@code MemoryAccessListener} to register
     */
    public void addMemoryAccessListener(MemoryAccessListener l);

    /**
     * Removes a {@link MemoryAccessListener}.
     *
     * @param l
     *          the {@code MemoryAccessListener} to remove
     */
    public void removeMemoryAccessListener(MemoryAccessListener l);
}