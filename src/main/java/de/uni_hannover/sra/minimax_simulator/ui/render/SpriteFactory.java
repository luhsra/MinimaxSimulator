package de.uni_hannover.sra.minimax_simulator.ui.render;

public interface SpriteFactory<T>
{
	public Sprite createSprite(T owner);
}