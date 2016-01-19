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

/**
 * Groups the parts of a multiplexer.
 *
 * @author Martin L&uuml;ck
 */
public class MultiplexerGroup extends AbstractGroup {

    private final String muxName;
    private final String muxLabel;
    private final boolean topDown;
    private final Port port;

    /**
     * Constructs a new {@code MultiplexerGroup} with the specified multiplexer name,
     * display name (label), {@code top down} property and {@link Port}.
     *
     * @param muxName
     *          the name of the multiplexer
     * @param muxLabel
     *          the display name of the multiplexer
     * @param topDown
     *          whether the group will be displayed top down or not
     * @param port
     *          the {@code Port} of the multiplexer
     */
    public MultiplexerGroup(String muxName, String muxLabel, boolean topDown, Port port) {
        this.muxName = muxName;
        this.muxLabel = muxLabel;
        this.topDown = topDown;
        this.port = port;
    }

    @Override
    public void initialize(MachineTopology cr, FontMetricsProvider fontProvider) {
        Multiplexer mux = cr.getCircuit(Multiplexer.class, muxName);

        Label label = new Label(muxLabel);
        label.setShape(new LabelShape(fontProvider));

        Wire selectWire = new Wire(2, port.getDataOut(), mux.getSelectPin());

        add(label, muxName + Parts._LABEL);
        add(port, muxName + Parts._PORT);
        addWire(selectWire, muxName + Parts._WIRE_SELECT);
    }

    @Override
    public boolean hasLayouts() {
        return true;
    }

    @Override
    public LayoutSet createLayouts() {
        return new MultiplexerLayoutSet(muxName, topDown);
    }
}