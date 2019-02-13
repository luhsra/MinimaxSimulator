package de.uni_hannover.sra.minimax_simulator;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals(false, "".equals(String.valueOf(nullVersion.getJvmMajor())));
        assertEquals(false, "".equals(String.valueOf(nullVersion.getJvmBuild())));
        assertEquals(true, "".equals(nullVersion.getAuthorName()));
        assertEquals(true, "".equals(nullVersion.getCompanyName()));
        assertEquals(true, "".equals(nullVersion.getModuleName()));
        assertEquals(true, "".equals(nullVersion.getBuildJdk()));
        assertEquals(true, "".equals(nullVersion.getBuildTime()));
        assertEquals(true, "".equals(nullVersion.getVersionNumber()));
        assertEquals(true, "".equals(nullVersion.getRevisionNumber()));
        assertEquals(false, nullVersion.isJar());

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
        assertEquals("isJvmEqual: same version", true, version.isJvmEqual(major, feature, update, build));
        assertEquals("isJvmEqual: different major", false, version.isJvmEqual(0, feature, update, build));
        assertEquals("isJvmEqual: different feature", false, version.isJvmEqual(major, feature - 2, update, build));
        assertEquals("isJvmEqual: different update", false, version.isJvmEqual(major, feature, update + 5, build));
        assertEquals("isJvmEqual: different build", false, version.isJvmEqual(major, feature, update, build + 100));
    }

    /**
     * Tests the implementation of {@link Version#isJvmLower(int, int, int, int)}.
     */
    @Test
    public void testIsJvmLower() {
        assertEquals("isJvmLower: same version", false, version.isJvmLower(major, feature, update, build));

        // higher running version
        assertEquals("isJvmLower: lower major", false, version.isJvmLower(0, feature, update, build));
        assertEquals("isJvmLower: lower feature", false, version.isJvmLower(major, feature - 2, update, build));
        assertEquals("isJvmLower: lower update", false, version.isJvmLower(major, feature, -5, build));
        assertEquals("isJvmLower: lower build", false, version.isJvmLower(major, feature, update, 0));

        // lower running version
        assertEquals("isJvmLower: higher major", true, version.isJvmLower(Integer.MAX_VALUE, feature, update, build));
        assertEquals("isJvmLower: higher feature", true, version.isJvmLower(major, feature + 2, update, build));
        assertEquals("isJvmLower: higher update", true, version.isJvmLower(major, feature, update + 5, build));
        assertEquals("isJvmLower: higher build", true, version.isJvmLower(major, feature, update, build + 100));
    }

    /**
     * Tests the implementation of {@link Version#isJvmLowerOrEqual(int, int, int, int)}.
     */
    @Test
    public void testIsJvmLowerOrEqual() {
        assertEquals("isJvmLowerOrEqual: same version", true, version.isJvmLowerOrEqual(major, feature, update, build));

        // higher running version
        assertEquals("isJvmLowerOrEqual: lower major", false, version.isJvmLowerOrEqual(0, feature, update, build));
        assertEquals("isJvmLowerOrEqual: lower feature", false, version.isJvmLowerOrEqual(major, feature - 2, update, build));
        assertEquals("isJvmLowerOrEqual: lower update", false, version.isJvmLowerOrEqual(major, feature, -5, build));
        assertEquals("isJvmLowerOrEqual: lower build", false, version.isJvmLowerOrEqual(major, feature, update, -1));

        // lower running version
        assertEquals("isJvmLowerOrEqual: higher major", true, version.isJvmLowerOrEqual(Integer.MAX_VALUE, feature, update, build));
        assertEquals("isJvmLowerOrEqual: higher feature", true, version.isJvmLowerOrEqual(major, feature + 2, update, build));
        assertEquals("isJvmLowerOrEqual: higher update", true, version.isJvmLowerOrEqual(major, feature, update + 5, build));
        assertEquals("isJvmLowerOrEqual: higher build", true, version.isJvmLowerOrEqual(major, feature, update, build + 100));
    }

    /**
     * Tests the implementation of {@link Version#isJvmHigher(int, int, int, int)}.
     */
    @Test
    public void testIsJvmHigher() {
        assertEquals("isJvmHigher: same version", false, version.isJvmHigher(major, feature, update, build));

        // higher running version
        assertEquals("isJvmHigher: lower major", true, version.isJvmHigher(0, feature, update, build));
        assertEquals("isJvmHigher: lower feature", true, version.isJvmHigher(major, feature - 2, update, build));
        assertEquals("isJvmHigher: lower update", true, version.isJvmHigher(major, feature, -5, build));
        assertEquals("isJvmHigher: lower build", true, version.isJvmHigher(major, feature, update, -1));

        // lower running version
        assertEquals("isJvmHigher: higher major", false, version.isJvmHigher(Integer.MAX_VALUE, feature, update, build));
        assertEquals("isJvmHigher: higher feature", false, version.isJvmHigher(major, feature + 2, update, build));
        assertEquals("isJvmHigher: higher update", false, version.isJvmHigher(major, feature, update + 5, build));
        assertEquals("isJvmHigher: higher build", false, version.isJvmHigher(major, feature, update, build + 100));
    }

    /**
     * Tests the implementation of {@link Version#isJvmHigherOrEqual(int, int, int, int)}.
     */
    @Test
    public void testIsJvmHigherOrEqual() {
        assertEquals("isJvmHigherOrEqual: same version", true, version.isJvmHigherOrEqual(major, feature, update, build));

        // higher running version
        assertEquals("isJvmHigherOrEqual: lower major", true, version.isJvmHigherOrEqual(0, feature, update, build));
        assertEquals("isJvmHigherOrEqual: lower feature", true, version.isJvmHigherOrEqual(major, feature - 2, update, build));
        assertEquals("isJvmHigherOrEqual: lower update", true, version.isJvmHigherOrEqual(major, feature, -5, build));
        assertEquals("isJvmHigherOrEqual: lower build", true, version.isJvmHigherOrEqual(major, feature, update, 0));

        // lower running version
        assertEquals("isJvmHigherOrEqual: higher major", false, version.isJvmHigherOrEqual(Integer.MAX_VALUE, feature, update, build));
        assertEquals("isJvmHigherOrEqual: higher feature", false, version.isJvmHigherOrEqual(major, feature + 2, update, build));
        assertEquals("isJvmHigherOrEqual: higher update", false, version.isJvmHigherOrEqual(major, feature, update + 5, build));
        assertEquals("isJvmHigherOrEqual: higher build", false, version.isJvmHigherOrEqual(major, feature, update, build + 100));
    }
}
