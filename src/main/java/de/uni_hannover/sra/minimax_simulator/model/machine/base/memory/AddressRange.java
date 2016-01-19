package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

/**
 * The {@code AddressRange} is the very basis of all memory types.<br>
 * It defines the range of the accessible addresses.
 *
 * @author Martin L&uuml;ck
 */
public interface AddressRange {

    /**
     * Gets the lowest accessible address.
     *
     * @return
     *          the lowest accessible address
     */
    public int getMinAddress();

    /**
     * Gets the highest accessible address.
     *
     * @return
     *          the highest accessible address
     */
    public int getMaxAddress();

    /**
     * Gets the width of the {@code AddressRange}.
     *
     * @return
     *          the width of the {@code AddressRange}
     */
    public int getAddressWidth();
}