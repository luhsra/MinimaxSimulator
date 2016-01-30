package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.MultiplexerSprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.render.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a multiplexer as component part of a machine.
 *
 * @author Martin L&uuml;ck
 */
public class Multiplexer extends SimplePart implements SpriteOwner {

    private final IngoingPin select = new IngoingPin(this);
    private final List<IngoingPin> dataIns;

    /**
     * Constructs a new {@code Multiplexer} without any data ins.
     */
    public Multiplexer() {
        this(0);
    }

    /**
     * Constructs a new {@code Multiplexer} with the specified amount of data ins.
     *
     * @param numberOfInputs
     *          the amount of data ins
     */
    public Multiplexer(int numberOfInputs) {
        dataIns = new ArrayList<>(numberOfInputs);
        for (int i = 0; i < numberOfInputs; i++) {
            dataIns.add(new IngoingPin(this));
        }
    }

    /**
     * Gets the {@link IngoingPin}s.
     *
     * @return
     *          a list of the data ins
     */
    public List<IngoingPin> getDataInputs() {
        return dataIns;
    }

    /**
     * Gets the selected {@link IngoingPin}.
     *
     * @return
     *          the selected data in
     */
    public IngoingPin getSelectPin() {
        return select;
    }

    @Override
    public void update() {
        int index = select.read();
        if (index < dataIns.size()) {
            getDataOut().write(dataIns.get(index).read());
        }
        else {
            getDataOut().write(0);
        }
    }

    @Override
    public Sprite createSprite() {
        return new MultiplexerSprite(this);
    }
}