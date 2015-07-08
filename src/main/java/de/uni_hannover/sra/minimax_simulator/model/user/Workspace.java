package de.uni_hannover.sra.minimax_simulator.model.user;

import java.io.File;

import de.uni_hannover.sra.minimax_simulator.io.ProjectExportException;
import de.uni_hannover.sra.minimax_simulator.io.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.io.exporter.xml.ProjectZipExporter;
import de.uni_hannover.sra.minimax_simulator.io.importer.xml.ProjectZipImporter;
import de.uni_hannover.sra.minimax_simulator.util.ListenerContainer;

/**
 * A container that can either hold a single project or be empty.
 * 
 * @author Martin
 * 
 */
public class Workspace extends ListenerContainer<WorkspaceListener>
{
	private Project	_currentProject;
	private File	_currentProjectFile;

	private File	_lastProjectFolder;

	public Workspace()
	{
		// on application start, no project is loaded
		_currentProject = null;
		_currentProjectFile = null;
		_lastProjectFolder = null;
	}

	public Project getProject()
	{
		return _currentProject;
	}

	/**
	 * Uses the given file to import a project from it. On failure, an exception is thrown. On
	 * success, the current project, if existing, is discarded. <br>
	 * <br>
	 * Then all {@link WorkspaceListener}s are notified via their
	 * {@link WorkspaceListener#projectOpened()} method. <br>
	 * <br>
	 * This method does not check if the current project has unsaved data.
	 * 
	 * @param file
	 * @throws ProjectImportException
	 * @throws Exception
	 */
	public void openProject(File file) throws ProjectImportException
	{
		if (_currentProject != null)
			closeProject();
		System.out.println("DEBUG: create new project zip importer");
		_currentProject = new ProjectZipImporter(file).importProject();
		System.out.println("DEBUG: set current project file");
		_currentProjectFile = file;
		System.out.println("DEBUG: getParentFile()");
		_lastProjectFolder = file.getParentFile();

		System.out.println("DEBUG: notify all listeners");
		for (WorkspaceListener l : getListeners()) {
			System.out.println("DEBUG: notify listener " + l.toString());
			l.onProjectOpened(_currentProject);
		}
	}

	/**
	 * Closes the current project and sets it to <code>null</code>. <br>
	 * <br>
	 * Then all {@link WorkspaceListener}s are notified via their
	 * {@link WorkspaceListener#projectClosed()} method. <br>
	 * <br>
	 * Discards any unsaved project data.
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void closeProject()
	{
		Project oldProject = _currentProject;
		_currentProject = null;
		_currentProjectFile = null;

		if (oldProject == null)
			return;

		for (WorkspaceListener l : getListeners())
			l.onProjectClosed(oldProject);
	}

	/**
	 * Saves the currently open project to the given file. The file is also saved as return value
	 * for following {@link #getLastProjectFolder()} calls. <br>
	 * <br>
	 * Then all {@link WorkspaceListener}s are notified via their
	 * {@link WorkspaceListener#projectSaved()} method.
	 * 
	 * @param file
	 * @throws ProjectExportException
	 * @throws Exception
	 */
	public void saveProject(File file) throws ProjectExportException
	{
		new ProjectZipExporter(file).exportProject(_currentProject);

		_currentProject.setIsSaved();
		_currentProjectFile = file;
		_lastProjectFolder = file.getParentFile();

		for (WorkspaceListener l : getListeners())
			l.onProjectSaved(_currentProject);
	}

	/**
	 * Creates a new project. <br>
	 * <br>
	 * Then all {@link WorkspaceListener}s are notified via their
	 * {@link WorkspaceListener#projectOpened()} method. <br>
	 * <br>
	 * Discards any unsaved data.
	 */
	public void newProject()
	{
		if (_currentProject != null)
			closeProject();
		System.out.println("DEBUG: call NewProjectBuilder().buildProject()");
		_currentProject = new NewProjectBuilder().buildProject();
		System.out.println("DEBUG: call NewProjectBuilder().buildProject() succeeded");
		for (WorkspaceListener l : getListeners())
			l.onProjectOpened(_currentProject);
	}

	/**
	 * Marks the current opened project as unsaved and notifies all listeners. <br>
	 * <br>
	 * Does nothing if there is currently no open project or if it is already marked as unsaved.
	 * 
	 */
	public void setProjectUnsaved()
	{
		if (_currentProject == null)
			return;

		if (_currentProject.isUnsaved())
			return;

		_currentProject.setIsUnsaved();

		for (WorkspaceListener l : getListeners())
			l.onProjectDirty(_currentProject);
	}

	/**
	 * @return the {@link File} representing the folder where the last save or load action for a
	 *         Project was executed. This is only <code>null</code> if there was no project saved or
	 *         loaded yet.
	 */
	public File getLastProjectFolder()
	{
		return _lastProjectFolder;
	}

	/**
	 * @return the {@link File} argument of the last {@link #saveProject(File)} call, or
	 *         <code>null</code> if the method was not yet called for the current project, that is
	 *         if the current project was never saved or does not exist.
	 */
	public File getCurrentProjectFile()
	{
		return _currentProjectFile;
	}

	/**
	 * @return <tt>true</tt> if there is actually an open project and if this project has unsaved
	 *         changes, <tt>false</tt> otherwise. A newly created project has no unsaved changes.
	 */
	public boolean isUnsaved()
	{
		return _currentProject != null && _currentProject.isUnsaved();
	}
}