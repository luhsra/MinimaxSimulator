package de.uni_hannover.sra.minimax_simulator.io;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;

/**
 * A checked exception indicating that exporting a {@link Project} has failed.
 * 
 * @author Martin
 *
 */
public class ProjectExportException extends Exception
{
	public ProjectExportException()
	{
		super();
	}

	public ProjectExportException(String message)
	{
		super(message);
	}

	public ProjectExportException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ProjectExportException(Throwable cause)
	{
		super(cause);
	}
}