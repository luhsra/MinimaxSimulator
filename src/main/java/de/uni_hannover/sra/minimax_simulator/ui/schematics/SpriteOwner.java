package de.uni_hannover.sra.minimax_simulator.ui.schematics;

import de.uni_hannover.sra.minimax_simulator.ui.schematics.render.Sprite;

/**
 * A {code SpriteOwner} is a class that contains {@link Sprite}s.
 *
 * @author Martin L&uuml;ck
 */
@FunctionalInterface
public interface SpriteOwner {

    /**
     * Creates the {@code Sprite} of the owner.
     *
     * @return
     *          the created {@code Sprite}
     */
    public Sprite createSprite();

}