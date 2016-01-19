package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Register;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The sprite for a {@link Register}.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class RegisterSprite extends CircuitSprite {

    /** The {@code Color} to highlight extended registers. */
    private static final Color EXTENDED_REGISTER_FX = new javafx.scene.paint.Color(0.95f, 0.95f, 0.95f, 1f);

    private final Register register;

    /**
     * Initializes the {@code RegisterSprite}.
     *
     * @param reg
     *          the {@code Register} this sprite will represent
     */
    public RegisterSprite(Register reg) {
        register = checkNotNull(reg);
    }

    @Override
    public void paint(GraphicsContext gc) {
        final Bounds b = register.getBounds();
        debugBounds(gc, b);

        String name = register.getLabel();
        FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont());
        double textWidth = fm.computeStringWidth(name);
        double textHeight = fm.getLineHeight();

        if (register.isExtended()) {
            gc.setFill(EXTENDED_REGISTER_FX);
            gc.fillRect(b.x + 0.5, b.y + 0.5, b.w, b.h);
            gc.setFill(javafx.scene.paint.Color.BLACK);
        }

        double xCenter = b.x + b.w / 2;
        double yCenter = b.y + b.h / 2;

        gc.strokeRect(b.x + 0.5, b.y + 0.5, b.w, b.h);
        gc.fillText(name, xCenter - textWidth / 2, yCenter + textHeight / 4 + 1);

        debugPin(gc, register.getDataIn());
        debugPin(gc, register.getDataOut());
        debugPin(gc, register.getWriteEnabled());
    }
}