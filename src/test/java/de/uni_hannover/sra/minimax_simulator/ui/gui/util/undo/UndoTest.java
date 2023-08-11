package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.io.exporter.ProjectExportException;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.*;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.DefaultJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the complete undo/redo system of the application.
 *
 * @author Philipp Rohde
 */
public class UndoTest {

    /** temporary folder */
    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    private static Workspace workspace;
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
        Project project = workspace.getProject();
        mConfig = project.getMachineConfiguration();
        signalTable = project.getSignalTable();
        undoManager.reset();
    }

    /**
     * Tests if the values of {@link UndoManager#isUndoAvailableProperty()} and
     * {@link UndoManager#isRedoAvailableProperty()} are set correct as well as marking the {@code Project} as
     * saved / unsaved works as expected.
     *
     * @throws IOException
     *          thrown if the temporary file could not be created or modified
     * @throws ProjectExportException
     *          thrown if the {@code Project} could not be exported
     */
    @Test
    public void testUndoManager() throws IOException, ProjectExportException {
        File file = tmpDir.newFile("test.zip");

        assertFalse("[UndoManager] clean start", undoManager.isUndoAvailableProperty().get());
        assertFalse("[UndoManager] clean start", undoManager.isRedoAvailableProperty().get());
        assertTrue("[UndoManager] project saved at start", undoManager.isProjectSaved());

        undoManager.addCommand(new MuxInputMovedCommand(MuxType.A, 0, 1, mConfig));
        assertTrue("[UndoManager] one executed command", undoManager.isUndoAvailableProperty().get());
        assertFalse("[UndoManager] one executed command", undoManager.isRedoAvailableProperty().get());
        assertFalse("[UndoManager] project is unsaved after executed command", undoManager.isProjectSaved());

        undoManager.addCommand(new AluOpMovedCommand(0, 1, mConfig));
        assertTrue("[UndoManager] two executed commands", undoManager.isUndoAvailableProperty().get());
        assertFalse("[UndoManager] two executed commands", undoManager.isRedoAvailableProperty().get());
        assertFalse("[UndoManager] project is unsaved after two executed commands", undoManager.isProjectSaved());

        undoManager.undo();
        assertTrue("[UndoManager] one executed and one undone command", undoManager.isUndoAvailableProperty().get());
        assertTrue("[UndoManager] one executed and one undone command", undoManager.isRedoAvailableProperty().get());
        assertFalse("[UndoManager] project is unsaved after executed and undone command", undoManager.isProjectSaved());

        undoManager.undo();
        assertFalse("[UndoManager] two undone commands", undoManager.isUndoAvailableProperty().get());
        assertTrue("[UndoManager] two undone commands", undoManager.isRedoAvailableProperty().get());
        assertTrue("[UndoManager] project is saved after two undone commands and no save", undoManager.isProjectSaved());

        undoManager.redo();
        assertTrue("[UndoManager] one executed and one undone command", undoManager.isUndoAvailableProperty().get());
        assertTrue("[UndoManager] one executed and one undone command", undoManager.isRedoAvailableProperty().get());
        assertFalse("[UndoManager] project is unsaved after executed and undone command", undoManager.isProjectSaved());

        undoManager.redo();
        assertTrue("[UndoManager] two executed commands", undoManager.isUndoAvailableProperty().get());
        assertFalse("[UndoManager] two executed commands", undoManager.isRedoAvailableProperty().get());
        assertFalse("[UndoManager] project is unsaved after two executed commands", undoManager.isProjectSaved());

        undoManager.undo();
        undoManager.addCommand(new RegisterAddedCommand(new RegisterExtension("TMP", RegisterSize.BITS_32, "desc", true), mConfig));
        assertTrue("[UndoManager] new command after undo", undoManager.isUndoAvailableProperty().get());
        assertFalse("[UndoManager] new command after undo", undoManager.isRedoAvailableProperty().get());
        assertFalse("[UndoManager] project is unsaved after new command after undo", undoManager.isProjectSaved());

        undoManager.addCommand(new MuxInputMovedCommand(MuxType.B, 0, 1, mConfig));
        undoManager.addCommand(new MuxInputMovedCommand(MuxType.A, 0, 1, mConfig));
        undoManager.addCommand(new MuxInputMovedCommand(MuxType.B, 0, 1, mConfig));
        undoManager.undo();
        workspace.saveProject(file);
        assertTrue("[UndoManager] project saved after saving", undoManager.isProjectSaved());

        undoManager.addCommand(new MuxInputMovedCommand(MuxType.A, 0, 1, mConfig));
        assertTrue("[UndoManager] executed command(s)", undoManager.isUndoAvailableProperty().get());
        assertFalse("[UndoManager] executed command(s)", undoManager.isRedoAvailableProperty().get());
        assertFalse("[UndoManager] project is unsaved after executed command", undoManager.isProjectSaved());

        undoManager.undo();
        assertTrue("[UndoManager] executed command(s); one undone", undoManager.isUndoAvailableProperty().get());
        assertTrue("[UndoManager] executed command(s); one undone", undoManager.isRedoAvailableProperty().get());
        assertTrue("[UndoManager] project is saved due to no new changes", undoManager.isProjectSaved());

        undoManager.undo();
        assertTrue("[UndoManager] executed command(s); two undone", undoManager.isUndoAvailableProperty().get());
        assertTrue("[UndoManager] executed command(s); two undone", undoManager.isRedoAvailableProperty().get());
        assertFalse("[UndoManager] project is unsaved after undone command", undoManager.isProjectSaved());

        undoManager.redo();
        assertTrue("[UndoManager] executed command(s); one undone", undoManager.isUndoAvailableProperty().get());
        assertTrue("[UndoManager] executed command(s); one undone", undoManager.isRedoAvailableProperty().get());
        assertTrue("[UndoManager] project is saved due to no new changes", undoManager.isProjectSaved());

        undoManager.redo();
        assertTrue("[UndoManager] executed command(s)", undoManager.isUndoAvailableProperty().get());
        assertFalse("[UndoManager] executed command(s)", undoManager.isRedoAvailableProperty().get());
        assertFalse("[UndoManager] project is unsaved after redone command", undoManager.isProjectSaved());
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
        assertTrue("[MuxInputModified] type after execution", mConfig.getMuxSources(MuxType.A).get(1) instanceof RegisterMuxInput);
        assertTrue("[MuxInputModified] value after execution", "MDR".equals(((RegisterMuxInput) mConfig.getMuxSources(MuxType.A).get(1)).getRegisterName()));

        // undo command
        undoManager.undo();
        assertTrue("[MuxInputModified] type after undo", mConfig.getMuxSources(MuxType.A).get(1) instanceof ConstantMuxInput);
        assertEquals("[MuxInputModified] value after undo", 1, ((ConstantMuxInput) mConfig.getMuxSources(MuxType.A).get(1)).getConstant());

        // redo command
        undoManager.redo();
        assertTrue("[MuxInputModified] type after redo", mConfig.getMuxSources(MuxType.A).get(1) instanceof RegisterMuxInput);
        assertTrue("[MuxInputModified] value after redo", "MDR".equals(((RegisterMuxInput) mConfig.getMuxSources(MuxType.A).get(1)).getRegisterName()));
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
                assertTrue(assertText + i, (actual instanceof ConstantMuxInput));
                ConstantMuxInput expCMI = (ConstantMuxInput) expected;
                ConstantMuxInput actCMI = (ConstantMuxInput) actual;
                assertEquals(assertText + i, expCMI.getConstant(), actCMI.getConstant());
            }
            else if (expected instanceof RegisterMuxInput) {
                assertTrue(assertText + i, (actual instanceof RegisterMuxInput));
                RegisterMuxInput expRMI = (RegisterMuxInput) expected;
                RegisterMuxInput actRMI = (RegisterMuxInput) actual;
                assertTrue(assertText + i, expRMI.getRegisterName().equals(actRMI.getRegisterName()));
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
            assertTrue(assertText + i, expected.equals(actual));
        }
    }

    /**
     * Tests the implementation of {@link SignalRowAddedCommand}.
     */
    @Test
    public void testSignalRowAdded() {
        // test case setup
        List<SignalRow> precondition = createSignalRowList();
        signalTable.addSignalRow(2, new SignalRow());
        List<SignalRow> postcondition = createSignalRowList();
        signalTable.removeSignalRow(2);

        // execute command
        undoManager.addCommand(new SignalRowAddedCommand(2, signalTable));
        checkSignalRows("[SignalRowAdded] executed", postcondition);

        // undo command
        undoManager.undo();
        checkSignalRows("[SignalRowAdded] undo", precondition);

        // redo command
        undoManager.redo();
        checkSignalRows("[SignalRowAdded] redo", postcondition);

        // now the whole thing again for adding the row to the end of the list
        // test case setup
        precondition = createSignalRowList();
        signalTable.addSignalRow(new SignalRow());
        postcondition = createSignalRowList();
        signalTable.removeSignalRow(signalTable.getRowCount()-1);

        // execute command
        undoManager.addCommand(new SignalRowAddedCommand(-1, signalTable));
        checkSignalRows("[SignalRowAdded] executed", postcondition);

        // undo command
        undoManager.undo();
        checkSignalRows("[SignalRowAdded] undo", precondition);

        // redo command
        undoManager.redo();
        checkSignalRows("[SignalRowAdded] redo", postcondition);
    }

    /**
     * Tests the implementation of {@link SignalRowModifiedCommand}.
     */
    @Test
    public void testSignalRowModified() {
        // test case setup
        List<SignalRow> precondition = createSignalRowList();
        SignalRow oldRow = new SignalRow(signalTable.getRow(3));
        signalTable.getRow(3).setBreakpoint(true);
        List<SignalRow> postcondition = createSignalRowList();
        SignalRow newRow = new SignalRow(signalTable.getRow(3));
        signalTable.getRow(3).setBreakpoint(false);

        // execute command
        undoManager.addCommand(new SignalRowModifiedCommand(3, oldRow, newRow, signalTable));
        checkSignalRows("[SignalRowModified] executed", postcondition);

        // undo command
        undoManager.undo();
        checkSignalRows("[SignalRowModified] undo", precondition);

        // redo command
        undoManager.redo();
        checkSignalRows("[SignalRowModified] redo", postcondition);
    }

    /**
     * Tests the implementation of {@link SignalRowMovedCommand}.
     */
    @Test
    public void testSignalRowMoved() {
        // test case setup
        List<SignalRow> precondition = createSignalRowList();
        signalTable.moveSignalRows(3, 3, 1);
        List<SignalRow> postcondition = createSignalRowList();
        signalTable.moveSignalRows(4, 4, -1);

        // execute command
        undoManager.addCommand(new SignalRowMovedCommand(3, 1, signalTable));
        checkSignalRows("[SignalRowMoved] executed", postcondition);

        // undo command
        undoManager.undo();
        checkSignalRows("[SignalRowMoved] undo", precondition);

        // redo command
        undoManager.redo();
        checkSignalRows("[SignalRowMoved] redo", postcondition);
    }

    /**
     * Tests the implementation of {@link SignalRowRemovedCommand}.
     */
    @Test
    public void testSignalRowRemoved() {
        // test case setup
        List<SignalRow> precondition = createSignalRowList();
        signalTable.removeSignalRow(5);
        List<SignalRow> postcondition = createSignalRowList();
        resetProject();

        // execute command
        undoManager.addCommand(new SignalRowRemovedCommand(5, signalTable));
        checkSignalRows("[SignalRowReoved] executed", postcondition);

        // undo command
        undoManager.undo();
        checkSignalRows("[SignalRowRemoved] undo", precondition);

        // redo command
        undoManager.redo();
        checkSignalRows("[SignalRowRemoved] redo", postcondition);
    }

    /**
     * Creates a list of the current {@code SignalRow}s.
     *
     * @return
     *         the list of the {@code SignalRows}
     */
    private List<SignalRow> createSignalRowList() {
        List<SignalRow> rows = new ArrayList<>();
        for (SignalRow row : signalTable.getRows()) {
            rows.add(new SignalRow(row));
        }
        return rows;
    }

    /**
     * Checks whether the specified list of {@code SignalRow}s is a representation of the current {@code SignalTable}.
     *
     * @param assertText
     *         the text for the {@code assertEquals} call
     * @param signalRows
     *         the list of expected {@code SignalRow}s
     */
    private void checkSignalRows(String assertText, List<SignalRow> signalRows) {
        assertText += " ";
        String idx = "; row index: ";

        assertEquals(assertText + "number of signal rows", signalRows.size(), signalTable.getRowCount());
        for (int i = 0; i < signalRows.size(); i++) {
            SignalRow expected = signalRows.get(i);
            SignalRow actual = signalTable.getRow(i);

            // check breakpoint
            assertEquals(assertText + "breakpoint" + idx + i, expected.isBreakpoint(), actual.isBreakpoint());

            // check label
            if (expected.getLabel() != null) {
                assertTrue(assertText + "label" + idx + i, expected.getLabel().equals(actual.getLabel()));
            }

            // check signal values
            assertEquals(assertText + "signal value key set size" + idx + i, expected.getSignalValues().keySet().size(), actual.getSignalValues().keySet().size());
            for (String key : expected.getSignalValues().keySet()) {
                assertTrue(assertText + "contains key: " + key + idx + i, actual.getSignalValues().keySet().contains(key));
                assertEquals(assertText + "value of key: " + key + idx + i, expected.getSignalValue(key), actual.getSignalValue(key));
            }

            // check jump
            Jump jumpExpected = expected.getJump();
            Jump jumpActual = actual.getJump();
            if (jumpExpected instanceof DefaultJump) {
                assertTrue(assertText + "default jump" + idx + i, (jumpActual instanceof DefaultJump));
                DefaultJump djExp = (DefaultJump) jumpExpected;
                DefaultJump djAct = (DefaultJump) jumpActual;
                assertEquals(assertText + "jump target" + idx + i, djExp.getTargetRow(i, 0), djAct.getTargetRow(i, 0));
            }
            else if (jumpExpected instanceof UnconditionalJump) {
                assertTrue(assertText + "unconditional jump" + idx + i, (jumpActual instanceof UnconditionalJump));
                UnconditionalJump ujExp = (UnconditionalJump) jumpExpected;
                UnconditionalJump ujAct = (UnconditionalJump) jumpActual;
                assertEquals(assertText + "jump target" + idx + i, ujExp.getTargetRow(), ujAct.getTargetRow());
            }
            else if (jumpExpected instanceof ConditionalJump) {
                assertTrue(assertText + "conditional jump" + idx + i, (jumpActual instanceof ConditionalJump));
                ConditionalJump cjExp = (ConditionalJump) jumpExpected;
                ConditionalJump cjAct = (ConditionalJump) jumpActual;
                assertEquals(assertText + "jump target 0" + idx + i, cjExp.getTargetRow(0), cjAct.getTargetRow(0));
                assertEquals(assertText + "jump target 1" + idx + i, cjExp.getTargetRow(1), cjAct.getTargetRow(1));
            }
        }
    }
}
