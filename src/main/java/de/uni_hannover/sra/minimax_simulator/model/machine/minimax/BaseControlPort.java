package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.Port;

public enum BaseControlPort
{
	ALU_SELECT_A,
	ALU_SELECT_B,
	MDR_SEL,
	MEM_CS,
	MEM_RW,
	ALU_CTRL;

	private final Port _port;

	private BaseControlPort()
	{
		_port = new Port(this.name());
	}

	public Port port()
	{
		return _port;
	}
}