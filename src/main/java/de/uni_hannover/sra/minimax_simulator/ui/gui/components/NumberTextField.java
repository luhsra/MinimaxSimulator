package de.uni_hannover.sra.minimax_simulator.ui.gui.components;

import javafx.scene.control.TextField;

/**
 * The {@code NumberTextField} is a {@link TextField} but only the empty String or a numeric String is allowed.
 *
 * @author Philipp Rohde
 */
public class NumberTextField extends TextField {

    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
        }
    }

    /**
     * Checks if a {@link String} is empty or numeric.
     *
     * @param text
     *          the String to validate
     * @return
     *          {@code true} if the String is empty or a number, {@code false} otherwise
     */
    private boolean validate(String text) {
        return ("".equals(text) || text.matches("[0-9]"));
    }
}
