package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import de.uni_hannover.sra.minimax_simulator.ui.layout.Insets;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.ConfigurableMachine;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.ExtensionList;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.PagedArrayMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.RegisterManager.RegisterType;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.*;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.GroupLayout;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;

import java.util.*;

/**
 * The actual implementation of a Minimax machine.
 *
 * @author Martin L&uuml;ck
 */
public class MinimaxMachine implements ConfigurableMachine {

	private final static int						ADDRESS_WIDTH	= 24;
	private final static int						PAGE_WIDTH		= 12;

	private final MinimaxLayout						_layout;
	private final MinimaxTopology					_topology;
	private final MinimaxDisplay					_display;
	private final MachineMemory						_memory;

	private final GroupManager						_groupManager;
	private final RegisterManager					_registerManager;

	private final ExtensionList<RegisterExtension>	_registerExtensions;
	private final Map<MuxType, MuxInputManager>		_muxExtensions;
	private final ExtensionList<AluOperation>		_aluExtensions;

	/**
	 * Constructs a new {@code MinimaxMachine}.
	 */
	public MinimaxMachine() {
		// initialize basic network topology and create Part instances.
		_topology = new MinimaxTopology();
		_display = new MinimaxDisplay();
		_layout = new MinimaxLayout();

		_memory = new PagedArrayMemory(ADDRESS_WIDTH, PAGE_WIDTH);

		_groupManager = new DefaultGroupManager(_layout, _topology, _display);

		RegisterInputGroupManager rig = new RegisterInputGroupManager(this);
		_registerManager = rig;

		// create part groups and add remaining parts
		for (Group group : createGroups()) {
			_groupManager.initializeGroup("base-group:<" + group.toString() + ">", group);
		}

		_registerManager.addRegister(RegisterType.MDR, Parts.MDR);
		_registerManager.addRegister(RegisterType.BASE, Parts.IR);
		_registerManager.addRegister(RegisterType.BASE, Parts.MAR);
		_registerManager.addRegister(RegisterType.BASE, Parts.PC);
		_registerManager.addRegister(RegisterType.BASE, Parts.ACCU);

		// Layout base parts and group parts
		_layout.initPartLayouts(_topology, _display);

		_registerExtensions = new RegisterExtensionList(this, _registerManager);

		_muxExtensions = new EnumMap<MuxType, MuxInputManager>(MuxType.class);
		_muxExtensions.put(MuxType.A, new DefaultMuxInputManager(MuxType.A, Parts.MUX_A, this));
		_muxExtensions.put(MuxType.B, new DefaultMuxInputManager(MuxType.B, Parts.MUX_B, this));

		List<MuxInputGroupManager> inputGroupManagers = Arrays.asList(
			new ConstantInputGroupManager(Parts.GROUP_MUX_CONSTANTS,
				Arrays.asList(Parts.MUX_A, Parts.MUX_B), this), rig, new NullInputGroupManager(
				_groupManager));

		for (MuxType mux : MuxType.values()) {
			for (MuxInputGroupManager mig : inputGroupManagers) {
				_muxExtensions.get(mux).registerGroupManager(mig);
			}
		}

		_layout.putLayout(Parts.GROUP_MUX_CONSTANTS, new GroupLayout(Arrays.asList(Parts.MUX_A, Parts.MUX_B)));

		_aluExtensions = new AluExtensionList(_topology.getCircuit(Alu.class, Parts.ALU));

		// == Tweaks ==

		_layout.getContainer().setInsets(new Insets(40, 40, 40, 40));

		// remove visual representation of MAR junction
		_topology.getCircuit(Junction.class, Parts.MAR + Parts._JUNCTION).getDataOuts().remove(1);

		updateLayout();

		// Now, the machine is displayable, as soon as a RenderEnvironment is set on the
		// MachineDisplay.
	}

	/**
	 * Updates the {@link MinimaxLayout} and size of the {@link MinimaxDisplay}.
	 */
	void updateLayout() {
		// relocate all parts based on constraints (...almost there)
		_layout.updateLayout();

		// setup display instance with calculated size
		_display.setDimension(_layout.getDimension());
	}

	/**
	 * Gets the {@link MinimaxLayout} of the {@code MinimaxMachine}.
	 *
	 * @return
	 *          the layout
	 */
	MinimaxLayout getLayout() {
		return _layout;
	}

	@Override
	public MinimaxTopology getTopology() {
		return _topology;
	}

	@Override
	public MinimaxDisplay getDisplay() {
		return _display;
	}

	@Override
	public MachineMemory getMemory() {
		return _memory;
	}

	/**
	 * Gets the {@link GroupManager} of the {@code MinimaxMachine}.
	 *
	 * @return
	 *          the {@code GroupManager}
	 */
	GroupManager getGroupManager() {
		return _groupManager;
	}

	/**
	 * Gets the {@link RegisterManager} of the {@code MinimaxMachine}.
	 *
	 * @return
	 *          the {@code RegisterManager}
	 */
	RegisterManager getRegisterManager() {
		return _registerManager;
	}

	@Override
	public ExtensionList<RegisterExtension> getRegisterExtensions() {
		return _registerExtensions;
	}

	@Override
	public ExtensionList<MuxInput> getMuxInputExtensions(MuxType type) {
		return _muxExtensions.get(type);
	}

	@Override
	public ExtensionList<AluOperation> getAluOperations() {
		return _aluExtensions;
	}

	/**
	 * Creates the {@link Group}s of the base machine.
	 *
	 * @return
	 *          a list of the {@code Group}s
	 */
	private List<Group> createGroups() {
		List<Group> list = new ArrayList<Group>();

		list.add(new BasePartGroup(_memory));
		list.add(new AluGroup());

		// now done by RegisterManager
		// list.add(new DefaultRegisterGroup(MAR));
		// list.add(new DefaultRegisterGroup(IR));
		// list.add(new DefaultRegisterGroup(PC));
		// list.add(new MdrRegisterGroup(MDR));
		list.add(new MultiplexerGroup(Parts.MDR_SELECT, "MDR.Sel", true, BaseControlPort.MDR_SEL.port()));
		list.add(new MultiplexerGroup(Parts.MUX_A, "ALUSel.A", true, BaseControlPort.ALU_SELECT_A.port()));
		list.add(new MultiplexerGroup(Parts.MUX_B, "ALUSel.B", false, BaseControlPort.ALU_SELECT_B.port()));
		list.add(new MemoryGroup());
		list.add(new SignExtGroup());
		list.add(new BaseRegisterOutWireGroup());
		return list;
	}
}