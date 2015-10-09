package de.uni_hannover.sra.minimax_simulator.ui.render;

import static com.google.common.base.Preconditions.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

// TODO: update to JavaFX
public class SpriteCanvas<T> extends JPanel
{
	private final Map<T, Sprite> _sprites;

	private RenderEnvironment _env;
	private SpriteFactory _spriteFactory;

	public SpriteCanvas()
	{
		_sprites = new HashMap<T, Sprite>();
	}

	@Override
	public void paintComponent(Graphics g0)
	{
		if (_env == null)
			throw new IllegalStateException("Cannot render SpriteCanvas without RenderEnvironment set");

		assert (g0 instanceof Graphics2D);
		Graphics2D g = _env.createGraphics((Graphics2D) g0);

		g.setColor(_env.getBackgroundColor());
		g.setBackground(_env.getBackgroundColor());
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(_env.getForegroundColor());

		for (Sprite sprite : _sprites.values())
			sprite.paint(g, _env);

		g.dispose();
	}

	public void setSprite(T owner, Sprite sprite)
	{
		checkNotNull(owner, "Sprite owner must not be null");
		checkNotNull(sprite, "Sprite must not be null");

		_sprites.put(owner, sprite);
		repaint();
	}

	public void setSprite(T owner)
	{
		checkNotNull(owner, "Sprite owner must not be null");
		checkState(_spriteFactory != null, "Must provide a sprite or set a SpriteFactory");

		Sprite sprite = _spriteFactory.createSprite(owner);
		setSprite(owner, sprite);
	}

	public void removeSprite(T owner)
	{
		if (_sprites.remove(owner) != null)
			repaint();
	}

	public SpriteFactory getSpriteFactory()
	{
		return _spriteFactory;
	}

	public void setSpriteFactory(SpriteFactory spriteFactory)
	{
		_spriteFactory = spriteFactory;
	}

	public RenderEnvironment getRenderEnvironment()
	{
		return _env;
	}

	public void setEnvironment(RenderEnvironment env)
	{
		_env = env;
	}
}