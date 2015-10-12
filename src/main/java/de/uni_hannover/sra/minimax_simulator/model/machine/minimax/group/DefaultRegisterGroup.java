package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.DefaultRegisterLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.*;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.LabelShape;

public class DefaultRegisterGroup extends AbstractGroup
{
	private final String  _registerId;

	public DefaultRegisterGroup(String registerId)
	{
		_registerId = registerId;
	}

	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider)
	{
		Register register = cr.getCircuit(Register.class, _registerId);

		Junction junction = new Junction();
		junction.getDataOuts().add(new OutgoingPin(junction));
		// For now, don't chain the register junctions, but make a new cable to each of them
		// just create an empty port for the looks
		junction.getDataOuts().add(new OutgoingPin(junction));

		Label label = new Label(register.getLabel() + ".W");
		label.setShape(new LabelShape(fontProvider));

		Port port = new Port(register.getLabel() + ".W");

		Wire aluWire = new Wire(3, cr.getCircuit(Alu.class, Parts.ALU).getOutData(), junction.getDataIn());
		Wire dataInWire = new Wire(2, junction.getDataOuts().get(0), register.getDataIn());
		Wire enabledWire = new Wire(2, port.getDataOut(), register.getWriteEnabled());

		add(junction, _registerId + Parts._JUNCTION);
		add(label, _registerId + Parts._LABEL);
		add(port, _registerId + Parts._PORT);

		//addVirtual(_registerId + _OUT_JUNCTION + _ANCHOR);

		addWire(aluWire, _registerId + Parts._JUNCTION + Parts._WIRE_DATA_IN);
		addWire(dataInWire, _registerId + Parts._WIRE_DATA_IN);
		addWire(enabledWire, _registerId + Parts._WIRE_ENABLED);
	}

	@Override
	public boolean hasLayouts()
	{
		return true;
	}

	@Override
	public LayoutSet createLayouts()
	{
		return new DefaultRegisterLayoutSet(_registerId);
	}
}