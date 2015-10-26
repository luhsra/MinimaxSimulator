package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import javafx.scene.control.SpinnerValueFactory;

/**
 * A {@link SpinnerValueFactory} for hexadecimal values.<br>
 * The range is the same as of {@link Integer}.
 *
 * @author Philipp Rohde
 */
public class HexSpinnerValueFactory extends SpinnerValueFactory.IntegerSpinnerValueFactory {

    /**
     * Constructs a {@code HexSpinnerValueFactory} and sets the converter to {@link HexStringConverter}.
     */
    public HexSpinnerValueFactory() {
        // set range in parent class
        super(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.setWrapAround(true);

        // render values as hexadecimal String
        this.setConverter(new HexStringConverter(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

}
