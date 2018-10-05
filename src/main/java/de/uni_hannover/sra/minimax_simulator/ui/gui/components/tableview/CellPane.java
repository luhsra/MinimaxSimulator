package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * The {@code CellPane} is a {@link GridPane} that is used align a {@link Node} in a {@link javafx.scene.control.TableCell}.
 *
 * @author Philipp Rohde
 */
public class CellPane extends GridPane {

    private final Node child;

    /**
     * Creates a {@code CellPane} with a {@link Node}.
     *
     * @param node
     *          the {@code Node} of the CellPane
     */
    public CellPane(Node node) {
        super();

        child = node;
        this.add(child, 0, 0);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);
        columnConstraints.setHgrow(Priority.ALWAYS);
        this.getColumnConstraints().add(columnConstraints);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setFillHeight(true);
        rowConstraints.setVgrow(Priority.ALWAYS);
        this.getRowConstraints().add(rowConstraints);
        //this.setHalignment(child, HPos.CENTER);
        //this.setValignment(child, VPos.CENTER);
    }

    /**
     * Creates a {@code CellPane} with a {@link Label}.
     *
     * @param label
     *          the text of the {@code Label}
     */
    public CellPane(String label) {
        this(new Label(label));
    }

    /**
     * Creates a {@code CellPane} with a styled {@link Label}.
     *
     * @param label
     *          the text of the {@code Label}
     * @param style
     *          the style of the {@code Label}
     */
    public CellPane(String label, String style) {
        this(label);
        if (style != null && !"".equals(style)) {
            child.setStyle(style);
        }
    }

    /**
     * Creates a {@code CellPane} with signal-label style class.
     *
     * @param label
     *          the text of the {@code Label}
     * @param signalLabel
     *          whether or not the signal-label style class should be added
     */
    public CellPane(String label, boolean signalLabel) {
        this(label, "", signalLabel);
    }

    /**
     * Creates a {@code CellPane} with a styled {@link Label} with signal-label style class.
     *
     * @param label
     *          the text of the {@code Label}
     * @param style
     *          the style of the {@code Label}
     * @param signalLabel
     *          whether or not the signal-label style class should be added
     */
    public CellPane(String label, String style, boolean signalLabel) {
        this(label, style);
        if (signalLabel) {
            child.getStyleClass().add(0, "signal-label");
        }
    }
}
