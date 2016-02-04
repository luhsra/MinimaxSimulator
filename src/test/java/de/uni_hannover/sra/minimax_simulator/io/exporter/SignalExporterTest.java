package de.uni_hannover.sra.minimax_simulator.io.exporter;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.io.exporter.csv.SignalCsvExporter;
import de.uni_hannover.sra.minimax_simulator.io.exporter.csv.SignalHtmlExporter;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.user.NewProjectBuilder;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of the {@code SignalTable} exporter.
 *
 * @author Philipp Rohde
 */
public class SignalExporterTest {

    /** temporary folder */
    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    private static SignalTable table;
    private static SignalConfiguration config;

    /**
     * Initializes the test instance.
     */
    @BeforeClass
    public static void initialize() {
        Project project = new NewProjectBuilder().buildProject();
        table = project.getSignalTable();
        config = project.getSignalConfiguration();
    }

    /**
     * Tests the implementation of {@link SignalCsvExporter}.
     *
     * @throws IOException
     *          thrown if the file could not be created
     */
    @Test
    public void testCsvExporter() throws IOException {
        String lineSeparator = System.getProperty("line.separator");
        if (lineSeparator.isEmpty()) {
            lineSeparator = "\n";
        }

        String expected = "#,Label,ALU_SELECT_A,ALU_SELECT_B,MDR_SEL,MEM_CS,MEM_RW,ALU_CTRL,PC.W,IR.W,MDR.W,MAR.W,ACCU.W,Jump0,Jump1,Description" + lineSeparator +
                "0,start,01,11,-,0,-,00,0,0,0,0,1,1,1,ACCU ← 1 + ACCU" + lineSeparator +
                "1,,10,11,-,0,-,00,1,0,0,0,1,2,2,PC ← ACCU + ACCU : ACCU ← ACCU + ACCU" + lineSeparator +
                "2,,10,11,-,0,-,00,1,0,0,0,1,3,3,PC ← ACCU + ACCU : ACCU ← ACCU + ACCU" + lineSeparator +
                "3,,10,11,-,0,-,00,1,0,0,0,1,4,4,PC ← ACCU + ACCU : ACCU ← ACCU + ACCU" + lineSeparator +
                "4,,-,01,-,0,-,11,0,0,0,0,0,5,7,PC == 0?" + lineSeparator +
                "5,,10,11,-,0,-,00,0,0,1,0,1,6,6,MDR ← ACCU + ACCU : ACCU ← ACCU + ACCU" + lineSeparator +
                "6,,01,01,-,0,-,01,1,0,0,0,0,4,4,PC ← PC - 1" + lineSeparator +
                "7,store,-,-,-,1,0,-,0,0,0,0,0,8,8,M[MAR] ← MDR" + lineSeparator;

        File file = tmpDir.newFile("signal.csv");
        SignalCsvExporter csvEx = new SignalCsvExporter(file);
        csvEx.exportSignalTable(table, config);

        String exported = IOUtils.readFile(file.getAbsolutePath());
        assertEquals("signal table csv export", expected, exported);

        // test line separator property being null
        Properties p = new Properties(System.getProperties());
        p.setProperty("line.separator", "");
        System.setProperties(p);
        assertEquals("line separator", true, System.getProperty("line.separator").isEmpty());

        expected.replace(lineSeparator, "\n");
        csvEx.exportSignalTable(table, config);
        exported = IOUtils.readFile(file.getAbsolutePath());
        assertEquals("signal table csv export", expected, exported);
    }

    /**
     * Tests the implementation of {@link SignalHtmlExporter}.
     *
     * @throws IOException
     *          thrown if the file could not be created
     */
    @Test
    public void testHtmlExporter() throws IOException {
        String expected = "<!doctype html><html><head><meta charset=\"utf-8\" content=\"text/html\">" +
                "<style>td.defaultJump { color:#777777; }</style></head><body><table><tr><th>#</th><th>Label</th>" +
                "<th>ALU_SELECT_A</th><th>ALU_SELECT_B</th><th>MDR_SEL</th><th>MEM_CS</th><th>MEM_RW</th>" +
                "<th>ALU_CTRL</th><th>PC.W</th><th>IR.W</th><th>MDR.W</th><th>MAR.W</th><th>ACCU.W</th>" +
                "<th>Alu == 0?</th><th>Jump</th><th>Description</th></tr><tr><td>0</td><td>start</td><td>01</td>" +
                "<td>11</td><td>-</td><td>0</td><td>-</td><td>00</td><td>0</td><td>0</td><td>0</td><td>0</td><td>1</td>" +
                "<td></td><td class=\"defaultJump\">1</td><td>ACCU ← 1 + ACCU</td></tr><tr><td>1</td><td></td>" +
                "<td>10</td><td>11</td><td>-</td><td>0</td><td>-</td><td>00</td><td>1</td><td>0</td><td>0</td>" +
                "<td>0</td><td>1</td><td></td><td class=\"defaultJump\">2</td>" +
                "<td>PC ← ACCU + ACCU<br>ACCU ← ACCU + ACCU</td></tr><tr><td>2</td><td></td><td>10</td><td>11</td>" +
                "<td>-</td><td>0</td><td>-</td><td>00</td><td>1</td><td>0</td><td>0</td><td>0</td><td>1</td><td></td>" +
                "<td class=\"defaultJump\">3</td><td>PC ← ACCU + ACCU<br>ACCU ← ACCU + ACCU</td></tr><tr><td>3</td>" +
                "<td></td><td>10</td><td>11</td><td>-</td><td>0</td><td>-</td><td>00</td><td>1</td><td>0</td><td>0</td>" +
                "<td>0</td><td>1</td><td></td><td class=\"defaultJump\">4</td>" +
                "<td>PC ← ACCU + ACCU<br>ACCU ← ACCU + ACCU</td></tr><tr><td>4</td><td></td><td>-</td><td>01</td>" +
                "<td>-</td><td>0</td><td>-</td><td>11</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td>" +
                "<td>1<br>0</td><td>7<br>5</td><td>PC == 0?</td></tr><tr><td>5</td><td></td><td>10</td><td>11</td>" +
                "<td>-</td><td>0</td><td>-</td><td>00</td><td>0</td><td>0</td><td>1</td><td>0</td><td>1</td><td>" +
                "</td><td class=\"defaultJump\">6</td><td>MDR ← ACCU + ACCU<br>ACCU ← ACCU + ACCU</td></tr>" +
                "<tr><td>6</td><td></td><td>01</td><td>01</td><td>-</td><td>0</td><td>-</td><td>01</td><td>1</td>" +
                "<td>0</td><td>0</td><td>0</td><td>0</td><td></td><td>4</td><td>PC ← PC - 1</td></tr><tr><td>7</td>" +
                "<td>store</td><td>-</td><td>-</td><td>-</td><td>1</td><td>0</td><td>-</td><td>0</td><td>0</td>" +
                "<td>0</td><td>0</td><td>0</td><td></td><td class=\"defaultJump\">8</td><td>M[MAR] ← MDR</td></tr>" +
                "</table></body></html></body></html>";

        File file = tmpDir.newFile("signal.html");
        SignalHtmlExporter htmlEx = new SignalHtmlExporter(file);
        htmlEx.exportSignalTable(table, config);

        String exported = IOUtils.readFile(file.getAbsolutePath());
        assertEquals("signal table html export", expected, exported);
    }
}
