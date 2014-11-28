package de.uni_hannover.sra.minimax_simulator.model.configuration.register;

public enum RegisterSize
{
	BITS_32
	{
		@Override
		public String getName()
		{
			return "32 Bits";
		}

		@Override
		public String getHexFormat()
		{
			return "0x%08X";
		}

		@Override
		public int getBitMask()
		{
			return 0xFFFFFFFF;
		}
	},
	BITS_24
	{
		@Override
		public String getName()
		{
			return "24 Bits";
		}

		@Override
		public String getHexFormat()
		{
			return "0x%06X";
		}

		@Override
		public int getBitMask()
		{
			return 0x00FFFFFF;
		}
	};

	public abstract String getName();

	public abstract String getHexFormat();

	public abstract int getBitMask();
}