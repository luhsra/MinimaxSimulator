package de.uni_hannover.sra.minimax_simulator.model.machine;

import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.model.user.NewProjectBuilder;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests the simulation of a machine.
 *
 * @see Simulation
 * @see de.uni_hannover.sra.minimax_simulator.model.machine.minimax.MinimaxSimulation
 *
 * @author Philipp Rohde
 */
public class SimulationTest {

    /**
     * Tests the simulation of all cycles of the default microprogram.
     */
    @Test
    public void test() {
        final Project project = new NewProjectBuilder().buildProject();

        // create list of all registers
        List<RegisterExtension> registers = new ArrayList<>();
        registers.addAll(project.getMachineConfiguration().getBaseRegisters());
        registers.addAll(project.getMachineConfiguration().getRegisterExtensions());

        // initialize simulation
        Simulation simulation = project.getSimulation();
        simulation.init();
        simulation.step();
        simulation.step();

        // check results after first cycle
        assertEquals("ALU result first cycle", (Integer) 1, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name)) {
                expected = 1;
            }
            assertEquals("Register " + name + " value first cycle", expected, simulation.getRegisterValue(name).get());
        });

        // check results after second cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result second cycle", (Integer) 2, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "PC".equals(name)) {
                expected = 2;
            }
            assertEquals("Register " + name + " value second cycle", expected, simulation.getRegisterValue(name).get());
        });

        // check result after third cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result third cycle", (Integer) 4, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "PC".equals(name)) {
                expected = 4;
            }
            assertEquals("Register " + name + " value third cycle", expected, simulation.getRegisterValue(name).get());
        });

        // check result after fourth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result fourth cycle", (Integer) 8, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "PC".equals(name)) {
                expected = 8;
            }
            assertEquals("Register " + name + " value fourth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // cycle five only checks PC == 0
        simulation.step();
        simulation.step();

        // check result after sixth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result sixth cycle", (Integer) 16, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 16;
            }
            else if ("PC".equals(name)) {
                expected = 8;
            }
            assertEquals("Register " + name + " value sixth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // check result after seventh cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result seventh cycle", (Integer) 7, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 16;
            }
            else if ("PC".equals(name)) {
                expected = 7;
            }
            assertEquals("Register " + name + " value seventh cycle", expected, simulation.getRegisterValue(name).get());
        });

        // cycle eight only checks PC == 0
        simulation.step();
        simulation.step();

        // check result after ninth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result ninth cycle", (Integer) 32, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 32;
            }
            else if ("PC".equals(name)) {
                expected = 7;
            }
            assertEquals("Register " + name + " value ninth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // check result after tenth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result tenth cycle", (Integer) 6, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 32;
            }
            else if ("PC".equals(name)) {
                expected = 6;
            }
            assertEquals("Register " + name + " value tenth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // cycle eleven only checks PC == 0
        simulation.step();
        simulation.step();

        // check result after twelfth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result twelfth cycle", (Integer) 64, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 64;
            }
            else if ("PC".equals(name)) {
                expected = 6;
            }
            assertEquals("Register " + name + " value twelfth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // check result after thirteenth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result thirteenth cycle", (Integer) 5, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 64;
            }
            else if ("PC".equals(name)) {
                expected = 5;
            }
            assertEquals("Register " + name + " value thirteenth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // cycle fourteen only checks PC == 0
        simulation.step();
        simulation.step();

        // check result after fifteenth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result fifteenth cycle", (Integer) 128, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 128;
            }
            else if ("PC".equals(name)) {
                expected = 5;
            }
            assertEquals("Register " + name + " value fifteenth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // check result after sixteenth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result sixteenth cycle", (Integer) 4, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 128;
            }
            else if ("PC".equals(name)) {
                expected = 4;
            }
            assertEquals("Register " + name + " value sixteenth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // cycle seventeen only checks PC == 0
        simulation.step();
        simulation.step();

        // check result after eighteenth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result eighteenth cycle", (Integer) 256, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 256;
            }
            else if ("PC".equals(name)) {
                expected = 4;
            }
            assertEquals("Register " + name + " value eighteenth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // check result after nineteenth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result nineteenth cycle", (Integer) 3, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 256;
            }
            else if ("PC".equals(name)) {
                expected = 3;
            }
            assertEquals("Register " + name + " value nineteenth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // cycle twenty only checks PC == 0
        simulation.step();
        simulation.step();

        // check result after twenty-first cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result twenty-first cycle", (Integer) 512, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 512;
            }
            else if ("PC".equals(name)) {
                expected = 3;
            }
            assertEquals("Register " + name + " value twenty-first cycle", expected, simulation.getRegisterValue(name).get());
        });

        // check result after twenty-second cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result twenty-second cycle", (Integer) 2, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 512;
            }
            else if ("PC".equals(name)) {
                expected = 2;
            }
            assertEquals("Register " + name + " value twenty-second cycle", expected, simulation.getRegisterValue(name).get());
        });

        // cycle twenty-three only checks PC == 0
        simulation.step();
        simulation.step();

        // check result after twenty-fourth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result twenty-fourth cycle", (Integer) 1024, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 1024;
            }
            else if ("PC".equals(name)) {
                expected = 2;
            }
            assertEquals("Register " + name + " value twenty-fourth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // check result after twenty-fifth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result twenty-fifth cycle", (Integer) 1, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 1024;
            }
            else if ("PC".equals(name)) {
                expected = 1;
            }
            assertEquals("Register " + name + " value twenty-fifth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // cycle twenty-six only checks PC == 0
        simulation.step();
        simulation.step();

        // check result after twenty-seventh cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result twenty-seventh cycle", (Integer) 2048, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 2048;
            }
            else if ("PC".equals(name)) {
                expected = 1;
            }
            assertEquals("Register " + name + " value twenty-seventh cycle", expected, simulation.getRegisterValue(name).get());
        });

        // check result after twenty-eighth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result twenty-eighth cycle", (Integer) 0, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 2048;
            }
            assertEquals("Register " + name + " value twenty-eighth cycle", expected, simulation.getRegisterValue(name).get());
        });

        // cycle twenty-nine only checks PC == 0
        simulation.step();
        simulation.step();

        // check result after thirtieth cycle
        simulation.step();
        simulation.step();
        assertEquals("ALU result twenty-eighth cycle", (Integer) 2048, simulation.getAluResult().get());
        registers.forEach((RegisterExtension register) -> {
            String name = register.getName();
            Integer expected = 0;
            if ("ACCU".equals(name) || "MDR".equals(name)) {
                expected = 2048;
            }
            assertEquals("Register " + name + " value twenty-eighth cycle", expected, simulation.getRegisterValue(name).get());
        });
        assertEquals("written memory result", 2048, simulation.getMemoryState().getMemoryState().getInt(0));

        // close the simulation
        simulation.stop();
    }
}
