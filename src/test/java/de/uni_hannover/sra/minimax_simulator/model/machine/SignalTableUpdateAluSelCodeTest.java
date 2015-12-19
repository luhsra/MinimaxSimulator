package de.uni_hannover.sra.minimax_simulator.model.machine;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
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
 * Tests the implementation of updating ALU select codes for a {@link SignalTable}.
 *
 * @see MachineSignalTable
 *
 * @author Philipp Rohde
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignalTableUpdateAluSelCodeTest {

    private static SignalTable signalTable;
    private static MachineConfiguration machineConfig;
    private static final String SEL_A = "ALU_SELECT_A";
    private static final String SEL_B = "ALU_SELECT_B";

    @BeforeClass
    public static void initialize() {
        Project project = new NewProjectBuilder().buildProject();
        signalTable = project.getSignalTable();
        machineConfig = project.getMachineConfiguration();
    }

    /**
     * Tests updating of ALU selection codes after adding {@code MuxSource}s.
     */
    @Test
    public void test01() {
        machineConfig.addMuxSource(MuxType.A, new ConstantMuxInput(42));
        List<Boolean> containsA = new ArrayList<>(Arrays.asList(true, true, true, true, false, true, true, false));
        List<Boolean> containsB = new ArrayList<>(Arrays.asList(true, true, true, true, true, true, true, false));
        List<Integer> valueA = new ArrayList<>(Arrays.asList(1, 2, 2, 2, 0, 2, 1, 0));
        List<Integer> valueB = new ArrayList<>(Arrays.asList(3, 3, 3, 3, 1, 3, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.SelA signal", containsA.get(i), row.getSignalValues().containsKey(SEL_A));
            assertEquals("contains ALU.SelB signal", containsB.get(i), row.getSignalValues().containsKey(SEL_B));
            assertEquals("ALUSelect A", valueA.get(i).intValue(), row.getSignalValue(SEL_A));
            assertEquals("ALUSelect B", valueB.get(i).intValue(), row.getSignalValue(SEL_B));
        }

        machineConfig.addMuxSource(MuxType.B, new ConstantMuxInput(84));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.SelA signal", containsA.get(i), row.getSignalValues().containsKey(SEL_A));
            assertEquals("contains ALU.SelB signal", containsB.get(i), row.getSignalValues().containsKey(SEL_B));
            assertEquals("ALUSelect A", valueA.get(i).intValue(), row.getSignalValue(SEL_A));
            assertEquals("ALUSelect B", valueB.get(i).intValue(), row.getSignalValue(SEL_B));
        }
    }

    /**
     * Tests updating of ALU selection codes after moving {@code MuxSource}s.
     */
    @Test
    public void test02() {
        machineConfig.exchangeMuxSources(MuxType.A, 1, 2);
        List<Boolean> containsA = new ArrayList<>(Arrays.asList(true, true, true, true, false, true, true, false));
        List<Boolean> containsB = new ArrayList<>(Arrays.asList(true, true, true, true, true, true, true, false));
        List<Integer> valueA = new ArrayList<>(Arrays.asList(2, 1, 1, 1, 0, 1, 2, 0));
        List<Integer> valueB = new ArrayList<>(Arrays.asList(3, 3, 3, 3, 1, 3, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.SelA signal", containsA.get(i), row.getSignalValues().containsKey(SEL_A));
            assertEquals("contains ALU.SelB signal", containsB.get(i), row.getSignalValues().containsKey(SEL_B));
            assertEquals("ALUSelect A", valueA.get(i).intValue(), row.getSignalValue(SEL_A));
            assertEquals("ALUSelect B", valueB.get(i).intValue(), row.getSignalValue(SEL_B));
        }

        machineConfig.exchangeMuxSources(MuxType.A, 1, 2);
        containsA = new ArrayList<>(Arrays.asList(true, true, true, true, false, true, true, false));
        containsB = new ArrayList<>(Arrays.asList(true, true, true, true, true, true, true, false));
        valueA = new ArrayList<>(Arrays.asList(1, 2, 2, 2, 0, 2, 1, 0));
        valueB = new ArrayList<>(Arrays.asList(3, 3, 3, 3, 1, 3, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.SelA signal", containsA.get(i), row.getSignalValues().containsKey(SEL_A));
            assertEquals("contains ALU.SelB signal", containsB.get(i), row.getSignalValues().containsKey(SEL_B));
            assertEquals("ALUSelect A", valueA.get(i).intValue(), row.getSignalValue(SEL_A));
            assertEquals("ALUSelect B", valueB.get(i).intValue(), row.getSignalValue(SEL_B));
        }

        machineConfig.exchangeMuxSources(MuxType.B, 3, 4);
        containsA = new ArrayList<>(Arrays.asList(true, true, true, true, false, true, true, false));
        containsB = new ArrayList<>(Arrays.asList(true, true, true, true, true, true, true, false));
        valueA = new ArrayList<>(Arrays.asList(1, 2, 2, 2, 0, 2, 1, 0));
        valueB = new ArrayList<>(Arrays.asList(4, 4, 4, 4, 1, 4, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.SelA signal", containsA.get(i), row.getSignalValues().containsKey(SEL_A));
            assertEquals("contains ALU.SelB signal", containsB.get(i), row.getSignalValues().containsKey(SEL_B));
            assertEquals("ALUSelect A", valueA.get(i).intValue(), row.getSignalValue(SEL_A));
            assertEquals("ALUSelect B", valueB.get(i).intValue(), row.getSignalValue(SEL_B));
        }

        machineConfig.exchangeMuxSources(MuxType.B, 3, 4);
        containsA = new ArrayList<>(Arrays.asList(true, true, true, true, false, true, true, false));
        containsB = new ArrayList<>(Arrays.asList(true, true, true, true, true, true, true, false));
        valueA = new ArrayList<>(Arrays.asList(1, 2, 2, 2, 0, 2, 1, 0));
        valueB = new ArrayList<>(Arrays.asList(3, 3, 3, 3, 1, 3, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.SelA signal", containsA.get(i), row.getSignalValues().containsKey(SEL_A));
            assertEquals("contains ALU.SelB signal", containsB.get(i), row.getSignalValues().containsKey(SEL_B));
            assertEquals("ALUSelect A", valueA.get(i).intValue(), row.getSignalValue(SEL_A));
            assertEquals("ALUSelect B", valueB.get(i).intValue(), row.getSignalValue(SEL_B));
        }
    }

    /**
     * Tests updating of ALU selection codes after removing {@code MuxSource}s.
     */
    @Test
    public void test03() {
        machineConfig.removeMuxSource(MuxType.A, 3);
        machineConfig.removeMuxSource(MuxType.B, 4);
        List<Boolean> containsA = new ArrayList<>(Arrays.asList(true, true, true, true, false, true, true, false));
        List<Boolean> containsB = new ArrayList<>(Arrays.asList(true, true, true, true, true, true, true, false));
        List<Integer> valueA = new ArrayList<>(Arrays.asList(1, 2, 2, 2, 0, 2, 1, 0));
        List<Integer> valueB = new ArrayList<>(Arrays.asList(3, 3, 3, 3, 1, 3, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.SelA signal", containsA.get(i), row.getSignalValues().containsKey(SEL_A));
            assertEquals("contains ALU.SelB signal", containsB.get(i), row.getSignalValues().containsKey(SEL_B));
            assertEquals("ALUSelect A", valueA.get(i).intValue(), row.getSignalValue(SEL_A));
            assertEquals("ALUSelect B", valueB.get(i).intValue(), row.getSignalValue(SEL_B));
        }

        machineConfig.removeMuxSource(MuxType.B, 3);
        containsA = new ArrayList<>(Arrays.asList(true, true, true, true, false, true, true, false));
        containsB = new ArrayList<>(Arrays.asList(false, false, false, false, true, false, true, false));
        valueA = new ArrayList<>(Arrays.asList(1, 2, 2, 2, 0, 2, 1, 0));
        valueB = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 1, 0, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.SelA signal", containsA.get(i), row.getSignalValues().containsKey(SEL_A));
            assertEquals("contains ALU.SelB signal", containsB.get(i), row.getSignalValues().containsKey(SEL_B));
            assertEquals("ALUSelect A", valueA.get(i).intValue(), row.getSignalValue(SEL_A));
            assertEquals("ALUSelect B", valueB.get(i).intValue(), row.getSignalValue(SEL_B));
        }

        machineConfig.removeMuxSource(MuxType.A, 0);
        containsA = new ArrayList<>(Arrays.asList(true, true, true, true, false, true, true, false));
        containsB = new ArrayList<>(Arrays.asList(false, false, false, false, true, false, true, false));
        valueA = new ArrayList<>(Arrays.asList(0, 1, 1, 1, 0, 1, 0, 0));
        valueB = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 1, 0, 1, 0));
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            SignalRow row = signalTable.getRow(i);
            assertEquals("contains ALU.SelA signal", containsA.get(i), row.getSignalValues().containsKey(SEL_A));
            assertEquals("contains ALU.SelB signal", containsB.get(i), row.getSignalValues().containsKey(SEL_B));
            assertEquals("ALUSelect A", valueA.get(i).intValue(), row.getSignalValue(SEL_A));
            assertEquals("ALUSelect B", valueB.get(i).intValue(), row.getSignalValue(SEL_B));
        }
    }
}
