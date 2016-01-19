package de.uni_hannover.sra.minimax_simulator.model.machine.base;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplay;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;

/**
 * The {@code Machine} interface provides the basic methods each machine type has to implement.
 *
 * @author Martin L&uuml;ck
 */
public interface Machine {

    /**
     * Gets the visual representation of the machine.
     *
     * @return
     *          the {@link MachineDisplay} representing the machine
     */
    public MachineDisplay getDisplay();

    /**
     * Gets the machine's memory.
     *
     * @return
     *          the {@link MachineMemory} of the machine
     */
    public MachineMemory getMemory();

    /**
     * Gets the machine's topology.
     *
     * @return
     *          the {@link MachineTopology} of the machine
     */
    public MachineTopology getTopology();
}