package de.uni_hannover.sra.minimax_simulator.model.machine;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all tests belonging to the implementation of {@link SignalTable}.
 *
 * @see MachineSignalTable
 *
 * @author Philipp Rohde
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({SignalTableUpdateAluOpCodeTest.class, SignalTableUpdateAluSelCodeTest.class, SignalTableUpdateJumpTargetTest.class})
public class SignalTableTest {
    // this is a test suite!
}
