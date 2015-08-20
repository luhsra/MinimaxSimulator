package de.uni_hannover.sra.minimax_simulator.gui.common;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;

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

			@Override
			public UnaryOperator<TextFormatter.Change> getFilter() {
				return new UnaryOperator<Change>() {
					@Override
					public Change apply(Change change) {
						if (change.isContentChange()) {
/*							ParsePosition parsePosition = new ParsePosition(0);
							// NumberFormat evaluates the beginning of the text
							NumberFormat format = NumberFormat.getIntegerInstance();
							format.parse(change.getControlNewText(), parsePosition);
							if (parsePosition.getIndex() == 0 ||
									parsePosition.getIndex() < change.getControlNewText().length()) {
								// reject, parsing the complete text failed
								return null;
							}	*/
							//TODO: improve
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
							System.out.println("HEY: " + newValue);
							if (!newValue.matches("-?[0-9a-fA-F]+")) {
								return null;
							}
						}
						return change;
					}
				};
			}
		};

		public StringConverter getConverter() {
			return null;
		}
		public UnaryOperator<TextFormatter.Change> getFilter() {
			return null;
		}
	}

	public NullAwareIntFormatter(Mode mode) {
		super(mode.getConverter(), 0, mode.getFilter());
		//super(mode.getConverter());
	}


}