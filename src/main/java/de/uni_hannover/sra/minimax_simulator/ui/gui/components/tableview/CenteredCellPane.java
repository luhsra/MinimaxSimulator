package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * The {@code CenteredCellPane} is a {@link GridPane} that is used to center a {@link Node} in a {@link javafx.scene.control.TableCell}.
 *
 * @author Philipp Rohde
 */
public class CenteredCellPane extends GridPane {

    private final Node child;

    /**
     * Creates a {@code CenteredCellPane} with a {@link Node}.
     *
     * @param node
     *          the {@code Node} that should be centered
     */
    public CenteredCellPane(Node node) {
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
        this.setHalignment(child, HPos.CENTER);
        this.setValignment(child, VPos.CENTER);
    }

    /**
     * Creates a {@code CenteredCellPane} with a {@link Label}.
     *
     * @param label
     *          the text of the {@code Label}
     */
    public CenteredCellPane(String label) {
        this(new Label(label));
    }

    /**
     * Creates a {@code CenteredCellPane} with a styled {@link Label}.
     *
     * @param label
     *          the text of the {@code Label}
     * @param style
     *          the style of the {@code Label}
     */
    public CenteredCellPane(String label, String style) {
        this(label);
        if (style != null && !style.equals("")) {
            child.setStyle(style);
        }
    }
}
