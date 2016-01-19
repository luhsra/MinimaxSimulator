package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

/**
 * The {@code MemoryState} represents the actual memory, i.e. the storing of values to the different memory addresses.
 *
 * @author Martin L&uuml;ck
 */
public interface MemoryState {
    //public int[] getInts(int fromAddress, int length);

    /**
     * Gets the value at the specified memory address.
     *
     * @param address
     *          the memory address whose value will be read
     * @return
     *          the value of the memory address
     */
    public int getInt(int address);

    /**
     * Sets the specified value at the specified memory address.
     *
     * @param address
     *          the memory address whose value will be set
     * @param value
     *          the new value of the memory address
     */
    public void setInt(int address, int value);

    /**
     * Zeros the entire {@code MemoryState}.
     */
    public void zero();
}