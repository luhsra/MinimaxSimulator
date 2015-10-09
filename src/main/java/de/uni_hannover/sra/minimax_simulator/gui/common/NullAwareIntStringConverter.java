package de.uni_hannover.sra.minimax_simulator.gui.common;

import javafx.util.StringConverter;

/**
 * The NullAwareIntStringConverter is capable of generating a String representation of an Integer for different numeral systems e.g. unsigned hexadecimal and signed decimal.<br>
 * <br>
 * <b>Caution:</b><br>
 * Due to the use of the synchronous {@link javafx.scene.control.Spinner}s at {@link de.uni_hannover.sra.minimax_simulator.gui.MuxView} the fromString method interprets every String as decimal input.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class NullAwareIntStringConverter extends StringConverter {

    private final int _radix;
    private final boolean _signed;

    public NullAwareIntStringConverter(int radix, boolean signed) {
        _radix = radix;
        _signed = signed;
    }

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
        if (!_signed) {
            // for unsigned values
            str = Long.toString(nr & 0xFFFFFFFFL, _radix);
        }
        else {
            // for signed values
            str = Integer.toString(nr, _radix);
        }
        return _radix <= 10 ? str : str.toUpperCase();
    }

    @Override
    public Object fromString(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        try {
            Long l = Long.valueOf(text, _radix);
            return l.intValue();
        } catch (NumberFormatException nfe) {
            return null;        //TODO: exception handling; doesn't seem to be needed
        }
    }
}
