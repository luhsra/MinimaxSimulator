package de.uni_hannover.sra.minimax_simulator.io.exporter;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;

/**
 * A checked exception indicating that exporting a {@link Project} has failed.
 * 
 * @author Martin L&uuml;ck
 */
public class ProjectExportException extends Exception {

    /**
     * Constructs a new {@code ProjectExportException} with {@code null} as its detailed message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public ProjectExportException() {
        super();
    }

    /**
     * Constructs a new {@code ProjectExportException} with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message
     *          the detail message
     */
    public ProjectExportException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ProjectExportException} with the specified detail message and cause.
     *
     * @param message
     *          the detail message
     * @param cause
     *          the cause of the exception
     */
    public ProjectExportException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code ProjectExportException} with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which typically contains
     * the class and detail message of <tt>cause</tt>).
     *
     * @param cause
     *          the cause of the exception
     */
    public ProjectExportException(Throwable cause) {
        super(cause);
    }
}