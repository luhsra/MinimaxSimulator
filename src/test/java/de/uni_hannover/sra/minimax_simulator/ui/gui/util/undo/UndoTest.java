package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.*;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests the complete undo/redo system of the application.
 *
 * @author Philipp Rohde
 */
public class UndoTest {

    private static Workspace workspace;
    private static Project project;
    private static MachineConfiguration mConfig;
    private static SignalTable signalTable;
    private static UndoManager undoManager;

    private static final String ALU_CTRL = "ALU_CTRL";
    private static final String ALU_SEL_A = "ALU_SELECT_A";

    /**
     * Initializes the test setup.
     */
    @BeforeClass
    public static void initialize() {
        workspace = Main.getWorkspace();
        undoManager = UndoManager.INSTANCE;
    }

    /**
     * Resets the {@link Project} and {@link UndoManager} for each test.
     */
    @Before
    public void resetProject() {
        workspace.newProject();
        project = workspace.getProject();
        mConfig = project.getMachineConfiguration();
        signalTable = project.getSignalTable();
        undoManager.reset();
    }

    /**
     * Tests if the values of {@link UndoManager#isUndoAvailableProperty()} and
     * {@link UndoManager#isRedoAvailableProperty()} are set correct.
     */
    @Test
    public void testUndoRedoAvailable() {
        // TODO
    }

    /**
     * Tests the implementation of {@link AluOpAddedCommand}.
     */
    @Test
    public void testAluOpAdded() {
        List<AluOperation> precondition = new ArrayList<>(Arrays.asList(AluOperation.A_ADD_B,
                AluOperation.B_SUB_A,
                AluOperation.TRANS_A,
                AluOperation.TRANS_B));

        // execute command
        AluOperation op = AluOperation.A_AND_B;
        undoManager.addCommand(new AluOpAddedCommand(op, mConfig));
        assertEquals("[AluOpAdded] five OPs", 5, mConfig.getAluOperations().size());
        for (int i = 0; i < precondition.size(); i++) {
            assertEquals("[AluOpAdded] OP"+i, precondition.get(i), mConfig.getAluOperation(i));
        }
        assertEquals("[AluOpAdded] OP4", op, mConfig.getAluOperation(precondition.size()));

        // undo command
        undoManager.undo();
        assertEquals("[AluOpAdded] four OPs", 4, mConfig.getAluOperations().size());
        for (int i = 0; i < precondition.size(); i++) {
            assertEquals("[AluOpAdded] OP"+i, precondition.get(i), mConfig.getAluOperation(i));
        }

        // redo command
        undoManager.redo();
        assertEquals("[AluOpAdded] five OPs", 5, mConfig.getAluOperations().size());
        for (int i = 0; i < precondition.size(); i++) {
            assertEquals("[AluOpAdded] OP"+i, precondition.get(i), mConfig.getAluOperation(i));
        }
        assertEquals("[AluOpAdded] OP4", op, mConfig.getAluOperation(precondition.size()));
    }

