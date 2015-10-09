package de.uni_hannover.sra.minimax_simulator.ui.render;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * A {@link Canvas} that holds and draws {@link Sprite}s.
 *
 * @param <T>
 *          the sprite owner class
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class FXSpriteCanvas<T> extends Canvas {

	private final Map<T, Sprite> _sprites;

	private RenderEnvironment _env;
	private SpriteFactory _spriteFactory;

	private final GraphicsContext gc;

	/**
	 * Initializes the {@code FXSpriteCanvas}.
	 */
	public FXSpriteCanvas() {
		_sprites = new HashMap<T, Sprite>();
		gc = this.getGraphicsContext2D();
	}

	/**
	 * Draws all {@code Sprite}s on a white background with a black border.
	 */
	protected void draw() {
		if (_env == null) {
			throw new IllegalStateException("Cannot render SpriteCanvas without RenderEnvironment set");
		}
		gc.setFont(_env.getFontFX());
		gc.clearRect(0, 0, getWidth(), getHeight());

		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, getWidth(), getHeight());

		drawBorder();

		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);

		for (Sprite sprite : _sprites.values()) {
			sprite.paint(gc, _env);
		}
	}

	/**
	 * Draws a thin border around the {@code FXSpriteCanvas}.
	 */
	private void drawBorder() {
		double maxY = getHeight();
		double maxX = getWidth();

		gc.save();
		gc.setLineWidth(1);

		gc.beginPath();
		gc.moveTo(0, 0);
		gc.lineTo(0, maxY);
		gc.lineTo(maxX, maxY);
		gc.lineTo(maxX, 0);
		gc.lineTo(0, 0);
		gc.closePath();
		gc.stroke();

		gc.restore();
	}

	/**
	 * Adds a {@code Sprite} and it's owner.
	 *
	 * @param owner
	 *          the owner of the {@code Sprite}
	 * @param sprite
	 *          the {@code Sprite} to add
	 */
	public void setSprite(T owner, Sprite sprite) {
		checkNotNull(owner, "Sprite owner must not be null");
		checkNotNull(sprite, "Sprite must not be null");

		_sprites.put(owner, sprite);
		draw();
	}

	/**
	 * Creates the {@code Sprite} of the owner and adds them.
	 *
	 * @param owner
	 *          the owner of the {@code Sprite} to add
	 */
	public void setSprite(T owner) {
		checkNotNull(owner, "Sprite owner must not be null");
		checkState(_spriteFactory != null, "Must provide a sprite or set a SpriteFactory");

		Sprite sprite = _spriteFactory.createSprite(owner);
		setSprite(owner, sprite);
	}

	/**
	 * Removes all {@code Sprite}s of an owner.
	 *
	 * @param owner
	 *          the {@code SpriteOwner} for which all {@code Sprites} will be removed
	 */
	public void removeSprite(T owner) {
		if (_sprites.remove(owner) != null)
			draw();
	}

	/**
	 * Gets the {@code SpriteFactory} of the {@code FXSpriteCanvas}.
	 *
	 * @return
	 *          the {@code SpriteFactory} of the {@code FXSpriteCanvas}
	 */
	public SpriteFactory getSpriteFactory() {
		return _spriteFactory;
	}

	/**
	 * Sets the {@code SpriteFactory} of the {@code FXSpriteCanvas}.
	 *
	 * @param spriteFactory
	 *          the {@code SpriteFactory} to set
	 */
	public void setSpriteFactory(SpriteFactory spriteFactory) {
		_spriteFactory = spriteFactory;
	}

	/**
	 * Gets the {@code RenderEnvironment} of the {@code FXSpriteCanvas}.
	 *
	 * @return
	 *          the {@code RenderEnvironment} of the {@code FXSpriteCanvas}
	 */
	public RenderEnvironment getRenderEnvironment() {
		return _env;
	}

	/**
	 * Sets the {@code RenderEnvironment} of the {@code FXSpriteCanvas}.
	 *
	 * @param env
	 *          the {@code RenderEnvironment} to set
	 */
	public void setEnvironment(RenderEnvironment env) {
		_env = env;
	}

	/**
	 * Get the {@link FontMetrics} of a {@link Font}.
	 *
	 * @param font
	 *          the {@code Font} for which the {@code FontMetrics} should be get
	 * @return
	 *          the {@code FontMetrics} of the {@code Font}
	 */
	public FontMetrics getFontMetrics(Font font) {
		return Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
	}

	/**
	 * Sets the width and height of the {@code FXSpriteCanvas}.
	 *
	 * @param width
	 *          the width to set
	 * @param height
	 *          the height to set
	 */
	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
}