package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.Version;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;

@Deprecated
public class JInfoDialog extends JDialog implements ActionListener
{
	public JInfoDialog(TextResource res)
	{
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle(res.get("info.title"));
		setResizable(false);

		Version version = Application.getVersion();

		JLabel image1 = new JLabel(Icons.getInstance().get(res.get("info.image1")));
		JLabel image2 = new JLabel(Icons.getInstance().get(res.get("info.image2")));

		JLabel appname = new JLabel(version.getModuleName() + " "
			+ version.getVersionNumber());
		JLabel apprevision = new JLabel(res.format("info.build", version.getBuildTime(),
			version.getBuildJdk()));

		Icon authorIcon = Icons.getInstance().get(res.get("info.author.icon"));

		JLabel author = new JLabel(res.format("info.author", version.getAuthorName()),
			authorIcon, SwingConstants.RIGHT);
		JLabel company = new JLabel(version.getCompanyName());

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = new Insets(20, 20, 20, 20);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		add(image1, c);

		c.insets.top = c.insets.bottom = 10;
		c.anchor = GridBagConstraints.WEST;
		c.gridy++;
		add(appname, c);
		c.gridy++;
		add(apprevision, c);

		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 3;
		c.insets.bottom = 20;
		add(image2, c);
		c.insets.bottom = 10;
		c.gridheight = 1;
		c.gridx = 1;
		add(author, c);
		c.gridy++;
		add(company, c);

		c.gridy++;
		c.anchor = GridBagConstraints.EAST;

		JButton ok = new JButton(res.get("info.ok"));
		ok.addActionListener(this);
		add(ok, c);

		pack();
		setLocationRelativeTo(null);

		UIUtil.closeOnEscapePressed(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		dispose();
	}
}
