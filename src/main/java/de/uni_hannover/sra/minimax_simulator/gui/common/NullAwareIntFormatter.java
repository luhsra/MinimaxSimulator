package de.uni_hannover.sra.minimax_simulator.gui.common;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

/**
 * TODO: documentation
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