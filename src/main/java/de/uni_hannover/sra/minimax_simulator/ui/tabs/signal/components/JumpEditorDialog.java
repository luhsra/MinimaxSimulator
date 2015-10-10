package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.components;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import de.uni_hannover.sra.minimax_simulator.Main;
import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.DefaultJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.AbstractDocumentListener;

@Deprecated
public class JumpEditorDialog extends JDialog
{
	private class OkAction extends AbstractAction
	{
		public OkAction(String str)
		{
			super(str);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (_jumpIsDefault.isSelected())
			{
				_jump = DefaultJump.INSTANCE;
			}
			else if (_jumpIsUnconditional.isSelected())
			{
				try
				{
					int targetRow = Integer.parseInt(_uncondRow.getText());
					_jump = new UnconditionalJump(targetRow);
				}
				catch (NumberFormatException nfe)
				{
					// ok button should not be enabled
					_jump = null;
					return;
				}
			}
			else if (_jumpIsConditional.isSelected())
			{
				try
				{
					int targetRow0 = Integer.parseInt(_cond0Row.getText());
					int targetRow1 = Integer.parseInt(_cond1Row.getText());
					_jump = new ConditionalJump(targetRow0, targetRow1);
				}
				catch (NumberFormatException nfe)
				{
					// ok button should not be enabled
					_jump = null;
					return;
				}
			}
			dispose();
		}
	}

	private class DocumentListener extends AbstractDocumentListener
	{
		private final JTextField	_textField;

		public DocumentListener(JTextField text)
		{
			_textField = text;
		}

		@Override
		public void documentChanged(DocumentEvent e)
		{
			_okAction.setEnabled(isValidRowNumber(_textField.getText()));
		}
	}

	private Jump				_jump;

	private final JTextField	_uncondRow;
	private final JTextField	_cond1Row;
	private final JTextField	_cond0Row;

	private final JComboBox		_uncondRowHelper;
	private final JComboBox		_cond0RowHelper;
	private final JComboBox		_cond1RowHelper;

	private final JRadioButton	_jumpIsDefault;
	private final JRadioButton	_jumpIsUnconditional;
	private final JRadioButton	_jumpIsConditional;

	private final Action		_okAction;

	private LayoutManager createLayout()
	{
		// selection buttons, jump condition, jump target, label combobox
		String columns = "[left]unrel[min!]rel[]unrel[]";

		// default jump, 1-jump, 2-jump, button row
		String rows = "[]unrel[]unrel[]rel[]50lp[]";

		MigLayout layout = new MigLayout("fill", columns, rows);
		return layout;
	}

