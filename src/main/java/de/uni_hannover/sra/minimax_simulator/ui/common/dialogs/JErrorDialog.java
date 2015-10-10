package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;

/**
 * This dialog will present the details of a throwable exception to a user.
 */
@Deprecated
public class JErrorDialog extends JDialog
{
	private static final long	serialVersionUID	= 1L;

	public JErrorDialog(final JFrame owner, final Throwable e)
	{
		super(owner, true);

		// Default value in case that resources cannot be loaded
		String titleStr = "Error";
		try
		{
			TextResource res = Main.getTextResource("exception");
			titleStr = res.get("exception-dialog.title");
		}
		catch (Throwable e0)
		{
		}

		setModal(true);
		setResizable(false);
		setPreferredSize(new Dimension(500, 150));
		setTitle(titleStr);

		setContentPane(buildContentPanel(e));

		pack();
		setLocationRelativeTo(null);

		UIUtil.closeOnEscapePressed(this);
	}

	private String formatStackTrace(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		t.printStackTrace(pw);
		pw.flush();
		sw.flush();

		return sw.toString();
	}

	private JPanel buildContentPanel(Throwable error)
	{
		// Default values in case that resources cannot be loaded
		String closeStr = "Close";
		String detailsStr = "Details >>";
		String messageStr = "";
		String clipStr = "Copy to clipboard";
		String errorDetailsStr = "Error details";

		Icon errorIcon = null;

		try
		{
			TextResource res = Main.getTextResource("exception");
			closeStr = res.get("exception-dialog.close");
			detailsStr = res.get("exception-dialog.details");
			messageStr = res.get("exception-dialog.message");
			clipStr = res.get("exception-dialog.copytoclipboard");

			errorIcon = Icons.getInstance().get(res.get("exception-dialog.icon"));
		}
		catch (Throwable e0)
		{
		}

		final JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel errorPanel = new JPanel();
		errorPanel.setLayout(new BorderLayout());
		if (errorIcon != null)
		{
			JLabel iconLabel = new JLabel(errorIcon);
			iconLabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 7));
			errorPanel.add(iconLabel, BorderLayout.LINE_START);
		}
		
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		messagePanel.add(
			new JLabel(messageStr),
			BorderLayout.PAGE_START);
		
		JLabel errorType = new JLabel(error.getClass().getSimpleName() + ": " + error.getLocalizedMessage());
		errorType.setFont(errorType.getFont().deriveFont(Font.BOLD));
		messagePanel.add(errorType, BorderLayout.CENTER);
		
		errorPanel.add(messagePanel, BorderLayout.CENTER);
		
		contentPane.add(errorPanel, BorderLayout.PAGE_START);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		// Print the stack trace to a string...
		final String errorText = formatStackTrace(error);

		JButton clipBoard = new JButton(clipStr);
		clipBoard.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					StringSelection selection = new StringSelection(errorText);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
				}
				catch (Exception e1)
				{
					// Do (almost) nothing
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(clipBoard);

		JButton close = new JButton(closeStr);
		close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});

		buttonPanel.add(close);

		final String errorDetailsStrF = errorDetailsStr;

		final JButton details = new JButton(detailsStr);
		details.addActionListener(new ActionListener()
		{
			/**
			 * When the user clicks the details button, add the stack trace to the dialog and
			 * disable the details button.
			 */
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				JPanel detailsPanel = new JPanel();
				detailsPanel.setLayout(new BorderLayout());
				detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

				contentPane.add(detailsPanel, BorderLayout.CENTER);

				JLabel detailsLabel = new JLabel(errorDetailsStrF);
				detailsPanel.add(detailsLabel, BorderLayout.PAGE_START);

				/* This special construction of a JTextPane prevents line wrapping, allowing the
				 * scrollpane to do its job horizontally. */
				final JTextPane detailsText = new JTextPane()
				{

					private static final long	serialVersionUID	= 1L;

					@Override
					public boolean getScrollableTracksViewportWidth()
					{
						return (getSize().width < getParent().getSize().width);
					}

					@Override
					public void setSize(Dimension d)
					{
						if (d.width < getParent().getSize().width)
						{
							d.width = getParent().getSize().width;
						}
						super.setSize(d);
					}
				};

				detailsText.setEditable(false);
				/* Uncomment this line if you don't like the white background of the text pane */
				// detailsText.setBackground(contentPane.getBackground());

				detailsText.setText(errorText);

				JScrollPane scrollPane = new JScrollPane(detailsText);

				detailsPanel.add(scrollPane, BorderLayout.CENTER);

				details.setEnabled(false);

				// expand the dialog size to make room for the details
				setPreferredSize(new Dimension(600, 400));

				pack();
			}
		});

		buttonPanel.add(details);
		contentPane.add(buttonPanel, BorderLayout.PAGE_END);

		return contentPane;
	}
}