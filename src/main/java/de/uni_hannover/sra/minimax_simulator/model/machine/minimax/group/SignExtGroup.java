package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.SignExtLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.*;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.CuLabelShape;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.SignExtShape;

public class SignExtGroup extends AbstractGroup
{
	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider)
	{
		SignExtension signExt = new SignExtension("sign ext.");
		signExt.setShape(new SignExtShape(fontProvider));

		Junction irSplitJunction = new Junction();
		irSplitJunction.getDataOuts().add(new OutgoingPin(irSplitJunction));
		irSplitJunction.getDataOuts().add(new OutgoingPin(irSplitJunction));

		ReadablePort irCuPort = new ReadablePort();
		CuLabel irCuLabel = new CuLabel();
		irCuLabel.setShape(new CuLabelShape(fontProvider));

		Register ir = cr.getCircuit(Register.class, Parts.IR);

		Wire irToJunctionWire = new Wire(2, ir.getDataOut(), irSplitJunction.getDataIn());
		Wire junctionToCuWire = new Wire(2, irSplitJunction.getDataOuts().get(0), irCuPort.getIn());
		Wire junctionToSeWire = new Wire(2, irSplitJunction.getDataOuts().get(1), signExt.getDataIn());

		add(signExt, Parts.SIGN_EXTENSION);
		add(irSplitJunction, Parts.IR + Parts._WIRE + Parts._JUNCTION);
		add(irCuPort, Parts.SIGN_EXTENSION + Parts._CU + Parts._PORT);
		add(irCuLabel, Parts.SIGN_EXTENSION + Parts._CU + Parts._LABEL);
		addWire(irToJunctionWire, Parts.SIGN_EXTENSION + Parts._JUNCTION + Parts._WIRE_DATA_IN);
		addWire(junctionToCuWire, Parts.IR + Parts._CU + Parts._WIRE_DATA_IN);
		addWire(junctionToSeWire, Parts.SIGN_EXTENSION + Parts._WIRE_DATA_IN);
	}

	@Override
	public boolean hasLayouts()
	{
		return true;
	}

	@Override
	public LayoutSet createLayouts()
	{
		return new SignExtLayoutSet();
	}
}