package de.uni_hannover.sra.minimax_simulator.io.importer;

import de.uni_hannover.sra.minimax_simulator.io.exporter.ProjectExporter;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;

/**
 * A {@code ProjectImporter} is an entity that represents some kind of data source, like
 * a project file, folder or database, being the converse of a {@link ProjectExporter}.<br>
 * It is able to import and return a {@link Project}.
 * 
 * @author Martin L&uuml;ck
 */
@FunctionalInterface
public interface ProjectImporter {

    /**
     * Imports a project from this data sink. Calling this method multiple times is not guaranteed to produce equal
     * {@link Project} instances since the import process is usually tied to underlying storage like file systems.<br>
     * A failed import will not affect the importer instance but will result in a thrown exception.
     *
     * @return
     *          the successfully imported {@link Project} instance
     * @throws ProjectImportException
     *          thrown if the import failed
     */
    public Project importProject() throws ProjectImportException;
}