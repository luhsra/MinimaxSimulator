package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.ExtendedRegisterLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.*;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.LabelShape;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.RegisterShape;

/**
 * Groups the parts of an extended register.
 *
 * @author Martin L&uuml;ck
 */
public class ExtendedRegisterGroup extends AbstractGroup {

    private final String registerId;
    private final String label;

    /**
     * Constructs a new {@code ExtendedRegisterGroup} with the specified register ID and label.
     *
     * @param registerId
     *          the ID of the register
     * @param label
     *          the label of the group
     */
    public ExtendedRegisterGroup(String registerId, String label) {
        this.registerId = registerId;
        this.label = label;
    }

    @Override
    public void initialize(MachineTopology cr, FontMetricsProvider fontProvider) {
        Register register = new Register(label, RegisterSize.BITS_32, true);
        register.setName(registerId);
        register.setShape(new RegisterShape(fontProvider));

        Junction junction = new Junction();
        junction.getDataOuts().add(new OutgoingPin(junction));
        // For now, don't chain the register junctions, but make a new cable to each of them
        // just create an empty port for the looks
        junction.getDataOuts().add(new OutgoingPin(junction));

        Label label = new Label(register.getLabel() + ".W");
        label.setShape(new LabelShape(fontProvider));

        Port port = new Port(this.label + ".W");

        Wire aluWire = new Wire(3, cr.getCircuit(Alu.class, Parts.ALU).getOutData(),
            junction.getDataIn());
        Wire dataInWire = new Wire(2, junction.getDataOuts().get(0), register.getDataIn());
        Wire enabledWire = new Wire(2, port.getDataOut(), register.getWriteEnabled());

        add(register, registerId);
        add(junction, registerId + Parts._JUNCTION);
        add(label, registerId + Parts._LABEL);
        add(port, registerId + Parts._PORT);

        addWire(aluWire, registerId + Parts._JUNCTION + Parts._WIRE_DATA_IN);
        addWire(dataInWire, registerId + Parts._WIRE_DATA_IN);
        addWire(enabledWire, registerId + Parts._WIRE_ENABLED);

        Junction outJunction = new Junction();
        Wire registerOutWire = new Wire(2, register.getDataOut(), outJunction.getDataIn());

        add(outJunction, registerId + Parts._OUT_JUNCTION);
        addVirtual(registerId + Parts._OUT_JUNCTION + Parts._ANCHOR);
        addWire(registerOutWire, registerId + Parts._WIRE_DATA_OUT);
    }

    @Override
    public boolean hasLayouts() {
        return true;
    }

    @Override
    public LayoutSet createLayouts() {
        return new ExtendedRegisterLayoutSet(registerId);
    }
}