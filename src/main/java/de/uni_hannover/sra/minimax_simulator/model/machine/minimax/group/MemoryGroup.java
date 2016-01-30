package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.BaseControlPort;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.MemoryLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.*;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.LabelShape;

/**
 * Groups the parts of the memory.
 *
 * @author Martin L&uuml;ck
 */
public class MemoryGroup extends AbstractGroup {

    @Override
    public void initialize(MachineTopology cr, FontMetricsProvider fontProvider) {
        Memory memory = cr.getCircuit(Memory.class, Parts.MEMORY);

        Label csLabel = new Label("CS");
        csLabel.setShape(new LabelShape(fontProvider));

        Label rwLabel = new Label("R/\u00ACW");
        rwLabel.setShape(new LabelShape(fontProvider));

        Label diLabel = new Label("DI");
        diLabel.setShape(new LabelShape(fontProvider));

        Label doLabel = new Label("DO");
        doLabel.setShape(new LabelShape(fontProvider));

        Label adrLabel = new Label("A");
        adrLabel.setShape(new LabelShape(fontProvider));

        Port csPort = BaseControlPort.MEM_CS.port();
        Port rwPort = BaseControlPort.MEM_RW.port();

        Multiplexer mdrSelect = cr.getCircuit(Multiplexer.class, Parts.MDR_SELECT);
        Register mar = cr.getCircuit(Register.class, Parts.MAR);

        Wire csWire = new Wire(2, csPort.getDataOut(), memory.getCs());
        Wire rwWire = new Wire(2, rwPort.getDataOut(), memory.getRw());
        Wire doWire = new Wire(4, memory.getDataOut(), mdrSelect.getDataInputs().get(1));
        Wire adrWire = new Wire(4, mar.getDataOut(), memory.getAdr());

        add(csLabel, Parts.MEMORY_CS + Parts._LABEL);
        add(rwLabel, Parts.MEMORY_RW + Parts._LABEL);
        add(diLabel, Parts.MEMORY_DI + Parts._LABEL);
        add(doLabel, Parts.MEMORY_DO + Parts._LABEL);
        add(adrLabel, Parts.MEMORY_ADR + Parts._LABEL);

        add(csPort, Parts.MEMORY_CS + Parts._PORT);
        add(rwPort, Parts.MEMORY_RW + Parts._PORT);

        addWire(csWire, Parts.MEMORY_CS + Parts._WIRE_PORT);
        addWire(rwWire, Parts.MEMORY_RW + Parts._WIRE_PORT);
        addWire(doWire, Parts.MEMORY_DO + Parts._WIRE);
        addWire(adrWire, Parts.MEMORY_ADR + Parts._WIRE);
    }

    @Override
    public boolean hasLayouts() {
        return true;
    }

    @Override
    public LayoutSet createLayouts() {
        return new MemoryLayoutSet();
    }
}