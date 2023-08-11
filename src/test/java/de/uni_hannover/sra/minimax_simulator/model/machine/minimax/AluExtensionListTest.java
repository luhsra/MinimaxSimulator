package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the implementation of the {@link AluExtensionList}.
 *
 * @author Philipp Rohde
 */
public class AluExtensionListTest {

    /**
     * Tests the {@code AluExtensionList}'s methods.
     */
    @Test
    public void testAluExtensionList() {
        final Alu alu = new Alu();
        final AluExtensionList list = new AluExtensionList(alu);

        // add
        list.add(AluOperation.A_ADD_B);
        list.add(AluOperation.A_AND_B);

        List<AluOperation> aluList = alu.getAluOperations();
        assertTrue("adding ALU operations: A_ADD_B", aluList.contains(AluOperation.A_ADD_B));
        assertTrue("adding ALU operations: A_AND_B", aluList.contains(AluOperation.A_AND_B));

        // add all
        List<AluOperation> ops = new ArrayList<>(Arrays.asList(AluOperation.B_DIV_A, AluOperation.B_DEC));
        list.addAll(ops);

        aluList = alu.getAluOperations();
        assertTrue("adding all ALU operations", aluList.contains(AluOperation.B_DIV_A));
        assertTrue("adding all ALU operations", aluList.contains(AluOperation.B_DEC));

        // remove
        // currently the following operations are added to the ALU
        // index 0: AluOperation.A_ADD_B
        // index 1: AluOperation.A_AND_B
        // index 2: AluOperation.B_DIV_A
        // index 3: AluOperation.B_DEC
        list.remove(3);
        aluList = alu.getAluOperations();
        assertFalse("removing ALU operation: B DEC", aluList.contains(AluOperation.B_DEC));

        list.remove(0);
        aluList = alu.getAluOperations();
        assertFalse("removing ALU operation: A ADD B", aluList.contains(AluOperation.A_ADD_B));

        // swap
        // currently the following operations are added to the ALU
        // index 0: AluOperation.A_AND_B
        // index 1: AluOperation.B_DIV_A
        list.swap(0, 1);

        aluList = alu.getAluOperations();
        assertEquals("swapping ALU operations", AluOperation.B_DIV_A, aluList.get(0));
        assertEquals("swapping ALU operations", AluOperation.A_AND_B, aluList.get(1));
    }

    /**
     * Tests {@link AluExtensionList#set(int, AluOperation)}.
     */
    @Test(expected = AssertionError.class)
    public void testSet() {
        Alu alu = new Alu();
        AluExtensionList list = new AluExtensionList(alu);
        list.add(AluOperation.A_ADD_B);
        list.set(0, AluOperation.A_MOD_B);
    }
}
