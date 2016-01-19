package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

/**
 * A {@code MemoryAccessListener} is a class that needs to react to events
 * concerning the memory of the machine.
 *
 * @author Martin L&uuml;ck
 */
public interface MemoryAccessListener {

    /**
     * Called if there was a read access to the memory.
     *
     * @param address
     *          the address that was read from
     * @param value
     *          the read value of the memory address
     */
    void memoryReadAccess(int address, int value);

    /**
     * Called if there was a write access to the memory.
     *
     * @param address
     *          the address that was written to
     * @param value
     *          the value that was written to the address
     */
    void memoryWriteAccess(int address, int value);

    /**
     * Called if the memory was reset. Not to be mistaken with the zeroing of the memory.
     */
    void memoryReset();

    /**
     * Called if the memory was changed for a different reason, e.g. zeroing.
     */
    void memoryChanged();
}