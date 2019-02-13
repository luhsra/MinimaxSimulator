package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplay;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplayListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.FontMetrics;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.render.DummyRenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.render.RenderEnvironment;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the {@link MachineDisplay} for a Minimax machine.
 *
 * @author Martin L&uuml;ck
 */
class MinimaxDisplay implements MachineDisplay, FontMetricsProvider {

    private final List<MachineDisplayListener> listeners;

    private final Set<SpriteOwner> spriteOwners;

    private RenderEnvironment renderEnvironment;
    private Dimension dimension;

    /**
     * Constructs a new and empty {@code MinimaxDisplay}.
     */
    public MinimaxDisplay() {
        listeners = new ArrayList<>(1);
        spriteOwners = new HashSet<>();

        renderEnvironment = new DummyRenderEnvironment();
        dimension = Dimension.ZERO;
    }

    @Override
    public RenderEnvironment getRenderEnvironment() {
        return renderEnvironment;
    }

    @Override
    public void setRenderEnvironment(RenderEnvironment env) {
        renderEnvironment = env;

        listeners.forEach(MachineDisplayListener::machineDisplayChanged);
    }

    @Override
    public void addSpriteOwner(SpriteOwner spriteOwner) {
        spriteOwners.add(spriteOwner);
        fireSpriteOwnerAdded(spriteOwner);
    }

    @Override
    public void removeSpriteOwner(SpriteOwner spriteOwner) {
        spriteOwners.remove(spriteOwner);
        fireSpriteOwnerRemoved(spriteOwner);
    }

    /**
     * Updates a {@link SpriteOwner}.
     *
     * @param spriteOwner
     *          the updated {@code SpriteOwner}
     */
    public void updateSpriteOwner(SpriteOwner spriteOwner) {
        fireSpriteOwnerChanged(spriteOwner);
    }

    /**
     * Adds all {@link SpriteOwner}s of the specified {@link Group}.
     *
     * @param group
     *          the {@code Group} whose {@code SpriteOwner}s will be added
     */
    public void addGroup(Group group) {
        group.getSpriteOwners().forEach(this::addSpriteOwner);
    }

    /**
     * Removes all {@link SpriteOwner}s of the specified {@link Group}.
     *
     * @param group
     *          the {@code Group} whose {@code SpriteOwner}s will be removed
     */
    public void removeGroup(Group group) {
        group.getSpriteOwners().forEach(this::removeSpriteOwner);
    }

    @Override
    public void addMachineDisplayListener(MachineDisplayListener l) {
        listeners.add(l);
    }

    @Override
    public void removeMachineDisplayListener(MachineDisplayListener l) {
        listeners.remove(l);
    }

    /**
     * Sets the {@link Dimension} of the {@code MinimaxDisplay} and notifies
     * the {@link MachineDisplayListener}s about it.
     *
     * @param dimension
     *          the new {@code Dimension}
     */
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;

        listeners.forEach(MachineDisplayListener::machineSizeChanged);
    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public Set<SpriteOwner> getAllSpriteOwners() {
        return spriteOwners;
    }

    /**
     * Notifies the {@link MachineDisplayListener}s of the addition of a {@link SpriteOwner}.
     *
     * @param spriteOwner
     *          the added {@code SpriteOwner}
     */
    protected void fireSpriteOwnerAdded(SpriteOwner spriteOwner) {
        for (MachineDisplayListener l : listeners) {
            l.onSpriteOwnerAdded(spriteOwner);
        }
    }

    /**
     * Notifies the {@link MachineDisplayListener}s of the removal of a {@link SpriteOwner}.
     *
     * @param spriteOwner
     *          the removed {@code SpriteOwner}
     */
    protected void fireSpriteOwnerRemoved(SpriteOwner spriteOwner) {
        for (MachineDisplayListener l : listeners) {
            l.onSpriteOwnerRemoved(spriteOwner);
        }
    }

    /**
     * Notifies the {@link MachineDisplayListener}s of the change of a {@link SpriteOwner}.
     *
     * @param spriteOwner
     *          the changed {@code SpriteOwner}
     */
    protected void fireSpriteOwnerChanged(SpriteOwner spriteOwner) {
        for (MachineDisplayListener l : listeners) {
            l.onSpriteOwnerChanged(spriteOwner);
        }
    }

    @Override
    public Font getFont() {
        return renderEnvironment.getFont();
    }

    @Override
    public FontMetrics getFontMetrics() {
        return renderEnvironment.getFontMetrics();
    }
}