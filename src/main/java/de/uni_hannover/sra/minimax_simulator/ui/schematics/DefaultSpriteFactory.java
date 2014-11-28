package de.uni_hannover.sra.minimax_simulator.ui.schematics;

import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.render.SpriteFactory;

public class DefaultSpriteFactory implements SpriteFactory<SpriteOwner>
{
	@Override
	public Sprite createSprite(SpriteOwner owner)
	{
		return owner.createSprite();
	}
}
