package de.uni_hannover.sra.minimax_simulator.gui.common;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

/**
 * The NullAwareIntFormatter is a {@link TextFormatter} using the {@link NullAwareIntStringConverter}.
 * This class is used for the multiplexer constant input {@link javafx.scene.control.Spinner}s of the {@link de.uni_hannover.sra.minimax_simulator.gui.MuxView}.
 *
 * @author Philipp Rohde
 */
public class NullAwareIntFormatter extends TextFormatter
{

	public enum Mode {
		DEC {
			@Override
			public StringConverter getConverter() {
				// signed decimal
				return new NullAwareIntStringConverter(10, true);
			}
		},
		HEX {
			@Override
			public StringConverter getConverter() {
				// unsigned hexadecimal
				return new NullAwareIntStringConverter(16, false);
			}
		};

		public StringConverter getConverter() {
			return null;
		}
	}

	public NullAwareIntFormatter(Mode mode) {
		super(mode.getConverter());
	}
}