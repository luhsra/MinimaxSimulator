package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.user.NewProjectBuilder;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link MinimaxSignalDescription}.
 *
 * @author Philipp Rohde
 */
@RunWith(Parameterized.class)
public class MinimaxSignalDescriptionTest {

    private static MinimaxSignalDescription signalDescription;
    private static SignalTable signalTable;
    private final int row;
    private final String expected;

    /**
     * Initializes the test instance.
     *
     * @param row
     *          the index of the {@code SignalRow} to get the description for
     * @param expected
     *          the expected description
     */
    public MinimaxSignalDescriptionTest(int row, String expected) {
        this.row = row;
        this.expected = expected;
    }

    /**
     * Builds the default project and adds an empty {@code SignalRow} before any test is executed.
     */
    @BeforeClass
    public static void initialize() {
        Project project = new NewProjectBuilder().buildProject();
        signalDescription = new MinimaxSignalDescription(project.getMachineConfiguration());
        signalTable = project.getSignalTable();
        signalTable.addSignalRow(new SignalRow());
    }

    /**
     * Creates the parameters for the test.
     *
     * @return
     *          the parameters for the test
     */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0, "ACCU \u2190 1 + ACCU"},
                {3, "PC \u2190 ACCU + ACCU\nACCU \u2190 ACCU + ACCU"},
                {4, "PC == 0?"},
                {7, "M[MAR] \u2190 MDR"},
                {8, ""}
        });
    }

    /**
     * Tests the description creation.
     */
    @Test
    public void test() {
        assertEquals("description of row " + row, expected, signalDescription.createDescription(row, signalTable.getRow(row)));
    }
}