	public JumpEditorDialog(SignalTable signalTable, SignalRow row)
	{
		_jump = null;

		TextResource res = Main.getTextResource("machine").using("signal.jump");

		_okAction = new OkAction(res.get("ok"));

		setModalityType(ModalityType.APPLICATION_MODAL);
		setLayout(createLayout());
		setResizable(false);

		ButtonGroup group = new ButtonGroup();

		_jumpIsDefault = new JRadioButton(res.get("default"));
		_jumpIsUnconditional = new JRadioButton(res.get("unconditional"));
		_jumpIsConditional = new JRadioButton(res.get("conditional"));

		_jumpIsDefault.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				selectDefault();
			}
		});
		_jumpIsUnconditional.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				selectUnconditional();
			}
		});
		_jumpIsConditional.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				selectConditional();
			}
		});

		group.add(_jumpIsDefault);
		group.add(_jumpIsUnconditional);
		group.add(_jumpIsConditional);

		add(_jumpIsDefault, "cell 0 0");
		add(_jumpIsUnconditional, "cell 0 1");
		add(_jumpIsConditional, "cell 0 2 1 2, top");

		JLabel cond1Label = new JLabel("1");
		JLabel cond0Label = new JLabel("0");

		add(cond1Label, "cell 1 2");
		add(cond0Label, "cell 1 3");

		_uncondRow = new JTextField(4);
		_cond1Row = new JTextField(4);
		_cond0Row = new JTextField(4);
		_uncondRow.setEnabled(false);
		_cond1Row.setEnabled(false);
		_cond0Row.setEnabled(false);

		_uncondRow.getDocument().addDocumentListener(new DocumentListener(_uncondRow));
		_cond1Row.getDocument().addDocumentListener(new DocumentListener(_cond1Row));
		_cond0Row.getDocument().addDocumentListener(new DocumentListener(_cond0Row));

		add(_uncondRow, "cell 2 1");
		add(_cond1Row, "cell 2 2");
		add(_cond0Row, "cell 2 3");

		Jump jump = row.getJump();
		_uncondRowHelper = new JumpLabelSelector(_uncondRow, signalTable, getCurrentUnconditionalRow(jump));
		_cond1RowHelper = new JumpLabelSelector(_cond1Row, signalTable, getCurrentConditionalRow(jump, 1));
		_cond0RowHelper = new JumpLabelSelector(_cond0Row, signalTable, getCurrentConditionalRow(jump, 0));
		add(_uncondRowHelper, "cell 3 1");
		add(_cond1RowHelper, "cell 3 2");
		add(_cond0RowHelper, "cell 3 3");

		JButton cancel = new JButton(res.get("cancel"));
		add(new JButton(_okAction), "cell 0 4 3 1");
		add(cancel, "cell 0 4 3 1");

		UIUtil.closeOnEscapePressed(this);

		pack();
		setLocationRelativeTo(null);

		cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				_jump = null;
				dispose();
			}
		});

		setJump(row.getJump());
	}

	private int getCurrentUnconditionalRow(Jump jump)
	{
		if (jump instanceof UnconditionalJump)
		{
			return ((UnconditionalJump) jump).getTargetRow();
		}
		return -1;
	}

	private int getCurrentConditionalRow(Jump jump, int cond)
	{
		if (jump instanceof ConditionalJump)
		{
			return ((ConditionalJump) jump).getTargetRow(cond);
		}
		return -1;
	}

	private void setJump(Jump jump)
	{
		if (jump instanceof DefaultJump)
		{
			_jumpIsDefault.setSelected(true);
			selectDefault();
		}
		else if (jump instanceof UnconditionalJump)
		{
			_jumpIsUnconditional.setSelected(true);
			int targetRow = ((UnconditionalJump) jump).getTargetRow();
			_uncondRow.setText(Integer.toString(targetRow));
			selectUnconditional();
		}
		else if (jump instanceof ConditionalJump)
		{
			_jumpIsConditional.setSelected(true);
			int targetRow0 = ((ConditionalJump) jump).getTargetRow(0);
			int targetRow1 = ((ConditionalJump) jump).getTargetRow(1);
			_cond0Row.setText(Integer.toString(targetRow0));
			_cond1Row.setText(Integer.toString(targetRow1));
			selectConditional();
		}
	}

	private void selectDefault()
	{
		disable(_uncondRow);
		disable(_uncondRowHelper);
		disable(_cond0Row);
		disable(_cond0RowHelper);
		disable(_cond1Row);
		disable(_cond1RowHelper);
		_okAction.setEnabled(true);
	}

	private void selectUnconditional()
	{
		enable(_uncondRow);
		enable(_uncondRowHelper);
		disable(_cond0Row);
		disable(_cond0RowHelper);
		disable(_cond1Row);
		disable(_cond1RowHelper);
		_okAction.setEnabled(isValidRowNumber(_uncondRow.getText()));
	}

	private void selectConditional()
	{
		disable(_uncondRow);
		disable(_uncondRowHelper);
		enable(_cond0Row);
		enable(_cond0RowHelper);
		enable(_cond1Row);
		enable(_cond1RowHelper);
		_okAction.setEnabled(isValidRowNumber(_cond0Row.getText())
			&& isValidRowNumber(_cond1Row.getText()));
	}

	private boolean isValidRowNumber(String text)
	{
		try
		{
			return Integer.parseInt(text) >= 0;
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}

	private void enable(JComponent component)
	{
		if (!component.isEnabled())
			component.setEnabled(true);
	}

	private void disable(JComponent component)
	{
		if (component.isEnabled())
			component.setEnabled(false);
	}

	public Jump getJump()
	{
		return _jump;
	}
}