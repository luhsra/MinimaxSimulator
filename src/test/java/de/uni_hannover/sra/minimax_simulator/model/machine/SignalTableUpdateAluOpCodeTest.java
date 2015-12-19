package de.uni_hannover.sra.minimax_simulator.model.machine;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.user.NewProjectBuilder;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of updating ALU operation codes for a {@link SignalTable}.
 *
 * @see MachineSignalTable
 * @see AluOperation
 *
 * @author Philipp Rohde
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignalTableUpdateAluOpCodeTest {

    private static SignalTable signalTable;
    private static MachineConfiguration machineConfig;
    private static final String OP = "ALU_CTRL";

    @BeforeClass
    public static void initialize() {
        Project project = new NewProjectBuilder().buildProject();
        signalTable = project.getSignalTable();
        machineConfig = project.getMachineConfiguration();
    }

    /**
     * Tests updating of ALU OP codes after moving {@code AluOperation}s.
     */
    @Test
    public void test01() {
        machineConfig.exchangeAluOperations(0, 1);
        List<Boolean> contains = new ArrayList<>(Arrays.asList(true, true, true, true, true, true, true, false));
        List<Integer> value = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 3, 1, 0, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.Ctrl signal", contains.get(i), row.getSignalValues().containsKey(OP));
            assertEquals("ALU OP code", value.get(i).intValue(), row.getSignalValue(OP));
        }

        machineConfig.exchangeAluOperations(0, 1);
        value = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 3, 0, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.Ctrl signal", contains.get(i), row.getSignalValues().containsKey(OP));
            assertEquals("ALU OP code", value.get(i).intValue(), row.getSignalValue(OP));
        }

        machineConfig.exchangeAluOperations(2, 3);
        value = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 2, 0, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.Ctrl signal", contains.get(i), row.getSignalValues().containsKey(OP));
            assertEquals("ALU OP code", value.get(i).intValue(), row.getSignalValue(OP));
        }

        machineConfig.exchangeAluOperations(2, 3);
        value = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 3, 0, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.Ctrl signal", contains.get(i), row.getSignalValues().containsKey(OP));
            assertEquals("ALU OP code", value.get(i).intValue(), row.getSignalValue(OP));
        }
    }

    /**
     * Tests updating of ALU OP codes after adding {@code AluOperation}s.
     */
    @Test
    public void test02() {
        machineConfig.addAluOperation(AluOperation.A_INC);
        List<Boolean> contains = new ArrayList<>(Arrays.asList(true, true, true, true, true, true, true, false));
        List<Integer> value = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 3, 0, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.Ctrl signal", contains.get(i), row.getSignalValues().containsKey(OP));
            assertEquals("ALU OP code", value.get(i).intValue(), row.getSignalValue(OP));
        }

        machineConfig.addAluOperation(AluOperation.B_INC);
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.Ctrl signal", contains.get(i), row.getSignalValues().containsKey(OP));
            assertEquals("ALU OP code", value.get(i).intValue(), row.getSignalValue(OP));
        }
    }

    /**
     * Tests updating of ALU OP codes after removing {@code AluOperation}s.
     */
    @Test
    public void test03() {
        machineConfig.removeAluOperation(AluOperation.A_INC);
        List<Boolean> contains = new ArrayList<>(Arrays.asList(true, true, true, true, true, true, true, false));
        List<Integer> value = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 3, 0, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.Ctrl signal", contains.get(i), row.getSignalValues().containsKey(OP));
            assertEquals("ALU OP code", value.get(i).intValue(), row.getSignalValue(OP));
        }

        machineConfig.removeAluOperation(AluOperation.B_INC);
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.Ctrl signal", contains.get(i), row.getSignalValues().containsKey(OP));
            assertEquals("ALU OP code", value.get(i).intValue(), row.getSignalValue(OP));
        }

        machineConfig.removeAluOperation(AluOperation.TRANS_A);
        value = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 2, 0, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.Ctrl signal", contains.get(i), row.getSignalValues().containsKey(OP));
            assertEquals("ALU OP code", value.get(i).intValue(), row.getSignalValue(OP));
        }

        machineConfig.removeAluOperation(AluOperation.A_ADD_B);
        contains = new ArrayList<>(Arrays.asList(false, false, false, false, true, false, true, false));
        value = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 1, 0, 0, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.Ctrl signal", contains.get(i), row.getSignalValues().containsKey(OP));
            assertEquals("ALU OP code", value.get(i).intValue(), row.getSignalValue(OP));
        }
    }
}
