package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.ExtendedRegisterLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.OutgoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Port;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Register;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.LabelShape;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.RegisterShape;

public class ExtendedRegisterGroup extends AbstractGroup
{
	private final String	_registerId;
	private final String	_label;

	public ExtendedRegisterGroup(String registerId, String label)
	{
		_registerId = registerId;
		_label = label;
	}

	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider)
	{
		Register register = new Register(_label, RegisterSize.BITS_32, true);
		register.setName(_registerId);
		register.setShape(new RegisterShape(fontProvider));

		Junction junction = new Junction();
		junction.getDataOuts().add(new OutgoingPin(junction));
		// For now, don't chain the register junctions, but make a new cable to each of them
		// just create an empty port for the looks
		junction.getDataOuts().add(new OutgoingPin(junction));

		Label label = new Label(register.getLabel() + ".W");
		label.setShape(new LabelShape(fontProvider));

		Port port = new Port(_label + ".W");

		Wire aluWire = new Wire(3, cr.getCircuit(Alu.class, Parts.ALU).getOutData(),
			junction.getDataIn());
		Wire dataInWire = new Wire(2, junction.getDataOuts().get(0), register.getDataIn());
		Wire enabledWire = new Wire(2, port.getDataOut(), register.getWriteEnabled());

		add(register, _registerId);
		add(junction, _registerId + Parts._JUNCTION);
		add(label, _registerId + Parts._LABEL);
		add(port, _registerId + Parts._PORT);

		addWire(aluWire, _registerId + Parts._JUNCTION + Parts._WIRE_DATA_IN);
		addWire(dataInWire, _registerId + Parts._WIRE_DATA_IN);
		addWire(enabledWire, _registerId + Parts._WIRE_ENABLED);

		Junction outJunction = new Junction();
		Wire registerOutWire = new Wire(2, register.getDataOut(), outJunction.getDataIn());

		add(outJunction, _registerId + Parts._OUT_JUNCTION);
		addVirtual(_registerId + Parts._OUT_JUNCTION + Parts._ANCHOR);
		addWire(registerOutWire, _registerId + Parts._WIRE_DATA_OUT);
	}

	@Override
	public boolean hasLayouts()
	{
		return true;
	}

	@Override
	public LayoutSet createLayouts()
	{
		return new ExtendedRegisterLayoutSet(_registerId);
	}
}