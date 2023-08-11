package de.uni_hannover.sra.minimax_simulator.model.machine.base;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Port;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Tests the implementation of {@link DefaultCircuitTopology} that is not covered by other tests.
 *
 * @author Philipp Rohde
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultCircuitTopologyTest {

    private static final DefaultCircuitTopology top = new DefaultCircuitTopology();
    private static final String EXCEPTION_EXPECTED = "expected to throw exception";
    private static final Alu alu = new Alu();
    private static final Port port = new Port("port");

    /**
     * Tests the implementation of {@link DefaultCircuitTopology#addCircuit(Class, String, Circuit)}.
     */
    @Test
    public void test01addCircuit() {
        top.addCircuit(Alu.class, "alu", alu);
        assertEquals("added ALU equals returned one", alu, top.getCircuit(Alu.class, "alu"));

        // add something with ID 'alu' again
        try {
            top.addCircuit(Port.class, "alu", new Port("ALU"));
            fail(EXCEPTION_EXPECTED);
        } catch (IllegalStateException e) {
            assertEquals("IllegalStateException", "Circuit already defined as: Alu", e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link DefaultCircuitTopology#getCircuit(Class, String)}.
     */
    @Test
    public void test02addCircuit() {
        top.addCircuit("port", port);
        assertEquals("added Port equals returned one", port, top.getCircuit(Port.class, "port"));

        // add something with ID 'port' again
        try {
            top.addCircuit("port", new Alu());
            fail(EXCEPTION_EXPECTED);
        } catch (IllegalStateException e) {
            assertEquals("IllegalStateException", "Circuit port already defined as: " + port, e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link DefaultCircuitTopology#getCircuit(Class, String)}.
     */
    @Test
    public void test03getCircuit() {
        // get non-existing circuit
        try {
            top.getCircuit(Alu.class, "notThere");
            fail(EXCEPTION_EXPECTED);
        } catch (NullPointerException e) {
            assertEquals("Null Pointer Exception", "No circuit for the id notThere", e.getMessage());
        }

        // get non-assignable circuit
        try {
            top.getCircuit(Port.class, "alu");
            fail(EXCEPTION_EXPECTED);
        } catch (ClassCastException e) {
            String aluClass = Alu.class.getCanonicalName();
            String portClass = Port.class.getCanonicalName();
            assertEquals("ClassCastException", "alu is a " + aluClass + ", not a " + portClass, e.getMessage());
        }
    }

    /**
     * Tests the implementation of {@link DefaultCircuitTopology#getAllCircuits()}.
     */
    @Test
    public void test04getAllCircuits() {
        Set<Circuit> circuits = top.getAllCircuits();
        assertEquals("size", 2, circuits.size());
        assertTrue("contains alu", circuits.contains(alu));
        assertTrue("contains port", circuits.contains(port));
    }

    /**
     * Tests the implementation of {@link DefaultCircuitTopology#removeCircuit(String)}.
     */
    @Test
    public void test05removeCircuit() {
        top.removeCircuit("alu");
        assertFalse("alu removed", top.getAllCircuits().contains(alu));
        assertEquals("new number of circuits", 1, top.getAllCircuits().size());

        // remove non-existing circuit
        top.removeCircuit("notThere");
        assertEquals("new number of circuits", 1, top.getAllCircuits().size());
    }
}
