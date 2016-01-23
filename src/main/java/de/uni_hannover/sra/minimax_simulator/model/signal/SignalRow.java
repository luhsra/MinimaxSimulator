package de.uni_hannover.sra.minimax_simulator.model.signal;

import de.uni_hannover.sra.minimax_simulator.model.signal.jump.DefaultJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A single row of a {@link SignalTable}.<br>
 * <br>
 * The {@code SignalRow} contains all information needed for simulation of it
 * as a row of the machine's control table.
 *
 * @author Martin L&uuml;ck
 */
public final class SignalRow {

    private final Map<String, SignalValue> values;

    private String label;
    private boolean isBreakpoint;
    private Jump jump;

    private String description;

    /**
     * Constructs a new {@code SignalRow}. All signals have their default value and the jump is
     * {@link DefaultJump#INSTANCE}.
     */
    public SignalRow() {
        values = new HashMap<>();
        label = null;
        isBreakpoint = false;
        jump = DefaultJump.INSTANCE;
    }

    /**
     * Gets the label of the {@code SignalRow}.
     *
     * @return
     *          the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of the {@code SignalRow}.
     *
     * @param label
     *          the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets the {@link SignalValue}s of the {@code SignalRow}.
     *
     * @return
     *          a map of the {@code SignalValue}s
     */
    public Map<String, SignalValue> getSignalValues() {
        return Collections.unmodifiableMap(values);
    }

    /**
     * Gets the value of the {@link SignalValue} with the specified name.
     *
     * @param signal
     *          the name of the {@code SignalValue}
     * @return
     *          the value of the {@code SignalValue}
     */
    public int getSignalValue(String signal) {
        SignalValue val = values.get(signal);
        if (val == null) {
            return 0;
        }
        return val.intValue();
    }

    /**
     * Gets the {@link SignalValue} with the specified name.
     *
     * @param signal
     *          the name of the {@code SignalValue}
     * @param dflt
     *          the default {@code SignalValue}
     * @return
     *          the {@code SignalValue} with the specified name or {@code dflt} if the {@code SignalValue} does not exist
     */
    public SignalValue getSignal(String signal, SignalValue dflt) {
        SignalValue val = values.get(signal);
        if (val == null) {
            return dflt;
        }
        return val;
    }

    /**
     * Gets the {@link SignalValue} related to the specified {@link SignalType}.
     *
     * @param signalType
     *          the {@code SignalType}
     * @return
     *          the {@code SignalValue}
     */
    public SignalValue getSignal(SignalType signalType) {
        return getSignal(signalType.getId(), signalType.getDefault());
    }

    /**
     * Sets the {@link SignalValue} with the specified name to the specified value.
     *
     * @param signal
     *          the name of the {@code SignalValue}
     * @param value
     *          the new value
     */
    public void setSignalValue(String signal, int value) {
        values.put(signal, SignalValue.valueOf(value));
    }

    public void setSignal(String signal, SignalValue value) {
        if (value == null) {
            values.remove(signal);
        }
        else {
            values.put(signal, value);
        }
    }

    /**
     * Sets the {@link SignalValue} of the specified {@link SignalType} to the specified value.
     *
     * @param signalType
     *          the {@code SignalType}
     * @param value
     *          the new value
     */
    public void setSignal(SignalType signalType, SignalValue value) {
        setSignal(signalType.getId(), value);
    }

    /**
     * Resets the value of the signal with the specified name.
     *
     * @param signal
     *          the name of the signal
     */
    public void resetSignal(String signal) {
        values.remove(signal);
    }

    /**
     * Gets the value of the {@code is goto operation} property.
     *
     * @return
     *          always {@code false}
     */
    public boolean isGotoOp() {
        return false;
    }

    /**
     * Gets the {@link Jump} of the {@code SignalRow}.
     *
     * @return
     *          the {@code Jump}
     */
    public Jump getJump() {
        return jump;
    }

    /**
     * Sets the {@link Jump} of the {@code SignalRow} to the specified {@code Jump}.
     *
     * @param jump
     *          the new {@code Jump}
     */
    public void setJump(Jump jump) {
        this.jump = checkNotNull(jump);
    }

    /**
     * Gets the value of the {@code breakpoint} property.
     *
     * @return
     *          {@code true} if the {@code SignalRow} is a breakpoint, {@code false} otherwise
     */
    public boolean isBreakpoint() {
        return isBreakpoint;
    }

    /**
     * Sets the value of the {@code breakpoint} property to the specified value.
     *
     * @param isBreakpoint
     *          the new value
     */
    public void setBreakpoint(boolean isBreakpoint) {
        this.isBreakpoint = isBreakpoint;
    }

    /**
     * Gets the description of the {@code SignalRow}.
     *
     * @return
     *          the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the  {@code SignalRow}.
     *
     * @param description
     *          the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isBreakpoint) {
            sb.append('*');
        }
        sb.append('<');
        if (label != null) {
            sb.append(label);
        }
        sb.append(">: ");
        sb.append(values.toString());
        return sb.toString();
    }
}