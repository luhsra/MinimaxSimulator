package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.gui.MemoryView;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.Machine;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.MachineSchematics;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The controller for the JavaFX GUI.
 *
 * @author Philipp
 *
 */

public class Controller {

    public Menu menuProject;
    public MenuItem project_new;
    public MenuItem project_open;
    public MenuItem project_save;
    public MenuItem project_saveas;
    public MenuItem project_export_schematics;
    public MenuItem project_export_signal;
    public MenuItem project_close;
    public MenuItem exitApplication;
    private List<MenuItem> disabledMenuItems = null;

    public Menu menuView;
    public MenuItem view_overview;
    public MenuItem view_memory;
    public MenuItem view_debugger;

    public Menu menuMachineConfiguration;
    public MenuItem view_conf_alu;
    public MenuItem view_conf_reg;
    public MenuItem view_conf_mux;
    public MenuItem view_conf_signal;

    public Menu menuHelp;
    public MenuItem help_about;

    public TabPane tabpane;
    public Tab tab_overview;
    public Tab tab_signal;
    public Tab tab_memory;
    public Tab tab_debugger;
    public Tab tab_alu;
    public Tab tab_reg;
    public Tab tab_mux;
    public Tab tab_experimental;
    public ScrollPane panel_machine_overview;
    public Canvas canvas;

    @FXML
    private MemoryView embeddedMemoryViewController;

    public void exitApplication(ActionEvent ae) {
        Platform.exit();
    }

    public void newProject(ActionEvent ae) {
        setDisable(false);
        UIUtil.executeWorker(new Runnable() {
            @Override
            public void run() {
                try {
                    Main.getWorkspace().newProject();
                } catch (RuntimeException e) {
                    Main.getWorkspace().closeProject();
                    throw e;
                }
            }
        }, "Bitte warten...", "Neues Projekt wird erstellt...");
        final SwingNode swingNode = new SwingNode();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final Machine _machine = Main.getWorkspace().getProject().getMachine();
                final MachineSchematics _machineView = new MachineSchematics(_machine);
                _machine.getDisplay().setRenderEnvironment(_machineView.getRenderEnvironment());
                _machine.getDisplay().addMachineDisplayListener(_machineView);

                //swingNode.setContent(new JButton("Click me!"));
                swingNode.setContent(_machineView);
            }
        });
        while (swingNode.getContent() == null) {
            /*  We have to wait for the SwingUtilities thread to set the content of the swingNode
             *  because otherwise there will be a null pointer exception. Also it is not possible
             *  to add the swingNode to the JavaFX pane inside the SwingUtilities thread.
             *
             *  This is necessary until the MachineSchematics are converted to JavaFX.
             */
        }
        double widthSchematics = swingNode.getContent().getWidth();
        double widthViewport = panel_machine_overview.getViewportBounds().getWidth();
        double insetLeft = (widthViewport - widthSchematics) / 2;
        panel_machine_overview.setPadding(new Insets(0, 0, 0, insetLeft));
        panel_machine_overview.setContent(swingNode);

        drawCanvas();           // CANVAS TEST

        embeddedMemoryViewController.fillMemTable();

        // TODO: create project data
    }

    private void drawCanvas() {
/*        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.YELLOW);
        gc.strokeLine(40, 10, 10, 40);  */
    }

    public void openProject(ActionEvent ae) {
        System.out.println(ae.toString());
        System.out.println(ae.getSource().toString());
        if (ae.getSource().toString().substring(12, 19).equals("project")) {
            System.out.println("I'm a project");
            String source = ae.getSource().toString().substring(20).split(",")[0];
            System.out.println("real source: " + source);
            switch (source) {
                case "open":
                    System.out.println("Opening project now...");
                    break;
                default:
                    break;
            }
        }
        // TODO: open existing project
    }

    public void saveProject(ActionEvent ae) {
        // TODO: save the project data
    }

    public void saveProjectAs(ActionEvent ae) {
        // TODO
    }

    public void exportSchematics(ActionEvent ae) {
        // TODO
    }

    public void exportSignal(ActionEvent ae) {
        // TODO
    }

    public void closeProject(ActionEvent ae) {
        setDisable(true);
        // TODO: close project data
    }

    private void setDisable(boolean disabled) {
        if (disabledMenuItems == null) {
            disabledMenuItems = new ArrayList<>(Arrays.asList(project_save, project_saveas, project_export_schematics, project_export_signal, project_close, view_overview, view_memory, view_debugger));
        }

        for (MenuItem mi : disabledMenuItems) {
            mi.setDisable(disabled);
        }

        menuMachineConfiguration.setDisable(disabled);

        boolean visible = !disabled;
        tabpane.setVisible(visible);

    }

    public void openViewOverview() {
        tabpane.getTabs().add(0, tab_overview);
        tabpane.getSelectionModel().select(tab_overview);
    }


}
