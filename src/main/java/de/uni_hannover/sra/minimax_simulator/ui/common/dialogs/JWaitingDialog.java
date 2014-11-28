package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

public final class JWaitingDialog extends JDialog
{
	private final JLabel _message;

	public JWaitingDialog(JFrame parent, String title, String message,
			final Runnable cancelAction)
	{
		super(parent);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle(title);

		setModalityType(ModalityType.APPLICATION_MODAL);
		setLayout(new GridBagLayout());

		GridBagConstraints up = new GridBagConstraints();
		GridBagConstraints middle = new GridBagConstraints();
		GridBagConstraints down = new GridBagConstraints();

		up.insets = down.insets = middle.insets = new Insets(10, 10, 10, 10);
		middle.gridy = 1;
		down.gridy = 2;

		JProgressBar prog = new JProgressBar();
		prog.setIndeterminate(true);
		_message = new JLabel(message);

		add(_message, up);
		add(prog, middle);

		if (cancelAction != null)
		{
			TextResource res = Application.getTextResource("application");
			final JButton cancel = new JButton(res.get("wait.cancel"));
			add(cancel, down);

			cancel.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					cancel.setEnabled(false);
					cancelAction.run();
				}
			});
		}

		pack();
		setLocationRelativeTo(null);
	}
}