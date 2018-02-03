package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TableView;

/**
 * A {@code PTableColumn} allows to specify a percentage for the width of the column of a {@link TableView}.
 *
 * @param <S>
 *          The type of the TableView generic type (i.e. S == TableView&lt;S&gt;)
 * @param <T>
 *          The type of the content in all cells in this TableColumn
 *
 * @author twasyl
 */
public class PTableColumn<S, T> extends javafx.scene.control.TableColumn<S, T> {

    private final DoubleProperty percentageWidth = new SimpleDoubleProperty(1);

    /**
     * Constructs a new {@code PTableColumn}.
     */
    public PTableColumn() {
        super();
        tableViewProperty().addListener((ov, t, t1) -> {
            if(PTableColumn.this.prefWidthProperty().isBound()) {
                PTableColumn.this.prefWidthProperty().unbind();
            }

            PTableColumn.this.prefWidthProperty().bind(t1.widthProperty().multiply(percentageWidth));
        });
    }

    /**
     * Gets the {@code percentage width} property.
     *
     * @return
     *          the {@code percentage width} property
     */
    public final DoubleProperty percentageWidthProperty() {
        return this.percentageWidth;
    }

    /**
     * Gets the value of the {@code percentage width} property.
     *
     * @return
     *          the value of the {@code percentage width}
     */
    public final double getPercentageWidth() {
        return this.percentageWidthProperty().get();
    }

    /**
     * Sets the value of the {@code percentage width}.
     *
     * @param value
     *          the new value
     * @throws IllegalArgumentException
     *          thrown if the argument was not in the range 0.0 to 1.0
     */
    public final void setPercentageWidth(double value) {
        if(value >= 0 && value <= 1) {
            this.percentageWidthProperty().set(value);
        } else {
            throw new IllegalArgumentException(String.format("The provided percentage width is not between 0.0 and 1.0. Value is: %1$s", value));
        }
    }
}
