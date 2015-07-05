package de.uni_hannover.sra.minimax_simulator.ui;

import static com.google.common.base.Preconditions.*;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.google.common.base.Throwables;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.actions.ProjectSaveTo;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.common.FillLayout;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXWaitingDialog;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 *
 * @author Philipp Rohde
 * @author Martin L&uuml;ck
 */
public class UIUtil
{
	/**
	 * Registers an Action on the dialog on the Esc key that disposes the dialog.
	 * 
	 * @param dialog
	 */
	//TODO: convert to JavaFX
	public static void closeOnEscapePressed(final JDialog dialog)
	{
		JRootPane rootPane = dialog.getRootPane();
		InputMap im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = rootPane.getActionMap();
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

		im.put(ks, "UiUtilsEscape");
		actionMap.put("UiUtilsEscape", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
	}

	/**
	 * Run a {@link Runnable} instance a single time as soon as the given {@link JComponent} is
	 * removed from its ancestor.
	 * 
	 * @param component
	 * @param runner
	 */
	@Deprecated
	public static void runOnRemoval(final JComponent component, Runnable runner)
	{
		final Runnable runner0 = checkNotNull(runner);
		component.addHierarchyListener(new HierarchyListener() {
			@Override
			public void hierarchyChanged(HierarchyEvent arg0) {
				if ((arg0.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0) {
					if (!arg0.getComponent().isDisplayable()) {
						runner0.run();
						component.removeHierarchyListener(this);
					}
				}
			}
		});
	}

	/**
	 * Disposes an object that is also an {@link JComponent} as soon as it is removed from its
	 * ancestor.
	 * 
	 * @param component
	 */
	@Deprecated
	public static <T extends JComponent & Disposable> void disposeOnRemoval(
			final T component)
	{
		runOnRemoval(component, new Runnable()
		{
			@Override
			public void run()
			{
				component.dispose();
			}
		});
	}

	/**
	 * Disposes an object as soon as the given {@link JComponent} is removed from its ancestor.
	 * 
	 * @param component
	 */
	@Deprecated
	public static void disposeOnRemoval(final JComponent component,
			final Disposable disposable)
	{
		runOnRemoval(component, new Runnable()
		{
			@Override
			public void run()
			{
				disposable.dispose();
			}
		});
	}

	/**
	 * Checks if the current project is unsaved, and in this case asks the user to confirm to
	 * discard the unsaved changes inside the project and continue the selected action.
	 * 
	 * @return <code>true</code>, if the project is already saved or the user confirmed to discard
	 *         pending changes, otherwise <code>false</code>
	 */
	//TODO: convert to JavaFX
	public static boolean confirmCloseProject()
	{
		if (Application.getWorkspace().isUnsaved())
		{
			TextResource res = Application.getTextResource("application");

			return confirmCloseProject(res.get("close-project.generic.message"),
				res.get("close-project.generic.title"));
		}
		return true;
	}

	/**
	 * Checks if the current project is unsaved, and in this case asks the user to confirm to
	 * discard the unsaved changes inside the project and exit the application.
	 * 
	 * @return <code>true</code>, if the project is already saved or the user confirmed to discard
	 *         pending changes, otherwise <code>false</code>
	 */
	//TODO: convert to JavaFX
	public static boolean confirmCloseProjectAndExit()
	{
		if (Application.getWorkspace().isUnsaved())
		{
			TextResource res = Application.getTextResource("application");

			return confirmCloseProject(res.get("close-project.exit.message"),
				res.get("close-project.exit.title"));
		}
		return true;
	}

	//TODO: convert to JavaFX
	public static boolean confirmOverwriteFile(String filename)
	{
		TextResource res = Application.getTextResource("application");

		return JOptionPane.showConfirmDialog(Application.getMainWindow(),
			res.format("application.overwrite.confirm.message", filename),
			res.get("application.overwrite.confirm.title"), JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION;
	}

	/**
	 * Utility method for modal dialogs.<br>
	 * <br>
	 * <br>
	 * This method enqueues a single {@link Runnable} into the event dispatch thread that
	 * immediately throws the given {@link Throwable}.<br>
	 * <br>
	 * This is useful for exception handling (like logging) in modal dialogs, since the event
	 * dispatch thread ignores its exception handler when a modal dialog is open.<br>
	 * A client can call this method when an error occured in a modal situation and then close the
	 * dialog before returning control to the event dispatch thread.<br>
	 * <br>
	 * However, an exception that was re-posted in this way can not be catched from the caller of
	 * the client itself. The client therefore should be a listener called from the event dispatch
	 * thread itself.
	 * 
	 * @param t
	 */
	@Deprecated
	public static void throwInEDT(final Throwable t)
	{
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Throwables.propagate(t);
			}
		});
	}

	/**
	 * Like {@link #executeWorker(Runnable, String, String)}, using an empty title and message for
	 * the progress dialog.
	 */
	public static void executeWorker(Runnable runnable)
	{
		executeWorker(runnable, "", "", null);
	}

