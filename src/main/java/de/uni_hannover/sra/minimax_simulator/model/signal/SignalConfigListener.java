package de.uni_hannover.sra.minimax_simulator.model.signal;

/**
 * A {@code SignalConfigListener} is a class that needs to react to changes of the {@link SignalConfiguration}.
 *
 * @author Martin L&uuml;ck
 */
@FunctionalInterface
public interface SignalConfigListener {

//  public void signalAdded(int index, SignalType signal);
//
//  public void signalRemoved(int index);
//
//  public void signalReplaced(int index, SignalType signal);
//
//  public void signalsExchanged(int index1, int index2);

    /**
     * Notifies the listener about a change of the structure.
     */
    public void signalStructureChanged();
}