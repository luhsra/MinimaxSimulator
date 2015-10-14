package de.uni_hannover.sra.minimax_simulator.io.importer;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;

/**
 * A checked exception indicating that importing a {@link Project} has failed.
 * 
 * @author Martin L&uuml;ck
 */
public class ProjectImportException extends Exception {

	/**
	 * Constructs a new {@code ProjectImportException} with {@code null} as its detailed message.
	 * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
	 */
	public ProjectImportException() {
		super();
	}

	/**
	 * Constructs a new {@code ProjectImportException} with the specified detail message.
	 * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
	 *
	 * @param message
	 *          the detail message
	 */
	public ProjectImportException(String message) {
		super(message);
	}

	/**
	 * Constructs a new {@code ProjectImportException} with the specified detail message and cause.
	 *
	 * @param message
	 *          the detail message
	 * @param cause
	 *          the cause of the exception
	 */
	public ProjectImportException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new {@code ProjectImportException} with the specified cause and a detail
	 * message of <tt>(cause==null ? null : cause.toString())</tt> (which typically contains
	 * the class and detail message of <tt>cause</tt>).
	 *
	 * @param cause
	 *          the cause of the exception
	 */
	public ProjectImportException(Throwable cause) {
		super(cause);
	}
}