	/**
	 * Like {@link #executeWorker(Runnable, String, String, Runnable)}, but the user cannot cancel
	 * the computation.
	 */
	public static void executeWorker(Runnable runnable, String waitingTitle,
			String waitingMessage)
	{
		executeWorker(runnable, waitingTitle, waitingMessage, null);
	}

	/**
	 * Execute a {@link Runnable} instance and show a cancelable progress dialog until the
	 * computation finished.<br>
	 * Canceling the action in the dialog will NOT automatically interrupt the {@link Runnable} or
	 * prevent it from executing, this must be done manually inside the <code>run()</code> method.<br>
	 * The user can specify a second {@link Runnable} that is executed if the user wishes to cancel
	 * the computation.
	 * 
	 * @param runnable
	 *            the {@link Runnable} to execute
	 * @param waitingTitle
	 *            the title of the progress dialog
	 * @param waitingMessage
	 *            the message of the progress dialog
	 * @param cancelAction
	 *            the {@link Runnable} to execute if the user chooses to cancel the computation
	 */
	//TODO: conversion to JavaFX; finished?
	public static void executeWorker(final Runnable runnable, String waitingTitle,
			String waitingMessage, Runnable cancelAction)
	{

		FXWaitingDialog waitingDialog = new FXWaitingDialog(waitingTitle, waitingMessage);

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				//Thread.sleep(5*1000);
				runnable.run();
				return null;
			}
		};

		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t)
			{
				waitingDialog.close();
			}
		});

		task.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t)
			{
				if (cancelAction != null) {
					cancelAction.run();
				}
			}
		});

		Thread th = new Thread(task);
		th.setDaemon(false);
		th.start();

		if (waitingDialog.isCanceled()) {
			task.cancel(true);
			System.out.println("canceled...");
		}

	}

	//TODO: Conversion to JavaFX
	private static boolean confirmCloseProject(String message, String title)
	{
		int result = JOptionPane.showConfirmDialog(Application.getMainWindow(), message,
			title, JOptionPane.YES_NO_CANCEL_OPTION);

		switch (result)
		{
			case JOptionPane.YES_OPTION:
				return new ProjectSaveTo().save(Application.getWorkspace().getLastProjectFolder());

			case JOptionPane.NO_OPTION:
				return true;

				// cancel
			default:
				return false;
		}
	}

	@Deprecated
	public static JButton loadButton(ResourceBundle res, String key)
	{
		return new JButton(res.getString(key + ".label"), Icons.getInstance().get(
			res.getString(key + ".icon")));
	}

	@Deprecated
	public static JButton loadButton(TextResource res, String key)
	{
		return new JButton(res.get(key + ".label"), Icons.getInstance().get(
			res.get(key + ".icon")));
	}

	@Deprecated
	public static Border createGroupBorder(String title)
	{
		return new TitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
			title, TitledBorder.LEFT, TitledBorder.TOP);
	}

	@Deprecated
	public static Border createTitledBevelBorder(String title)
	{
		return new TitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED),
			title, TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
	}

	@Deprecated
	public static JPanel wrapInTitledGroupScroller(JComponent comp, String title)
	{
		JScrollPane scroller = new JScrollPane(comp);

		JPanel panel = new JPanel();
		panel.setBorder(createGroupBorder(title));
		panel.setLayout(FillLayout.INSTANCE);
		panel.add(scroller);
		return panel;
	}

	@Deprecated
	public static JPanel wrapInTitledGroupPanel(JComponent comp, String title)
	{
		JPanel panel = new JPanel();
		panel.setBorder(createGroupBorder(title));
		panel.setLayout(FillLayout.INSTANCE);
		panel.add(comp);
		return panel;
	}

	@Deprecated
	public static JPanel wrapInTitledBevelPanel(JComponent comp, String title)
	{
		JPanel panel = new JPanel();
		panel.setBorder(createTitledBevelBorder(title));
		panel.setLayout(FillLayout.INSTANCE);
		panel.add(comp);
		return panel;
	}

	@Deprecated
	public static JPanel wrapInBorderPanel(JComponent comp, Border border, String title)
	{
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(border, title));
		panel.setLayout(FillLayout.INSTANCE);
		panel.add(comp);
		return panel;
	}

	@Deprecated
	public static void doEnable(JComponent component)
	{
		if (!component.isEnabled())
			component.setEnabled(true);
	}

	@Deprecated
	public static void doEnable(JComponent... components)
	{
		for (JComponent comp : components)
			if (!comp.isEnabled())
				comp.setEnabled(true);
	}

	@Deprecated
	public static void doDisable(JComponent component)
	{
		if (component.isEnabled())
			component.setEnabled(false);
	}

	@Deprecated
	public static void doDisable(JComponent... components)
	{
		for (JComponent comp : components)
			if (comp.isEnabled())
				comp.setEnabled(false);
	}

	@Deprecated
	public static void doEnable(boolean enabled, JComponent component)
	{
		if (component.isEnabled() != enabled)
			component.setEnabled(enabled);
	}

	@Deprecated
	public static void doEnable(boolean enabled, JComponent... components)
	{
		for (JComponent comp : components)
			if (comp.isEnabled() != enabled)
				comp.setEnabled(enabled);
	}

}