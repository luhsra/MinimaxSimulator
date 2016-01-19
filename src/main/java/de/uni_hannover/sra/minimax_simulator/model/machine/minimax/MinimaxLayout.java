package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.GroupLayout;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.OriginLayout;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.StackLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Memory;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Multiplexer;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.AluShape;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.MemoryShape;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.MuxShape;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Insets;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.*;

import java.util.Arrays;

/**
 * Represents the layout of a Minimax machine.
 *
 * @see Layout
 * @see MinimaxMachine
 *
 * @author Martin L&uuml;ck
 */
class MinimaxLayout {

    private final ConstraintContainer container;
    private final LayoutManager layout;

    private Dimension dimension;

    /**
     * Constructs a new and empty {@code MinimaxLayout}.
     */
    public MinimaxLayout() {
        container = new ConstraintContainer();
        layout = new DefaultLayoutManager(container);
    }

    /**
     * Gets the {@link Dimension} of the {@code MinimaxLayout}.
     *
     * @return
     *          the {@code Dimension} of the {@code MinimaxLayout}
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * Gets the {@link ConstraintContainer} of the {@code MinimaxLayout}.
     *
     * @return
     *          the {@code ConstraintContainer} of the {@code MinimaxLayout}
     */
    public ConstraintContainer getContainer() {
        return container;
    }

    /**
     * Updates the {@code MinimaxLayout}.
     */
    public void updateLayout() {
        container.updateSize();

        dimension = container.getDimension();
        Insets in = container.getInsets();

        container.setBounds(new Bounds(in.l, in.t, dimension.w, dimension.h));
        container.doLayout();
    }

    /**
     * Initializes the {@link Layout}s of the {@link de.uni_hannover.sra.minimax_simulator.model.machine.part.Part}s
     * provided by the specified {@link MachineTopology}.
     *
     * @param cr
     *          the {@code MachineTopology} of the {@link MinimaxMachine}
     * @param fontProvider
     *          a {@link FontMetricsProvider} for rendering text
     */
    public void initPartLayouts(MachineTopology cr, FontMetricsProvider fontProvider) {
        // layout for base registers
        putLayouts(new StackLayoutSet(Parts.MEMORY, Arrays.asList(Parts.MAR, Parts.MDR, Parts.IR, Parts.PC, Parts.ACCU),
                Arrays.asList(65, 45, 70, 80, 45), Parts.GROUP_BASE_REGISTERS));

        putLayout(Parts.GROUP_ALL_REGISTERS, new GroupLayout(Arrays.asList(Parts.GROUP_BASE_REGISTERS, Parts.GROUP_EXTENDED_REGISTERS)));

        alignMemory(cr);
        alignMdrSelect(cr);
        alignMultiplexers(cr);
        alignAlu(cr);

        putLayout(Parts.GROUP_MUX_LABEL,
            new GroupLayout(Arrays.asList(Parts.MUX_A + Parts._LABEL, Parts.MUX_B + Parts._LABEL)));
        putLayout(Parts.GROUP_MUX_LINE,
            new GroupLayout(Arrays.asList(Parts.GROUP_MUX_LABEL, Parts.GROUP_MUX_CONSTANTS)));

        putLayout(Parts.GROUP_MUX_BASE_REGISTERS,
            new GroupLayout(Arrays.asList(Parts.MDR + Parts._OUT_JUNCTION, Parts.IR + Parts._OUT_JUNCTION, Parts.PC
                + Parts._OUT_JUNCTION, Parts.ACCU + Parts._OUT_JUNCTION)));
    }

    /**
     * Adds all virtual and non-virtual {@link Component}s of the specified {@link Group}.
     *
     * @param group
     *          the {@code Group}
     */
    public void addGroup(Group group) {
        for (Component component : group.getComponents()) {
            container.addComponent(component, group.getName(component));
        }
        group.getVirtualComponents().forEach(container::addVirtualComponent);
    }

    /**
     * Removes all virtual and non-virtal {@link Component}s of the specified {@link Group}.
     *
     * @param group
     *          the {@code Group}
     */
    public void removeGroup(Group group) {
        group.getComponents().forEach(container::removeComponent);
        group.getVirtualComponents().forEach(container::removeComponent);
    }

    /**
     * Puts the specified {@link Layout} to the list of {@code Layout}s with the specified name.
     *
     * @param name
     *          the name of the {@code Layout}
     * @param layout
     *          the {@code Layout} to add
     */
    public void putLayout(String name, Layout layout) {
        this.layout.putLayout(name, layout);
    }

    /**
     * Removes the {@link Layout} with the specified name.
     *
     * @param name
     *          the name of the {@code Layout} to remove
     */
    public void removeLayout(String name) {
        layout.removeLayout(name);
    }

    /**
     * Updates the {@link Layout} with the specified name.
     *
     * @param name
     *          the name of the {@code Layout} to update
     */
    public void updateLayout(String name) {
        layout.updateLayout(name);
    }

