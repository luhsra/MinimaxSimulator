package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.ExtensionList;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.RegisterManager.RegisterType;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.FlowLeftLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.StackLayoutSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An {@link ExtensionList} for the machine's {@link RegisterExtension}s.
 *
 * @author Martin L&uuml;ck
 */
class RegisterExtensionList implements ExtensionList<RegisterExtension> {

	private final MinimaxMachine machine;
	private final MinimaxLayout layout;

	private final List<String> registerNames;
	private final List<RegisterExtension> registerextensions;

	private LayoutSet stackLayout;
	private LayoutSet flowLayout;

	private RegisterManager registerManager;

	/**
	 * Constructs a new {@code RegisterExtensionList} for the specified {@link MinimaxMachine}
	 * using the specified {@link RegisterManager}.
	 *
	 * @param machine
	 *          the {@code MinimaxMachine}
	 * @param registerManager
	 *          the {@code RegisterManager}
	 */
	public RegisterExtensionList(MinimaxMachine machine, RegisterManager registerManager) {
		this.machine = machine;
		layout = machine.getLayout();

		registerNames = new ArrayList<String>();
		registerextensions = new ArrayList<RegisterExtension>();

		this.registerManager = registerManager;

		setStackLayout();
	}

	@Override
	public void add(RegisterExtension element) {
		removeStackLayout();

		addInternal(element);

		// align the array of registers and display
		setStackLayout();
		machine.updateLayout();
	}

	@Override
	public void addAll(Collection<? extends RegisterExtension> elements) {
		removeStackLayout();

		elements.forEach(this::addInternal);

		// align the array of registers and display
		setStackLayout();
		machine.updateLayout();
	}

	@Override
	public void remove(int index) {
		removeStackLayout();

		removeInternal(index);

		// align the array of registers and display
		setStackLayout();
		machine.updateLayout();
	}

	@Override
	public void swap(int index1, int index2) {
		removeStackLayout();

		Collections.swap(registerNames, index1, index2);
		Collections.swap(registerextensions, index1, index2);

		// align the array of registers and display
		setStackLayout();
		machine.updateLayout();
	}

	@Override
	public void set(int index, RegisterExtension element) {
		removeStackLayout();

		removeInternal(index);
		addInternal(index, element);

		// align the array of registers and display
		setStackLayout();
		machine.updateLayout();
	}

	/**
	 * Adds the specified {@link RegisterExtension} to the end of the internal list.
	 *
	 * @param element
	 *          the {@code RegisterExtension} to add
	 */
	private void addInternal(RegisterExtension element) {
		addInternal(registerNames.size(), element);
	}

	/**
	 * Adds the specified {@link RegisterExtension} at the specified index to the internal list.
	 *
	 * @param index
	 *          the index of the new {@code RegisterExtension}
	 * @param element
	 *          the {@code RegisterExtension} to add
	 */
	private void addInternal(int index, RegisterExtension element) {
		String registerId = registerManager.addRegister(RegisterType.EXTENDED, element.getName());
		registerNames.add(index, registerId);
		registerextensions.add(index, element);
	}

	/**
	 * Removes the {@link RegisterExtension} at the specified index of the internal list.
	 *
	 * @param index
	 *          the index of the {@code RegisterExtension} to remove
	 */
	private void removeInternal(int index) {
		RegisterExtension ext = registerextensions.remove(index);
		registerNames.remove(index);
		registerManager.removeRegister(ext.getName());
	}

	/**
	 * Sets a {@link StackLayoutSet}.
	 */
	private void setStackLayout() {
		List<String> outJunctionNames = new ArrayList<String>(registerNames.size());
		for (String name : registerNames) {
			outJunctionNames.add(name + Parts._OUT_JUNCTION + Parts._ANCHOR);
		}

		List<String> names = new ArrayList<String>(registerNames);
		Collections.reverse(names);

		stackLayout = new StackLayoutSet(Parts.GROUP_BASE_REGISTERS, names, 40, Parts.GROUP_EXTENDED_REGISTERS);
		layout.putLayouts(stackLayout);

		flowLayout = new FlowLeftLayoutSet(Parts.GROUP_MUX_LINE, outJunctionNames, 8, Parts.GROUP_MUX_EXT_REGISTERS);
		layout.putLayouts(flowLayout);
	}

	/**
	 * Removes the {@link StackLayoutSet}.
	 */
	private void removeStackLayout() {
		if (stackLayout != null) {
			layout.removeLayouts(stackLayout);
			stackLayout = null;
		}

		if (flowLayout != null) {
			layout.removeLayouts(flowLayout);
			flowLayout = null;
		}
	}
}