    /**
     * Tests the implementation of {@link AluOpMovedCommand}.
     */
    @Test
    public void testAluOpMoved() {
        List<AluOperation> preconditionOps = new ArrayList<>(Arrays.asList(AluOperation.A_ADD_B,
                AluOperation.B_SUB_A,
                AluOperation.TRANS_A,
                AluOperation.TRANS_B));

        List<Integer> preconditionAluCtrl = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 3, 0, 1, 0));

        List<AluOperation> postconditionOps = new ArrayList<>(Arrays.asList(AluOperation.B_SUB_A,
                AluOperation.A_ADD_B,
                AluOperation.TRANS_A,
                AluOperation.TRANS_B));

        List<Integer> postconditionAluCtrl = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 3, 1, 0, 0));

        // execute command
        undoManager.addCommand(new AluOpMovedCommand(0, 1, mConfig));
        for (int i = 0; i < postconditionOps.size(); i++) {
            assertEquals("[AluOpMoved] order after execution; index: " + i, postconditionOps.get(i), mConfig.getAluOperation(i));
        }
        checkSignalValues("[AluOpMoved] ALU OpCodes after execution", postconditionAluCtrl, ALU_CTRL);

        // undo command
        undoManager.undo();
        for (int i = 0; i < preconditionOps.size(); i++) {
            assertEquals("[AluOpMoved] order after undo; index: " + i, preconditionOps.get(i), mConfig.getAluOperation(i));
        }
        checkSignalValues("[AluOpMoved] ALU OpCodes after undo", preconditionAluCtrl, ALU_CTRL);

        // redo command
        undoManager.redo();
        for (int i = 0; i < postconditionOps.size(); i++) {
            assertEquals("[AluOpMoved] order after redo; index: " + i, postconditionOps.get(i), mConfig.getAluOperation(i));
        }
        checkSignalValues("[AluOpMoved] ALU OpCodes after redo", postconditionAluCtrl, ALU_CTRL);
    }

    /**
     * Tests the implementation of {@link AluOpRemovedCommand}.
     */
    @Test
    public void testAluOpRemoved() {
        List<AluOperation> preconditionOps = new ArrayList<>(Arrays.asList(AluOperation.A_ADD_B,
                AluOperation.B_SUB_A,
                AluOperation.TRANS_A,
                AluOperation.TRANS_B));

        List<Integer> preconditionAluCtrl = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 3, 0, 1, 0));

        List<AluOperation> postconditionOps = new ArrayList<>(Arrays.asList(AluOperation.A_ADD_B,
                AluOperation.TRANS_A,
                AluOperation.TRANS_B));

        List<Integer> postconditionAluCtrl = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 2, 0, 0, 0));

        // execute command
        undoManager.addCommand(new AluOpRemovedCommand(AluOperation.B_SUB_A, mConfig));
        for (int i = 0; i < postconditionOps.size(); i++) {
            assertEquals("[AluOpRemoved] order after execution; index: " + i, postconditionOps.get(i), mConfig.getAluOperation(i));
        }
        checkSignalValues("[AluOpRemoved] ALU OpCodes after execution", postconditionAluCtrl, ALU_CTRL);

        // undo command
        undoManager.undo();
        for (int i = 0; i < preconditionOps.size(); i++) {
            assertEquals("[AluOpRemoved] order after undo; index: " + i, preconditionOps.get(i), mConfig.getAluOperation(i));
        }
        checkSignalValues("[AluOpRemoved] ALU OpCodes after undo", preconditionAluCtrl, ALU_CTRL);

        // redo command
        undoManager.redo();
        for (int i = 0; i < postconditionOps.size(); i++) {
            assertEquals("[AluOpRemoved] order after redo; index: " + i, postconditionOps.get(i), mConfig.getAluOperation(i));
        }
        checkSignalValues("[AluOpRemoved] ALU OpCodes after redo", postconditionAluCtrl, ALU_CTRL);
    }

    /**
     * Checks whether the specified values are the current state of the values according to the specified signal name.
     *
     * @param assertText
     *         the text for the {@code assertEquals} call
     * @param expectedValues
     *         the list of expected signal values
     * @param signalName
     *         the name of the signal to check
     */
    private void checkSignalValues(String assertText, List<Integer> expectedValues, String signalName) {
        assertText += "; row index: ";
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(assertText + i, (int) expectedValues.get(i), signalTable.getRow(i).getSignalValue(signalName));
        }
    }

    /**
     * Tests the implementation of {@link MuxInputAddedCommand}.
     */
    @Test
    public void testMuxInputAdded() {
        MuxType mux = MuxType.A;
        MuxInput in1 = new ConstantMuxInput(0);
        MuxInput in2 = new ConstantMuxInput(1);
        MuxInput in3 = new RegisterMuxInput("ACCU");
        MuxInput in4 = new ConstantMuxInput(0);

        List<MuxInput> precondition = new ArrayList<>(Arrays.asList(in1, in2, in3));
        List<MuxInput> postcondition = new ArrayList<>(Arrays.asList(in1, in2, in3, in4));

        // execute command
        undoManager.addCommand(new MuxInputAddedCommand(mux, mConfig));
        checkMuxInputs("[MuxInputAdded] order after execution", postcondition, mux);

        // undo command
        undoManager.undo();
        checkMuxInputs("[MuxInputAdded] order after undo", precondition, mux);

        // redo command
        undoManager.redo();
        checkMuxInputs("[MuxInputAdded] order after redo", postcondition, mux);
    }

    /**
     * Tests the implementation of {@link MuxInputModifiedCommand}.
     */
    @Test
    public void testMuxInputModified() {
        // execute command
        undoManager.addCommand(new MuxInputModifiedCommand(MuxType.A, 1, new RegisterMuxInput("MDR"), mConfig));
        assertEquals("[MuxInputModified] type after execution", true, mConfig.getMuxSources(MuxType.A).get(1) instanceof RegisterMuxInput);
        assertEquals("[MuxInputModified] value after execution", true, "MDR".equals(((RegisterMuxInput) mConfig.getMuxSources(MuxType.A).get(1)).getRegisterName()));

        // undo command
        undoManager.undo();
        assertEquals("[MuxInputModified] type after undo", true, mConfig.getMuxSources(MuxType.A).get(1) instanceof ConstantMuxInput);
        assertEquals("[MuxInputModified] value after undo", 1, ((ConstantMuxInput) mConfig.getMuxSources(MuxType.A).get(1)).getConstant());

        // redo command
        undoManager.redo();
        assertEquals("[MuxInputModified] type after redo", true, mConfig.getMuxSources(MuxType.A).get(1) instanceof RegisterMuxInput);
        assertEquals("[MuxInputModified] value after redo", true, "MDR".equals(((RegisterMuxInput) mConfig.getMuxSources(MuxType.A).get(1)).getRegisterName()));
    }

    /**
     * Tests the implementation of {@link MuxInputMovedCommand}.
     */
    @Test
    public void testMuxInputMoved() {
        MuxType mux = MuxType.A;
        ConstantMuxInput in1 = new ConstantMuxInput(0);
        ConstantMuxInput in2 = new ConstantMuxInput(1);
        RegisterMuxInput in3 = new RegisterMuxInput("ACCU");

        List<MuxInput> preconditionInput = new ArrayList<>(Arrays.asList(in1, in2, in3));
        List<MuxInput> postconditionInput = new ArrayList<>(Arrays.asList(in1, in3, in2));

        List<Integer> preconditionSelect = new ArrayList<>(Arrays.asList(1, 2, 2, 2, 0, 2, 1, 0));
        List<Integer> postconditionSelect = new ArrayList<>(Arrays.asList(2, 1, 1, 1, 0, 1, 2, 0));

        // execute command
        undoManager.addCommand(new MuxInputMovedCommand(mux, 1, 2, mConfig));
        checkMuxInputs("[MuxInputMoved] order after execution", postconditionInput, mux);
        checkSignalValues("[MuxInputMoved] signal values after execution", postconditionSelect, ALU_SEL_A);

        // undo command
        undoManager.undo();
        checkMuxInputs("[MuxInputMoved] order after undo", preconditionInput, mux);
        checkSignalValues("[MuxInputMoved] signal values after undo", preconditionSelect, ALU_SEL_A);

        // redo command
        undoManager.redo();
        checkMuxInputs("[MuxInputMoved] order after redo", postconditionInput, mux);
        checkSignalValues("[MuxInputMoved] signal values after redo", postconditionSelect, ALU_SEL_A);
    }

    /**
     * Tests the implementation of {@link MuxInputRemovedCommand}.
     */
    @Test
    public void testMuxInputRemoved() {
        MuxType mux = MuxType.A;
        ConstantMuxInput in1 = new ConstantMuxInput(0);
        ConstantMuxInput in2 = new ConstantMuxInput(1);
        RegisterMuxInput in3 = new RegisterMuxInput("ACCU");

        List<MuxInput> preconditionInput = new ArrayList<>(Arrays.asList(in1, in2, in3));
        List<MuxInput> postconditionInput = new ArrayList<>(Arrays.asList(in1, in3));

        List<Integer> preconditionSelect = new ArrayList<>(Arrays.asList(1, 2, 2, 2, 0, 2, 1, 0));
        List<Integer> postconditionSelect = new ArrayList<>(Arrays.asList(0, 1, 1, 1, 0, 1, 0, 0));

        // execute command
        undoManager.addCommand(new MuxInputRemovedCommand(mux, 1, mConfig));
        checkMuxInputs("[MuxInputRemoved] order after execution", postconditionInput, mux);
        checkSignalValues("[MuxInputRemoved] signal values after execution", postconditionSelect, ALU_SEL_A);

        // undo command
        undoManager.undo();
        checkMuxInputs("[MuxInputRemoved] order after undo", preconditionInput, mux);
        checkSignalValues("[MuxInputRemoved] signal values after undo", preconditionSelect, ALU_SEL_A);

        // redo command
        undoManager.redo();
        checkMuxInputs("[MuxInputRemoved] order after redo", postconditionInput, mux);
        checkSignalValues("[MuxInputRemoved] signal values after redo", postconditionSelect, ALU_SEL_A);
    }

    /**
     * Checks whether the specified list of {@code MuxInput}s is the current representation of the {@code MuxInput}s
     * of the specified multiplexer.
     *
     * @param assertText
     *         the text for the {@code assertEquals} call
     * @param muxInputs
     *         the list of the expected {@code MuxInput}s
     * @param mux
     *         the {@code MuxType} of the multiplexer
     */
    private void checkMuxInputs(String assertText, List<MuxInput> muxInputs, MuxType mux) {
        assertText += "; index: ";
        for (int i = 0; i < muxInputs.size(); i++) {
            MuxInput expected = muxInputs.get(i);
            MuxInput actual = mConfig.getMuxSources(mux).get(i);

            if (expected instanceof ConstantMuxInput) {
                assertEquals(assertText + i, true, (actual instanceof  ConstantMuxInput));
                ConstantMuxInput expCMI = (ConstantMuxInput) expected;
                ConstantMuxInput actCMI = (ConstantMuxInput) actual;
                assertEquals(assertText + i, expCMI.getConstant(), actCMI.getConstant());
            }
            else if (expected instanceof RegisterMuxInput) {
                assertEquals(assertText + i, true, (actual instanceof RegisterMuxInput));
                RegisterMuxInput expRMI = (RegisterMuxInput) expected;
                RegisterMuxInput actRMI = (RegisterMuxInput) actual;
                assertEquals(assertText + i, true, expRMI.getRegisterName().equals(actRMI.getRegisterName()));
            }
        }
    }

    /**
     * Tests the implementation of {@link RegisterAddedCommand}.
     */
    @Test
    public void testRegisterAdded() {
        RegisterExtension reg1 = new RegisterExtension("REG1", RegisterSize.BITS_32, "desc reg1", true);
        RegisterExtension reg2 = new RegisterExtension("REG2", RegisterSize.BITS_32, "desc reg2", true);
        RegisterExtension reg3 = new RegisterExtension("REG3", RegisterSize.BITS_32, "desc reg3", true);

        List<RegisterExtension> precondition = new ArrayList<>(Arrays.asList(reg1, reg2));
        List<RegisterExtension> postcondition = new ArrayList<>(Arrays.asList(reg1, reg2, reg3));

        // test case setup
        mConfig.addRegisterExtension(reg1);
        mConfig.addRegisterExtension(reg2);

        // execute command
        undoManager.addCommand(new RegisterAddedCommand(reg3, mConfig));
        checkRegisters("[RegisterAdded] order after execution", postcondition);

        // undo command
        undoManager.undo();
        checkRegisters("[RegisterAdded] order after undo", precondition);

        // redo command
        undoManager.redo();
        checkRegisters("[RegisterAdded] order after redo", postcondition);
    }

    /**
     * Tests the implementation of {@link RegisterModifiedCommand}.
     */
    @Test
    public void testRegisterModified() {
        RegisterExtension oldOne = new RegisterExtension("TMP", RegisterSize.BITS_32, "desc", true);
        RegisterExtension newOne = new RegisterExtension("TMP", RegisterSize.BITS_32, "register for temporary values", true);

        List<RegisterExtension> precondition = Collections.singletonList(oldOne);
        List<RegisterExtension> postcondition = Collections.singletonList(newOne);

        // test case setup
        mConfig.addRegisterExtension(oldOne);

        // execute command
        undoManager.addCommand(new RegisterModifiedCommand(0, oldOne, newOne, mConfig));
        checkRegisters("[RegisterModified] values after execution", postcondition);

        // undo command
        undoManager.undo();
        checkRegisters("[RegisterModified] values after undo", precondition);

        // redo command
        undoManager.redo();
        checkRegisters("[RegisterModified] values after redo", postcondition);
    }

    /**
     * Tests the implementation of {@link RegisterMovedCommand}.
     */
    @Test
    public void testRegisterMoved() {
        RegisterExtension reg1 = new RegisterExtension("REG1", RegisterSize.BITS_32, "desc reg1", true);
        RegisterExtension reg2 = new RegisterExtension("REG2", RegisterSize.BITS_32, "desc reg2", true);
        RegisterExtension reg3 = new RegisterExtension("REG3", RegisterSize.BITS_32, "desc reg3", true);
        RegisterExtension reg4 = new RegisterExtension("REG4", RegisterSize.BITS_32, "desc reg4", true);
        RegisterExtension reg5 = new RegisterExtension("REG5", RegisterSize.BITS_32, "desc reg5", true);

        List<RegisterExtension> precondition = new ArrayList<>(Arrays.asList(reg1, reg2, reg3, reg4, reg5));
        List<RegisterExtension> postcondition = new ArrayList<>(Arrays.asList(reg1, reg2, reg4, reg3, reg5));

        // test case setup
        mConfig.addRegisterExtension(reg1);
        mConfig.addRegisterExtension(reg2);
        mConfig.addRegisterExtension(reg3);
        mConfig.addRegisterExtension(reg4);
        mConfig.addRegisterExtension(reg5);

        // execute command
        undoManager.addCommand(new RegisterMovedCommand(2, 3, mConfig));
        checkRegisters("[RegisterMoved] order after execution", postcondition);

        // undo command
        undoManager.undo();
        checkRegisters("[RegisterMoved] order after undo", precondition);

        // redo command
        undoManager.redo();
        checkRegisters("[RegisterMoved] order after redo", postcondition);
    }

    /**
     * Tests the implementation of {@link RegisterRemovedCommand}.
     */
    @Test
    public void testRegisterRemoved() {
        RegisterExtension reg1 = new RegisterExtension("REG1", RegisterSize.BITS_32, "desc reg1", true);
        RegisterExtension reg2 = new RegisterExtension("REG2", RegisterSize.BITS_32, "desc reg2", true);
        List<RegisterExtension> preconditionRegister = new ArrayList<>(Arrays.asList(reg1, reg2));
        List<RegisterExtension> postconditionRegister = Collections.singletonList(reg2);

        MuxType mux = MuxType.A;
        ConstantMuxInput in1 = new ConstantMuxInput(0);
        ConstantMuxInput in2 = new ConstantMuxInput(1);
        RegisterMuxInput in3 = new RegisterMuxInput("ACCU");
        RegisterMuxInput in4 = new RegisterMuxInput("REG1");
        List<MuxInput> preconditionMuxInput = new ArrayList<>(Arrays.asList(in1, in2, in3, in4));
        List<MuxInput> postconditionMuxInput = new ArrayList<>(Arrays.asList(in1, in2, in3, NullMuxInput.INSTANCE));

        // test case setup
        mConfig.addRegisterExtension(reg1);
        mConfig.addRegisterExtension(reg2);
        mConfig.addMuxSource(mux, in4);

        // execute command
        undoManager.addCommand(new RegisterRemovedCommand(reg1, mConfig));
        checkRegisters("[RegisterRemoved] registers after execution", postconditionRegister);
        checkMuxInputs("[RegisterRemoved] MUX inputs after execution", postconditionMuxInput, mux);

        // undo command
        undoManager.undo();
        checkRegisters("[RegisterRemoved] registers after undo", preconditionRegister);
        checkMuxInputs("[RegisterRemoved] MUX inputs after undo", preconditionMuxInput, mux);

        // redo command
        undoManager.redo();
        checkRegisters("[RegisterRemoved] registers after redo", postconditionRegister);
        checkMuxInputs("[RegisterRemoved] MUX inputs after redo", postconditionMuxInput, mux);
    }

    /**
     * Checks whether the specified list represents the current list of {@code RegisterExtension}s.
     *
     * @param assertText
     *         the text for the {@code assertEquals} call
     * @param registers
     *         the list of expected registers
     */
    private void checkRegisters(String assertText, List<RegisterExtension> registers) {
        assertText += "; index: ";
        for (int i = 0; i < registers.size(); i++) {
            RegisterExtension expected = registers.get(i);
            RegisterExtension actual = mConfig.getRegisterExtension(i);
            assertEquals(assertText + i, true, expected.equals(actual));
        }
    }

    /**
     * Tests the implementation of {@link SignalRowAddedCommand}.
     */
    @Test
    public void testSignalRowAdded() {
        // TODO
    }

    /**
     * Tests the implementation of {@link SignalRowModifiedCommand}.
     */
    @Test
    public void testSignalRowModified() {
        // TODO
    }

    /**
     * Tests the implementation of {@link SignalRowMovedCommand}.
     */
    @Test
    public void testSignalRowMoved() {
        // TODO
    }

    /**
     * Tests the implementation of {@link SignalRowRemovedCommand}.
     */
    @Test
    public void testSignalRowRemoved() {
        // TODO
    }

}
