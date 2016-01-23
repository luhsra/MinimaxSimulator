package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import com.google.common.collect.Sets;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.AluSprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.render.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the component part ALU of a machine.
 *
 * @author Martin L&uuml;ck
 */
public class Alu extends Part implements SpriteOwner {

    private static final Logger LOG = Logger.getLogger(Alu.class.getName());

    private final List<AluOperation> aluOperations;

    private final IngoingPin inCtrl;
    private final IngoingPin inA;
    private final IngoingPin inB;
    private final OutgoingPin outData;
    private final OutgoingPin outZero;

    private int result;

    /**
     * Constructs a new instance of the {@code Alu}.
     */
    public Alu() {
        aluOperations = new ArrayList<>();

        inCtrl = new IngoingPin(this);
        inA = new IngoingPin(this);
        inB = new IngoingPin(this);
        outData = new OutgoingPin(this);
        outZero = new OutgoingPin(this);

        result = 0;
    }

    @Override
    public void update() {
        int mode = inCtrl.read();
        if (mode < aluOperations.size()) {
            AluOperation op = aluOperations.get(mode);
            result = op.execute(inA.read(), inB.read());

            if (LOG.isLoggable(Level.FINER)) {
                LOG.log(Level.FINER, "Applying ALU operation " + op + " to " + inA.read()
                        + " and " + inB.read() + " with result " + result);
            }
        }
        else {
            result = 0;

            if (LOG.isLoggable(Level.FINER)) {
                LOG.log(Level.FINER, "Unknown ALU operation: " + mode + ", writing 0");
            }
        }

        outZero.write(result == 0 ? 1 : 0);
        outData.write(result);
    }

    @Override
    public Set<? extends Circuit> getSuccessors() {
        return Sets.union(outData.getSuccessors(), outZero.getSuccessors());
    }

    /**
     * Gets the {@link AluOperation}s the {@code Alu} can execute.
     *
     * @return
     *          a list of the {@code AluOperation}s
     */
    public List<AluOperation> getAluOperations() {
        return aluOperations;
    }

    /**
     * Gets the control {@link IngoingPin}.
     *
     * @return
     *          the control pin
     */
    public IngoingPin getInCtrl() {
        return inCtrl;
    }

    /**
     * Gets the {@link IngoingPin} for port A.
     *
     * @return
     *          the pin of port A
     */
    public IngoingPin getInA() {
        return inA;
    }

    /**
     * Gets the {@link IngoingPin} for port B.
     *
     * @return
     *          the pin of port B
     */
    public IngoingPin getInB() {
        return inB;
    }

    /**
     * Gets the data {@link OutgoingPin}.
     *
     * @return
     *          the data pin
     */
    public OutgoingPin getOutData() {
        return outData;
    }

    /**
     * Gets the condition {@link OutgoingPin}.
     *
     * @return
     *          the ALU condition pin
     */
    public OutgoingPin getOutZero() {
        return outZero;
    }

    /**
     * Gets the ALU result.
     *
     * @return
     *          the result
     */
    public int getResult() {
        return result;
    }

    @Override
    public Sprite createSprite() {
        return new AluSprite(this);
    }

    @Override
    public void reset() {
        result = 0;
    }
}