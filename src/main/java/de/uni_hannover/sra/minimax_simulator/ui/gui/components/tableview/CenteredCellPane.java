package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * The {@code CenteredCellPane} is a {@link GridPane} that is used to center a {@link Node} in a {@link javafx.scene.control.TableCell}.
 *
 * @author Philipp Rohde
 */
public class CenteredCellPane extends CellPane {

    private final Node child;

    /**
     * Creates a {@code CenteredCellPane} with a {@link Node}.
     *
     * @param node
     *          the {@code Node} that should be centered
     */
    public CenteredCellPane(Node node) {
        super(node);

        child = node;
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
        if (style != null && !"".equals(style)) {
            child.setStyle(style);
        }
    }

    /**
     * Creates a {@code CenteredCellPane} with signal-label style class.
     *
     * @param label
     *          the text of the {@code Label}
     * @param signalLabel
     *          whether or not the signal-label style class should be added
     */
    public CenteredCellPane(String label, boolean signalLabel) {
        this(label, "", signalLabel);
    }

    /**
     * Creates a {@code CenteredCellPane} with a styled {@link Label} with signal-label style class.
     *
     * @param label
     *          the text of the {@code Label}
     * @param style
     *          the style of the {@code Label}
     * @param signalLabel
     *          whether or not the signal-label style class should be added
     */
    public CenteredCellPane(String label, String style, boolean signalLabel) {
        this(label, style);
        if (signalLabel) {
            child.getStyleClass().add(0, "signal-label");
        }
    }
}
