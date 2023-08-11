package de.uni_hannover.sra.minimax_simulator.resources;

import org.junit.BeforeClass;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import static org.junit.Assert.*;

/**
 * Tests the implementation of all resource related classes.
 *
 * @see de.uni_hannover.sra.minimax_simulator.resources
 *
 * @author Philipp Rohde
 */
public class ResourceTest {

    private static DefaultResourceBundleLoader bundleENG;
    private static DefaultResourceBundleLoader bundleGER;

    /**
     * Initializes the test instance.
     */
    @BeforeClass
    public static void initialize() {
        Control control = new PropertyResourceControl("text/");
        bundleENG = new DefaultResourceBundleLoader(control, new Locale("base"));
        bundleGER = new DefaultResourceBundleLoader(control, Locale.GERMAN);
    }

    /**
     * Tests if the {@code Local} is correct.
     */
    @Test
    public void testLocale() {
        assertEquals("english", "base" , bundleENG.getDefaultLocale().toString());
        assertEquals("german", "de", bundleGER.getDefaultLocale().toString());
    }

    /**
     * Tests if the {@code ResourceBundle} loads the correct file.
     */
    @Test
    public void testBundle() {
        ResourceBundle signalBundle = bundleENG.getBundle("signal");

        assertTrue("contains col.label", signalBundle.containsKey("col.label"));
        assertTrue("contains col.aluselA", signalBundle.containsKey("col.aluselA"));
        assertTrue("contains col.breakpoint", signalBundle.containsKey("col.breakpoint"));
        assertTrue("contains col.condition", signalBundle.containsKey("col.condition"));
        assertFalse("does not contain register.name", signalBundle.containsKey("register.name"));
    }

    /**
     * Tests {@link TextResource#get(String)}.
     */
    @Test
    public void testTextResourceGet() {
        TextResource resSignal = bundleENG.getTextResource("signal");
        TextResource resSignalGER = bundleGER.getTextResource("signal");

        assertEquals("value of col.label", "Label", resSignal.get("col.label"));

        assertEquals("value of col.breakpoint", "Breakpoint", resSignal.get("col.breakpoint"));
        assertEquals("value of col.breakpoint (german)", "Haltepunkt", resSignalGER.get("col.breakpoint"));

        assertEquals("value of col.description", "Description", resSignal.get("col.description"));
        assertEquals("value of col.description", "Beschreibung", resSignalGER.get("col.description"));
    }

    /**
     * Tests {@link TextResource#using(String)} and therefore {@link PrefixTextResource}.
     */
    @Test
    public void testTextResourceUsing() {
        TextResource resSignal = bundleENG.getTextResource("signal").using("col");
        TextResource resSignalGER = bundleGER.getTextResource("signal").using("col");

        assertEquals("value of col.label", "Label", resSignal.get("label"));

        assertEquals("value of col.breakpoint", "Breakpoint", resSignal.get("breakpoint"));
        assertEquals("value of col.breakpoint (german)", "Haltepunkt", resSignalGER.get("breakpoint"));

        assertEquals("value of col.description", "Description", resSignal.get("description"));
        assertEquals("value of col.description", "Beschreibung", resSignalGER.get("description"));
    }

    /**
     * Tests {@link TextResource#format(String, Object...)}.
     */
    @Test
    public void testTextResourceFormat() {
        TextResource res = bundleENG.getTextResource("debugger");

        assertEquals("format cycles (String)", "Cycle: 123", res.format("cycles.label", "123"));
        assertEquals("format cycles (int)", "Cycle: 42", res.format("cycles.label", 42));

        res = bundleENG.getTextResource("project");

        assertEquals("format memory page (String, String)", "page 23 of 56", res.format("memtable.page", "23", "56"));
        assertEquals("format memory page (int, String)", "page 23 of 56", res.format("memtable.page", 23, "56"));
        assertEquals("format memory page (String, int)", "page 23 of 56", res.format("memtable.page", "23", 56));
        assertEquals("format memory page (int, int)", "page 23 of 56", res.format("memtable.page", 23, 56));
    }

    /**
     * Tests {@link TextResource#createFormat(String)}.
     */
    @Test
    public void testTextResourceCreateFormat() {
        TextResource res = bundleENG.getTextResource("debugger");
        MessageFormat halted = res.createFormat("cycles.label");
        MessageFormat read = res.createFormat("cycles.read.label");
        MessageFormat write = res.createFormat("cycles.write.label");
        final Object[] cyclesFormatParam = new Object[1];

        cyclesFormatParam[0] = "---";
        assertEquals("halted", "Cycle: ---", halted.format(cyclesFormatParam));

        cyclesFormatParam[0] = 42;
        assertEquals("read 42", "Cycle: 42 (read)", read.format(cyclesFormatParam));
        assertEquals("write 42", "Cycle: 42 (write)", write.format(cyclesFormatParam));
    }
}
