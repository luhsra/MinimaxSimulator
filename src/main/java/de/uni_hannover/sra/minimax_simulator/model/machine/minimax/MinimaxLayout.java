package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import java.util.Arrays;

import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.layout.Component;
import de.uni_hannover.sra.minimax_simulator.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.layout.Insets;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.ConstraintContainer;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.ConstraintFactory;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.DefaultLayoutManager;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.Layout;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.LayoutManager;
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
import de.uni_hannover.sra.minimax_simulator.layout.constraint.AttributeType;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

class MinimaxLayout
{
	private final ConstraintContainer	_container;
	private final LayoutManager			_layout;

	private Dimension					_dimension;

	public MinimaxLayout()
	{
		_container = new ConstraintContainer();
		_layout = new DefaultLayoutManager(_container);
	}

	public Dimension getDimension()
	{
		return _dimension;
	}

	public ConstraintContainer getContainer()
	{
		return _container;
	}

	public void updateLayout()
	{
		_container.updateSize();

		_dimension = _container.getDimension();
		Insets in = _container.getInsets();

		_container.setBounds(new Bounds(in.l, in.t, _dimension.w, _dimension.h));
		_container.doLayout();
	}

	public void initPartLayouts(MachineTopology cr, FontMetricsProvider fontProvider)
	{
		// Layout for base registers
		putLayouts(new StackLayoutSet(Parts.MEMORY, Arrays.asList(Parts.MAR, Parts.MDR, Parts.IR, Parts.PC),
			Arrays.asList(65, 45, 70, 80), Parts.GROUP_BASE_REGISTERS));

		putLayout(
				Parts.GROUP_ALL_REGISTERS,
			new GroupLayout(Arrays.asList(Parts.GROUP_BASE_REGISTERS, Parts.GROUP_EXTENDED_REGISTERS)));

		alignMemory(cr);
		alignMdrSelect(cr);
		alignMultiplexers(cr);
		alignAlu(cr);

		putLayout(Parts.GROUP_MUX_LABEL,
			new GroupLayout(Arrays.asList(Parts.MUX_A + Parts._LABEL, Parts.MUX_B + Parts._LABEL)));
		putLayout(
				Parts.GROUP_MUX_LINE,
			new GroupLayout(Arrays.asList(Parts.GROUP_MUX_LABEL, Parts.GROUP_MUX_CONSTANTS)));

		putLayout(
				Parts.GROUP_MUX_BASE_REGISTERS,
			new GroupLayout(Arrays.asList(Parts.MDR + Parts._OUT_JUNCTION, Parts.IR + Parts._OUT_JUNCTION, Parts.PC
				+ Parts._OUT_JUNCTION)));
	}

	public void addGroup(Group group)
	{
		for (Component component : group.getComponents())
			_container.addComponent(component, group.getName(component));
		for (String virtual : group.getVirtualComponents())
			_container.addVirtualComponent(virtual);
	}

	public void removeGroup(Group group)
	{
		for (Component component : group.getComponents())
			_container.removeComponent(component);
		for (String virtual : group.getVirtualComponents())
			_container.removeComponent(virtual);
	}

	public void putLayout(String name, Layout layout)
	{
		_layout.putLayout(name, layout);
	}

	public void removeLayout(String name)
	{
		_layout.removeLayout(name);
	}

	public void updateLayout(String name)
	{
		_layout.updateLayout(name);
	}

	public void putLayouts(LayoutSet set)
	{
		for (String name : set.getComponents())
			putLayout(name, set.getLayout(name));
	}

	public void removeLayouts(LayoutSet set)
	{
		for (String name : set.getComponents())
			removeLayout(name);
	}

	private void addWire(Wire wire, String name)
	{
		int index = 0;
		for (Component component : wire.createWireComponents())
		{
			_container.addComponent(component, name + "." + index);
			index++;
		}
	}

