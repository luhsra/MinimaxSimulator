package de.uni_hannover.sra.minimax_simulator.model.machine;

import java.lang.reflect.Field;

import org.junit.Test;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import static org.junit.Assert.*;

public class PartNameTest {

    @Test
    public void checkNames() throws IllegalAccessException {
        for (Field field : Parts.class.getDeclaredFields()) {
            assertEquals(field.getName(), field.get(null));
        }
    }
}