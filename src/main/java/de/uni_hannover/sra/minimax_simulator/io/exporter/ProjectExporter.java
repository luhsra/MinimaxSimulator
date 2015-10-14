package de.uni_hannover.sra.minimax_simulator.io.exporter;

import de.uni_hannover.sra.minimax_simulator.io.importer.ProjectImporter;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;

/**
 * A {@code ProjectExporter} is an entity that represents some kind of data sink, like
 * a project file, folder or database, being the converse of a {@link ProjectImporter}.<br>
 * It is able to export a {@link Project}.
 * 
 * @author Martin L&uuml;ck
 */
public interface ProjectExporter {

	/**
	 * Exports a project into this data sink. Calling this method successfully will overwrite
	 * previously exported projects.<br>
	 * A failed export will leave the exporter in a undetermined, possibly corrupted, state.
	 * This behaviour is implementation dependent.
	 * 
	 * @param project
	 *          the {@link Project} instance to export. May not be null.
	 * @throws ProjectExportException
	 *          thrown if the export failed
	 */
	public void exportProject(Project project) throws ProjectExportException;
}