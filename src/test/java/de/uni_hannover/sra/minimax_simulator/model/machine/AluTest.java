package de.uni_hannover.sra.minimax_simulator.model.machine;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import static org.junit.Assert.*;

import org.junit.Test;

public class AluTest
{
	@Test
	public void testArithmetic()
	{
		int a = 0xD030D000;
		int b = 0x0020C000;
		
		int add = 0xD0519000;
		int subAB = 0xD0101000;
		int subBA = ~subAB + 1;
		
		assertEquals("a_add_b", add, AluOperation.A_ADD_B.execute(a, b));
		assertEquals("a_sub_b", subAB, AluOperation.A_SUB_B.execute(a, b));
		assertEquals("b_sub_a", subBA, AluOperation.B_SUB_A.execute(a, b));
		assertEquals("a_inc", a + 1, AluOperation.A_INC.execute(a, b));
		assertEquals("b_inc", b + 1, AluOperation.B_INC.execute(a, b));
		assertEquals("a_dec", a - 1, AluOperation.A_DEC.execute(a, b));
		assertEquals("b_dec", b - 1, AluOperation.B_DEC.execute(a, b));

		int c = 0xFFFFFFFE; // -2
		int d = 0x00000003;

		assertEquals("a_mul_b", -6, AluOperation.A_MUL_B.execute(c, d));
		assertEquals("a_div_b", 0, AluOperation.A_DIV_B.execute(c, d));
		assertEquals("b_div_a", -1, AluOperation.B_DIV_A.execute(c, d));
		assertEquals("a_mod_b", -2, AluOperation.A_MOD_B.execute(c, d));
		assertEquals("b_mod_a", 1, AluOperation.B_MOD_A.execute(c, d));
	}

	@Test
	public void testLogic()
	{
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

		assertEquals("a_sl", 0x00000002, AluOperation.A_SL.execute(c, 0));
		assertEquals("a_sl_b", 0x00000004, AluOperation.A_SL_B.execute(c, d));
		assertEquals("a_rotl", 0x00000003, AluOperation.A_ROTL.execute(c, d));
		assertEquals("a_rot_b", 0x00000006, AluOperation.A_ROTL_B.execute(c, d));
	}
}
