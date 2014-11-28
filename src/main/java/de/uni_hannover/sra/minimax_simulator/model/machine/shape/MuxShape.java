package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import de.uni_hannover.sra.minimax_simulator.layout.Component;
import de.uni_hannover.sra.minimax_simulator.layout.ComponentShape;
import de.uni_hannover.sra.minimax_simulator.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.layout.Insets;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Multiplexer;

public class MuxShape implements ComponentShape
{
	public final static int	MUX_WIDTH			= 20;
	public final static int	MUX_HEIGHT_PER_PIN	= 18;
	public final static int	MUX_CORNER_HEIGHT	= 20;
	public final static int	MUX_CORNER_SPACING	= 10;

	public final static int	MUX_SPACING			= 15;

	private final Insets	_insets				= new Insets(MUX_SPACING, MUX_SPACING, 0,
													0);

	@Override
	public void updateShape(Component component)
	{
		Multiplexer mux = (Multiplexer) component;

		int height;

		int size = mux.getDataInputs().size();
		if (size > 0)
		{
			height = (size - 1) * MUX_HEIGHT_PER_PIN
				+ MUX_CORNER_SPACING * 2;
		}
		else
		{
			height = MUX_CORNER_SPACING * 2;
		}

		mux.setDimension(new Dimension(MUX_WIDTH, height));
		mux.setInsets(_insets);
	}

	@Override
	public void layout(Component component)
	{
	}
}