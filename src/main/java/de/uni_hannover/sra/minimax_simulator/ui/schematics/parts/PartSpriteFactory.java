package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Memory;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Multiplexer;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Port;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Register;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.render.SpriteFactory;

public class PartSpriteFactory implements SpriteFactory<Circuit>
{
	@Override
	public Sprite createSprite(Circuit circuit)
	{
		if (circuit instanceof Register)
		{
			return new RegisterSprite((Register) circuit);
		}
		if (circuit instanceof Alu)
		{
			return new AluSprite((Alu) circuit);
		}
		if (circuit instanceof Multiplexer)
		{
			return new MultiplexerSprite((Multiplexer) circuit);
		}
		if (circuit instanceof Wire)
		{
			return new WireSprite((Wire) circuit);
		}
		if (circuit instanceof Memory)
		{
			return new MemorySprite((Memory) circuit);
		}
		if (circuit instanceof Junction)
		{
			return new JunctionSprite((Junction) circuit);
		}
		if (circuit instanceof Port)
		{
			return new EmptySprite();
		}
		if (circuit instanceof Label)
		{
			return new LabelSprite((Label) circuit);
		}
		throw new IllegalArgumentException("No sprite class for object: " + circuit);
	}
}