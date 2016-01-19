package de.uni_hannover.sra.minimax_simulator.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Some static utility methods for dealing with I/O classes, mostly similar to a part of apache-commons-io.
 * 
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public final class IOUtils {

    /**
     * Prevents creating instances of the utility class.
     */
    private IOUtils() {
        // not instantiable
        throw new AssertionError();
    }

    /**
     * Closes a {@link Closeable} instance (like a stream or socket) by calling
     * its {@link Closeable#close()} method but ignores any occurring exceptions
     * during the call of the method.<br>
     * If the parameter is null, calling this method has no effect.
     *
     * @param closeable
     *          the {@code Closeable} to close
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * Returns the given {@link Reader} casted to a {@link BufferedReader} if possible,
     * otherwise wraps it into a new {@code BufferedReader}.
     *
     * @param reader
     *          the {@code Reader} to convert to a {@code BufferedReader}
     * @return
     *          the {@code Reader} as {@code BufferedReader}
     * @throws NullPointerException
     *          thrown if the argument is null
     */
    public static BufferedReader toBufferedReader(Reader reader) {
        checkNotNull(reader, "reader must not be null");

        if (reader instanceof BufferedReader) {
            return (BufferedReader) reader;
        }
        else {
            return new BufferedReader(reader);
        }
    }

    /**
     * Returns the given {@link Writer} casted to a {@link BufferedWriter} if possible,
     * otherwise wraps it into a new {@code BufferedWriter}.
     *
     * @param writer
     *          the {@code Writer} to convert to a {@code BufferedWriter}
     * @return
     *          the {@code Writer} as {@code BufferedWriter}
     * @throws NullPointerException
     *          thrown if the argument is null
     */
    public static BufferedWriter toBufferedWriter(Writer writer) {
        checkNotNull(writer, "writer must not be null");

        if (writer instanceof BufferedWriter) {
            return (BufferedWriter) writer;
        }
        else {
            return new BufferedWriter(writer);
        }
    }

    /**
     * Returns the given {@link InputStream} casted to a {@link BufferedInputStream} if possible,
     * otherwise wraps it into a new {@code BufferedInputStream}.
     *
     * @param is
     *          the {@code InputStream} to convert to a {@code BufferedInputStream}
     * @return
     *          the {@code InputStream} as {@code BufferedInputStream}
     * @throws NullPointerException
     *          thrown if the argument is null
     */
    public static BufferedInputStream toBufferedStream(InputStream is) {
        checkNotNull(is, "input stream must not be null");

        if (is instanceof BufferedInputStream) {
            return (BufferedInputStream) is;
        }
        else {
            return new BufferedInputStream(is);
        }
    }

    /**
     * Returns the given {@link OutputStream} casted to a {@link BufferedOutputStream} if possible,
     * otherwise wraps it into a new {@code BufferedOutputStream}.
     *
     * @param os
     *          the {@code OutputStream} to convert to a {@code BufferedOutputStream}
     * @return
     *          the {@code OutputStream} as {@code BufferedOutputStream}
     * @throws NullPointerException
     *          thrown if the argument is null
     */
    public static BufferedOutputStream toBufferedStream(OutputStream os) {
        checkNotNull(os, "output stream must not be null");

        if (os instanceof BufferedOutputStream) {
            return (BufferedOutputStream) os;
        }
        else {
            return new BufferedOutputStream(os);
        }
    }

    /**
     * Converts a given {@link InputStream} to a {@code String}.
     *
     * @param is
     *          the {@code InputStream} to convert to a {@code String}
     * @return
     *          the {@code InputStream} as {@code String}
     */
    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * Reads the specified file to a String using the specified encoding.
     *
     * @param path
     *          the path of the file to read
     * @param encoding
     *          the file's encoding
     * @return
     *          the file's content as String
     * @throws IOException
     *          thrown if the file could not be read
     */
    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    /**
     * Reads the specified file to a String using UTF-8.
     *
     * @param path
     *          the path of the file to read
     * @return
     *          the file's content as String
     * @throws IOException
     *          thrown if the file could not be read
     */
    public static String readFile(String path) throws IOException {
        return readFile(path, StandardCharsets.UTF_8);
    }
}