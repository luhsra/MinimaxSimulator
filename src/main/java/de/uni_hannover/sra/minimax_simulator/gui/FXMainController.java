package de.uni_hannover.sra.minimax_simulator.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.io.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.model.user.WorkspaceListener;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UI;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.actions.ProjectExportSchematics;
import de.uni_hannover.sra.minimax_simulator.ui.actions.ProjectExportSignalTable;
import de.uni_hannover.sra.minimax_simulator.ui.actions.ProjectSave;
import de.uni_hannover.sra.minimax_simulator.ui.actions.ProjectSaveTo;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.util.*;

/**
 * <b>The main controller for the JavaFX GUI.</b><br>
 * <br>
 * This controller handles the GUI interactions with the {@link MenuBar} and {@link TabPane}
 * as well as the bound shortcuts.
 *
 * @author Philipp Rohde
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

    private FileChooser fc = new FileChooser();

    @FXML private MemoryView embeddedMemoryViewController;
    @FXML private AluView embeddedAluViewController;
    @FXML private MuxView embeddedMuxViewController;
    @FXML private RegView embeddedRegViewController;
    @FXML private DebuggerView embeddedDebuggerViewController;
    @FXML private SignalView embeddedSignalViewController;
    @FXML private Overview embeddedOverviewController;

    private final TextResource _res;

    private Map<String, Tab> tabMap;

    private static final String _versionString = Main.getVersionString();
    private static final Workspace _ws = Main.getWorkspace();

    private final ExtensionFilter extFilterSignal;
    private final ExtensionFilter extFilterProject;
    private final ExtensionFilter extFilterSchematics;

    /**
     * The constructor initializes the final variables.
     */
    public FXMainController() {
        System.out.println("Constructor was called!");
        _res = Main.getTextResource("application");

        extFilterSignal = new ExtensionFilter(_res.get("project.signalfile.description"), "*.csv", "*.html");
        extFilterProject = new ExtensionFilter(_res.get("project.filedescription"), "*.zip");
        extFilterSchematics = new ExtensionFilter(_res.get("project.imagefile.description"), "*.jpg", "*.png");
    }

    /**
     * This method is called during application start up and initializes the GUI.
     */
    public void initialize() {
        _ws.addListener(this);

        this.tabMap = ImmutableMap.<String, Tab>builder()
                .put("view_project_overview",   tab_overview)
                .put("view_machine_alu", tab_alu)
                .put("view_machine_register",   tab_reg)
                .put("view_machine_mux",        tab_mux)
                .put("view_machine_signal",     tab_signal)
                .put("view_project_memory", tab_memory)
                .put("view_project_debugger", tab_debugger)
                .build();

        this.disabledMenuItems = ImmutableList.<MenuItem>builder()
                .add(project_saveas, project_export_schematics, project_export_signal, project_close, view_overview, view_memory, view_debugger)
                .build();

        setShortcuts();
        setLocalizedTexts();
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
        final List<MenuItem> projectMenu = new ArrayList<>(Arrays.asList(project_new, project_open, project_save, project_saveas, project_export_schematics, project_export_signal, project_close, exitApplication,
                view_overview, view_memory, view_debugger, view_conf_alu, view_conf_mux, view_conf_reg, view_conf_signal, help_about));
        for (MenuItem mi : projectMenu) {
            String id = mi.getId().replace("_", ".");
            String mne = res.get(id + ".mne");

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

        // menu: help
        menuHelp.setText(res.get("help"));

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
    public boolean exitApplication() {
        if (Main.getWorkspace().isUnsaved()) {
            ButtonType choice = new FXUnsavedDialog(_res.get("close-project.exit.title"), _res.get("close-project.exit.message")).getChoice();
            if (choice.equals(ButtonType.YES)) {
                saveConfirmed();
            }
            else if (choice.equals(ButtonType.CANCEL)) {
                return false;
            }
        }
        Platform.exit();
        return true;
    }

    private void saveConfirmed() {
        if (Main.getWorkspace().getCurrentProjectFile() == null) {
            saveProjectAs();
        }
        else {
            saveProject();
        }
    }

    /**
     * Creates a new project with default project data and initializes the rest of the GUI.
     */
    public void newProject() {
        if (Main.getWorkspace().isUnsaved()) {
            ButtonType choice = new FXUnsavedDialog(_res.get("close-project.generic.title"), _res.get("close-project.generic.message")).getChoice();
            if (choice.equals(ButtonType.YES)) {
                saveConfirmed();
            }
            else if (choice.equals(ButtonType.CANCEL)) {
                return;
            }
        }

        UIUtil.executeWorker(new Runnable() {
            @Override
            public void run() {
                try {
                    Main.getWorkspace().newProject();
                    initProjectGUI();
                } catch (RuntimeException e) {
                    System.out.println("EXCEPTION DURING PROJECT CREATION");
                    Main.getWorkspace().closeProject();
                    throw e;
                }
            }
        }, _res.get("wait.title"), _res.get("wait.project.new"));
    }

    /**
     * Opens a new project from file.
     */
    public void openProject() {
        if (Main.getWorkspace().isUnsaved()) {
            ButtonType choice = new FXUnsavedDialog(_res.get("close-project.generic.title"), _res.get("close-project.generic.message")).getChoice();
            if (choice.equals(ButtonType.YES)) {
                saveConfirmed();
            }
            else if (choice.equals(ButtonType.CANCEL)) {
                return;
            }
        }

        if (!UIUtil.confirmCloseProject()) {
            return;
        }
        fc.getExtensionFilters().clear();
        fc.getExtensionFilters().add(extFilterProject);

        File lastFolder = Main.getWorkspace().getLastProjectFolder();
        if (lastFolder != null && lastFolder.exists()) {
            fc.setInitialDirectory(lastFolder);
        }

        File file = fc.showOpenDialog(Main.getPrimaryStage());

        if (file == null) {
            return;
        }

        UIUtil.executeWorker(new Runnable() {
            @Override
            public void run() {
                try {
                    Main.getWorkspace().openProject(file);
                    //TODO: do all views work correctly?
                    initProjectGUI();
                } catch (ProjectImportException e) {
                    Main.getWorkspace().closeProject();
                    UI.invokeInFAT(new Runnable() {
                        @Override
                        public void run() {
                            new FXDialog(Alert.AlertType.ERROR, _res.get("load-error.title"), _res.get("load-error.message")).showAndWait();
                        }
                    });
                    //log.log(Level.WARNING, e.getMessage(), e);
                }
            }
        }, _res.get("wait.title"), _res.format("wait.project.load", file.getName()));

    }

    /**
     * Prepares the GUI for working with a project.
     */
    private void initProjectGUI() {
        UI.invokeInFAT(new Runnable() {
            @Override
            public void run() {
                setDisable(false);
                initTabs();
                closeNonDefaultTabs();
            }
        });
    }

    /**
     * Initializes all tabs of the simulator.
     */
    private void initTabs() {
        embeddedMemoryViewController.initMemoryView();
        embeddedAluViewController.initAluView();
        embeddedMuxViewController.initMuxView();
        embeddedRegViewController.initRegView();
        embeddedDebuggerViewController.initDebuggerView();
        embeddedSignalViewController.initSignalView();
        embeddedOverviewController.initOverview();
    }

    /**
     * Removes the non-default {@link Tab}s from the {@link TabPane}
     */
    private void closeNonDefaultTabs() {
        tabpane.getTabs().removeAll(tab_alu, tab_reg, tab_mux);
    }

    /**
     * Saves the current project to the current file.
     */
    public void saveProject() {
        // TODO: improve
        new ProjectSave().save(Main.getWorkspace().getCurrentProjectFile());
    }

    /**
     * Saves the current project to another file.
     */
    public void saveProjectAs() {
        // TODO: improve
        fc.getExtensionFilters().clear();
        fc.getExtensionFilters().add(extFilterProject);
        File file = fc.showSaveDialog(Main.getPrimaryStage());
        new ProjectSaveTo().save(file);
    }

    /**
     * Exports the schematics of the current project.
     */
    public void exportSchematics() {
        // TODO: improve
        fc.getExtensionFilters().clear();
        fc.getExtensionFilters().add(extFilterSchematics);
        File file = fc.showSaveDialog(Main.getPrimaryStage());
        new ProjectExportSchematics().export(file);
    }

    /**
     * Exports the {@link de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable} of the current project.
     */
    public void exportSignal() {
        // TODO: improve
        fc.getExtensionFilters().clear();
        fc.getExtensionFilters().add(extFilterSignal);
        File file = fc.showSaveDialog(Main.getPrimaryStage());
        new ProjectExportSignalTable().exportSignalTable(file);
    }

    /**
     * Closes the current project.
     */
    public void closeProject() {
        if (Main.getWorkspace().isUnsaved()) {
            ButtonType choice = new FXUnsavedDialog(_res.get("close-project.generic.title"), _res.get("close-project.generic.message")).getChoice();
            if (choice.equals(ButtonType.YES)) {
                saveConfirmed();
            }
            else if (choice.equals(ButtonType.CANCEL)) {
                return;
            }
        }

        setDisable(true);
        Main.getWorkspace().closeProject();
    }

    /**
     * Disables/Enables the project dependent {@link MenuItem}s.
     * This method is called if a project was opened or closed.
     *
     * @param disabled
     *          whether the GUI components should be disabled
     */
    private void setDisable(boolean disabled) {
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
        project_save.setDisable(true);
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
        if (Main.getWorkspace().getCurrentProjectFile() != null) {
            project_save.setDisable(false);
        }
    }
}
