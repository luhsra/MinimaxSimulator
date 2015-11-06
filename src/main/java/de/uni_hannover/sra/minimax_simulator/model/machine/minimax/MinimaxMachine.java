package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


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
import de.uni_hannover.sra.minimax_simulator.ui.layout.Insets;

import java.util.*;

/**
 * The actual implementation of a Minimax machine.
 *
 * @author Martin L&uuml;ck
 */
public class MinimaxMachine implements ConfigurableMachine {

	private final static int ADDRESS_WIDTH	= 24;
	private final static int PAGE_WIDTH		= 12;

	private final MinimaxLayout layout;
	private final MinimaxTopology topology;
	private final MinimaxDisplay display;
	private final MachineMemory memory;

	private final GroupManager groupManager;
	private final RegisterManager registerManager;

	private final ExtensionList<RegisterExtension> registerExtensions;
	private final Map<MuxType, MuxInputManager> muxExtensions;
	private final ExtensionList<AluOperation> aluExtensions;

	/**
	 * Constructs a new {@code MinimaxMachine}.
	 */
	public MinimaxMachine() {
		// initialize basic network topology and create Part instances.
		topology = new MinimaxTopology();
		display = new MinimaxDisplay();
		layout = new MinimaxLayout();

		memory = new PagedArrayMemory(ADDRESS_WIDTH, PAGE_WIDTH);

		groupManager = new DefaultGroupManager(layout, topology, display);

		RegisterInputGroupManager rig = new RegisterInputGroupManager(this);
		registerManager = rig;

		// create part groups and add remaining parts
		for (Group group : createGroups()) {
			groupManager.initializeGroup("base-group:<" + group.toString() + ">", group);
		}

		registerManager.addRegister(RegisterType.MDR, Parts.MDR);
		registerManager.addRegister(RegisterType.BASE, Parts.IR);
		registerManager.addRegister(RegisterType.BASE, Parts.MAR);
		registerManager.addRegister(RegisterType.BASE, Parts.PC);
		registerManager.addRegister(RegisterType.BASE, Parts.ACCU);

		// Layout base parts and group parts
		layout.initPartLayouts(topology, display);

		registerExtensions = new RegisterExtensionList(this, registerManager);

		muxExtensions = new EnumMap<MuxType, MuxInputManager>(MuxType.class);
		muxExtensions.put(MuxType.A, new DefaultMuxInputManager(MuxType.A, Parts.MUX_A, this));
		muxExtensions.put(MuxType.B, new DefaultMuxInputManager(MuxType.B, Parts.MUX_B, this));

		List<MuxInputGroupManager> inputGroupManagers = Arrays.asList(
			new ConstantInputGroupManager(Parts.GROUP_MUX_CONSTANTS,
				Arrays.asList(Parts.MUX_A, Parts.MUX_B), this), rig, new NullInputGroupManager(
						groupManager));

		for (MuxType mux : MuxType.values()) {
			for (MuxInputGroupManager mig : inputGroupManagers) {
				muxExtensions.get(mux).registerGroupManager(mig);
			}
		}

		layout.putLayout(Parts.GROUP_MUX_CONSTANTS, new GroupLayout(Arrays.asList(Parts.MUX_A, Parts.MUX_B)));

		aluExtensions = new AluExtensionList(topology.getCircuit(Alu.class, Parts.ALU));

		// == Tweaks ==

		layout.getContainer().setInsets(new Insets(40, 40, 40, 40));

		// remove visual representation of MAR junction
		topology.getCircuit(Junction.class, Parts.MAR + Parts._JUNCTION).getDataOuts().remove(1);

		updateLayout();

		// Now, the machine is displayable, as soon as a RenderEnvironment is set on the
		// MachineDisplay.
	}

	/**
	 * Updates the {@link MinimaxLayout} and size of the {@link MinimaxDisplay}.
	 */
	void updateLayout() {
		// relocate all parts based on constraints (...almost there)
		layout.updateLayout();

		// setup display instance with calculated size
		display.setDimension(layout.getDimension());
	}

	/**
	 * Gets the {@link MinimaxLayout} of the {@code MinimaxMachine}.
	 *
	 * @return
	 *          the layout
	 */
	MinimaxLayout getLayout() {
		return layout;
	}

	@Override
	public MinimaxTopology getTopology() {
		return topology;
	}

	@Override
	public MinimaxDisplay getDisplay() {
		return display;
	}

	@Override
	public MachineMemory getMemory() {
		return memory;
	}

	/**
	 * Gets the {@link GroupManager} of the {@code MinimaxMachine}.
	 *
	 * @return
	 *          the {@code GroupManager}
	 */
	GroupManager getGroupManager() {
		return groupManager;
	}

	/**
	 * Gets the {@link RegisterManager} of the {@code MinimaxMachine}.
	 *
	 * @return
	 *          the {@code RegisterManager}
	 */
	RegisterManager getRegisterManager() {
		return registerManager;
	}

	@Override
	public ExtensionList<RegisterExtension> getRegisterExtensions() {
		return registerExtensions;
	}

	@Override
	public ExtensionList<MuxInput> getMuxInputExtensions(MuxType type) {
		return muxExtensions.get(type);
	}

	@Override
	public ExtensionList<AluOperation> getAluOperations() {
		return aluExtensions;
	}

	/**
	 * Creates the {@link Group}s of the base machine.
	 *
	 * @return
	 *          a list of the {@code Group}s
	 */
	private List<Group> createGroups() {
		List<Group> list = new ArrayList<Group>();

		list.add(new BasePartGroup(memory));
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