package de.uni_hannover.sra.minimax_simulator.model.machine;

import java.lang.reflect.Field;

import org.junit.Test;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import static org.junit.Assert.*;

/**
 * Tests the access to the part names listed in {@link Parts}.
 *
 * @author Martin L&uuml;ck
 */
public class PartNameTest {

    /**
     * Checks the names provided by {@code Parts}.
     *
     * @throws IllegalAccessException
     *          thrown if a field could not be accessed
     */
    @Test
    public void checkNames() throws IllegalAccessException {
        for (Field field : Parts.class.getDeclaredFields()) {
            if (field.isSynthetic()) {
                // e.g. JaCoCo coverage analysis
                continue;
            }
            assertEquals(field.getName(), field.get(null));
        }
    }
}