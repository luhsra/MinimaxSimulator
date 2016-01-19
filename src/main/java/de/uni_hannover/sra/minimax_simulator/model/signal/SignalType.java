package de.uni_hannover.sra.minimax_simulator.model.signal;

import com.google.common.collect.ImmutableList;

/**
 * This interface provides the methods each implementation of a {@code SignalType} needs.
 *
 * @author Martin L&uuml;ck
 */
public interface SignalType {

    /**
     * Gets the ID of the {@code SignalType}.
     *
     * @return
     *          the ID
     */
    public String getId();

    /**
     * Gets the name of the {@code SignalType}.
     *
     * @return
     *          the name
     */
    public String getName();

    /**
     * Gets the name of the signal at the specified index.
     *
     * @param index
     *          the index of the signal name to get
     * @return
     *          the name of the signal at the specified index
     */
    public String getSignalName(int index);

    /**
     * Gets the name according to the specified {@link SignalValue}.
     *
     * @param value
     *          the {@code SignalValue}
     * @return
     *          the name of the {@code SignalValue}
     */
    public String getSignalName(SignalValue value);

    /**
     * Gets the default {@link SignalValue} of the {@code SignalType}.
     *
     * @return
     *          the default {@code SignalValue}
     */
    public SignalValue getDefault();

    /**
     * Gets all {@link SignalValue}s of the {@code SignalType}.
     *
     * @return
     *          a list of the {@code SignalValue}s
     */
    public ImmutableList<SignalValue> getValues();

    /**
     * Gets the bit width of the {@code SignalType}.
     *
     * @return
     *          the bit width of the {@code SignalType}
     */
    public int getBitWidth();
}