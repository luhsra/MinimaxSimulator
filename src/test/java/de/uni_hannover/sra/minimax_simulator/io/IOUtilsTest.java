package de.uni_hannover.sra.minimax_simulator.io;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of {@link IOUtils} which is not covered by other tests.
 *
 * @author Philipp Rohde
 */
public class IOUtilsTest {

    /** temporary folder */
    @ClassRule
    public static final TemporaryFolder TMP_DIR = new TemporaryFolder();

    private static File zip;

    /**
     * Sets up the test instance.
     *
     * @throws IOException
     *          thrown if the test file could not be created
     */
    @BeforeClass
    public static void initialize() throws IOException {
        zip = TMP_DIR.newFile("test.zip");
    }

    /**
     * Tests the uncovered implementation of {@link IOUtils#toBufferedReader(Reader)}.
     *
     * @throws FileNotFoundException
     *          thrown if the test file was not found
     */
    @Test
    public void testToBufferedReader() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(zip));
        assertEquals(br, IOUtils.toBufferedReader(br));
    }

    /**
     * Tests the uncovered implementation of {@link IOUtils#toBufferedWriter(Writer)}.
     *
     * @throws IOException
     *          thrown if an I/O error occurred
     */
    @Test
    public void testToBufferedWriter() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(zip));
        assertEquals(bw, IOUtils.toBufferedWriter(bw));
    }

    /**
     * Tests the implementation of {@link IOUtils#toBufferedStream(InputStream)}.
     *
     * @throws FileNotFoundException
     *          thrown if the test file was not found
     */
    @Test
    public void testToBufferedInputStream() throws FileNotFoundException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zip));
        assertEquals(bis, IOUtils.toBufferedStream(bis));
    }

    /**
     * Tests the implementation of {@link IOUtils#toBufferedStream(OutputStream)}.
     *
     * @throws FileNotFoundException
     *          thrown if the test file was not found
     */
    @Test
    public void testToBufferedOutputStream() throws FileNotFoundException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zip));
        assertEquals(bos, IOUtils.toBufferedStream(bos));
    }

    /**
     * Tests the implementation of {@link IOUtils#readFile(String)} and {@link IOUtils#readFile(String, Charset)}.
     *
     * @throws IOException
     *          thrown if an I/O error occurred
     */
    @Test
    public void testReadFile() throws IOException {
        File file = TMP_DIR.newFile("test.txt");
        String expected = "Lorem ipsum dolor sit amet";

        FileWriter fw = new FileWriter(file);
        fw.write(expected);
        fw.flush();
        fw.close();

        assertEquals(expected, IOUtils.readFile(file.getAbsolutePath()));
        assertEquals(expected, IOUtils.readFile(file.getAbsolutePath(), StandardCharsets.UTF_8));
    }
}
