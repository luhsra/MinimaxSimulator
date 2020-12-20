package de.uni_hannover.sra.minimax_simulator.model.user;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.DefaultJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;
import de.uni_hannover.sra.minimax_simulator.resources.ResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link NewProjectBuilder}.
 *
 * @author Philipp Rohde
 */
public class NewProjectBuilderTest {

    private static Project project;
    private static RegisterExtension accu;
    private static RegisterExtension pc;
    private static RegisterExtension ir;
    private static RegisterExtension mdr;
    private static RegisterExtension mar;
    private static MuxInput constZero;
    private static MuxInput constOne;
    private static MuxInput regAccu;
    private static MuxInput regMdr;
    private static MuxInput regPc;
    private static MuxInput regAt;
    private static final String ALU_SEL_A = "ALU_SELECT_A";
    private static final String ALU_SEL_B = "ALU_SELECT_B";
    private static final String MEM_CS = "MEM_CS";
    private static final String MEM_RW = "MEM_RW";
    private static final String ALU_CTRL = "ALU_CTRL";

    /**
     * Initializes the test instance.
     */
    @BeforeClass
    public static void initialize() {
        ResourceBundleLoader res = Main.getResourceLoader();
        TextResource resReg = res.getTextResource("register");
        project = new NewProjectBuilder().buildProject();

        accu = new RegisterExtension("ACCU", RegisterSize.BITS_32, resReg.get("register.accu.description"), false);
        pc = new RegisterExtension("PC", RegisterSize.BITS_32, resReg.get("register.pc.description"), false);
        ir = new RegisterExtension("IR", RegisterSize.BITS_32, resReg.get("register.ir.description"), false);
        mdr = new RegisterExtension("MDR", RegisterSize.BITS_32, resReg.get("register.mdr.description"), false);
        mar = new RegisterExtension("MAR", RegisterSize.BITS_24, resReg.get("register.mar.description"), false);

        constZero = new ConstantMuxInput(0);
        constOne = new ConstantMuxInput(1);
        regAccu = new RegisterMuxInput("ACCU");
        regMdr = new RegisterMuxInput("MDR");
        regPc = new RegisterMuxInput("PC");
        regAt = new RegisterMuxInput("IR", "AT");
    }

    /**
     * Tests if a new project contains the basic machine configuration.<br>
     * <br>
     * Base registers: PC, IR, MDR, MAR, ACCU<br>
     * Extended registers: [none]<br>
     * <br>
     * ALU operations: A ADD B, B SUB A, TRANS A, TRANS B<br>
     * <br>
     * MUX sources A: 0, 1, ACCU
     * MUX sources B: MDR, PC, AT, ACCU
     */
    @Test
    public void testMachineConfig() {
        final MachineConfiguration config = project.getMachineConfiguration();

        // base registers
        List<RegisterExtension> baseRegisters = new ArrayList<>(Arrays.asList(pc, ir, mdr, mar, accu));
        assertEquals("number of base registers", baseRegisters.size(), config.getBaseRegisters().size());
        for (int i = 0; i < baseRegisters.size(); i++) {
            assertEquals("expected base registers", baseRegisters.get(i), config.getBaseRegister(i));
        }

        // no extended registers
        assertTrue("no extended registers", config.getRegisterExtensions().isEmpty());

        // ALU operations
        List<AluOperation> aluOps = new ArrayList<>(Arrays.asList(AluOperation.A_ADD_B, AluOperation.B_SUB_A, AluOperation.TRANS_A, AluOperation.TRANS_B));
        assertEquals("number of ALU operations", aluOps.size(), config.getAluOperations().size());
        for (int i = 0; i < aluOps.size(); i++) {
            assertEquals("expected ALU operations", aluOps.get(i), config.getAluOperation(i));
        }

        // MUX sources A
        List<MuxInput> muxA = new ArrayList<>(Arrays.asList(constZero, constOne, regAccu));
        assertEquals("number of MUX sources A", muxA.size(), config.getMuxSources(MuxType.A).size());
        for (int i = 0; i < muxA.size(); i++) {
            assertEquals("expected sources MUX A", muxA.get(i), config.getMuxSources(MuxType.A).get(i));
        }

        // MUX sources B
        List<MuxInput> muxB = new ArrayList<>(Arrays.asList(regMdr, regPc, regAt, regAccu));
        assertEquals("number of MUX sources B", muxB.size(), config.getMuxSources(MuxType.B).size());
        for (int i = 0; i < muxB.size(); i++) {
            assertEquals("expected sources MUX B", muxB.get(i), config.getMuxSources(MuxType.B).get(i));
        }
    }

