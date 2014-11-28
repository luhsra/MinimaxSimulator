package de.uni_hannover.sra.minimax_simulator.io;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;

/**
 * A checked exception indicating that importing a {@link Project} has failed.
 * 
 * @author Martin
 *
 */
public class ProjectImportException extends Exception
{
	public ProjectImportException()
	{
		super();
	}

	public ProjectImportException(String message)
	{
		super(message);
	}

	public ProjectImportException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ProjectImportException(Throwable cause)
	{
		super(cause);
	}
}