package de.uni_hannover.sra.minimax_simulator.ui.schematics;

import static com.google.common.base.Preconditions.*;

import de.uni_hannover.sra.minimax_simulator.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.Machine;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplayListener;
import de.uni_hannover.sra.minimax_simulator.ui.render.DefaultRenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.render.FXSpriteCanvas;
import javafx.scene.text.Font;

/**
 * The {@code MachineSchematics} are used to draw the schematics of the {@link Machine}.
 * All sprites of the machine's components will be drawn on a {@code Canvas}.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class MachineSchematics extends FXSpriteCanvas<SpriteOwner> implements MachineDisplayListener {

	public final static Font FONT = new Font("SansSerif", 17.0);

	private final Machine machine;

	/**
	 * Initializes the {@code MachineSchematics}.
	 *
	 * @param machine
	 *          the {@code Machine} for which the schematics should be drawn
	 */
	public MachineSchematics(Machine machine) {
		this.machine = checkNotNull(machine);
		this.machine.getDisplay().addMachineDisplayListener(this);

		setEnvironment(new DefaultRenderEnvironment(FONT, getFontMetrics(FONT)));
		setSpriteFactory(new DefaultSpriteFactory());

		this.machine.getDisplay().getAllSpriteOwners().forEach(sprite -> setSprite(sprite));

		updatePreferredSize();
	}

	/**
	 * Updates the size of the {@code Canvas}.
	 */
	private void updatePreferredSize() {
		Dimension dim = this.machine.getDisplay().getDimension();
		setSize(dim.getWidth(), dim.getHeight());
		draw();
	}

	@Override
	public void machineSizeChanged() {
		updatePreferredSize();
	}

	@Override
	public void machineDisplayChanged() {
		draw();
	}

	@Override
	public void onSpriteOwnerAdded(SpriteOwner spriteOwner) {
		setSprite(spriteOwner);
	}

	@Override
	public void onSpriteOwnerRemoved(SpriteOwner spriteOwner) {
		removeSprite(spriteOwner);
	}

	@Override
	public void onSpriteOwnerChanged(SpriteOwner spriteOwner) {
		setSprite(spriteOwner);
	}

}