package de.uni_hannover.sra.minimax_simulator.ui.common.exception;

import java.awt.EventQueue;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;

/**
 * The {@link SimpleExceptionHandler} class can be registered as an {@link UncaughtExceptionHandler}
 * and provides feedback about occuring exception via logging to a file and showing a brief message
 * box
 * 
 * @author Martin
 * 
 */
public class SimpleExceptionHandler implements UncaughtExceptionHandler
{
	private final String	_logFileName;

	public SimpleExceptionHandler(String logFileName)
	{
		_logFileName = logFileName;
	}

	@Override
	public void uncaughtException(Thread t, final Throwable e)
	{
		// If running from a console
		e.printStackTrace();

		logError(e);
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JOptionPane.showMessageDialog(null, "Exception occured: " + e.getClass().getSimpleName()
					+ " - check " + _logFileName + " for details");
				System.exit(1);
			}
		});
	}

	protected void logError(Throwable t)
	{
		FileWriter fw = null;
		try
		{
			fw = new FileWriter(_logFileName);
			t.printStackTrace(new PrintWriter(fw));
		}
		catch (Throwable t1)
		{
			// ignore
		}
		finally
		{
			IOUtils.closeQuietly(fw);
		}
	}
}