package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.BaseRegisterOutWireLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Memory;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.OutgoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Register;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.SignExtension;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;

public class BaseRegisterOutWireGroup extends AbstractGroup
{
	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider)
	{
		initMdr(cr);
		initIr(cr);
		initPc(cr);
	}

	private void initMdr(MachineTopology cr)
	{
		Register mdr = cr.getCircuit(Register.class, Parts.MDR);
		Memory memory = cr.getCircuit(Memory.class, Parts.MEMORY);

		Junction mdrOutJunction = new Junction();

		Junction mdrMemJunction = new Junction();
		mdrMemJunction.getDataOuts().add(new OutgoingPin(mdrMemJunction));
		mdrMemJunction.getDataOuts().add(new OutgoingPin(mdrMemJunction));

		Wire mdrMemJunctionWire = new Wire(2, mdr.getDataOut(), mdrMemJunction.getDataIn());
		Wire mdrJunctionOutWire = new Wire(2, mdrMemJunction.getDataOuts().get(0), mdrOutJunction.getDataIn());
		Wire mdrMemDiWire = new Wire(3, mdrMemJunction.getDataOuts().get(1), memory.getDataIn());

		add(mdrOutJunction, Parts.MDR + Parts._OUT_JUNCTION);
		add(mdrMemJunction, Parts.MEMORY + Parts._JUNCTION);
		addWire(mdrMemJunctionWire, Parts.MDR + Parts._WIRE_DATA_OUT);
		addWire(mdrJunctionOutWire, Parts.MDR + Parts._OUT_JUNCTION + Parts._WIRE_DATA_IN);
		addWire(mdrMemDiWire, Parts.MEMORY + Parts._WIRE_DATA_IN);
	}

	private void initIr(MachineTopology cr)
	{
		SignExtension signExt = cr.getCircuit(SignExtension.class, Parts.SIGN_EXTENSION);
		Junction irOutJunction = new Junction();
		Wire irOutWire = new Wire(3, signExt.getDataOut(), irOutJunction.getDataIn());

		add(irOutJunction, Parts.IR + Parts._OUT_JUNCTION);
		addWire(irOutWire, Parts.IR + Parts._WIRE_DATA_OUT);
	}

	private void initPc(MachineTopology cr)
	{
		Register pc = cr.getCircuit(Register.class, Parts.PC);
		Junction pcOutJunction = new Junction();
		Wire pcOutWire = new Wire(2, pc.getDataOut(), pcOutJunction.getDataIn());

		add(pcOutJunction, Parts.PC + Parts._OUT_JUNCTION);
		addWire(pcOutWire, Parts.PC + Parts._WIRE_DATA_OUT);
	}

	@Override
	public boolean hasLayouts()
	{
		return true;
	}

	@Override
	public LayoutSet createLayouts()
	{
		return new BaseRegisterOutWireLayoutSet();
	}
}