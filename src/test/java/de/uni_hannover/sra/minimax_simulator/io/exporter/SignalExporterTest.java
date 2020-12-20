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
import static org.junit.Assert.assertTrue;

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
        assertTrue("line separator", System.getProperty("line.separator").isEmpty());

        expected = expected.replace(lineSeparator, "\n");
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
        String expected = "<!doctype html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\" content=\"text/html\">\n" +
                "    <style>\n" +
                "      table { border-spacing: 0px 0; }\n" +
                "      th { background-color: #dadada; }\n" +
                "      tr:nth-child(even) { background-color: #f9f9f9; }\n" +
                "      th, td {\n" +
                "        text-align: center;\n" +
                "        padding-left: 10px;\n" +
                "        padding-right: 10px;\n" +
                "      }\n" +
                "      td.defaultJump { color: #777777; }\n" +
                "      td.description, td.label { text-align: left; }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <table>\n" +
                "      <tr>\n" +
                "        <th>#</th>\n" +
                "        <th>Label</th>\n" +
                "        <th>ALU_SELECT_A</th>\n" +
                "        <th>ALU_SELECT_B</th>\n" +
                "        <th>MDR_SEL</th>\n" +
                "        <th>MEM_CS</th>\n" +
                "        <th>MEM_RW</th>\n" +
                "        <th>ALU_CTRL</th>\n" +
                "        <th>PC.W</th>\n" +
                "        <th>IR.W</th>\n" +
                "        <th>MDR.W</th>\n" +
                "        <th>MAR.W</th>\n" +
                "        <th>ACCU.W</th>\n" +
                "        <th>Alu == 0?</th>\n" +
                "        <th>Jump</th>\n" +
                "        <th>Description</th>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>0</td>\n" +
                "        <td class=\"label\">start</td>\n" +
                "        <td>01</td>\n" +
                "        <td>11</td>\n" +
                "        <td>-</td>\n" +
                "        <td>0</td>\n" +
                "        <td>-</td>\n" +
                "        <td>00</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>1</td>\n" +
                "        <td></td>\n" +
                "        <td class=\"defaultJump\">1</td>\n" +
                "        <td class=\"description\">ACCU ← 1 + ACCU</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>1</td>\n" +
                "        <td></td>\n" +
                "        <td>10</td>\n" +
                "        <td>11</td>\n" +
                "        <td>-</td>\n" +
                "        <td>0</td>\n" +
                "        <td>-</td>\n" +
                "        <td>00</td>\n" +
                "        <td>1</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>1</td>\n" +
                "        <td></td>\n" +
                "        <td class=\"defaultJump\">2</td>\n" +
                "        <td class=\"description\">PC ← ACCU + ACCU<br>ACCU ← ACCU + ACCU</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>2</td>\n" +
                "        <td></td>\n" +
                "        <td>10</td>\n" +
                "        <td>11</td>\n" +
                "        <td>-</td>\n" +
                "        <td>0</td>\n" +
                "        <td>-</td>\n" +
                "        <td>00</td>\n" +
                "        <td>1</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>1</td>\n" +
                "        <td></td>\n" +
                "        <td class=\"defaultJump\">3</td>\n" +
                "        <td class=\"description\">PC ← ACCU + ACCU<br>ACCU ← ACCU + ACCU</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>3</td>\n" +
                "        <td></td>\n" +
                "        <td>10</td>\n" +
                "        <td>11</td>\n" +
                "        <td>-</td>\n" +
                "        <td>0</td>\n" +
                "        <td>-</td>\n" +
                "        <td>00</td>\n" +
                "        <td>1</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>1</td>\n" +
                "        <td></td>\n" +
                "        <td class=\"defaultJump\">4</td>\n" +
                "        <td class=\"description\">PC ← ACCU + ACCU<br>ACCU ← ACCU + ACCU</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>4</td>\n" +
                "        <td></td>\n" +
                "        <td>-</td>\n" +
                "        <td>01</td>\n" +
                "        <td>-</td>\n" +
                "        <td>0</td>\n" +
                "        <td>-</td>\n" +
                "        <td>11</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>1<br>0</td>\n" +
                "        <td>7<br>5</td>\n" +
                "        <td class=\"description\">PC == 0?</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>5</td>\n" +
                "        <td></td>\n" +
                "        <td>10</td>\n" +
                "        <td>11</td>\n" +
                "        <td>-</td>\n" +
                "        <td>0</td>\n" +
                "        <td>-</td>\n" +
                "        <td>00</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>1</td>\n" +
                "        <td>0</td>\n" +
                "        <td>1</td>\n" +
                "        <td></td>\n" +
                "        <td class=\"defaultJump\">6</td>\n" +
                "        <td class=\"description\">MDR ← ACCU + ACCU<br>ACCU ← ACCU + ACCU</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>6</td>\n" +
                "        <td></td>\n" +
                "        <td>01</td>\n" +
                "        <td>01</td>\n" +
                "        <td>-</td>\n" +
                "        <td>0</td>\n" +
                "        <td>-</td>\n" +
                "        <td>01</td>\n" +
                "        <td>1</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td></td>\n" +
                "        <td>4</td>\n" +
                "        <td class=\"description\">PC ← PC - 1</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>7</td>\n" +
                "        <td class=\"label\">store</td>\n" +
                "        <td>-</td>\n" +
                "        <td>-</td>\n" +
                "        <td>-</td>\n" +
                "        <td>1</td>\n" +
                "        <td>0</td>\n" +
                "        <td>-</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td>0</td>\n" +
                "        <td></td>\n" +
                "        <td class=\"defaultJump\">8</td>\n" +
                "        <td class=\"description\">M[MAR] ← MDR</td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>\n";

        File file = tmpDir.newFile("signal.html");
        SignalHtmlExporter htmlEx = new SignalHtmlExporter(file);
        htmlEx.exportSignalTable(table, config);

        String exported = IOUtils.readFile(file.getAbsolutePath());
        assertEquals("signal table html export", expected, exported);
    }
}
