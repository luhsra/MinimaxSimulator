package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
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
        for (int j = 0; j < postconditionAluCtrl.size(); j++) {
            SignalRow row = signalTable.getRow(j);
            assertEquals("[AluOpMoved] ALU OpCodes after execution; row index: " + j, (int) postconditionAluCtrl.get(j), row.getSignalValue(ALU_CTRL));
        }

        // undo command
        undoManager.undo();
        for (int i = 0; i < preconditionOps.size(); i++) {
            assertEquals("[AluOpMoved] order after undo; index: " + i, preconditionOps.get(i), mConfig.getAluOperation(i));
        }
        for (int j = 0; j < preconditionAluCtrl.size(); j++) {
            SignalRow row = signalTable.getRow(j);
            assertEquals("[AluOpMoved] ALU OpCodes after undo; row index: " + j, (int) preconditionAluCtrl.get(j), row.getSignalValue(ALU_CTRL));
        }

        // redo command
        undoManager.redo();
        for (int i = 0; i < postconditionOps.size(); i++) {
            assertEquals("[AluOpMoved] order after redo; index: " + i, postconditionOps.get(i), mConfig.getAluOperation(i));
        }
        for (int j = 0; j < postconditionAluCtrl.size(); j++) {
            SignalRow row = signalTable.getRow(j);
            assertEquals("[AluOpMoved] ALU OpCodes after redo; row index: " + j, (int) postconditionAluCtrl.get(j), row.getSignalValue(ALU_CTRL));
        }
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
        for (int j = 0; j < postconditionAluCtrl.size(); j++) {
            SignalRow row = signalTable.getRow(j);
            assertEquals("[AluOpRemoved] ALU OpCodes after execution; row index: " + j, (int) postconditionAluCtrl.get(j), row.getSignalValue(ALU_CTRL));
        }

        // undo command
        undoManager.undo();
        for (int i = 0; i < preconditionOps.size(); i++) {
            assertEquals("[AluOpRemoved] order after undo; index: " + i, preconditionOps.get(i), mConfig.getAluOperation(i));
        }
        for (int j = 0; j < preconditionAluCtrl.size(); j++) {
            SignalRow row = signalTable.getRow(j);
            assertEquals("[AluOpRemoved] ALU OpCodes after undo; row index: " + j, (int) preconditionAluCtrl.get(j), row.getSignalValue(ALU_CTRL));
        }

        // redo command
        undoManager.redo();
        for (int i = 0; i < postconditionOps.size(); i++) {
            assertEquals("[AluOpRemoved] order after redo; index: " + i, postconditionOps.get(i), mConfig.getAluOperation(i));
        }
        for (int j = 0; j < postconditionAluCtrl.size(); j++) {
            SignalRow row = signalTable.getRow(j);
            assertEquals("[AluOpRemoved] ALU OpCodes after redo; row index: " + j, (int) postconditionAluCtrl.get(j), row.getSignalValue(ALU_CTRL));
        }
    }

    /**
     * Tests the implementation of {@link MuxInputAddedCommand}.
     */
    @Test
    public void testMuxInputAdded() {
        // TODO
    }

    /**
     * Tests the implementation of {@link MuxInputModifiedCommand}.
     */
    @Test
    public void testMuxInputModified() {
        // TODO
    }

    /**
     * Tests the implementation of {@link MuxInputMovedCommand}.
     */
    @Test
    public void testMuxInputMoved() {
        // TODO
    }

    /**
     * Tests the implementation of {@link MuxInputRemovedCommand}.
     */
    @Test
    public void testMuxInputRemoved() {
        // TODO
    }

    /**
     * Tests the implementation of {@link RegisterAddedCommand}.
     */
    @Test
    public void testRegisterAdded() {
        // TODO
    }

    /**
     * Tests the implementation of {@link RegisterModifiedCommand}.
     */
    @Test
    public void testRegisterModified() {
        // TODO
    }

    /**
     * Tests the implementation of {@link RegisterMovedCommand}.
     */
    @Test
    public void testRegisterMoved() {
        // TODO
    }

    /**
     * Tests the implementation of {@link RegisterRemovedCommand}.
     */
    @Test
    public void testRegisterRemoved() {
        // TODO
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
