package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.layout.AbstractComponent;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;

/**
 * A {@code Part} is a component part of a machine.<br>
 * <br>
 * It only needs the methods and fields provided by {@link AbstractComponent} and {@link Circuit}.
 * The purpose of this class is to only extend this class instead of extending {@code AbstractComponent}
 * <b>and</b> implementing {@code Circuit}.
 *
 * @author Martin L&uuml;ck
 */
public abstract class Part extends AbstractComponent implements Circuit {

}