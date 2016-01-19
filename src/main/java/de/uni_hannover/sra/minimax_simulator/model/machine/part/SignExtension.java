package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.SignExtSprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.render.Sprite;

/**
 * Implementation of a sign extension unit as component part of a machine.
 *
 * @author Martin L&uuml;ck
 */
public class SignExtension extends SimplePart implements SpriteOwner {

    private final IngoingPin dataIn;
    private final String label;

    /**
     * Constructs a new {@code SignExtension} with the specified display name.
     *
     * @param label
     *          the display name of the {@code SignExtension}
     */
    public SignExtension(String label) {
        dataIn = new IngoingPin(this);
        this.label = label;
    }

    /**
     * Gets the {@link IngoingPin}.
     *
     * @return
     *          the input pin
     */
    public IngoingPin getDataIn() {
        return dataIn;
    }

    @Override
    public void update() {
        int value = dataIn.read();

        int bit24 = value & 0x00800000;

        if (bit24 == 0)
            value = value & 0x00FFFFFF; // zero first 8 bits
        else
            value = value | 0xFF000000; // set first 8 bits

        getDataOut().write(value);
    }

    /**
     * Gets the display name of the {@code SignExtension}.
     *
     * @return
     *          the display name
     */
    public String getLabel() {
        return label;
    }

    @Override
    public Sprite createSprite() {
        return new SignExtSprite(this);
    }
}