	private void alignMdrSelect(MachineTopology cr)
	{
		ConstraintContainer c = _container;

		Multiplexer mdrSel = cr.getCircuit(Multiplexer.class, Parts.MDR_SELECT);
		mdrSel.setShape(new MuxShape());

		c.addComponent(mdrSel.getDataOut(), Parts.MDR_SELECT_OUT);
		c.addComponent(mdrSel.getDataInputs().get(0), Parts.MDR_SELECT_IN_0);
		c.addComponent(mdrSel.getDataInputs().get(1), Parts.MDR_SELECT_IN_1);
		c.addComponent(mdrSel.getSelectPin(), Parts.MDR_SELECT_SELECT);

		ConstraintFactory f = c.createConstraintFactory();

		// MDR select
		f.alignVertically(Parts.MDR_SELECT, Parts.MDR);
		f.rightTo(Parts.MDR_SELECT, Parts.GROUP_BASE_REGISTERS, 70);

		// MDR select data out
		f.alignVertically(Parts.MDR_SELECT_OUT, Parts.MDR_SELECT);
		f.leftTo(Parts.MDR_SELECT_OUT, Parts.MDR_SELECT);

		f.right(Parts.MDR_SELECT_IN_0, Parts.MDR_SELECT);
		f.right(Parts.MDR_SELECT_IN_1, Parts.MDR_SELECT);

		f.above(Parts.MDR_SELECT_IN_0, Parts.MDR_SELECT, -MuxShape.MUX_CORNER_SPACING);
		f.below(Parts.MDR_SELECT_IN_1, Parts.MDR_SELECT, -MuxShape.MUX_CORNER_SPACING);

		f.alignHorizontally(Parts.MDR_SELECT_SELECT, Parts.MDR_SELECT);
		f.above(Parts.MDR_SELECT_SELECT, Parts.MDR_SELECT);
	}

	private void alignMultiplexers(MachineTopology cr)
	{
		Multiplexer multiplexerA = cr.getCircuit(Multiplexer.class, Parts.MUX_A);
		Multiplexer multiplexerB = cr.getCircuit(Multiplexer.class, Parts.MUX_B);
		multiplexerA.setShape(new MuxShape());
		multiplexerB.setShape(new MuxShape());

		Wire muxOutA = cr.getCircuit(Wire.class, Parts.MUX_A + "_WIRE_OUT");
		Wire muxOutB = cr.getCircuit(Wire.class, Parts.MUX_B + "_WIRE_OUT");

		ConstraintContainer c = _container;
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

	private void alignAlu(MachineTopology cr)
	{
		ConstraintContainer c = _container;

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

		cf.leftTo(Parts.ALU_A_IN, Parts.ALU);
		cf.leftTo(Parts.ALU_B_IN, Parts.ALU);
		cf.alignVertically(Parts.ALU_A_IN, Parts.MUX_A_OUT);
		cf.alignVertically(Parts.ALU_B_IN, Parts.MUX_B_OUT);

		cf.alignHorizontally(Parts.ALU_CTRL_IN, Parts.ALU);
		cf.above(Parts.ALU_CTRL_IN, Parts.ALU, -17);

		cf.right(Parts.ALU_DATA_OUT, Parts.ALU);
		cf.relative(Parts.ALU_DATA_OUT, AttributeType.VERTICAL_CENTER, Parts.ALU, 4);
		cf.right(Parts.ALU_COND_OUT, Parts.ALU);
		cf.relative(Parts.ALU_COND_OUT, AttributeType.VERTICAL_CENTER, Parts.ALU, -4);

		c.addVirtualComponent(Parts.ALU_LINE);
		cf.rightTo(Parts.ALU_LINE, Parts.GROUP_ALL_REGISTERS, 170);

		// not used
		cf.alignVertically(Parts.ALU_LINE, Parts.ALU);
	}

	private void alignMemory(MachineTopology cr)
	{
		Memory mem = cr.getCircuit(Memory.class, Parts.MEMORY);
		mem.setShape(new MemoryShape());

		ConstraintContainer c = _container;

		// Memory
		putLayout(Parts.MEMORY, new OriginLayout());

		c.addComponent(mem.getAdr(), Parts.MEMORY_ADR);
		c.addComponent(mem.getCs(), Parts.MEMORY_CS);
		c.addComponent(mem.getDataIn(), Parts.MEMORY_DI);
		c.addComponent(mem.getDataOut(), Parts.MEMORY_DO);
		c.addComponent(mem.getRw(), Parts.MEMORY_RW);

		ConstraintFactory cf = _container.createConstraintFactory();
		cf.leftTo(Parts.MEMORY_ADR, Parts.MEMORY);
		cf.above(Parts.MEMORY_ADR, Parts.MEMORY, -14);
		cf.above(Parts.MEMORY_CS, Parts.MEMORY);
		cf.left(Parts.MEMORY_CS, Parts.MEMORY, -20);
		cf.leftTo(Parts.MEMORY_DI, Parts.MEMORY);
		cf.below(Parts.MEMORY_DI, Parts.MEMORY, -14);
		cf.right(Parts.MEMORY_DO, Parts.MEMORY);
		cf.alignVertically(Parts.MEMORY_DO, Parts.MEMORY);
		cf.above(Parts.MEMORY_RW, Parts.MEMORY);
		cf.rightTo(Parts.MEMORY_RW, Parts.MEMORY, -20);
	}
}