package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Tests the class {@link Constant}.
 *
 * @author Philipp Rohde
 */
@RunWith(Parameterized.class)
public class ConstantTest {

    private final int value;
    private final String expected;
    private final Constant constant;

    /**
     * Initializes the test instance.
     *
     * @param value
     *          the value of the constant
     * @param expected
     *          the expected constant string
     */
    public ConstantTest(int value, String expected) {
        this.value = value;
        this.expected = expected;
        this.constant = new Constant(value);
    }

    /**
     * Creates the parameters for the test.
     *
     * @return
     *          the parameters for the test
     */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {  Integer.MIN_VALUE,   "0x80000000"},
                {Integer.MIN_VALUE+1,   "0x80000001"},
                {                -10,   "0xFFFFFFF6"},
                {                 -1,   "0xFFFFFFFF"},
                {                  0,   "0"},
                {                 10,   "10"},
                {              65534,   "65534"},
                {              65535,   "0xFFFF"},
                {              65536,   "0x10000"},
                {Integer.MAX_VALUE-1,   "0x7FFFFFFE"},
                {  Integer.MAX_VALUE,   "0x7FFFFFFF"}
        });
    }

    /**
     * Tests the constant string conversion.
     */
    @Test
    public void test() {
        assertEquals("constant string of " + value, expected, constant.getConstantStr());
    }
}
