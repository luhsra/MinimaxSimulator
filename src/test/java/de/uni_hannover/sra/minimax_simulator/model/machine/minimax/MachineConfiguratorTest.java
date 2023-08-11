package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MinimaxConfigurationBuilder;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.MachineConfigurator;
import de.uni_hannover.sra.minimax_simulator.model.signal.DefaultSignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests uncovered parts of the implementation of {@link MachineConfigurator}.
 *
 * @author Philipp Rohde
 */
public class MachineConfiguratorTest {

    /**
     * Actually runs the test.
     *
     * @throws Exception
     *         thrown if something with the reflections went wrong.
     */
    @Test
    public void test() throws Exception {
        MachineConfiguration configuration = new MinimaxConfigurationBuilder().build();
        MinimaxMachine machine = new MinimaxMachine();
        MachineConfigurator configurator = new MachineConfigurator(machine, configuration);
        configuration.addMachineConfigListener(configurator);

        List<RegisterExtension> registers = getRegisterExtensionList(machine);

        // add register
        RegisterExtension reg = new RegisterExtension("reg", RegisterSize.BITS_32, "reg description", true);
        configuration.addRegisterExtension(reg);
        assertTrue("register was added", registers.contains(reg));

        RegisterExtension tmp = new RegisterExtension("tmp", RegisterSize.BITS_32, "temp register", true);
        configuration.addRegisterExtension(tmp);

        // exchange registers
        configuration.exchangeRegisterExtensions(0, 1);
        assertTrue(tmp.equals(registers.get(0)));
        assertTrue(reg.equals(registers.get(1)));

        // remove tmp register
        configuration.removeRegisterExtension(tmp);
        assertEquals("only one register left", 1, registers.size());
        assertTrue("remaining register", reg.equals(registers.get(0)));

        // replace register; the configuration needs a SignalTable because setRegisterExtensions checks for it
        SignalTable signals = new DefaultSignalTable();
        configuration.setSignalTable(signals);
        configuration.setRegisterExtension(0, tmp);
        assertTrue("tmp should be at 0", tmp.equals(registers.get(0)));

        // replace mux input
        List<MuxInputManager.InputEntry> muxInputs = getMuxInputList(machine, MuxType.A);
        ConstantMuxInput cmiOne = new ConstantMuxInput(1);
        ConstantMuxInput cmiTwo = new ConstantMuxInput(2);
        configuration.addMuxSource(MuxType.A, cmiOne);
        assertTrue("added mux input", cmiOne.equals(muxInputs.get(0).input));
        configuration.setMuxSource(MuxType.A, 0, cmiTwo);
        assertTrue("mux input replaced", cmiTwo.equals(muxInputs.get(0).input));
    }

    /**
     * Makes the field containing a standard list of the registers accessible.<br>
     * This is needed for testing purposes.
     *
     * @param machine
     *         the {@code MinimaxMachine} to get the registers list of
     * @return
     *         the list of the registers
     * @throws NoSuchFieldException
     *         thrown if the field could not be found
     * @throws IllegalAccessException
     *         thrown if the field could not be accessed
     */
    private List<RegisterExtension> getRegisterExtensionList(MinimaxMachine machine) throws NoSuchFieldException, IllegalAccessException {
        Field registerField = RegisterExtensionList.class.getDeclaredField("registerextensions");
        registerField.setAccessible(true);
        return  (List<RegisterExtension>) registerField.get(machine.getRegisterExtensions());
    }

    /**
     * Makes the field containing a standard list of the multiplexer inputs accessible.<br>
     * This is needed for testing purposes.
     *
     * @param machine
     *         the {@code MinimaxMachine} to get the mux inputs of
     * @param muxType
     *         the multiplexer for which the mux inputs should be returned
     * @return
     *         the list of the mux inputs
     * @throws NoSuchFieldException
     *         thrown if the field could not be found
     * @throws IllegalAccessException
     *         thrown if the field could not be accessed
     */
    private List<MuxInputManager.InputEntry> getMuxInputList(MinimaxMachine machine, MuxType muxType) throws NoSuchFieldException, IllegalAccessException {
        Field inputs = DefaultMuxInputManager.class.getDeclaredField("inputs");
        inputs.setAccessible(true);
        return (List<MuxInputManager.InputEntry>) inputs.get(machine.getMuxInputExtensions(muxType));
    }
}
