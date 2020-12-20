package de.uni_hannover.sra.minimax_simulator;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the version comparison of {@link Version}.
 *
 * @author Philipp Rohde
 */
public class VersionTest {

    private static Version version;
    private static int major;
    private static int feature;
    private static int update;
    private static int build;

    /**
     * Initializes the test setup.
     */
    @BeforeClass
    public static void initialize() {
        version = new Version(VersionTest.class);
        major = version.getJvmMajor();
        feature = version.getJvmFeature();
        update = version.getJvmUpdate();
        build = version.getJvmBuild();
    }

    /**
     * Tests the initialization of the version class.
     */
    @Test
    public void testConstructor() {
        Version nullVersion = new Version(null);
        assertNotEquals("", String.valueOf(nullVersion.getJvmMajor()));
        assertNotEquals("", String.valueOf(nullVersion.getJvmBuild()));
        assertEquals("", nullVersion.getAuthorName());
        assertEquals("", nullVersion.getCompanyName());
        assertEquals("", nullVersion.getModuleName());
        assertEquals("", nullVersion.getBuildJdk());
        assertEquals("", nullVersion.getBuildTime());
        assertEquals("", nullVersion.getVersionNumber());
        assertEquals("", nullVersion.getRevisionNumber());
        assertFalse(nullVersion.isJar());

        String[] shortDesc = nullVersion.getShortInfoStrings();
        assertEquals(shortDesc[0], "[Module: ]");
        assertEquals(shortDesc[1], "Version:  - Revision: ");

        String[] fullDesc = nullVersion.getFullInfoStrings();
        assertEquals(fullDesc[0], "[Module: ]");
        assertEquals(fullDesc[1], "Version:  - Revision:  - Time:  - Build JDK: ");
    }

    /**
     * Tests the implementation of {@link Version#isJvmEqual(int, int, int, int)}.
     */
    @Test
    public void testIsJvmEqual() {
        assertTrue("isJvmEqual: same version", version.isJvmEqual(major, feature, update, build));
        assertFalse("isJvmEqual: different major", version.isJvmEqual(0, feature, update, build));
        assertFalse("isJvmEqual: different feature", version.isJvmEqual(major, feature - 2, update, build));
        assertFalse("isJvmEqual: different update", version.isJvmEqual(major, feature, update + 5, build));
        assertFalse("isJvmEqual: different build", version.isJvmEqual(major, feature, update, build + 100));
    }

    /**
     * Tests the implementation of {@link Version#isJvmLower(int, int, int, int)}.
     */
    @Test
    public void testIsJvmLower() {
        assertFalse("isJvmLower: same version", version.isJvmLower(major, feature, update, build));

        // higher running version
        assertFalse("isJvmLower: lower major", version.isJvmLower(0, feature, update, build));
        assertFalse("isJvmLower: lower feature", version.isJvmLower(major, feature - 2, update, build));
        assertFalse("isJvmLower: lower update", version.isJvmLower(major, feature, -5, build));
        assertFalse("isJvmLower: lower build", version.isJvmLower(major, feature, update, 0));

        // lower running version
        assertTrue("isJvmLower: higher major", version.isJvmLower(Integer.MAX_VALUE, feature, update, build));
        assertTrue("isJvmLower: higher feature", version.isJvmLower(major, feature + 2, update, build));
        assertTrue("isJvmLower: higher update", version.isJvmLower(major, feature, update + 5, build));
        assertTrue("isJvmLower: higher build", version.isJvmLower(major, feature, update, build + 100));
    }

    /**
     * Tests the implementation of {@link Version#isJvmLowerOrEqual(int, int, int, int)}.
     */
    @Test
    public void testIsJvmLowerOrEqual() {
        assertTrue("isJvmLowerOrEqual: same version", version.isJvmLowerOrEqual(major, feature, update, build));

        // higher running version
        assertFalse("isJvmLowerOrEqual: lower major", version.isJvmLowerOrEqual(0, feature, update, build));
        assertFalse("isJvmLowerOrEqual: lower feature", version.isJvmLowerOrEqual(major, feature - 2, update, build));
        assertFalse("isJvmLowerOrEqual: lower update", version.isJvmLowerOrEqual(major, feature, -5, build));
        assertFalse("isJvmLowerOrEqual: lower build", version.isJvmLowerOrEqual(major, feature, update, -1));

        // lower running version
        assertTrue("isJvmLowerOrEqual: higher major", version.isJvmLowerOrEqual(Integer.MAX_VALUE, feature, update, build));
        assertTrue("isJvmLowerOrEqual: higher feature", version.isJvmLowerOrEqual(major, feature + 2, update, build));
        assertTrue("isJvmLowerOrEqual: higher update", version.isJvmLowerOrEqual(major, feature, update + 5, build));
        assertTrue("isJvmLowerOrEqual: higher build", version.isJvmLowerOrEqual(major, feature, update, build + 100));
    }

    /**
     * Tests the implementation of {@link Version#isJvmHigher(int, int, int, int)}.
     */
    @Test
    public void testIsJvmHigher() {
        assertFalse("isJvmHigher: same version", version.isJvmHigher(major, feature, update, build));

        // higher running version
        assertTrue("isJvmHigher: lower major", version.isJvmHigher(0, feature, update, build));
        assertTrue("isJvmHigher: lower feature", version.isJvmHigher(major, feature - 2, update, build));
        assertTrue("isJvmHigher: lower update", version.isJvmHigher(major, feature, -5, build));
        assertTrue("isJvmHigher: lower build", version.isJvmHigher(major, feature, update, -1));

        // lower running version
        assertFalse("isJvmHigher: higher major", version.isJvmHigher(Integer.MAX_VALUE, feature, update, build));
        assertFalse("isJvmHigher: higher feature", version.isJvmHigher(major, feature + 2, update, build));
        assertFalse("isJvmHigher: higher update", version.isJvmHigher(major, feature, update + 5, build));
        assertFalse("isJvmHigher: higher build", version.isJvmHigher(major, feature, update, build + 100));
    }

    /**
     * Tests the implementation of {@link Version#isJvmHigherOrEqual(int, int, int, int)}.
     */
    @Test
    public void testIsJvmHigherOrEqual() {
        assertTrue("isJvmHigherOrEqual: same version", version.isJvmHigherOrEqual(major, feature, update, build));

        // higher running version
        assertTrue("isJvmHigherOrEqual: lower major", version.isJvmHigherOrEqual(0, feature, update, build));
        assertTrue("isJvmHigherOrEqual: lower feature", version.isJvmHigherOrEqual(major, feature - 2, update, build));
        assertTrue("isJvmHigherOrEqual: lower update", version.isJvmHigherOrEqual(major, feature, -5, build));
        assertTrue("isJvmHigherOrEqual: lower build", version.isJvmHigherOrEqual(major, feature, update, 0));

        // lower running version
        assertFalse("isJvmHigherOrEqual: higher major", version.isJvmHigherOrEqual(Integer.MAX_VALUE, feature, update, build));
        assertFalse("isJvmHigherOrEqual: higher feature", version.isJvmHigherOrEqual(major, feature + 2, update, build));
        assertFalse("isJvmHigherOrEqual: higher update", version.isJvmHigherOrEqual(major, feature, update + 5, build));
        assertFalse("isJvmHigherOrEqual: higher build", version.isJvmHigherOrEqual(major, feature, update, build + 100));
    }
}
