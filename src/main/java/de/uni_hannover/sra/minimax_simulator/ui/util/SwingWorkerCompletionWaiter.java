package de.uni_hannover.sra.minimax_simulator.ui.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingWorker;

/**
 * A {@link Runnable} that can be registered on a {@link SwingWorker} as a {@link PropertyChangeListener}.
 * <br><br>
 * The {@link #run()} method will be called in the event dispatch thread once the SwingWorker finished.
 * 
 * @author Martin
 */
@Deprecated
public abstract class SwingWorkerCompletionWaiter implements PropertyChangeListener, Runnable
{
	@Override
	public void propertyChange(PropertyChangeEvent event)
	{
		if ("state".equals(event.getPropertyName())
				&& SwingWorker.StateValue.DONE == event.getNewValue())
		{
			run();
		}
	}
}