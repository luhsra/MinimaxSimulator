package de.uni_hannover.sra.minimax_simulator.model.user;

/**
 * A {@code WorkspaceListener} is a class that needs to react to changes of the {@link Workspace}.
 *
 * @author Martin L&uuml;ck
 */
public interface WorkspaceListener {

    /**
     * Notifies the listener about opening a {@link Project}.
     *
     * @param project
     *          the opened {@code Project}
     */
    public void onProjectOpened(Project project);

    /**
     * Notifies the listener about saving of a {@link Project}.
     *
     * @param project
     *          the saved {@code Project}
     */
    public void onProjectSaved(Project project);

    /**
     * Notifies the listener about closing of a {@link Project}.
     *
     * @param project
     *          the closed {@code Project}
     */
    public void onProjectClosed(Project project);

    /**
     * Notifies the listener about a change of a {@link Project}.
     *
     * @param project
     *          the modified {@code Project}
     */
    public void onProjectDirty(Project project);
}