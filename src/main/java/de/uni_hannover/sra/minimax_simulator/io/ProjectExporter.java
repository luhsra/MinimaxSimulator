package de.uni_hannover.sra.minimax_simulator.io;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;

/**
 * A project exporter is an entity that represents some kind of data sink, like
 * a project file, folder or database, being the converse of a {@link ProjectImporter}.<br>
 * It is able to export a {@link Project}.
 * 
 * @author Martin
 *
 */
public interface ProjectExporter
{
	/**
	 * Exports a project into this data sink. Calling this method successfully will overwrite
	 * previously exported projects.<br>
	 * A failed export will leave the exporter in a undetermined, possibly corrupted, state.
	 * This behaviour is implementation dependent.
	 * 
	 * @param project the {@link Project} instance to export. May not be null.
	 * @throws ProjectExportException if the export failed
	 */
	public void exportProject(Project project) throws ProjectExportException;
}