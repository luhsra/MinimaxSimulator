package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.user.NewProjectBuilder;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link MinimaxSignalDescription}.
 *
 * @author Philipp Rohde
 */
public class MinimaxSignalDescriptionTest {

    private static Project project;
    private static MinimaxSignalDescription signalDescription;
    private static SignalTable signalTable;

    /**
     * Builds the default project and adds an empty {@code SignalRow} before any test is executed.
     */
    @Before
    public void initialize() {
        project = new NewProjectBuilder().buildProject();
        signalDescription = new MinimaxSignalDescription(project.getMachineConfiguration());
        signalTable = project.getSignalTable();
        signalTable.addSignalRow(new SignalRow());
    }

    /**
     * Checks if the description of {@code SignalRow} zero is like expected.
     */
    @Test
    public void testDescriptionRowZero() {
        String rowZero = signalDescription.createDescription(0, signalTable.getRow(0));
        String expected = "ACCU \u2190 1 + ACCU";
        assertEquals("description of row zero", expected, rowZero);
    }

    /**
     * Checks if the description of {@code SignalRow} three is like expected.
     */
    @Test
    public void testDescriptionRowThree() {
        String rowThree = signalDescription.createDescription(3, signalTable.getRow(3));
        String expected = "PC \u2190 ACCU + ACCU\nACCU \u2190 ACCU + ACCU";
        assertEquals("description of row three", expected, rowThree);
    }

    /**
     * Checks if the description of {@code SignalRow} four is like expected.
     */
    @Test
    public void testDescriptionRowFour() {
        String rowFour = signalDescription.createDescription(4, signalTable.getRow(4));
        String expected = "PC == 0?";
        assertEquals("description of row four", expected, rowFour);
    }

    /**
     * Checks if the description of {@code SignalRow} seven is like expected.
     */
    @Test
    public void testDescriptionRowSeven() {
        String rowSeven = signalDescription.createDescription(7, signalTable.getRow(7));
        String expected = "M[MAR] \u2190 MDR";
        assertEquals("description of row seven", expected, rowSeven);
    }

    /**
     * Checks if the description of {@code SignalRow} eight is like expected.
     */
    @Test
    public void testDescriptionRowEight() {
        String rowEight = signalDescription.createDescription(8, signalTable.getRow(8));
        String expected = "";
        assertEquals("description of row seven", expected, rowEight);
    }
}