    /**
     * Puts the {@link Layout}s of the specified {@link LayoutSet} to the list of {@code Layout}s.
     *
     * @param set
     *          the {@code LayoutSet}
     */
    public void putLayouts(LayoutSet set) {
        for (String name : set.getComponents()) {
            putLayout(name, set.getLayout(name));
        }
    }

    /**
     * Removes the {@link Layout}s of the specified {@link LayoutSet}.
     *
     * @param set
     *          the {@code LayoutSet}
     */
    public void removeLayouts(LayoutSet set) {
        set.getComponents().forEach(this::removeLayout);
    }

    /**
     * Adds the specified {@link Wire} with the specified name.
     *
     * @param wire
     *          the {@code Wire}
     * @param name
     *          the name of the {@code Wire}
     */
    private void addWire(Wire wire, String name) {
        int index = 0;
        for (Component component : wire.createWireComponents()) {
            container.addComponent(component, name + "." + index);
            index++;
        }
    }

    /**
     * Aligns the layouts of the register MDR.
     *
     * @param cr
     *          the machine's topology
     */
    private void alignMdrSelect(MachineTopology cr) {
        ConstraintContainer c = container;

        Multiplexer mdrSel = cr.getCircuit(Multiplexer.class, Parts.MDR_SELECT);
        mdrSel.setShape(new MuxShape());

        c.addComponent(mdrSel.getDataOut(), Parts.MDR_SELECT_OUT);
        c.addComponent(mdrSel.getDataInputs().get(0), Parts.MDR_SELECT_IN_0);
        c.addComponent(mdrSel.getDataInputs().get(1), Parts.MDR_SELECT_IN_1);
        c.addComponent(mdrSel.getSelectPin(), Parts.MDR_SELECT_SELECT);

        ConstraintFactory f = c.createConstraintFactory();

        // MDR select
        f.alignVertically(Parts.MDR_SELECT, Parts.MDR);
        f.right(Parts.MDR_SELECT, Parts.GROUP_BASE_REGISTERS, 70);

        // MDR select data out
        f.alignVertically(Parts.MDR_SELECT_OUT, Parts.MDR_SELECT);
        f.left(Parts.MDR_SELECT_OUT, Parts.MDR_SELECT);

        f.right(Parts.MDR_SELECT_IN_0, Parts.MDR_SELECT);
        f.right(Parts.MDR_SELECT_IN_1, Parts.MDR_SELECT);

        f.above(Parts.MDR_SELECT_IN_0, Parts.MDR_SELECT, -MuxShape.MUX_CORNER_SPACING);
        f.below(Parts.MDR_SELECT_IN_1, Parts.MDR_SELECT, -MuxShape.MUX_CORNER_SPACING);

        f.alignHorizontally(Parts.MDR_SELECT_SELECT, Parts.MDR_SELECT);
        f.above(Parts.MDR_SELECT_SELECT, Parts.MDR_SELECT);
    }

    /**
     * Aligns the layouts of the multiplexers.
     *
     * @param cr
     *          the machine's topology
     */
    private void alignMultiplexers(MachineTopology cr) {
        Multiplexer multiplexerA = cr.getCircuit(Multiplexer.class, Parts.MUX_A);
        Multiplexer multiplexerB = cr.getCircuit(Multiplexer.class, Parts.MUX_B);
        multiplexerA.setShape(new MuxShape());
        multiplexerB.setShape(new MuxShape());

        Wire muxOutA = cr.getCircuit(Wire.class, Parts.MUX_A + "_WIRE_OUT");
        Wire muxOutB = cr.getCircuit(Wire.class, Parts.MUX_B + "_WIRE_OUT");

        ConstraintContainer c = container;
        c.addComponent(multiplexerA.getDataOut(), Parts.MUX_A_OUT);
        c.addComponent(multiplexerB.getDataOut(), Parts.MUX_B_OUT);
        c.addComponent(multiplexerA.getSelectPin(), Parts.MUX_A_SELECT);
        c.addComponent(multiplexerB.getSelectPin(), Parts.MUX_B_SELECT);

        String wireOutA = Parts.MUX_A + "_WIRE_OUT";
        String wireOutB = Parts.MUX_B + "_WIRE_OUT";
        addWire(muxOutA, wireOutA);
        addWire(muxOutB, wireOutB);

        ConstraintFactory cf = c.createConstraintFactory();

        // ALU multiplexer B
        cf.left(Parts.MUX_B, Parts.GROUP_ALL_REGISTERS, 50);
        cf.above(Parts.MUX_B, Parts.GROUP_ALL_REGISTERS, 90);

        // ALU multiplexer A
        cf.alignHorizontally(Parts.MUX_A, Parts.MUX_B);
        cf.above(Parts.MUX_A, Parts.MUX_B, 25);

        cf.alignHorizontally(Parts.MUX_A_SELECT, Parts.MUX_A);
        cf.alignHorizontally(Parts.MUX_B_SELECT, Parts.MUX_B);

        cf.above(Parts.MUX_A_SELECT, Parts.MUX_A);
        cf.below(Parts.MUX_B_SELECT, Parts.MUX_B);

        // the arc of the multiplexer has a radius of 10.
        // if the mux is empty, its sprite is a circle of height 20, so
        // the only reasonable vertical alignment is in the middle of such circle.
        cf.above(Parts.MUX_B_OUT, Parts.MUX_B, -10);
        cf.right(Parts.MUX_B_OUT, Parts.MUX_B);

        cf.right(Parts.MUX_A_OUT, Parts.MUX_A);
        cf.below(Parts.MUX_A_OUT, Parts.MUX_A, -10);

        cf.align(wireOutA + ".0", Parts.MUX_A_OUT);
        cf.align(wireOutB + ".0", Parts.MUX_B_OUT);
        cf.align(wireOutA + ".1", Parts.ALU_A_IN);
        cf.align(wireOutB + ".1", Parts.ALU_B_IN);

        c.addVirtualComponent(Parts.MUX_SPACING);
        cf.below(Parts.MUX_SPACING, Parts.MUX_A);
        cf.above(Parts.MUX_SPACING, Parts.MUX_B);

        // not used
        cf.alignHorizontally(Parts.MUX_SPACING, Parts.MUX_A);
    }

