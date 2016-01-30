package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.BaseControlPort;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.AluLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.*;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.CuLabelShape;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.LabelShape;

/**
 * The {@code AluGroup} groups all parts of the ALU.
 *
 * @author Martin L&uuml;ck
 */
public class AluGroup extends AbstractGroup {

    @Override
    public void initialize(MachineTopology cr, FontMetricsProvider fontProvider) {
        Alu alu = cr.getCircuit(Alu.class, Parts.ALU);

        Port aluCtrlPort = BaseControlPort.ALU_CTRL.port();

        Label aluCtrlLabel = new Label("ALU.Ctrl");
        aluCtrlLabel.setShape(new LabelShape(fontProvider));

        Wire aluCtrlWire = new Wire(2, aluCtrlPort.getDataOut(), alu.getInCtrl());

        CuLabel aluCuLabel = new CuLabel();
        aluCuLabel.setShape(new CuLabelShape(fontProvider));

        ReadablePort aluCondPort = new ReadablePort();

        Wire aluCondWire = new Wire(3, alu.getOutZero(), aluCondPort.getIn());

        Label aluResultLabel = new Label("ALUresult");
        Label aluZeroLabel = new Label("ALUresult == 0");

        aluResultLabel.setShape(new LabelShape(fontProvider));
        aluZeroLabel.setShape(new LabelShape(fontProvider));

        add(aluCtrlPort, Parts.ALU + Parts._PORT);
        add(aluCtrlLabel, Parts.ALU + Parts._LABEL);
        addWire(aluCtrlWire, Parts.ALU + Parts._WIRE_CTRL);
        add(aluCuLabel, Parts.ALU_COND_PORT + Parts._LABEL);
        add(aluCondPort, Parts.ALU_COND_PORT);
        addWire(aluCondWire, Parts.ALU + Parts._CU + Parts._WIRE);
        add(aluResultLabel, Parts.ALU_DATA_OUT + Parts._LABEL);
        add(aluZeroLabel, Parts.ALU_COND_OUT + Parts._LABEL);
    }

    @Override
    public boolean hasLayouts() {
        return true;
    }

    @Override
    public LayoutSet createLayouts() {
        return new AluLayoutSet();
    }
}