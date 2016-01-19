package de.uni_hannover.sra.minimax_simulator.ui.schematics;

import de.uni_hannover.sra.minimax_simulator.ui.schematics.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.render.SpriteFactory;

/**
 * Default {@link SpriteFactory} with sprite owner class {@link SpriteOwner}.
 */
public class DefaultSpriteFactory implements SpriteFactory<SpriteOwner> {

    @Override
    public Sprite createSprite(SpriteOwner owner) {
        return owner.createSprite();
    }
}
