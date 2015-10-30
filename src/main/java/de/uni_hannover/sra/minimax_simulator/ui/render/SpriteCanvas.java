package de.uni_hannover.sra.minimax_simulator.ui.render;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

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
public class SpriteCanvas<T> extends Canvas {

	private final Map<T, Sprite> sprites;

	private RenderEnvironment env;
	private SpriteFactory spritefactory;

	private final GraphicsContext gc;

	/**
	 * Initializes the {@code SpriteCanvas}.
	 */
	public SpriteCanvas() {
		sprites = new HashMap<T, Sprite>();
		gc = this.getGraphicsContext2D();
	}

	/**
	 * Draws all {@code Sprite}s on a {@code Canvas} with a black border.<br>
	 * The background color is defined by {@link RenderEnvironment#getBackgroundColor()} and the
	 * {@code Sprite}'s color by {@link RenderEnvironment#getForegroundColor()}.
	 *
	 * @throws IllegalStateException
	 *          thrown if the {@code RenderEnvironment} was not set yet
	 */
	protected void draw() {
		if (env == null) {
			throw new IllegalStateException("Cannot render SpriteCanvas without RenderEnvironment set");
		}
		gc.setFont(env.getFont());
		gc.clearRect(0, 0, getWidth(), getHeight());

		gc.setFill(env.getBackgroundColor());
		gc.fillRect(0, 0, getWidth(), getHeight());

		drawBorder();

		gc.setFill(env.getForegroundColor());
		gc.setStroke(env.getForegroundColor());

		for (Sprite sprite : sprites.values()) {
			sprite.paint(gc, env);
		}
	}

	/**
	 * Draws a thin border around the {@code SpriteCanvas}.
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

		sprites.put(owner, sprite);
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
		checkState(spritefactory != null, "Must provide a sprite or set a SpriteFactory");

		Sprite sprite = spritefactory.createSprite(owner);
		setSprite(owner, sprite);
	}

	/**
	 * Removes all {@code Sprite}s of an owner.
	 *
	 * @param owner
	 *          the {@code SpriteOwner} for which all {@code Sprite}s will be removed
	 */
	public void removeSprite(T owner) {
		if (sprites.remove(owner) != null)
			draw();
	}

	/**
	 * Gets the {@code SpriteFactory} of the {@code SpriteCanvas}.
	 *
	 * @return
	 *          the {@code SpriteFactory} of the {@code SpriteCanvas}
	 */
	public SpriteFactory getSpriteFactory() {
		return spritefactory;
	}

	/**
	 * Sets the {@code SpriteFactory} of the {@code SpriteCanvas}.
	 *
	 * @param spriteFactory
	 *          the {@code SpriteFactory} to set
	 */
	public void setSpriteFactory(SpriteFactory spriteFactory) {
		spritefactory = spriteFactory;
	}

	/**
	 * Gets the {@code RenderEnvironment} of the {@code SpriteCanvas}.
	 *
	 * @return
	 *          the {@code RenderEnvironment} of the {@code SpriteCanvas}
	 */
	public RenderEnvironment getRenderEnvironment() {
		return env;
	}

	/**
	 * Sets the {@code RenderEnvironment} of the {@code SpriteCanvas}.
	 *
	 * @param env
	 *          the {@code RenderEnvironment} to set
	 */
	public void setEnvironment(RenderEnvironment env) {
		this.env = env;
	}

	/**
	 * Gets the {@link FontMetrics} of a {@link Font}.
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
	 * Sets the width and height of the {@code SpriteCanvas}.
	 *
	 * @param width
	 *          the new width
	 * @param height
	 *          the new height
	 */
	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
}