package de.uni_hannover.sra.minimax_simulator.ui.common.exception;

import java.awt.Toolkit;

import javax.swing.JFrame;

import de.uni_hannover.sra.minimax_simulator.ui.UI;

/**
 * The {@link GuiExceptionHandler} extends the {@link SimpleExceptionHandler} class
 * and provides graphical feedback about occuring exception via construction of an {@link JErrorDialog} instance.
 * 
 * @author Martin
 */
@Deprecated
public class GuiExceptionHandler extends SimpleExceptionHandler
{
	private final JFrame _parent;

	public GuiExceptionHandler(JFrame parentWindow, String logFileName)
	{
		super(logFileName);
		_parent = parentWindow;
	}

	@Override
	public void uncaughtException(final Thread t, final Throwable e)
	{
		// If running from a console
		e.printStackTrace();

		try
		{
			UI.invokeInEDT(new Runnable()
			{
				@Override
				public void run()
				{
/*					JErrorDialog dialog = new JErrorDialog(_parent, e);

					// center on screen
					dialog.setLocationRelativeTo(null);

					Toolkit.getDefaultToolkit().beep();

					// blocking call until "ok" is clicked
					dialog.setVisible(true);

					dialog.dispose();		*/
				}
			});	
		}
		catch (Throwable t0)
		{
			logError(t0);
		}
	}

	@SuppressWarnings("unused")
	private String buildLongError(Thread t, Throwable e)
	{
		Throwable e0 = e;

		StringBuilder sb = new StringBuilder();
		sb.append("<html>");

		appendMessage(sb, e0);
		while (e0.getCause() != null)
		{
			e0 = e0.getCause();
			appendCauseMessage(sb, e0);
		}

		sb.append("<pre>\n----------------------\n\n");

		sb.append("Exception:\n");
		appendStackTrace(sb, e);

		sb.append("</pre></html>");
		return sb.toString();
	}

	private void appendMessage(StringBuilder sb, Throwable e)
	{
		sb.append("<b>" + e.getClass().getSimpleName() + ":</b><br/>\n");
		appendThrowable(sb, e);
		sb.append("<br/><br/>\n\n");
	}

	private void appendCauseMessage(StringBuilder sb, Throwable e)
	{
		sb.append("<b>Cause:</b><br/>\n");
		appendThrowable(sb, e);
		sb.append("<br/><br/>\n\n");
	}

	private void appendThrowable(StringBuilder sb, Throwable e)
	{
		sb.append("<i>").append(e.getClass().getSimpleName()).append("</i>: ").append(e.getLocalizedMessage());
	}

	private void appendStackTrace(StringBuilder sb, Throwable e)
	{
		sb.append(e.getClass().getSimpleName()).append(": ");
		sb.append(e.getLocalizedMessage()).append("\n");
		StackTraceElement[] ste = e.getStackTrace();
		for (StackTraceElement s : ste)
		{
			sb.append("\tat ").append(s.getClassName()).append(".");
			sb.append(s.getMethodName()).append(":").append(s.getLineNumber()).append("\n");
		}

		if (e.getCause() != null)
		{
			sb.append("Caused by:\n");
			appendStackTrace(sb, e.getCause());
		}
	}
}