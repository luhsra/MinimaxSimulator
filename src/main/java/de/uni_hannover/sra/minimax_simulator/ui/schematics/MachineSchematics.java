package de.uni_hannover.sra.minimax_simulator.ui.schematics;

import static com.google.common.base.Preconditions.*;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.Machine;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplayListener;
import de.uni_hannover.sra.minimax_simulator.ui.render.DefaultRenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.render.FXSpriteCanvas;
import de.uni_hannover.sra.minimax_simulator.ui.render.SpriteCanvas;

// TODO: update to JavaFX
public class MachineSchematics extends FXSpriteCanvas<SpriteOwner> implements MachineDisplayListener {

	public final static Font	FONT	= new Font("SansSerif", Font.PLAIN, 17);
	public final static javafx.scene.text.Font FXFONT = new javafx.scene.text.Font("SansSerif", 17.0);

	private final Machine		_machine;

	public MachineSchematics(Machine machine)
	{
		_machine = checkNotNull(machine);

		setEnvironment(new DefaultRenderEnvironment(FXFONT, getFontMetrics(FXFONT)));
		setSpriteFactory(new DefaultSpriteFactory());

		for (SpriteOwner sprite : _machine.getDisplay().getAllSpriteOwners()) {
            setSprite(sprite);
        }

		// TODO: set Border via CSS
		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
		updatePreferredSize();
	}

	private void updatePreferredSize()
	{
		//setPreferredSize(_machine.getDisplay().getDimension().toAWT());
		//revalidate();
		//repaint();
		this.setSize(_machine.getDisplay().getDimension().toAWT());
		draw();
	}

	@Override
	public void machineSizeChanged()
	{
		updatePreferredSize();
	}

	@Override
	public void machineDisplayChanged()
	{
		//repaint();
		draw();
	}

	@Override
	public void onSpriteOwnerAdded(SpriteOwner spriteOwner)
	{
		setSprite(spriteOwner);
	}

	@Override
	public void onSpriteOwnerRemoved(SpriteOwner spriteOwner)
	{
		removeSprite(spriteOwner);
	}

	@Override
	public void onSpriteOwnerChanged(SpriteOwner spriteOwner)
	{
		setSprite(spriteOwner);
	}
/*
	public int getWidth() {
		return _machine.getDisplay().getDimension().toAWT().width;
	}	*/
}