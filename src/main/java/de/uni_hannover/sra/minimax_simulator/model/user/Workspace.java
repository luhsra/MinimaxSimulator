package de.uni_hannover.sra.minimax_simulator.model.user;

import de.uni_hannover.sra.minimax_simulator.io.exporter.ProjectExportException;
import de.uni_hannover.sra.minimax_simulator.io.exporter.json.ProjectZipExporter;
import de.uni_hannover.sra.minimax_simulator.io.importer.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.io.importer.json.ProjectZipImporter;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.UndoManager;
import de.uni_hannover.sra.minimax_simulator.util.ListenerContainer;

import java.io.File;

/**
 * A container that can either hold a single project or be empty.
 * 
 * @author Martin L&uuml;ck
 */
public class Workspace extends ListenerContainer<WorkspaceListener> {

    private Project currentProject;
    private File currentProjectFile;

    private File lastProjectFolder;

    /**
     * Creates an empty {@code Workspace}.<br>
     * <br>
     * On application start no project is loaded.
     */
    public Workspace() {
        currentProject = null;
        currentProjectFile = null;
        lastProjectFolder = null;
    }

    /**
     * Gets the current {@link Project} of the {@code Workspace}.
     *
     * @return
     *          the current {@code Project}
     */
    public Project getProject() {
        return currentProject;
    }

    /**
     * Uses the specified file to import a project from it. On failure, an exception is thrown. On
     * success, the current project, if existing, is discarded.<br>
     * <br>
     * Then all {@link WorkspaceListener}s are notified via their
     * {@link WorkspaceListener#onProjectOpened(Project)} method.<br>
     * <br>
     * This method does not check if the current project has unsaved data.
     *
     * @param file
     *          the {@code File} to open
     * @throws ProjectImportException
     *          thrown if there was a failure during opening
     */
    public void openProject(File file) throws ProjectImportException {
        if (currentProject != null) {
            closeProject();
        }

        currentProject = new ProjectZipImporter(file).importProject();
        currentProjectFile = file;
        lastProjectFolder = file.getParentFile();

        for (WorkspaceListener l : getListeners()) {
            l.onProjectOpened(currentProject);
        }
    }

    /**
     * Closes the current project and sets it to <code>null</code>.<br>
     * <br>
     * Then all {@link WorkspaceListener}s are notified via their
     * {@link WorkspaceListener#onProjectOpened(Project)} method.<br>
     * <br>
     * Discards any unsaved project data.
     */
    public void closeProject() {
        Project oldProject = currentProject;
        currentProject = null;
        currentProjectFile = null;
        UndoManager.INSTANCE.reset();

        if (oldProject == null) {
            return;
        }

        for (WorkspaceListener l : getListeners()) {
            l.onProjectClosed(oldProject);
        }
    }

    /**
     * Saves the currently open project to the specified file. The file is also saved as return value
     * for following {@link #getLastProjectFolder()} calls.<br>
     * <br>
     * Then all {@link WorkspaceListener}s are notified via their
     * {@link WorkspaceListener#onProjectSaved(Project)} method.
     *
     * @param file
     *          the {@code File} to save to
     * @throws ProjectExportException
     *          thrown if there was a failure during saving
     */
    public void saveProject(File file) throws ProjectExportException {
        new ProjectZipExporter(file).exportProject(currentProject);

        currentProject.setIsSaved();
        currentProjectFile = file;
        lastProjectFolder = file.getParentFile();

        for (WorkspaceListener l : getListeners()) {
            l.onProjectSaved(currentProject);
        }
    }

    /**
     * Creates a new project.<br>
     * <br>
     * Then all {@link WorkspaceListener}s are notified via their
     * {@link WorkspaceListener#onProjectOpened(Project)} method.<br>
     * <br>
     * Discards any unsaved data.
     */
    public void newProject() {
        if (currentProject != null) {
            closeProject();
        }
        currentProject = new NewProjectBuilder().buildProject();

        for (WorkspaceListener l : getListeners()) {
            l.onProjectOpened(currentProject);
        }
    }

    /**
     * Marks the current opened project as unsaved and notifies all listeners.<br>
     * <br>
     * Does nothing if there is currently no open project or if it is already marked as unsaved.
     */
    public void setProjectUnsaved() {
        if (currentProject == null) {
            return;
        }
        else if (currentProject.isUnsaved()) {
            return;
        }

        currentProject.setIsUnsaved();

        for (WorkspaceListener l : getListeners()) {
            l.onProjectDirty(currentProject);
        }
    }

    /**
     * Gets the {@code File} representing the folder where the last save or load action for a
     * Project was executed. This is only {@code null} if there was no project saved or loaded yet.
     *
     * @return
     *          the last project folder
     */
    public File getLastProjectFolder() {
        return lastProjectFolder;
    }

    /**
     * Gets the {@code File} argument of the last {@link #saveProject(File)} call or {@code null}
     * if the method was not yet called for the current project, that is if the current project
     * was never saved or does not exist.
     *
     * @return
     *          the current {@code Project}'s {@code File}
     */
    public File getCurrentProjectFile() {
        return currentProjectFile;
    }

    /**
     * Gets the value of the {@code unsaved} property.
     *
     * @return
     *          {@code true} if there is actually an open {@code Project} and if this {@code Project} has
     *          unsaved changes, {@code false} otherwise. A newly created project has no unsaved changes.
     */
    public boolean isUnsaved() {
        return currentProject != null && currentProject.isUnsaved();
    }
}