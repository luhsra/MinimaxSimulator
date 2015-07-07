package de.uni_hannover.sra.minimax_simulator.gui;

import com.google.common.collect.ImmutableMap;
import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.model.user.WorkspaceListener;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UI;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXAboutDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.io.File;
import java.util.*;

/**
 * <b>The main controller for the JavaFX GUI.</b><br>
 * <br>
 * This controller handles the GUI interactions with the {@link MenuBar} and {@link TabPane} as well as
 * the binded shortcuts.
 *
 * @author Philipp
 *
 */
public class FXMainController implements WorkspaceListener {

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
    @FXML
    private MuxView embeddedMuxViewController;
    @FXML
    private RegView embeddedRegViewController;

    private TextResource _res;

    private Map<String, Tab> tabMap;

    private static String _versionString;
    private static final Workspace _ws = Main.getWorkspace();

    /**
     * This method is called during application start up and initializes the GUI.
     */
    public void initialize() {

        _versionString = Main.getVersionString();
        _ws.addListener(this);

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

    /**
     * Binds the shortcuts to the {@link MenuItem}s.
     */
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

    /**
     * Sets localized texts from resource for the GUI elements.
     */
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

    /**
     * Shuts down the application.
     */
    public void exitApplication() {
        Platform.exit();
    }

    /**
     * Creates a new project with default project data and initializes the rest of the GUI.
     */
    public void newProject() {
        setDisable(false);
        UIUtil.executeWorker(new Runnable() {
            @Override
            public void run() {
                try {
                    Main.getWorkspace().newProject();
                } catch (RuntimeException e) {
                    System.out.println("EXCEPTION DURING PROJECT CREATION");
                    Main.getWorkspace().closeProject();
                    throw e;
                }
            }
        }, _res.get("wait.title"), _res.get("wait.project.new"));
/*        final SwingNode swingNode = new SwingNode();
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
        while (swingNode.getContent() == null) {        */
            /*  We have to wait for the SwingUtilities thread to set the content of the swingNode
             *  because otherwise there will be a null pointer exception. Also it is not possible
             *  to add the swingNode to the JavaFX pane inside the SwingUtilities thread.
             *
             *  This is necessary until the MachineSchematics are converted to JavaFX.
             */
/*        }
        double widthSchematics = swingNode.getContent().getWidth();
        double widthViewport = panel_machine_overview.getViewportBounds().getWidth();
        double insetLeft = (widthViewport - widthSchematics) / 2;
        panel_machine_overview.setPadding(new Insets(0, 0, 0, insetLeft));
        panel_machine_overview.setContent(swingNode);

        drawCanvas();           // CANVAS TEST
*/
        embeddedMemoryViewController.initMemoryView();
        embeddedAluViewController.initAluView();
        embeddedMuxViewController.initMuxView();
        embeddedRegViewController.initRegView();

        // TODO: create project data
    }

    @Deprecated
    private void drawCanvas() {
/*        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.YELLOW);
        gc.strokeLine(40, 10, 10, 40);  */
    }

    /**
     * Opens a new project from file.
     *
     * @param ae
     *          the {@link ActionEvent} calling the method
     */
    public void openProject(ActionEvent ae) {
        System.out.println(ae.toString());
        System.out.println(ae.getSource().toString());
        // TODO: is this source thing necessary?
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

    public void saveProject() {
        // TODO: save the project data
    }

    public void saveProjectAs(ActionEvent ae) {
        // TODO: save as
    }

    public void exportSchematics(ActionEvent ae) {
        // TODO: export schematics
    }

    public void exportSignal(ActionEvent ae) {
        // TODO: export signal
    }

    public void closeProject(ActionEvent ae) {
        setDisable(true);
        // TODO: close project data
    }

    /**
     * Disables/Enables the project dependent {@link MenuItem}s.
     * This method is called if a project was opened or closed.
     *
     * @param disabled
     *          whether the GUI components should be disabled
     */
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

    /**
     * Opens the {@link Tab} corresponding to the {@link ActionEvent} calling the method.
     *
     * @param ae
     *          the {@link ActionEvent} calling the method
     */
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

    /**
     * Shows the about dialog.
     */
    public void openInfo() {
        new FXAboutDialog().showAndWait();
    }

    /**
     * Gets the name of the currently open {@link Project}.
     *
     * @return
     *          the name of the currently open {@link Project}
     */
    private String getProjectName() {
        File file = Main.getWorkspace().getCurrentProjectFile();
        if (file == null) {
            return _res.get("project.unnamed");
        }
        return file.getName();
    }

    /**
     * Sets the title of the application.
     *
     * @param newTitle
     *              the title to set
     */
    private void setApplicationTitle(String newTitle) {
        UI.invokeInFAT(new Runnable() {
            @Override
            public void run() {
                Main.getPrimaryStage().setTitle(newTitle);
            }
        });
    }

    /**
     * Sets the application title with the name of the opened project.
     *
     * @param project
     *          the opened {@link Project}
     */
    @Override
    public void onProjectOpened(Project project) {
        setApplicationTitle(_res.format("title.open-project", _versionString, getProjectName()));
    }

    /**
     * Sets the application title with the name of the saved project.
     *
     * @param project
     *          the saved {@link Project}
     */
    @Override
    public void onProjectSaved(Project project) {
        setApplicationTitle(_res.format("title.open-project", _versionString, getProjectName()));
    }

    /**
     * Sets the application title without any project name.
     *
     * @param project
     *          the closed {@link Project}
     */
    @Override
    public void onProjectClosed(Project project) {
        setApplicationTitle(_res.format("title", _versionString));
    }

    /**
     * Sets the application title with the modified project and marks it as unsaved.
     *
     * @param project
     *          the modified {@link Project}
     */
    @Override
    public void onProjectDirty(Project project) {
        setApplicationTitle(_res.format("title.open-unsaved-project", _versionString, getProjectName()));
    }
}