    /**
     * Aligns the layout of the ALU.
     *
     * @param cr
     *          the machine's topology
     */
    private void alignAlu(MachineTopology cr) {
        ConstraintContainer c = container;

        Alu alu = cr.getCircuit(Alu.class, Parts.ALU);
        alu.setShape(new AluShape());

        c.addComponent(alu.getInA(), Parts.ALU_A_IN);
        c.addComponent(alu.getInB(), Parts.ALU_B_IN);
        c.addComponent(alu.getInCtrl(), Parts.ALU_CTRL_IN);
        c.addComponent(alu.getOutData(), Parts.ALU_DATA_OUT);
        c.addComponent(alu.getOutZero(), Parts.ALU_COND_OUT);

        ConstraintFactory cf = c.createConstraintFactory();
        cf.alignHorizontally(Parts.ALU, Parts.GROUP_ALL_REGISTERS);
        cf.alignVertically(Parts.ALU, Parts.MUX_SPACING);

        cf.left(Parts.ALU_A_IN, Parts.ALU);
        cf.left(Parts.ALU_B_IN, Parts.ALU);
        cf.alignVertically(Parts.ALU_A_IN, Parts.MUX_A_OUT);
        cf.alignVertically(Parts.ALU_B_IN, Parts.MUX_B_OUT);

        cf.alignHorizontally(Parts.ALU_CTRL_IN, Parts.ALU);
        cf.above(Parts.ALU_CTRL_IN, Parts.ALU, -17);

        cf.right(Parts.ALU_DATA_OUT, Parts.ALU);
        cf.relative(Parts.ALU_DATA_OUT, AttributeType.VERTICAL_CENTER, Parts.ALU, 4);
        cf.right(Parts.ALU_COND_OUT, Parts.ALU);
        cf.relative(Parts.ALU_COND_OUT, AttributeType.VERTICAL_CENTER, Parts.ALU, -4);

        c.addVirtualComponent(Parts.ALU_LINE);
        cf.right(Parts.ALU_LINE, Parts.GROUP_ALL_REGISTERS, 170);

        // not used
        cf.alignVertically(Parts.ALU_LINE, Parts.ALU);
    }

    /**
     * Aligns the layout of the memory.
     *
     * @param cr
     *          the machine's topology
     */
    private void alignMemory(MachineTopology cr) {
        Memory mem = cr.getCircuit(Memory.class, Parts.MEMORY);
        mem.setShape(new MemoryShape());

        ConstraintContainer c = container;

        // memory
        putLayout(Parts.MEMORY, new OriginLayout());

        c.addComponent(mem.getAdr(), Parts.MEMORY_ADR);
        c.addComponent(mem.getCs(), Parts.MEMORY_CS);
        c.addComponent(mem.getDataIn(), Parts.MEMORY_DI);
        c.addComponent(mem.getDataOut(), Parts.MEMORY_DO);
        c.addComponent(mem.getRw(), Parts.MEMORY_RW);

        ConstraintFactory cf = container.createConstraintFactory();
        cf.left(Parts.MEMORY_ADR, Parts.MEMORY);
        cf.above(Parts.MEMORY_ADR, Parts.MEMORY, -14);
        cf.above(Parts.MEMORY_CS, Parts.MEMORY);
        cf.left(Parts.MEMORY_CS, Parts.MEMORY, -20);
        cf.left(Parts.MEMORY_DI, Parts.MEMORY);
        cf.below(Parts.MEMORY_DI, Parts.MEMORY, -14);
        cf.right(Parts.MEMORY_DO, Parts.MEMORY);
        cf.alignVertically(Parts.MEMORY_DO, Parts.MEMORY);
        cf.above(Parts.MEMORY_RW, Parts.MEMORY);
        cf.right(Parts.MEMORY_RW, Parts.MEMORY, -20);
    }
}