package de.uni_hannover.sra.minimax_simulator.model.machine;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.DefaultJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.user.NewProjectBuilder;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of updating jump targets for a {@link SignalTable}.
 *
 * @see MachineSignalTable
 *
 * @author Philipp Rohde
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignalTableUpdateJumpTargetTest {

    private static final SignalTable signalTable = new NewProjectBuilder().buildProject().getSignalTable();

    /**
     * Tests updating of jump targets of {@link ConditionalJump}s after adding a {@code SignalRow}.
     */
    @Test
    public void test01() {
        SignalRow affectedRow = signalTable.getRow(4);
        signalTable.addSignalRow(5, new SignalRow());
        assertEquals("jump target condition 0", 6, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 8, affectedRow.getJump().getTargetRow(4, 1));
    }

    /**
     * Tests updating of jump targets of {@link UnconditionalJump}s after adding a {@code SignalRow}.
     */
    @Test
    public void test02() {
        SignalRow affectedRow = signalTable.getRow(7);
        signalTable.addSignalRow(3, new SignalRow());
        assertEquals("jump target", 5, affectedRow.getJump().getTargetRow(8, 0));
    }

    /**
     * Tests updating of jump targets of {@link DefaultJump}s after adding a {@code SignalRow}.
     */
    @Test
    public void test03() {
        SignalRow affectedRow = signalTable.getRow(1);
        signalTable.addSignalRow(2, new SignalRow());
        assertEquals("jump target", 2, affectedRow.getJump().getTargetRow(1, 0));
    }

    /**
     * Tests updating of jump targets of {@link DefaultJump}s after removing a {@code SignalRow}.
     */
    @Test
    public void test04() {
        SignalRow affectedRow = signalTable.getRow(1);
        signalTable.removeSignalRow(2);
        assertEquals("jump target", 2, affectedRow.getJump().getTargetRow(1, 0));
    }

    /**
     * Tests updating of jump targets of {@link UnconditionalJump}s after removing a {@code SignalRow}.
     */
    @Test
    public void test05() {
        SignalRow affectedRow = signalTable.getRow(8);
        signalTable.removeSignalRow(3);
        assertEquals("jump target", 4, affectedRow.getJump().getTargetRow(7, 0));
    }

    /**
     * Tests updating of jump targets of {@link ConditionalJump}s after removing a {@code SignalRow}.
     */
    @Test
    public void test06() {
        SignalRow affectedRow = signalTable.getRow(4);
        signalTable.removeSignalRow(5);
        assertEquals("jump target condition 0", 5, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 1));
    }

    /**
     * Tests updating of jump targets of {@link ConditionalJump}s after moving a {@code SignalRow}.
     */
    @Test
    public void test07() {
        SignalRow affectedRow = signalTable.getRow(4);

        // move condition 0 target row
        signalTable.moveSignalRows(5, 5, 1);
        assertEquals("jump target condition 0", 6, affectedRow.getJump().getTargetRow(4, 0));

        signalTable.moveSignalRows(6, 6, -1);
        assertEquals("jump target condition 0", 5, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 1));

        signalTable.moveSignalRows(5, 5, 1);
        signalTable.moveSignalRows(6, 6, 1);
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 1", 6, affectedRow.getJump().getTargetRow(4, 1));

        signalTable.moveSignalRows(7, 7, -1);
        signalTable.moveSignalRows(6, 6, -1);
        assertEquals("jump target condition 0", 5, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 1));

        // move condition 1 target row
        signalTable.moveSignalRows(7, 7, -1);
        assertEquals("jump target condition 0", 5, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 6, affectedRow.getJump().getTargetRow(4, 1));

        signalTable.moveSignalRows(6, 6, 1);
        assertEquals("jump target condition 0", 5, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 1));

        signalTable.moveSignalRows(7, 7, -1);
        signalTable.moveSignalRows(6, 6, -1);
        signalTable.moveSignalRows(5, 5, -1);
        signalTable.moveSignalRows(4, 4, -1);
        assertEquals("jump target condition 0", 6, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 3, affectedRow.getJump().getTargetRow(4, 1));

        signalTable.moveSignalRows(3, 3, 1);
        signalTable.moveSignalRows(4, 4, 1);
        signalTable.moveSignalRows(5, 5, 1);
        signalTable.moveSignalRows(6, 6, 1);
        assertEquals("jump target condition 0", 5, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 1));

        // move row with the targets itself
        signalTable.moveSignalRows(4, 4, -1);
        assertEquals("jump target condition 0", 5, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 1));

        signalTable.moveSignalRows(3, 3, -1);
        assertEquals("jump target condition 0", 5, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 1));

        signalTable.moveSignalRows(2, 2, 1);
        assertEquals("jump target condition 0", 5, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 1));

        signalTable.moveSignalRows(3, 3, 1);
        assertEquals("jump target condition 0", 5, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 1));

        signalTable.moveSignalRows(4, 4, 1);
        assertEquals("jump target condition 0", 4, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 1));

        signalTable.moveSignalRows(5, 5, 1);
        signalTable.moveSignalRows(6, 6, 1);
        assertEquals("jump target condition 0", 4, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 6, affectedRow.getJump().getTargetRow(4, 1));

        signalTable.moveSignalRows(7, 7, -1);
        signalTable.moveSignalRows(6, 6, -1);
        signalTable.moveSignalRows(5, 5, -1);
        assertEquals("jump target condition 0", 5, affectedRow.getJump().getTargetRow(4, 0));
        assertEquals("jump target condition 0", 7, affectedRow.getJump().getTargetRow(4, 1));
    }

    /**
     * Tests updating of jump targets of {@link UnconditionalJump}s after moving a {@code SignalRow}.
     */
    @Test
    public void test08() {
        SignalRow affectedRow = signalTable.getRow(6);

        // move target row
        signalTable.moveSignalRows(4, 4, 1);
        assertEquals("jump target", 5, affectedRow.getJump().getTargetRow(6, 0));

        signalTable.moveSignalRows(5, 5, -1);
        assertEquals("jump target", 4, affectedRow.getJump().getTargetRow(6, 0));

        // move row with the jump target itself
        signalTable.moveSignalRows(6, 6, 1);
        assertEquals("jump target", 4, affectedRow.getJump().getTargetRow(6, 0));

        signalTable.moveSignalRows(7, 7, -1);
        assertEquals("jump target", 4, affectedRow.getJump().getTargetRow(6, 0));

        signalTable.moveSignalRows(6, 6, -1);
        signalTable.moveSignalRows(5, 5, -1);
        assertEquals("jump target", 5, affectedRow.getJump().getTargetRow(6, 0));

        signalTable.moveSignalRows(4, 4, -1);
        assertEquals("jump target", 5, affectedRow.getJump().getTargetRow(6, 0));
    }

    /**
     * Tests updating of jump targets of {@link DefaultJump} after moving a {@code SignalRow}.
     */
    @Test
    public void test09() {
        SignalRow affectedRow = signalTable.getRow(1);

        // move target row
        signalTable.moveSignalRows(2, 2, 1);
        assertEquals("jump target", 2, affectedRow.getJump().getTargetRow(1, 0));

        signalTable.moveSignalRows(2, 2, -1);
        assertEquals("jump target", 3, affectedRow.getJump().getTargetRow(2, 0));

        // move row itself
        signalTable.moveSignalRows(2, 2, 1);
        assertEquals("jump target", 4, affectedRow.getJump().getTargetRow(3, 0));

        signalTable.moveSignalRows(3, 3, 1);
        assertEquals("jump target", 5, affectedRow.getJump().getTargetRow(4, 0));

        signalTable.moveSignalRows(4, 4, -1);
        assertEquals("jump target", 4, affectedRow.getJump().getTargetRow(3, 0));

        signalTable.moveSignalRows(3, 3, -1);
        assertEquals("jump target", 3, affectedRow.getJump().getTargetRow(2, 0));
    }
}
