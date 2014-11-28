package de.uni_hannover.sra.minimax_simulator.layout.constraint;

class ConstrainedArea extends AbstractAttributeOwner
{
	public ConstrainedArea(String name)
	{
		super(name);
	}

	@Override
	public int getPreferredWidth()
	{
		return 0;
	}

	@Override
	public int getPreferredHeight()
	{
		return 0;
	}
}