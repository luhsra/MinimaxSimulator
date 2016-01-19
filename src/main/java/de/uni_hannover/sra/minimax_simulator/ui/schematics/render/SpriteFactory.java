package de.uni_hannover.sra.minimax_simulator.ui.schematics.render;

/**
 * A {@code SpriteFactory} creates the {@link Sprite}s of the {@link de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner}s.
 *
 * @param <T>
 *     the sprite owner class
 *
 * @author Martin L&uuml;ck
 */
public interface SpriteFactory<T> {

    /**
     * Creates the {@code Sprite} of the {@code SpriteOwner}.
     *
     * @param owner
     *          the owner of the {@code Sprite} that will be created
     * @return
     *          the created {@code Sprite}
     */
    public Sprite createSprite(T owner);
}