    /**
     * Tests if the default program is successfully added to the newly created project.
     */
    @Test
    public void testSignalTable() {
        final SignalTable table = project.getSignalTable();
        SignalRow row;

        assertEquals("number of rows", 8, table.getRowCount());

        boolean[] expectedBreakpoint = {false, false, false, false, false, false, false, true};
        String[] expectedLabels = {"start", null, null, null, null, null, null, "store"};
        boolean[] hasAluSelA = {true, true, true, true, false, true, true, false};
        int[] expectedAluSelA = {1, 2, 2, 2, 0, 2, 1, 0};
        boolean[] hasAluSelB = {true, true, true, true, true, true, true, false};
        int[] expectedAluSelB = {3, 3, 3, 3, 1, 3, 1, 0};
        boolean[] hasMemCs = {false, false, false, false, false, false, false, true};
        int[] expectedMemCs = {0, 0, 0, 0, 0, 0, 0, 1};
        boolean[] hasMemRw = {false, false, false, false, false, false, false, true};
        boolean[] hasAluCtrl = {true, true, true, true, true, true, true, false};
        int[] expectedAluCtrl = {0, 0, 0, 0, 3, 0, 1, 0};
        int[] pcW = {0, 1, 1, 1, 0, 0, 1, 0};
        int[] mdrW = {0, 0, 0, 0, 0, 1, 0, 0};
        int[] accuW = {1, 1, 1, 1, 0, 1, 0, 0};

        List<Jump> jumps = new ArrayList<>();
        jumps.add(DefaultJump.INSTANCE);        // row 0
        jumps.add(DefaultJump.INSTANCE);        // row 1
        jumps.add(DefaultJump.INSTANCE);        // row 2
        jumps.add(DefaultJump.INSTANCE);        // row 3
        jumps.add(new ConditionalJump(5, 7));   // row 4
        jumps.add(DefaultJump.INSTANCE);        // row 5
        jumps.add(new UnconditionalJump(4));    // row 6
        jumps.add(DefaultJump.INSTANCE);        // row 7

        for (int i = 0; i < table.getRowCount(); i++) {
            row = table.getRow(i);
            assertEquals("isBreakpoint of row " + i, expectedBreakpoint[i], row.isBreakpoint());

            assertEquals("label of row " + i, expectedLabels[i], row.getLabel());

            assertEquals("has ALUSel.A of row " + i, hasAluSelA[i], row.getSignalValues().containsKey(ALU_SEL_A));
            assertEquals("ALUSel.A of row " + i, expectedAluSelA[i], row.getSignalValue(ALU_SEL_A));

            assertEquals("has ALUSel.B of row " + i, hasAluSelB[i], row.getSignalValues().containsKey(ALU_SEL_B));
            assertEquals("ALUSel.B of row " + i, expectedAluSelB[i], row.getSignalValue(ALU_SEL_B));

            assertFalse("has MDR.SEL of row " + i, row.getSignalValues().containsKey("MDR_SEL"));

            assertEquals("has MEM.CS of row " + i, hasMemCs[i], row.getSignalValues().containsKey(MEM_CS));
            assertEquals("MEM.CS of row " + i, expectedMemCs[i], row.getSignalValue(MEM_CS));

            assertEquals("has MEM.RW of row " + i, hasMemRw[i], row.getSignalValues().containsKey(MEM_RW));
            assertEquals("MEM.RW of row " + i, 0, row.getSignalValue(MEM_RW));

            assertEquals("has ALU Ctrl of row " + i, hasAluCtrl[i], row.getSignalValues().containsKey(ALU_CTRL));
            assertEquals("ALU Ctrl of row " + i, expectedAluCtrl[i], row.getSignalValue(ALU_CTRL));

            assertEquals("PC.W of row " + i, pcW[i], row.getSignalValue("PC.W"));
            assertEquals("IR.W of row " + i, 0, row.getSignalValue("IR.W"));
            assertEquals("MDR.W of row " + i, mdrW[i], row.getSignalValue("MDR.W"));
            assertEquals("MAR.W of row " + i, 0, row.getSignalValue("MAR.W"));
            assertEquals("ACCU.W of row " + i, accuW[i], row.getSignalValue("ACCU.W"));

            assertEquals("jump of row " + i, jumps.get(i), row.getJump());

            // creation of description is tested with the MinimaxSignalDescriptionTest
        }
    }
}
