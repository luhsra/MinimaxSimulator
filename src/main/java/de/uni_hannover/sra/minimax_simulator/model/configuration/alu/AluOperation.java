package de.uni_hannover.sra.minimax_simulator.model.configuration.alu;

import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

/**
 * An enumeration of binary integer operations available to a typical ALU of a register machine.
 * 
 * @author Martin
 */
public enum AluOperation
{
	/**
	 * Rather an unary operation, the "Transfer A" operation discards its second parameter
	 * when executed and returns its first parameter.
	 */
	TRANS_A
	{
		@Override
		public int execute(int a, int b)
		{
			return a;
		}

		@Override
		public String getOperationName()
		{
			return "TRANSFER A";
		}

		@Override
		protected String getFormatKey()
		{
			return "transfer";
		}
	},

	/**
	 * Rather an unary operation, the "Transfer B" operation discards its first parameter
	 * when executed and returns its second parameter.
	 */
	TRANS_B
	{
		@Override
		public int execute(int a, int b)
		{
			return b;
		}

		@Override
		public String getOperationName()
		{
			return "TRANSFER B";
		}

		@Override
		protected String getFormatKey()
		{
			return "transfer";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},

	A_ADD_B
	{
		@Override
		public int execute(int a, int b)
		{
			return a + b;
		}

		@Override
		public String getOperationName()
		{
			return "A  ADD  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "add";
		}
	},

	A_SUB_B
	{
		@Override
		public int execute(int a, int b)
		{
			return a - b;
		}

		@Override
		public String getOperationName()
		{
			return "A  SUB  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "sub";
		}
	},
	B_SUB_A
	{
		@Override
		public int execute(int a, int b)
		{
			return b - a;
		}

		@Override
		public String getOperationName()
		{
			return "B  SUB  A";
		}

		@Override
		protected String getFormatKey()
		{
			return "sub";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},

	A_INC
	{
		@Override
		public int execute(int a, int b)
		{
			return a + 1;
		}

		@Override
		public String getOperationName()
		{
			return "INC  A";
		}

		@Override
		protected String getFormatKey()
		{
			return "inc";
		}
	},

	B_INC
	{
		@Override
		public int execute(int a, int b)
		{
			return b + 1;
		}

		@Override
		public String getOperationName()
		{
			return "INC  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "inc";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},

	A_DEC
	{
		@Override
		public int execute(int a, int b)
		{
			return a - 1;
		}

		@Override
		public String getOperationName()
		{
			return "DEC  A";
		}

		@Override
		protected String getFormatKey()
		{
			return "dec";
		}
	},

	B_DEC
	{
		@Override
		public int execute(int a, int b)
		{
			return b - 1;
		}

		@Override
		public String getOperationName()
		{
			return "DEC  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "dec";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},

	A_MUL_B
	{
		@Override
		public int execute(int a, int b)
		{
			return a * b;
		}

		@Override
		public String getOperationName()
		{
			return "A  MUL  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "mul";
		}
	},

	A_DIV_B
	{
		@Override
		public int execute(int a, int b)
		{
			return b == 0 ? 0 : a / b;
		}

		@Override
		public String getOperationName()
		{
			return "A  DIV  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "div";
		}
	},
	B_DIV_A
	{
		@Override
		public int execute(int a, int b)
		{
			return a == 0 ? 0 : b / a;
		}

		@Override
		public String getOperationName()
		{
			return "B  DIV  A";
		}

		@Override
		protected String getFormatKey()
		{
			return "div";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},

	A_MOD_B
	{
		@Override
		public int execute(int a, int b)
		{
			return b == 0 ? 0 : a % b;
		}

		@Override
		public String getOperationName()
		{
			return "A  MOD  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "mod";
		}
	},
	B_MOD_A
	{
		@Override
		public int execute(int a, int b)
		{
			return a == 0 ? 0 : b % a;
		}

		@Override
		public String getOperationName()
		{
			return "B  MOD  A";
		}

		@Override
		protected String getFormatKey()
		{
			return "mod";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},

	A_AND_B
	{
		@Override
		public int execute(int a, int b)
		{
			return a & b;
		}

		@Override
		public String getOperationName()
		{
			return "A  AND  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "and";
		}
	},
	A_OR_B
	{
		@Override
		public int execute(int a, int b)
		{
			return a | b;
		}

		@Override
		public String getOperationName()
		{
			return "A  OR  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "or";
		}
	},
	A_XOR_B
	{
		@Override
		public int execute(int a, int b)
		{
			return a ^ b;
		}

		@Override
		public String getOperationName()
		{
			return "A  XOR  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "xor";
		}
	},
	A_INV
	{
		@Override
		public int execute(int a, int b)
		{
			return ~a;
		}

		@Override
		public String getOperationName()
		{
			return "INV  A";
		}

		@Override
		protected String getFormatKey()
		{
			return "inv";
		}
	},
	B_INV
	{
		@Override
		public int execute(int a, int b)
		{
			return ~b;
		}

		@Override
		public String getOperationName()
		{
			return "INV  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "inv";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},

	A_SL
	{
		@Override
		public int execute(int a, int b)
		{
			return a << 1;
		}

		@Override
		public String getOperationName()
		{
			return "A  S.L.";
		}

		@Override
		protected String getFormatKey()
		{
			return "sl1";
		}
	},
	A_SL_B
	{
		@Override
		public int execute(int a, int b)
		{
			return a << b;
		}

		@Override
		public String getOperationName()
		{
			return "A  S.L.  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "sl2";
		}
	},
	B_SL
	{
		@Override
		public int execute(int a, int b)
		{
			return b << 1;
		}

		@Override
		public String getOperationName()
		{
			return "B  S.L.";
		}

		@Override
		protected String getFormatKey()
		{
			return "sl1";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},
	B_SL_A
	{
		@Override
		public int execute(int a, int b)
		{
			return b << a;
		}

		@Override
		public String getOperationName()
		{
			return "B  S.L.  A";
		}

		@Override
		protected String getFormatKey()
		{
			return "sl2";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},
	A_SR
	{
		@Override
		public int execute(int a, int b)
		{
			return a >> 1;
		}

		@Override
		public String getOperationName()
		{
			return "A  S.R.";
		}

		@Override
		protected String getFormatKey()
		{
			return "sr1";
		}
	},
	A_SR_B
	{
		@Override
		public int execute(int a, int b)
		{
			return a >> b;
		}

		@Override
		public String getOperationName()
		{
			return "A  S.R.  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "sr2";
		}
	},
	B_SR
	{
		@Override
		public int execute(int a, int b)
		{
			return b >> 1;
		}

		@Override
		public String getOperationName()
		{
			return "B  S.R.";
		}

		@Override
		protected String getFormatKey()
		{
			return "sr1";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},
	B_SR_A
	{
		@Override
		public int execute(int a, int b)
		{
			return b >> a;
		}

		@Override
		public String getOperationName()
		{
			return "B  S.R.  A";
		}

		@Override
		protected String getFormatKey()
		{
			return "sr2";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},

	A_SRU
	{
		@Override
		public int execute(int a, int b)
		{
			return a >>> 1;
		}

		@Override
		public String getOperationName()
		{
			return "A  S.R.U.";
		}

		@Override
		protected String getFormatKey()
		{
			return "sru1";
		}
	},
	A_SRU_B
	{
		@Override
		public int execute(int a, int b)
		{
			return a >>> b;
		}

		@Override
		public String getOperationName()
		{
			return "A  S.R.U.  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "sru2";
		}
	},
	B_SRU
	{
		@Override
		public int execute(int a, int b)
		{
			return b >>> 1;
		}

		@Override
		public String getOperationName()
		{
			return "B  S.R.U.";
		}

		@Override
		protected String getFormatKey()
		{
			return "sru1";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},
	B_SRU_A
	{
		@Override
		public int execute(int a, int b)
		{
			return b >>> a;
		}

		@Override
		public String getOperationName()
		{
			return "B  S.R.U.  A";
		}

		@Override
		protected String getFormatKey()
		{
			return "sru2";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},

	// like shift, but rotate MSB to LSB
	A_ROTL
	{
		@Override
		public int execute(int a, int b)
		{
			return Integer.rotateLeft(a, 1);
		}

		@Override
		public String getOperationName()
		{
			return "A  R.L.";
		}

		@Override
		protected String getFormatKey()
		{
			return "rotl1";
		}
	},
	A_ROTL_B
	{
		@Override
		public int execute(int a, int b)
		{
			return Integer.rotateLeft(a, b);
		}

		@Override
		public String getOperationName()
		{
			return "A  R.L.  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "rotl2";
		}
	},
	B_ROTL
	{
		@Override
		public int execute(int a, int b)
		{
			return Integer.rotateLeft(b, 1);
		}

		@Override
		public String getOperationName()
		{
			return "B  R.L.";
		}

		@Override
		protected String getFormatKey()
		{
			return "rotl1";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},
	B_ROTL_A
	{
		@Override
		public int execute(int a, int b)
		{
			return Integer.rotateLeft(b, a);
		}

		@Override
		public String getOperationName()
		{
			return "B  R.L.  A";
		}

		@Override
		protected String getFormatKey()
		{
			return "rotl2";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},
	// like shift, but rotate LSB to MSB
	A_ROTR
	{
		@Override
		public int execute(int a, int b)
		{
			return Integer.rotateRight(a, 1);
		}

		@Override
		public String getOperationName()
		{
			return "A  R.R.";
		}

		@Override
		protected String getFormatKey()
		{
			return "rotr1";
		}
	},
	A_ROTR_B
	{
		@Override
		public int execute(int a, int b)
		{
			return Integer.rotateRight(a, b);
		}

		@Override
		public String getOperationName()
		{
			return "A  R.R.  B";
		}

		@Override
		protected String getFormatKey()
		{
			return "rotr2";
		}
	},
	B_ROTR
	{
		@Override
		public int execute(int a, int b)
		{
			return Integer.rotateRight(b, 1);
		}

		@Override
		public String getOperationName()
		{
			return "B  R.R.";
		}

		@Override
		protected String getFormatKey()
		{
			return "rotr1";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	},
	B_ROTR_A
	{
		@Override
		public int execute(int a, int b)
		{
			return Integer.rotateRight(b, a);
		}

		@Override
		public String getOperationName()
		{
			return "B  R.R.  A";
		}

		@Override
		protected String getFormatKey()
		{
			return "rotr2";
		}

		@Override
		protected boolean swapFormalParameters()
		{
			return true;
		}
	};

	private String	_description;
	private String	_rtNotation;

	protected abstract String getFormatKey();

	protected boolean swapFormalParameters()
	{
		return false;
	}

	/**
	 * Returns the integer that results from applying the binary operation
	 * to the given integer parameters.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public abstract int execute(int a, int b);

	/**
	 * @return the name of this operation expressed with formal parameters, for example <b>A ADD
	 *         B</b>
	 */
	public abstract String getOperationName();

	/**
	 * @return the description of this operation, for example <tt>"Multiplies A and B."</tt>
	 */
	public String getDescription(TextResource resource)
	{
		if (_description == null)
		{
			String param1 = "A";
			String param2 = "B";
			if (swapFormalParameters())
			{
				param1 = "B";
				param2 = "A";
			}
			_description = resource.using("operation").format(
				getFormatKey() + ".description", param1, param2);
		}
		return _description;
	}

	/**
	 * @return this operation represented by a register-transfer expression using formal parameters,
	 *         for example <b>0@A[31..1]</b> (unsigned shift right).
	 */
	public String getRtNotation(TextResource resource)
	{
		if (_rtNotation == null)
		{
			String param1 = "A";
			String param2 = "B";
			if (swapFormalParameters())
			{
				param1 = "B";
				param2 = "A";
			}
			_rtNotation = resource.using("operation").format(getFormatKey() + ".rtn",
				param1, param2);
		}
		return _rtNotation;
	}

	/**
	 * @return this operation represented by a register-transfer expression using the given
	 *         parameters, for example <b>0@A[31..1]</b> (unsigned shift right).
	 */
	public String getRtOperation(TextResource resource, String param1, String param2)
	{
		String p1 = param1;
		String p2 = param2;
		if (swapFormalParameters())
		{
			p1 = param2;
			p2 = param1;
		}

		return resource.using("operation").format(getFormatKey() + ".rtn", p1, p2);
	}
}