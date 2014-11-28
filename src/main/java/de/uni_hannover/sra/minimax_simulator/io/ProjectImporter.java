package de.uni_hannover.sra.minimax_simulator.io;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;

/**
 * A project importer is an entity that represents some kind of data source, like
 * a project file, folder or database, being the converse of a {@link ProjectExporter}.<br>
 * It is able to import and return a {@link Project}.
 * 
 * @author Martin
 *
 */
public interface ProjectImporter
{
	/**
	 * Imports a project from this data sink. Calling this method multiple times is not guaranteed to produce equal
	 * {@link Project} instances since the import process is usually tied to underlying storages like file systems.<br>
	 * A failed import will not affect the importer instance but will result in a thrown exception.
	 * 
	 * @return the successfully imported {@link Project} instance
	 * @throws ProjectImportException if the import failed
	 */
	public Project importProject() throws ProjectImportException;
}