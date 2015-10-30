package de.uni_hannover.sra.minimax_simulator.model.configuration.alu;

import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

/**
 * An enumeration of binary integer operations available to a typical ALU of a register machine.
 * 
 * @author Martin L&uuml;ck
 */
public enum AluOperation {

	/**
	 * Rather an unary operation, the "Transfer A" operation discards its second parameter
	 * when executed and returns its first parameter.
	 */
	TRANS_A {
		@Override
		public int execute(int a, int b) {
			return a;
		}

		@Override
		public String getOperationName() {
			return "TRANSFER A";
		}

		@Override
		protected String getFormatKey() {
			return "transfer";
		}
	},

	/**
	 * Rather an unary operation, the "Transfer B" operation discards its first parameter
	 * when executed and returns its second parameter.
	 */
	TRANS_B {
		@Override
		public int execute(int a, int b) {
			return b;
		}

		@Override
		public String getOperationName() {
			return "TRANSFER B";
		}

		@Override
		protected String getFormatKey() {
			return "transfer";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},

	/**
	 * The "A ADD B" operation returns the result of {@code a + b}.
	 */
	A_ADD_B {
		@Override
		public int execute(int a, int b) {
			return a + b;
		}

		@Override
		public String getOperationName() {
			return "A  ADD  B";
		}

		@Override
		protected String getFormatKey() {
			return "add";
		}
	},

	/**
	 * The "A SUB B" operation returns the result of {@code a - b}.
	 */
	A_SUB_B {
		@Override
		public int execute(int a, int b) {
			return a - b;
		}

		@Override
		public String getOperationName() {
			return "A  SUB  B";
		}

		@Override
		protected String getFormatKey() {
			return "sub";
		}
	},

	/**
	 * The "B SUB A" operation returns the result of {@code b - a}.
	 */
	B_SUB_A {
		@Override
		public int execute(int a, int b) {
			return b - a;
		}

		@Override
		public String getOperationName() {
			return "B  SUB  A";
		}

		@Override
		protected String getFormatKey() {
			return "sub";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},

	/**
	 * Rather an unary operation, the "INC A" operation discards its second parameter
	 * when executed and returns its first parameter incremented by one.
	 */
	A_INC {
		@Override
		public int execute(int a, int b) {
			return a + 1;
		}

		@Override
		public String getOperationName() {
			return "INC  A";
		}

		@Override
		protected String getFormatKey() {
			return "inc";
		}
	},

	/**
	 * Rather an unary operation, the "INC B" operation discards its first parameter
	 * when executed and returns its second parameter incremented by one.
	 */
	B_INC {
		@Override
		public int execute(int a, int b) {
			return b + 1;
		}

		@Override
		public String getOperationName() {
			return "INC  B";
		}

		@Override
		protected String getFormatKey() {
			return "inc";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * Rather an unary operation, the "DEC A" operation discards its second parameter
	 * when executed and returns its first parameter decremented by one.
	 */
	A_DEC {
		@Override
		public int execute(int a, int b) {
			return a - 1;
		}

		@Override
		public String getOperationName() {
			return "DEC  A";
		}

		@Override
		protected String getFormatKey() {
			return "dec";
		}
	},

	/**
	 * Rather an unary operation, the "DEC B" operation discards its first parameter
	 * when executed and returns its second parameter decremented by one.
	 */
	B_DEC {
		@Override
		public int execute(int a, int b) {
			return b - 1;
		}

		@Override
		public String getOperationName() {
			return "DEC  B";
		}

		@Override
		protected String getFormatKey() {
			return "dec";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * The "A MUL B" operation returns the result of {@code a * b}.
	 */
	A_MUL_B {
		@Override
		public int execute(int a, int b) {
			return a * b;
		}

		@Override
		public String getOperationName() {
			return "A  MUL  B";
		}

		@Override
		protected String getFormatKey() {
			return "mul";
		}
	},

	/**
	 * The "A DIV B" operation returns the result of the integer division {@code a / b}
	 * or zero if {@code b} is zero.
	 */
	A_DIV_B {
		@Override
		public int execute(int a, int b) {
			return b == 0 ? 0 : a / b;
		}

		@Override
		public String getOperationName() {
			return "A  DIV  B";
		}

		@Override
		protected String getFormatKey() {
			return "div";
		}
	},

	/**
	 * The "B DIV A" operation returns the result of the integer division {@code b / a}
	 * or zero if {@code a} is zero.
	 */
	B_DIV_A {
		@Override
		public int execute(int a, int b) {
			return a == 0 ? 0 : b / a;
		}

		@Override
		public String getOperationName() {
			return "B  DIV  A";
		}

		@Override
		protected String getFormatKey() {
			return "div";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * The "A MOD B" operation returns the result of {@code a % b} or zero if {@code b} is zero.
	 */
	A_MOD_B {
		@Override
		public int execute(int a, int b) {
			return b == 0 ? 0 : a % b;
		}

		@Override
		public String getOperationName() {
			return "A  MOD  B";
		}

		@Override
		protected String getFormatKey() {
			return "mod";
		}
	},

	/**
	 * The "B MOD A" operation returns the result of {@code b % a} or zero if {@code a} is zero.
 	 */
	B_MOD_A {
		@Override
		public int execute(int a, int b) {
			return a == 0 ? 0 : b % a;
		}

		@Override
		public String getOperationName() {
			return "B  MOD  A";
		}

		@Override
		protected String getFormatKey() {
			return "mod";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * The "A AND B" operation returns the result of {@code a & b}.
	 */
	A_AND_B {
		@Override
		public int execute(int a, int b) {
			return a & b;
		}

		@Override
		public String getOperationName() {
			return "A  AND  B";
		}

		@Override
		protected String getFormatKey() {
			return "and";
		}
	},

	/**
	 * The "A OR B" operation returns the result of {@code a | b}.
	 */
	A_OR_B {
		@Override
		public int execute(int a, int b) {
			return a | b;
		}

		@Override
		public String getOperationName() {
			return "A  OR  B";
		}

		@Override
		protected String getFormatKey() {
			return "or";
		}
	},

	/**
	 * The "A XOR B" operation returns the result of {@code a ^ b}.
	 */
	A_XOR_B {
		@Override
		public int execute(int a, int b) {
			return a ^ b;
		}

		@Override
		public String getOperationName() {
			return "A  XOR  B";
		}

		@Override
		protected String getFormatKey() {
			return "xor";
		}
	},

	/**
	 * Rather an unary operation, the "INV A" operation discards its second parameter
	 * when executed and returns its first parameter inverted.
	 */
	A_INV {
		@Override
		public int execute(int a, int b) {
			return ~a;
		}

		@Override
		public String getOperationName() {
			return "INV  A";
		}

		@Override
		protected String getFormatKey() {
			return "inv";
		}
	},

	/**
	 * Rather an unary operation, the "INV B" operation discards its first parameter
	 * when executed and returns its second parameter inverted.
	 */
	B_INV {
		@Override
		public int execute(int a, int b) {
			return ~b;
		}

		@Override
		public String getOperationName() {
			return "INV  B";
		}

		@Override
		protected String getFormatKey() {
			return "inv";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * Rather an unary operation, the "A S.L." operation discards its second parameter
	 * when executed and returns its first parameter shifted left by one Bit.
	 */
	A_SL {
		@Override
		public int execute(int a, int b) {
			return a << 1;
		}

		@Override
		public String getOperationName() {
			return "A  S.L.";
		}

		@Override
		protected String getFormatKey() {
			return "sl1";
		}
	},

	/**
	 * The "A S.L. B" operation returns its first parameter shifted left by {@code b} Bits.
	 */
	A_SL_B {
		@Override
		public int execute(int a, int b) {
			return a << b;
		}

		@Override
		public String getOperationName() {
			return "A  S.L.  B";
		}

		@Override
		protected String getFormatKey() {
			return "sl2";
		}
	},

	/**
	 * Rather an unary operation, the "B S.L." operation discards its first parameter
	 * when executed and returns its second parameter shifted left by one Bit.
	 */
	B_SL {
		@Override
		public int execute(int a, int b) {
			return b << 1;
		}

		@Override
		public String getOperationName() {
			return "B  S.L.";
		}

		@Override
		protected String getFormatKey() {
			return "sl1";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * The "B S.L. A" operation returns its second parameter shifted left by {@code a} Bits.
	 */
	B_SL_A {
		@Override
		public int execute(int a, int b) {
			return b << a;
		}

		@Override
		public String getOperationName() {
			return "B  S.L.  A";
		}

		@Override
		protected String getFormatKey() {
			return "sl2";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * Rather an unary operation, the "A S.R." operation discards its second parameter
	 * when executed and returns its first parameter shifted right by one Bit.
	 */
	A_SR {
		@Override
		public int execute(int a, int b) {
			return a >> 1;
		}

		@Override
		public String getOperationName() {
			return "A  S.R.";
		}

		@Override
		protected String getFormatKey() {
			return "sr1";
		}
	},

	/**
	 * The "A S.R. B" operation returns its first parameter shifted right by {@code b} Bits.
	 */
	A_SR_B {
		@Override
		public int execute(int a, int b) {
			return a >> b;
		}

		@Override
		public String getOperationName() {
			return "A  S.R.  B";
		}

		@Override
		protected String getFormatKey() {
			return "sr2";
		}
	},

	/**
	 * Rather an unary operation, the "B S.R." operation discards its first parameter
	 * when executed and returns its second parameter shifted right by one Bit.
	 */
	B_SR {
		@Override
		public int execute(int a, int b) {
			return b >> 1;
		}

		@Override
		public String getOperationName() {
			return "B  S.R.";
		}

		@Override
		protected String getFormatKey() {
			return "sr1";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * The "B S.R. A" operation returns its second parameter shifted right by {@code a} Bits.
	 */
	B_SR_A {
		@Override
		public int execute(int a, int b) {
			return b >> a;
		}

		@Override
		public String getOperationName() {
			return "B  S.R.  A";
		}

		@Override
		protected String getFormatKey() {
			return "sr2";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * Rather an unary operation, the "A S.R.U." operation discards its second parameter
	 * when executed and returns its first parameter unsigned shifted right by one Bit.
	 */
	A_SRU {
		@Override
		public int execute(int a, int b) {
			return a >>> 1;
		}

		@Override
		public String getOperationName() {
			return "A  S.R.U.";
		}

		@Override
		protected String getFormatKey() {
			return "sru1";
		}
	},

	/**
	 * The "A S.R.U. B" operation returns its first parameter unsigned shifted right by {@code b} Bits.
	 */
	A_SRU_B {
		@Override
		public int execute(int a, int b) {
			return a >>> b;
		}

		@Override
		public String getOperationName() {
			return "A  S.R.U.  B";
		}

		@Override
		protected String getFormatKey() {
			return "sru2";
		}
	},

	/**
	 * Rather an unary operation, the "B S.R.U." operation discards its first parameter
	 * when executed and returns its second parameter unsigned shifted right by one Bit.
	 */
	B_SRU {
		@Override
		public int execute(int a, int b) {
			return b >>> 1;
		}

		@Override
		public String getOperationName() {
			return "B  S.R.U.";
		}

		@Override
		protected String getFormatKey() {
			return "sru1";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * The "B S.R.U. A" operation returns its second parameter unsigned shifted right by {@code a} Bits.
	 */
	B_SRU_A {
		@Override
		public int execute(int a, int b) {
			return b >>> a;
		}

		@Override
		public String getOperationName() {
			return "B  S.R.U.  A";
		}

		@Override
		protected String getFormatKey() {
			return "sru2";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * Rather an unary operation, the "A R.L." operation discards its second parameter
	 * when executed and returns its first parameter rotated left by one Bit.
	 */
	A_ROTL {
		@Override
		public int execute(int a, int b) {
			return Integer.rotateLeft(a, 1);
		}

		@Override
		public String getOperationName() {
			return "A  R.L.";
		}

		@Override
		protected String getFormatKey() {
			return "rotl1";
		}
	},

	/**
	 * The "A R.L. B" operation returns its first parameter rotated left by {@code b} Bits.
	 */
	A_ROTL_B {
		@Override
		public int execute(int a, int b) {
			return Integer.rotateLeft(a, b);
		}

		@Override
		public String getOperationName() {
			return "A  R.L.  B";
		}

		@Override
		protected String getFormatKey() {
			return "rotl2";
		}
	},

	/**
	 * Rather an unary operation, the "B. R.L." operation discards its first parameter
	 * when executed and returns its second parameter rotated left by one Bit.
	 */
	B_ROTL {
		@Override
		public int execute(int a, int b) {
			return Integer.rotateLeft(b, 1);
		}

		@Override
		public String getOperationName() {
			return "B  R.L.";
		}

		@Override
		protected String getFormatKey() {
			return "rotl1";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * The "B R.L. A" operation returns its second parameter rotated left by {@code a} Bits.
	 */
	B_ROTL_A {
		@Override
		public int execute(int a, int b) {
			return Integer.rotateLeft(b, a);
		}

		@Override
		public String getOperationName() {
			return "B  R.L.  A";
		}

		@Override
		protected String getFormatKey() {
			return "rotl2";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * Rather an unary operation, the "A R.R." operation discards its second parameter
	 * when executed and returns its first parameter rotated right by one Bit.
	 */
	A_ROTR {
		@Override
		public int execute(int a, int b) {
			return Integer.rotateRight(a, 1);
		}

		@Override
		public String getOperationName() {
			return "A  R.R.";
		}

		@Override
		protected String getFormatKey() {
			return "rotr1";
		}
	},

	/**
	 * The "A R.R. B" operation returns its first parameter rotated right by {@code b} Bits.
	 */
	A_ROTR_B {
		@Override
		public int execute(int a, int b) {
			return Integer.rotateRight(a, b);
		}

		@Override
		public String getOperationName() {
			return "A  R.R.  B";
		}

		@Override
		protected String getFormatKey() {
			return "rotr2";
		}
	},

	/**
	 * Rather an unary operation, the "B. R.R." operation discards its first parameter
	 * when executed and returns its second parameter rotated right by one Bit.
	 */
	B_ROTR {
		@Override
		public int execute(int a, int b) {
			return Integer.rotateRight(b, 1);
		}

		@Override
		public String getOperationName() {
			return "B  R.R.";
		}

		@Override
		protected String getFormatKey() {
			return "rotr1";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	},

	/**
	 * The "B R.R. A" operation returns its second parameter rotated right by {@code a} Bits.
	 */
	B_ROTR_A {
		@Override
		public int execute(int a, int b) {
			return Integer.rotateRight(b, a);
		}

		@Override
		public String getOperationName() {
			return "B  R.R.  A";
		}

		@Override
		protected String getFormatKey() {
			return "rotr2";
		}

		@Override
		protected boolean swapFormalParameters() {
			return true;
		}
	};

	private String	_description;
	private String	_rtNotation;

	/**
	 * Gets the format key of the operation.
	 *
	 * @return
	 *          the format key of the operation
	 */
	protected abstract String getFormatKey();

	/**
	 * Gets the value of the {@code swapFormalParameters} property.
	 *
	 * @return
	 *          {@code true} if formal parameters will be swapped, {@code false} otherwise
	 */
	protected boolean swapFormalParameters() {
		return false;
	}

	/**
	 * Returns the integer that results from applying the binary operation
	 * to the specified integer parameters.
	 * 
	 * @param a
	 *          the first parameter
	 * @param b
	 *          the second parameter
	 * @return
	 *          the result of the operation
	 */
	public abstract int execute(int a, int b);

	/**
	 * Gets the name of the operation expressed with formal parameters.
	 *
	 * @return
	 *          the name of this operation expressed with formal parameters, e.g. <b>A ADD B</b>
	 */
	public abstract String getOperationName();

	/**
	 * Gets the description of the operation.
	 *
	 * @param resource
	 *          the {@code TextResource} to get the localized description
	 * @return
	 *          the description of this operation, for example <tt>"Multiplies A and B."</tt>
	 */
	public String getDescription(TextResource resource) {
		if (_description == null) {
			String param1 = "A";
			String param2 = "B";
			if (swapFormalParameters()) {
				param1 = "B";
				param2 = "A";
			}
			_description = resource.using("operation").format(getFormatKey() + ".description", param1, param2);
		}
		return _description;
	}

	/**
	 * Gets the register-transfer expression of this operation.
	 *
	 * @param resource
	 *          the {@code TextResource} to get the register-transfer expression
	 * @return
	 *          this operation represented by a register-transfer expression using formal parameters,
	 *          for example <b>0@A[31..1]</b> (unsigned shift right).
	 */
	public String getRtNotation(TextResource resource) {
		if (_rtNotation == null) {
			String param1 = "A";
			String param2 = "B";
			if (swapFormalParameters()) {
				param1 = "B";
				param2 = "A";
			}
			_rtNotation = resource.using("operation").format(getFormatKey() + ".rtn", param1, param2);
		}
		return _rtNotation;
	}

	/**
	 * Gets the register-transfer expression of this operation.
	 *
	 * @param resource
	 *          the {@code TextResource} to get the register-transfer expression
	 * @param param1
	 *          the first parameter of the operation
	 * @param param2
	 *          the second parameter of the operation
	 * @return
	 *          this operation represented by a register-transfer expression using the specified
	 *          parameters, for example <b>0@A[31..1]</b> (unsigned shift right).
	 */
	public String getRtOperation(TextResource resource, String param1, String param2) {
		String p1 = param1;
		String p2 = param2;
		if (swapFormalParameters()) {
			p1 = param2;
			p2 = param1;
		}
		return resource.using("operation").format(getFormatKey() + ".rtn", p1, p2);
	}
}