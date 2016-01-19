package de.uni_hannover.sra.minimax_simulator.model.signal;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class represents a signal value, that is the value that a machine executing a signal row
 * containing this object should apply to a wire of the circuit.<br>
 * <br>
 * Those are mostly enable-switches and multiplexer controls and therefore the used signal values
 * are presumably in a low non-negative integer range.<br>
 * <br>
 * To avoid unnecessary object creation, and since a SignalValue contains almost only an integer as
 * its state, these instances are cached and can be obtained by the static factory method
 * {@link #valueOf(int)}.<br>
 * <br>
 * To mark that a signal value is not needed and therefore unspecified, the constant
 * {@link #DONT_CARE} is to be used.
 * 
 * @author Martin L&uuml;ck
 */
public final class SignalValue {

    /** An unspecified signal. This object is the only instance whose method {@link #isDontCare()} returns {@code true}. */
    public static final SignalValue DONT_CARE = new SignalValue();

    /** Caching {@code 0} to {@code 19} as {@code SignalValue}. */
    private static final SignalValue[] CACHE;
    static {
        CACHE = new SignalValue[20];
        for (int i = 0; i < CACHE.length; i++) {
            CACHE[i] = new SignalValue(i);
        }
    }

    private final int value;
    private final boolean isDontCare;

    /**
     * Constructs an {@code don't care} {@code SignalValue}.
     */
    private SignalValue() {
        isDontCare = true;
        value = 0;
    }

    /**
     * Constructs a new {@code SignalValue} with the specified value.
     *
     * @param value
     *          the value of the {@code SignalValue}
     */
    private SignalValue(int value) {
        isDontCare = false;
        this.value = value;
    }

    /**
     * Gets the value of the {@code don't care} property.<br>
     * If {@code true} the {@code SignalValue} is unspecified and any arbitrary value can be returned by {@link #intValue()}.
     *
     * @return
     *          {@code true} if the {@code SignalValue} is unspecified, {@code} false otherwise
     */
    public boolean isDontCare() {
        return isDontCare;
    }

    /**
     * Gets the integer value of the {@code SignalValue}.<br>
     * If the signal is the {@link #DONT_CARE} signal, the return value is unspecified.
     *
     * @return
     *          the value
     */
    public int intValue() {
        return value;
    }

    /**
     * Retrieves a {@code SignalValue} for the specified non-negative integer value.<br>
     * The resulting {@code SignalValue} will not be equal to {@link #DONT_CARE}, but it will be equal to
     * every other {@code SignalValue} constructed by this method with the same integer parameter.
     *
     * @param value
     *          the value of the {@code SignalValue}
     * @return
     *          the {@code SignalValue} of the value
     */
    public static SignalValue valueOf(int value) {
        checkArgument(value >= 0);

        if (value >= CACHE.length) {
            return new SignalValue(value);
        }
        else {
            return CACHE[value];
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isDontCare ? 1231 : 1237);
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        else if (obj == null) {
            return false;
        }
        else if (getClass() != obj.getClass()) {
            return false;
        }

        SignalValue other = (SignalValue) obj;
        if (isDontCare != other.isDontCare) {
            return false;
        }
        if (value != other.value) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SignalValue[" + (isDontCare ? "DONT_CARE" : value) + "]";
    }
}