package de.uni_hannover.sra.minimax_simulator.model.machine;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the arithmetic and logic of the ALU by testing the different ALU operations.
 *
 * @see AluOperation
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class AluTest {

    /**
     * Tests the arithmetic of the ALU.
     */
    @Test
    public void testArithmetic() {
        int a = 0xD030D000;         // -802107392
        int b = 0x0020C000;         //    2146304

        int add = 0xD0519000;       // -799961088
        int subAB = 0xD0101000;     // -804253696
        int subBA = ~subAB + 1;

        assertEquals("a_trans", 0xD030D000, AluOperation.TRANS_A.execute(a, b));
        assertEquals("b_trans", 0x0020C000, AluOperation.TRANS_B.execute(a, b));

        assertEquals("a_add_b", add, AluOperation.A_ADD_B.execute(a, b));
        assertEquals("a_sub_b", subAB, AluOperation.A_SUB_B.execute(a, b));
        assertEquals("b_sub_a", subBA, AluOperation.B_SUB_A.execute(a, b));
        assertEquals("a_inc", a + 1, AluOperation.A_INC.execute(a, b));
        assertEquals("b_inc", b + 1, AluOperation.B_INC.execute(a, b));
        assertEquals("a_dec", a - 1, AluOperation.A_DEC.execute(a, b));
        assertEquals("b_dec", b - 1, AluOperation.B_DEC.execute(a, b));

        int c = 0xFFFFFFFE;         // -2
        int d = 0x00000003;         //  3

        assertEquals("a_mul_b", -6, AluOperation.A_MUL_B.execute(c, d));
        assertEquals("a_div_b", 0, AluOperation.A_DIV_B.execute(c, d));
        assertEquals("b_div_a", -1, AluOperation.B_DIV_A.execute(c, d));
        assertEquals("a_mod_b", -2, AluOperation.A_MOD_B.execute(c, d));
        assertEquals("b_mod_a", 1, AluOperation.B_MOD_A.execute(c, d));

        // division by zero etc.
        assertEquals("a_div_b zero", 0, AluOperation.A_DIV_B.execute(c, 0));
        assertEquals("b_div_a zero", 0, AluOperation.B_DIV_A.execute(0, d));
        assertEquals("a_mod_b zero", 0, AluOperation.A_MOD_B.execute(d, 0));
        assertEquals("b_mod_a zero", 0, AluOperation.B_MOD_A.execute(0, c));
    }

    /**
     * Tests the logic of the ALU.
     */
    @Test
    public void testLogic() {
        int a = 0xFF0F0000;
        int b = 0xFFF00001;

        int and = 0xFF000000;
        int or = 0xFFFF0001;
        int xor = 0x00FF0001;

        assertEquals("a_and_b", and, AluOperation.A_AND_B.execute(a, b));
        assertEquals("a_or_b", or, AluOperation.A_OR_B.execute(a, b));
        assertEquals("a_xor_b", xor, AluOperation.A_XOR_B.execute(a, b));
        assertEquals("a_inv", ~a, AluOperation.A_INV.execute(a, b));
        assertEquals("b_inv", ~b, AluOperation.B_INV.execute(a, b));

        int c = 0x80000001;
        int d = 2;

        // shift left
        assertEquals("a_sl", 0x00000002, AluOperation.A_SL.execute(c, 0));
        assertEquals("a_sl_b", 0x00000004, AluOperation.A_SL_B.execute(c, d));
        assertEquals("b_sl", 0x00000002, AluOperation.B_SL.execute(0, c));
        assertEquals("b_sl_a", 0x00000004, AluOperation.B_SL_A.execute(d, c));

        // (signed) shift right
        assertEquals("a_sr", 0xC0000000, AluOperation.A_SR.execute(c, 0));
        assertEquals("a_sr_b", 0xE0000000, AluOperation.A_SR_B.execute(c, d));
        assertEquals("b_sr", 0xC0000000, AluOperation.B_SR.execute(0, c));
        assertEquals("b_sr_a", 0xE0000000, AluOperation.B_SR_A.execute(d, c));

        // unsigned sift right
        assertEquals("a_sru", 0x40000000, AluOperation.A_SRU.execute(c, 0));
        assertEquals("a_sru_b", 0x20000000, AluOperation.A_SRU_B.execute(c, d));
        assertEquals("b_sru", 0x40000000, AluOperation.B_SRU.execute(0, c));
        assertEquals("b_sru_a", 0x20000000, AluOperation.B_SRU_A.execute(d, c));

        // rotate left
        assertEquals("a_rotl", 0x00000003, AluOperation.A_ROTL.execute(c, d));
        assertEquals("a_rotl_b", 0x00000006, AluOperation.A_ROTL_B.execute(c, d));
        assertEquals("b_rotl", 0x00000003, AluOperation.B_ROTL.execute(d, c));
        assertEquals("b_rotl_a", 0x00000006, AluOperation.B_ROTL_A.execute(d, c));

        // rotate right
        assertEquals("a_rotr", 0xC0000000, AluOperation.A_ROTR.execute(c, d));
        assertEquals("a_rotr_b", 0x60000000, AluOperation.A_ROTR_B.execute(c, d));
        assertEquals("b_rotr", 0xC0000000, AluOperation.B_ROTR.execute(d, c));
        assertEquals("b_rotr_a", 0x60000000, AluOperation.B_ROTR_A.execute(d, c));
    }
}
