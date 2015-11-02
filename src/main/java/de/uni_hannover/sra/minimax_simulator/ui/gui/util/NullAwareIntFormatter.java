package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;

/**
 * The {@code NullAwareIntFormatter} is a {@link TextFormatter} using the {@link NullAwareIntStringConverter}.<br>
 * This class is used for the multiplexer constant input {@link javafx.scene.control.Spinner}s of the {@link de.uni_hannover.sra.minimax_simulator.ui.gui.MuxView}.
 *
 * @author Philipp Rohde
 */
public class NullAwareIntFormatter extends TextFormatter {

	/** The mode the {@code NullAwareIntFormatter} uses for formatting. */
	public enum Mode {
		DEC {
			@Override
			public StringConverter getConverter() {
				// signed decimal
				return new NullAwareIntStringConverter(10, true);
			}

			@Override
			public UnaryOperator<TextFormatter.Change> getFilter() {
				return new UnaryOperator<Change>() {
					@Override
					public Change apply(Change change) {
						if (change.isContentChange()) {
							try {
								Integer.parseInt(change.getControlNewText());
							} catch (NumberFormatException e) {
								return null;
							}
						}
						return change;
					}
				};
			}
		},
		HEX {
			@Override
			public StringConverter getConverter() {
				// unsigned hexadecimal
				return new NullAwareIntStringConverter(16, false);
			}

			@Override
			public UnaryOperator<TextFormatter.Change> getFilter() {
				return new UnaryOperator<Change>() {
					@Override
					public Change apply(Change change) {
						if (change.isContentChange()) {
							String newValue = change.getControlNewText();
							if (!newValue.matches("-?[0-9a-fA-F]+") || newValue.length() > 8) {
								return null;
							}
							if (newValue.length() == 8 && newValue.compareTo("7FFFFFFF") == 1) {
								return null;
							}
						}
						return change;
					}
				};
			}
		};


		/**
		 * Gets the {@link StringConverter} for the mode.
		 *
		 * @return
		 *          the {@code StringConverter}
		 */
		public abstract StringConverter getConverter();

		/**
		 * Gets the used filter of the mode.
		 *
		 * @return
		 *          the filter for the change
		 */
		public abstract UnaryOperator<TextFormatter.Change> getFilter();
	}

	/**
	 * Constructs a new {@code NullAwareIntFormatter} with the specified {@link de.uni_hannover.sra.minimax_simulator.ui.gui.util.NullAwareIntFormatter.Mode}.
	 *
	 * @param mode
	 *          the mode
	 */
	public NullAwareIntFormatter(Mode mode) {
		super(mode.getConverter(), 0, mode.getFilter());
	}


}