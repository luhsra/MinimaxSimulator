package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.MultiplexerLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Multiplexer;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Port;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.LabelShape;

public class MultiplexerGroup extends AbstractGroup
{
	private final String		_muxName;
	private final String		_muxLabel;
	private final boolean		_topDown;
	private final Port			_port;

	public MultiplexerGroup(String muxName, String muxLabel, boolean topDown, Port port)
	{
		_muxName = muxName;
		_muxLabel = muxLabel;
		_topDown = topDown;
		_port = port;
	}

	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider)
	{
		Multiplexer mux = cr.getCircuit(Multiplexer.class, _muxName);

		Label label = new Label(_muxLabel);
		label.setShape(new LabelShape(fontProvider));

		Wire selectWire = new Wire(2, _port.getDataOut(), mux.getSelectPin());

		add(label, _muxName + Parts._LABEL);
		add(_port, _muxName + Parts._PORT);
		addWire(selectWire, _muxName + Parts._WIRE_SELECT);
	}

	@Override
	public boolean hasLayouts()
	{
		return true;
	}

	@Override
	public LayoutSet createLayouts()
	{
		return new MultiplexerLayoutSet(_muxName, _topDown);
	}
}