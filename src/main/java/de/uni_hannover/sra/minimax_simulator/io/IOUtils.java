package de.uni_hannover.sra.minimax_simulator.io;

import static com.google.common.base.Preconditions.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.ZipFile;

/**
 * Some static utility methods for dealing with I/O classes, mostly similar to a part of apache-commons-io.
 * 
 * @author Martin
 *
 */
public final class IOUtils
{
	private IOUtils()
	{
		// Not instanciable
		throw new AssertionError();
	}

	/**
	 * Closes a {@link Closeable} instance (like a stream or socket)
	 * by calling its {@link Closeable#close()} method
	 * but ignores any occuring exceptions during the call of the method.
	 * <br>
	 * If the parameter is null, calling this method has no effect.
	 * 
	 * @param closeable the object to close
	 */
	public static void closeQuietly(Closeable closeable)
	{
		try
		{
			if (closeable != null)
				closeable.close();
		}
		catch (Exception e)
		{
			// ignore
		}
	}

	/**
	 * @see #closeQuietly(Closeable)
	 * 
	 * @param closeable
	 */
	// Sadly, ZipFile does not implement Closeable before Java 7.
	public static void closeQuietly(ZipFile closeable)
	{
		try
		{
			if (closeable != null)
				closeable.close();
		}
		catch (Exception e)
		{
			// ignore
		}
	}

	/**
	 * Returns the given {@link Reader} cast to a {@link BufferedReader} if possible,
	 * otherwise wraps it into a new {@link BufferedReader}.
	 * 
	 * @param reader a reader to convert to a buffered reader
	 * @return a buffered reader
	 * @throws NullPointerException if the argument is null
	 */
	public static BufferedReader toBufferedReader(Reader reader)
	{
		checkNotNull(reader, "reader must not be null");

		if (reader instanceof BufferedReader)
			return (BufferedReader) reader;

		return new BufferedReader(reader);
	}

	/**
	 * Returns the given {@link Writer} cast to a {@link BufferedWriter} if possible,
	 * otherwise wraps it into a new {@link BufferedWriter}.
	 * 
	 * @param writer a writer to convert to a buffered writer
	 * @return a buffered writer
	 * @throws NullPointerException if the argument is null
	 */
	public static BufferedWriter toBufferedWriter(Writer writer)
	{
		checkNotNull(writer, "writer must not be null");

		if (writer instanceof BufferedWriter)
			return (BufferedWriter) writer;

		return new BufferedWriter(writer);
	}

	/**
	 * Returns the given {@link InputStream} cast to a {@link BufferedInputStream} if possible,
	 * otherwise wraps it into a new {@link BufferedInputStream}.
	 * 
	 * @param is an input stream to convert to a buffered input stream
	 * @return a buffered input stream
	 * @throws NullPointerException if the argument is null
	 */
	public static BufferedInputStream toBufferedStream(InputStream is)
	{
		checkNotNull(is, "input stream must not be null");

		if (is instanceof BufferedInputStream)
			return (BufferedInputStream) is;

		return new BufferedInputStream(is);
	}

	/**
	 * Returns the given {@link OutputStream} cast to a {@link BufferedOutputStream} if possible,
	 * otherwise wraps it into a new {@link BufferedOutputStream}.
	 * 
	 * @param os an output stream to convert to a buffered output stream
	 * @return a buffered output stream
	 * @throws NullPointerException if the argument is null
	 */
	public static BufferedOutputStream toBufferedStream(OutputStream os)
	{
		checkNotNull(os, "output stream must not be null");

		if (os instanceof BufferedOutputStream)
			return (BufferedOutputStream) os;

		return new BufferedOutputStream(os);
	}
}