package de.uni_hannover.sra.minimax_simulator.model.signal;

/**
 * A {@code SignalConfigListener} is a class that needs to react to changes of the {@link SignalConfiguration}.
 *
 * @author Martin L&uuml;ck
 */
@FunctionalInterface
public interface SignalConfigListener {

    /**
     * Notifies the listener about a change of the structure.
     */
    public void signalStructureChanged();
}