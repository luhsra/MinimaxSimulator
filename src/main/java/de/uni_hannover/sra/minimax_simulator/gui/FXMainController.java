package de.uni_hannover.sra.minimax_simulator.gui;

import com.google.common.collect.ImmutableMap;
import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.gui.MemoryView;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.Machine;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXAboutDialog;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.MachineSchematics;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.util.*;

/**
 * The controller for the JavaFX GUI.
 *
 * @author Philipp
 *
 */

public class FXMainController {

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
    @FXML
    private AluView embeddedAluViewController;

    private TextResource _res;

    private Map<String, Tab> tabMap;

    /*
     *
     *      opening tabs in one method using the ActionEvent.getSource() to find out which one should be opened
     *
     */

    public void initialize() {
       _res = Main.getTextResource("application");
        this.setShortcuts();
        this.setLocalizedTexts();

       this.tabMap = ImmutableMap.<String, Tab>builder()
                .put("view_project_overview",   tab_overview)
                .put("view_machine_alu", tab_alu)
                .put("view_machine_register",   tab_reg)
                .put("view_machine_mux",        tab_mux)
                .put("view_machine_signal",     tab_signal)
                .put("view_project_memory",     tab_memory)
                .put("view_project_debugger", tab_debugger)
                .build();
    }

    private void setShortcuts() {
        // menu: project
        this.project_new.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        this.project_open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        this.project_save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        this.project_close.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN));
        this.exitApplication.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.SHORTCUT_DOWN));

        // menu: view
        this.view_overview.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN));
        this.view_memory.setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN));
        this.view_debugger.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN));

        // menu: machine configuration
        this.view_conf_alu.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN));
        this.view_conf_reg.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
        this.view_conf_mux.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.SHORTCUT_DOWN));
        this.view_conf_signal.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN));
    }

    private void setLocalizedTexts() {
        TextResource res = Main.getTextResource("menu");
        // menu: project

        menuProject.setText(res.get("project"));
//        final List<MenuItem> projectMenu = new ArrayList<>(Arrays.asList(project_new, project_open, project_save, project_saveas, project_export_schematics, project_export_signal, project_close, exitApplication));
        final List<MenuItem> projectMenu = new ArrayList<>(Arrays.asList(project_new, project_open, project_save, project_saveas, project_export_schematics, project_export_signal, project_close, exitApplication,
                view_overview, view_memory, view_debugger, view_conf_alu, view_conf_mux, view_conf_reg, view_conf_signal, help_about));
        for (MenuItem mi : projectMenu) {
            String id = mi.getId().replace("_", ".");
            String mne = res.get(id + ".mne");
            //String text = res.get("project."+id.replace("_", "-")).replaceFirst(mne, "_"+mne);
            //String text = res.get("project."+id.replace("_", "-")).replaceFirst("", "_"+"");

            String text = res.get(id);
            if (!mne.isEmpty()) {
                text = text.replaceFirst(mne, "_"+mne);
                mi.setMnemonicParsing(true);
            }

            mi.setText(text);
        }

        final List<Menu> allMenus = new ArrayList<>(Arrays.asList(menuProject, menuView, menuHelp));
        for (Menu m : allMenus) {
            String id = m.getId().replace("_", ".");
            String mne = res.get(id + ".mne");

            String text = res.get(id);
            if (!mne.isEmpty()) {
                text = text.replaceFirst(mne, "_"+mne);
                m.setMnemonicParsing(true);
            }

            m.setText(text);
        }
//        exitApplication.setText(res.get("project.exit"));

  //      System.out.println("new: " + project_new.getText() + "  - close: " + project_close.getText());

        // menu: view
/*        menuView.setText(res.get("view"));
        final List<MenuItem> viewMenu = new ArrayList<>(Arrays.asList(view_overview, view_memory, view_debugger));
        for (MenuItem mi : viewMenu) {
            String id = mi.getId().substring(5);
            mi.setText(res.get("view.project."+id));
        }
*/
        // menu: machine configuration
/*        menuMachineConfiguration.setText(res.get("view.machine"));
        final List<MenuItem> confMenu = new ArrayList<>(Arrays.asList(view_conf_alu, view_conf_mux, view_conf_reg, view_conf_signal));
        for (MenuItem mi : confMenu) {
            String id = mi.getId().substring(10);
            mi.setText(res.get("view.machine."+id));
        }
*/
        // menu: help
        menuHelp.setText(res.get("help"));
        //help_about.setText(res.get("help.info"));

        // tabs
        res = Main.getTextResource("project");
        final List<Tab> tabsProject = new ArrayList<>(Arrays.asList(tab_debugger, tab_memory, tab_overview));
        for (Tab tab : tabsProject) {
            String id = tab.getId();
            tab.setText(res.get(id.replace("_", ".")+".title"));
        }
        res = Main.getTextResource("machine");
        final List<Tab> tabsMachine = new ArrayList<>(Arrays.asList(tab_alu, tab_mux, tab_reg, tab_signal));
        for (Tab tab : tabsMachine) {
            String id = tab.getId();
            tab.setText(res.get(id.replace("_", ".") + ".title"));
        }
    }

    public void exitApplication() {
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
        }, _res.get("wait.title"), _res.get("wait.project.new"));
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

        embeddedMemoryViewController.initMemoryView();
        embeddedAluViewController.initAluView();

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

    public void openTab(ActionEvent ae) {

        if (!(ae.getSource() instanceof MenuItem)) {
            return;
        }

        MenuItem caller = (MenuItem) ae.getSource();
        String id = caller.getId();

        Tab toAdd = tabMap.get(id);

        if (toAdd == null) {
            return;
        }

        if (!tabpane.getTabs().contains(toAdd)) {
            if (toAdd.equals(tab_overview)) {
                tabpane.getTabs().add(0, tab_overview);
            }
            else {
                tabpane.getTabs().add(toAdd);
            }
        }
        tabpane.getSelectionModel().select(toAdd);
    }

    public void openInfo() {
        new FXAboutDialog().showAndWait();
    }

}
