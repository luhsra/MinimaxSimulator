package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.MdrLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.*;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.LabelShape;

public class MdrRegisterGroup extends AbstractGroup
{
	private final String _registerId;

	public MdrRegisterGroup(String registerId)
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

		Port port = new Port(Parts.MDR + ".W");

		Multiplexer mdrSelect = cr.getCircuit(Multiplexer.class, Parts.MDR_SELECT);
		Alu alu = cr.getCircuit(Alu.class, Parts.ALU);

		Wire dataInWire = new Wire(2, mdrSelect.getDataOut(), register.getDataIn());
		Wire enabledWire = new Wire(2, port.getDataOut(), register.getWriteEnabled());
		Wire aluWire = new Wire(3, alu.getOutData(), junction.getDataIn());
		Wire mdrSelectInput0Wire = new Wire(2, junction.getDataOuts().get(0),
			mdrSelect.getDataInputs().get(0));

		add(junction, _registerId + Parts._JUNCTION);
		add(label, _registerId + Parts._LABEL);
		add(port, _registerId + Parts._PORT);

		addWire(dataInWire, _registerId + Parts._WIRE_DATA_IN);
		addWire(enabledWire, _registerId + Parts._WIRE_ENABLED);
		addWire(aluWire, _registerId + Parts._JUNCTION + Parts._WIRE_DATA_IN);
		addWire(mdrSelectInput0Wire, _registerId + "_WIRE_MDR_SELECT_INPUT0");
	}

	@Override
	public boolean hasLayouts()
	{
		return true;
	}

	@Override
	public LayoutSet createLayouts()
	{
		return new MdrLayoutSet(_registerId);
	}
}