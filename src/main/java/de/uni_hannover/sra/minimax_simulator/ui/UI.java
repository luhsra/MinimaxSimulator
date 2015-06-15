package de.uni_hannover.sra.minimax_simulator.ui;

import static com.google.common.base.Preconditions.*;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.google.common.base.Throwables;

public class UI
{
	private static UI	instance;

	/**
	 * Initializes the UI of the application.
	 */
	public static void initialize()
	{
		assert (instance == null);
		instance = new UI();
	}

//	public static UI getInstance()
//	{
//		assert (instance != null);
//		return instance;
//	}

	private UI()
	{
		//initLaf("Nimbus");
	}
/*
	private void initLaf(String name)
	{
		// Set the Look and feel
		try
		{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if (name.equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());

					// Fix for http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6789983
					UIManager.getLookAndFeel().getDefaults().put(
						"ToolTip[Disabled].backgroundPainter",
						UIManager.get("ToolTip[Enabled].backgroundPainter"));
					break;
				}
			}
		}
		catch (Exception e)
		{
			LookAndFeel laf = UIManager.getLookAndFeel();
			String lafName;
			if (laf == null)
				lafName = "null";
			else
				lafName = laf.getName();

			System.err.println("Cannot use Swing LaF " + name
				+ ", falling back to default: " + lafName);
		}
	}
*/
	/**
	 * Schedule asynchronous execution of the {@link Runnable} in the Swing event dispatch thread.
	 * 
	 * @param r
	 */
	public static void invoke(Runnable r)
	{
		checkNotNull(r);
		EventQueue.invokeLater(r);
	}

	/**
	 * If called from the event dispatch thread, just calls {@link Runnable#run()} on <code>r</code>
	 * . <br>
	 * Otherwise, schedules its execution there like {@link #invoke(Runnable)}.
	 * 
	 * @param r
	 */
	public static void invokeInEDT(Runnable r)
	{
		checkNotNull(r);
		if (EventQueue.isDispatchThread())
			r.run();
		else
			invokeNow(r);
	}

	/**
	 * Schedule execution of the {@link Runnable} in the Swing event dispatch thread and blocks
	 * until the call succeeded or an exception or error occurs during the execution. <br>
	 * In the second case, this method will throw the occured {@link RuntimeException} or wrap the
	 * occured checked exception into one.
	 * 
	 * @param r
	 */
	public static void invokeNow(Runnable r)
	{
		checkNotNull(r);
		try
		{
			EventQueue.invokeAndWait(r);
		}
		catch (InvocationTargetException e)
		{
			throw Throwables.propagate(e.getCause());
		}
		catch (InterruptedException e)
		{
			throw new Error("Invoking thread interrupted while waiting: "
				+ Thread.currentThread().getName(), e);
		}
	}
}