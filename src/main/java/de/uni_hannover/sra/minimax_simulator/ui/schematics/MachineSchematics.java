package de.uni_hannover.sra.minimax_simulator.ui.schematics;

import static com.google.common.base.Preconditions.*;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.Machine;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplayListener;
import de.uni_hannover.sra.minimax_simulator.ui.render.DefaultRenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.render.SpriteCanvas;

public class MachineSchematics extends SpriteCanvas<SpriteOwner> implements MachineDisplayListener {

	public final static Font	FONT	= new Font("SansSerif", Font.PLAIN, 17);

	private final Machine		_machine;

	public MachineSchematics(Machine machine)
	{
		_machine = checkNotNull(machine);

		setEnvironment(new DefaultRenderEnvironment(FONT, this.getFontMetrics(FONT)));
		setSpriteFactory(new DefaultSpriteFactory());

		for (SpriteOwner sprite : _machine.getDisplay().getAllSpriteOwners()) {
            setSprite(sprite);
        }

		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		updatePreferredSize();
	}

	private void updatePreferredSize()
	{
		setPreferredSize(_machine.getDisplay().getDimension().toAWT());
		revalidate();
		repaint();
	}

	@Override
	public void machineSizeChanged()
	{
		updatePreferredSize();
	}

	@Override
	public void machineDisplayChanged()
	{
		repaint();
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

	public int getWidth() {
		return _machine.getDisplay().getDimension().toAWT().width;
	}
}