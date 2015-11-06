package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import javafx.util.StringConverter;

/**
 * The {@code NullAwareIntStringConverter} is capable of generating a String representation of an Integer for different numeral systems e.g. unsigned hexadecimal and signed decimal.<br>
 * <br>
 * <b>Caution:</b><br>
 * Due to the use of the synchronous {@link javafx.scene.control.Spinner}s at {@link de.uni_hannover.sra.minimax_simulator.ui.gui.MuxView} the fromString method interprets every String as decimal input.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class NullAwareIntStringConverter extends StringConverter {

    private final int radix;
    private final boolean signed;

    /**
     * Constructs a {@code NullAwareIntStringConverter} with the specified radix and value of the {@code signed} property.
     *
     * @param radix
     *          the radix
     * @param signed
     *          whether the Integer is signed or not
     */
    public NullAwareIntStringConverter(int radix, boolean signed) {
        this.radix = radix;
        this.signed = signed;
    }

    /**
     * Constructs a {@code NullAwareIntStringConverter} for signed decimal numbers.
     */
    public NullAwareIntStringConverter() {
        this(10, true);
    }

    @Override
    public String toString(Object value) {
        if (value == null) {
            return "";
        }

        int nr = (Integer) value;
        String str;
        if (!signed) {
            // for unsigned values
            str = Long.toString(nr & 0xFFFFFFFFL, radix);
        }
        else {
            // for signed values
            str = Integer.toString(nr, radix);
        }
        return radix <= 10 ? str : str.toUpperCase();
    }

    @Override
    public Object fromString(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        try {
            Long l = Long.valueOf(text, radix);
            return l.intValue();
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
}
