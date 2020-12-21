package de.uni_hannover.sra.minimax_simulator.ui.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.uni_hannover.sra.minimax_simulator.config.Config;
import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.MainGUI;
import de.uni_hannover.sra.minimax_simulator.io.exporter.csv.SignalCsvExporter;
import de.uni_hannover.sra.minimax_simulator.io.exporter.csv.SignalHtmlExporter;
import de.uni_hannover.sra.minimax_simulator.io.importer.ProjectImportException;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplayListener;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.model.user.WorkspaceListener;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.AboutDialog;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.ExceptionDialog;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.FXDialog;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.UnsavedDialog;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.UndoEvent;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.UndoListener;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.UndoManager;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.MachineSchematics;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <b>The main controller for the JavaFX GUI.</b><br>
 * <br>
 * This controller handles the GUI interactions with the {@link MenuBar} and {@link TabPane}
 * as well as the bound shortcuts.
 *
 * @author Philipp Rohde
 */
public class FXMainController implements WorkspaceListener, MachineDisplayListener, UndoListener {

    @FXML private Menu menuProject;
    @FXML private MenuItem projectNew;
    @FXML private MenuItem projectOpen;
    @FXML private MenuItem projectSave;
    @FXML private MenuItem projectSaveAs;
    @FXML private MenuItem projectUndo;
    @FXML private MenuItem projectRedo;
    @FXML private MenuItem projectExportSchematics;
    @FXML private MenuItem projectExportSignal;
    @FXML private MenuItem projectClose;
    @FXML private MenuItem exitApplication;
    private List<MenuItem> disabledMenuItems = null;

    private UndoManager undoManager = UndoManager.INSTANCE;

    @FXML private Menu menuView;
    @FXML private MenuItem viewOverview;
    @FXML private MenuItem viewMemory;
    @FXML private MenuItem viewDebugger;

    @FXML private Menu menuMachineConfiguration;
    @FXML private MenuItem viewConfAlu;
    @FXML private MenuItem viewConfReg;
    @FXML private MenuItem viewConfMux;
    @FXML private MenuItem viewConfSignal;

    @FXML private Menu menuHelp;
    @FXML private MenuItem helpAbout;
    @FXML private Menu helpLanguage;
    @FXML private MenuItem helpLanguageEnglish;
    @FXML private MenuItem helpLanguageGerman;
    @FXML private Menu helpTheme;
    @FXML private MenuItem helpThemeDefault;
    @FXML private MenuItem helpThemeJava;
    @FXML private MenuItem helpThemeDark;

    @FXML private TabPane tabpane;
    @FXML private Tab tabOverview;
    @FXML private Tab tabSignal;
    @FXML private Tab tabMemory;
    @FXML private Tab tabDebugger;
    @FXML private Tab tabAlu;
    @FXML private Tab tabReg;
    @FXML private Tab tabMux;

    @FXML private ScrollPane paneOverview;

    private FileChooser fc = new FileChooser();

    @FXML private MemoryView embeddedMemoryViewController;
    @FXML private AluView embeddedAluViewController;
    @FXML private MuxView embeddedMuxViewController;
    @FXML private RegView embeddedRegViewController;
    @FXML private DebuggerView embeddedDebuggerViewController;
    @FXML private SignalView embeddedSignalViewController;

    private MachineSchematics schematics;

    private final TextResource res;
    private final TextResource resMenu;

    private Map<String, Tab> tabMap;

    private static final String VERSION_STRING = Main.getVersionString();
    private static final Workspace WORKSPACE = Main.getWorkspace();

    private final ExtensionFilter extFilterSignal;
    private final ExtensionFilter extFilterProject;
    private final ExtensionFilter extFilterSchematics;

    private static final Logger LOG = Logger.getLogger("de.uni_hannover.sra.minimax_simulator");

    /**
     * Initializes the final variables.
     */
    public FXMainController() {
        res = Main.getTextResource("application");
        resMenu = Main.getTextResource("menu");

        extFilterSignal = new ExtensionFilter(res.get("project.signalfile.description"), "*.csv", "*.html");
        extFilterProject = new ExtensionFilter(res.get("project.filedescription"), "*.zip");
        extFilterSchematics = new ExtensionFilter(res.get("project.imagefile.description"), "*.jpg", "*.png");
    }

    /**
     * This method is called during application start up and initializes the GUI.
     */
    public void initialize() {
        WORKSPACE.addListener(this);
        undoManager.addListener(this);

        this.tabMap = ImmutableMap.<String, Tab>builder()
                .put("view_project_overview", tabOverview)
                .put("view_machine_alu", tabAlu)
                .put("view_machine_register", tabReg)
                .put("view_machine_mux", tabMux)
                .put("view_machine_signal", tabSignal)
                .put("view_project_memory", tabMemory)
                .put("view_project_debugger", tabDebugger)
                .build();

        this.disabledMenuItems = ImmutableList.<MenuItem>builder()
                .add(projectSaveAs, projectExportSchematics, projectExportSignal, projectClose, viewOverview, viewMemory, viewDebugger)
                .build();

        projectUndo.disableProperty().bind(undoManager.isUndoAvailableProperty().not());
        projectRedo.disableProperty().bind(undoManager.isRedoAvailableProperty().not());

        setShortcuts();
        setLocalizedTexts();
    }

    /**
     * Binds the shortcuts to the {@link MenuItem}s.
     */
    private void setShortcuts() {
        // menu: project
        this.projectNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        this.projectOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        this.projectSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        this.projectUndo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN));
        this.projectRedo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN));
        this.projectClose.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN));
        this.exitApplication.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.SHORTCUT_DOWN));

        // menu: view
        this.viewOverview.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN));
        this.viewMemory.setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN));
        this.viewDebugger.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN));

        // menu: machine configuration
        this.viewConfAlu.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN));
        this.viewConfReg.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
        this.viewConfMux.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.SHORTCUT_DOWN));
        this.viewConfSignal.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN));
    }

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
        // menu: project
        menuProject.setText(resMenu.get("project"));
        final List<MenuItem> menuElements = new ArrayList<>(Arrays.asList(projectNew, projectOpen, projectSave, projectSaveAs, projectExportSchematics, projectExportSignal, projectClose,
                exitApplication, viewOverview, viewMemory, viewDebugger, viewConfAlu, viewConfMux, viewConfReg, viewConfSignal, helpAbout, projectUndo, projectRedo, menuProject, menuView,
                menuHelp, menuMachineConfiguration, helpLanguage, helpLanguageEnglish, helpLanguageGerman, helpTheme));
        for (MenuItem mi : menuElements) {
            String id = mi.getId().replace("_", ".");
            String mne = resMenu.get(id + ".mne");

            String text = resMenu.get(id);
            if (!mne.isEmpty()) {
                text = text.replaceFirst(mne, "_"+mne);
                mi.setMnemonicParsing(true);
            }
            mi.setText(text);
        }

        // menu: help
        menuHelp.setText(resMenu.get("help"));

        // tabs
        TextResource resProject = Main.getTextResource("project");
        final List<Tab> tabsProject = new ArrayList<>(Arrays.asList(tabDebugger, tabMemory, tabOverview));
        for (Tab tab : tabsProject) {
            String id = tab.getId();
            tab.setText(resProject.get(id.replace("_", ".")+".title"));
        }
        TextResource resMachine = Main.getTextResource("machine");
        final List<Tab> tabsMachine = new ArrayList<>(Arrays.asList(tabAlu, tabMux, tabReg, tabSignal));
        for (Tab tab : tabsMachine) {
            String id = tab.getId();
            tab.setText(resMachine.get(id.replace("_", ".") + ".title"));
        }
    }

    /**
     * Shuts down the application.
     *
     * @return
     *          {@code true} if the application will be shut down; {@code false} otherwise
     */
    public boolean exitApplication() {
        if (confirmDismissUnsavedChanges(res.get("close-project.exit.title"), res.get("close-project.exit.message"))) {
            Platform.exit();
            return true;
        }
        return false;
    }

    /**
     * Asks the user to dismiss unsaved changes.
     *
     * @param dialogTitle
     *          the title of the {@link UnsavedDialog}
     * @param dialogMessage
     *          the message of the {@link UnsavedDialog}
     * @return
     *          {@code true} if changes will be dismissed or were saved; {@code false} otherwise
     */
    private boolean confirmDismissUnsavedChanges(String dialogTitle, String dialogMessage) {
        if (Main.getWorkspace().isUnsaved()) {
            ButtonType choice = new UnsavedDialog(dialogTitle, dialogMessage).getChoice();
            if (choice.equals(ButtonType.YES)) {
                if (!saveConfirmed()) {
                    return false;
                }
            }
            else if (choice.equals(ButtonType.CANCEL)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Asks the user to dismiss unsaved changes.<br>
     * <br>
     * Calls {@link FXMainController#confirmDismissUnsavedChanges(String, String)} with the title and message
     * used if the user wants to open, close or create a new project and the current project has unsaved changes.
     *
     * @return
     *          {@code true} if changes will be dismissed or were saved; {@code false} otherwise
     */
    private boolean confirmDismissUnsavedChanges() {
        return confirmDismissUnsavedChanges(res.get("close-project.generic.title"), res.get("close-project.generic.message"));
    }

    /**
     * Calls {@link #saveProjectAs} if the project was not yet saved, calls {@link #saveProject()} otherwise.
     *
     * @return
     *          {@code true} if the project was saved; {@code false} otherwise
     */
    private boolean saveConfirmed() {
        if (Main.getWorkspace().getCurrentProjectFile() == null) {
            return saveProjectAs();
        }
        else {
            return saveProject();
        }
    }

    /**
     * Creates a new project with default project data and initializes the rest of the GUI.
     */
    public void newProject() {
        if (!confirmDismissUnsavedChanges()) {
            return;
        }

        UIUtil.executeWorker(() -> {
            try {
                Main.getWorkspace().newProject();
                initProjectGUI();
            } catch (RuntimeException e) {
                closeProject();
                throw e;
            }
        }, res.get("wait.title"), res.get("wait.project.new"));
    }

    /**
     * Opens a new project from file.
     */
    public void openProject() {
        if (!confirmDismissUnsavedChanges()) {
            return;
        }

        fc.getExtensionFilters().clear();
        fc.getExtensionFilters().add(extFilterProject);

        File lastFolder = Main.getWorkspace().getLastProjectFolder();
        if (lastFolder != null && lastFolder.exists()) {
            fc.setInitialDirectory(lastFolder);
        }

        File file = fc.showOpenDialog(MainGUI.getPrimaryStage());

        if (file == null) {
            return;
        }

        UIUtil.executeWorker(() -> {
            try {
                Main.getWorkspace().openProject(file);
                initProjectGUI();
            } catch (ProjectImportException e) {
                LOG.log(Level.WARNING, "error during project import", e);
                UIUtil.invokeInFAT(() -> new FXDialog(Alert.AlertType.ERROR, res.get("load-error.title"), res.get("load-error.message")).showAndWait());
            }
        }, res.get("wait.title"), res.format("wait.project.load", file.getName()));

    }

    /**
     * Prepares the GUI for working with a project.
     */
    private void initProjectGUI() {
        UIUtil.invokeInFAT(() -> {
            setDisable(false);
            initTabs();
            closeNonDefaultTabs();
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

        // init overview tab
        this.schematics = new MachineSchematics(Main.getWorkspace().getProject().getMachine());
        //this.schematics.translateXProperty().bind(paneOverview.widthProperty().subtract(schematics.widthProperty()).divide(2))
        //paneOverview.setContent(this.schematics)

        // the canvas didn't resize itself correctly on mac; here is the workaround
        WORKSPACE.getProject().getMachine().getDisplay().addMachineDisplayListener(this);
        schematicsToImage();
    }

    /**
     * Workaround for Mac:
     * Using the schematics itself for rendering caused a bug on Mac. The canvas did resize but didn't fill
     * the new occupied space so the schematics were cropped. Using an image instead of the canvas solved the problem.
     */
    private void schematicsToImage() {
        ImageView imgView = new ImageView();
        Image img = this.schematics.snapshot(null, null);

        imgView.translateXProperty().bind(paneOverview.widthProperty().subtract(img.widthProperty()).divide(2));
        imgView.setFitHeight(img.getHeight());
        imgView.setFitWidth(img.getWidth());

        imgView.setImage(img);
        paneOverview.setContent(imgView);
    }

    /**
     * Removes the non-default {@link Tab}s from the {@link TabPane}
     */
    private void closeNonDefaultTabs() {
        tabpane.getTabs().removeAll(tabAlu, tabReg, tabMux);
    }

    /**
     * Calls {@link UndoManager#undo()} in order to undo the latest change.
     */
    public void undo() {
        undoManager.undo();
    }

    /**
     * Calls {@link UndoManager#redo()} in order to redo the latest change.
     */
    public void redo() {
        undoManager.redo();
    }

    /**
     * Saves the current project to the current file.
     *
     * @return
     *          {@code true} if the project was saved; {@code false} otherwise
     */
    public boolean saveProject() {
        return saveProjectToFile(Main.getWorkspace().getCurrentProjectFile());
    }

    /**
     * Saves the current project to another file.
     *
     * @return
     *          {@code true} if the project was saved; {@code false} otherwise
     */
    public boolean saveProjectAs() {
        fc.getExtensionFilters().clear();
        fc.getExtensionFilters().add(extFilterProject);
        File file = fc.showSaveDialog(MainGUI.getPrimaryStage());
        return saveProjectToFile(file);
    }

    /**
     * Saves the current project to the given file.
     *
     * @param file
     *          the {@code File} the project should be saved to
     * @return
     *          {@code true} if the project was saved; {@code false} otherwise
     */
    private boolean saveProjectToFile(File file) {
        if (file == null) {
            return false;
        }

        if (file.getName().lastIndexOf('.') == -1) {
            // append ending
            file = new File(file.getPath() + ".zip");
        }

        final File fileToSave = file;

        UIUtil.executeWorker(() -> {
            try {
                Main.getWorkspace().saveProject(fileToSave);
            } catch (Exception e) {
                UIUtil.invokeInFAT(() -> new ExceptionDialog(e).show());
            }
        }, res.get("wait.title"), res.format("wait.project.save", file.getName()));
        return true;
    }

    /**
     * Exports the schematics of the current project.
     */
    public void exportSchematics() {
        fc.getExtensionFilters().clear();
        fc.getExtensionFilters().add(extFilterSchematics);
        File file = fc.showSaveDialog(MainGUI.getPrimaryStage());

        exportSchematicsToFile(file);
    }

    /**
     * Saves the schematics of the current project to the given file.
     *
     * @param file
     *          the {@code File} the {@code MachineSchematics} should be saved to
     */
    private void exportSchematicsToFile(File file) {
        if (file == null) {
            return;
        }

        if (file.getName().lastIndexOf(".") == -1) {
            file = new File(file.getPath() + ".png");
        }

        final File imageFile = file;

        int dot = imageFile.getName().lastIndexOf('.');
        final String ending = imageFile.getName().substring(dot + 1);

        // get an image of the schematics
        final WritableImage image = this.schematics.snapshot(null, null);

        UIUtil.executeWorker(() -> {
            // write the image to disk
            try {
                if ("jpg".equals(ending)) {
                    // remove alpha channel
                    BufferedImage bImg = new BufferedImage( (int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = bImg.createGraphics();
                    g.drawImage(SwingFXUtils.fromFXImage(image, null), 0, 0, java.awt.Color.WHITE, null);
                    g.dispose();
                    if (!ImageIO.write(bImg, ending, imageFile)) {
                        ioError(imageFile.getPath());
                        return;
                    }
                }
                else {
                    if (!ImageIO.write(SwingFXUtils.fromFXImage(image, null), ending, imageFile)) {
                        ioError(imageFile.getPath());
                        return;
                    }
                }
            } catch (IOException e1) {
                // (almost) ignore
                LOG.log(Level.WARNING, "can not save the schematics", e1);
                ioError(imageFile.getPath());
                return;
            }
            // open the image
            try {
                MainGUI.getHostServicesStatic().showDocument(imageFile.getAbsolutePath());
            } catch (Exception e) {
                // (almost) ignore
                LOG.log(Level.WARNING, "can not open the schematics", e);
            }
        }, res.get("wait.title"), res.get("wait.image-export"));
    }

    /**
     * Exports the {@link de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable} of the current project.
     */
    public void exportSignal() {
        fc.getExtensionFilters().clear();
        fc.getExtensionFilters().add(extFilterSignal);
        File file = fc.showSaveDialog(MainGUI.getPrimaryStage());

        exportSignalToFile(file);
    }

    /**
     * Saves the {@code SignalTable} to the given file.
     *
     * @param file
     *          the {@code File} the {@code SignalTable} should be saved to
     */
    private void exportSignalToFile(File file) {
        final Project project = Main.getWorkspace().getProject();

        if (file == null) {
            return;
        }

        if (file.getName().lastIndexOf(".") == -1) {
            // append ending
            file = new File(file.getPath() + ".html");
        }

        final File fileToSave = file;

        UIUtil.executeWorker(() -> {
            try {
                SignalTable table = project.getSignalTable();
                SignalConfiguration config = project.getSignalConfiguration();
                if (fileToSave.getName().endsWith(".csv")) {
                    new SignalCsvExporter(fileToSave).exportSignalTable(table, config);
                } else if (fileToSave.getName().endsWith(".html")) {
                    new SignalHtmlExporter(fileToSave).exportSignalTable(table, config);
                } else {
                    ioError(fileToSave.getPath(), res.get("project.export.error.message.wrongformat"));
                }
            } catch (IOException e1) {
                // (almost) ignore
                LOG.log(Level.WARNING, "can not save the signal table", e1);

                ioError(fileToSave.getPath());
            }
        }, res.get("wait.title"), res.get("wait.signal-export"));
    }

    /**
     * Opens an error dialog if an {@code IOException} was thrown during export of the {@code MachineSchematics}
     * or {@code SignalTable}.
     *
     * @param filename
     *          the filename of the {@code File} where something went wrong
     * @param reason
     *          the reason of the error
     */
    private void ioError(String filename, String reason) {
        String error = res.format("project.export.error.message", filename, reason);
        String title = res.get("project.export.error.title");

        UIUtil.invokeInFAT(() -> new FXDialog(Alert.AlertType.ERROR, title, error).showAndWait());
    }

    /**
     * Opens an error dialog if an {@code IOException} was thrown during export of the {@code MachineSchematics}
     * or {@code SignalTable}.<br>
     * <br>
     * The method calls {@link FXMainController#ioError(String, String)} with the commonly used reason.
     *
     * @param filename
     *          the filename of the {@code File} where something went wrong
     */
    private void ioError(String filename) {
        ioError(filename, res.get("project.export.error.message.ioex"));
    }

    /**
     * Closes the current project.
     */
    public void closeProject() {
        if (!confirmDismissUnsavedChanges()) {
            return;
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
            if (toAdd.equals(tabOverview)) {
                tabpane.getTabs().add(0, tabOverview);
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
        new AboutDialog().showAndWait();
    }

    /**
     * Changes the language corresponding to the {@link ActionEvent} calling the method.
     *
     * @param ae
     *          the {@link ActionEvent} calling the method
     */
    public void changeLanguage(ActionEvent ae) {

        if (!(ae.getSource() instanceof MenuItem)) {
            return;
        }

        MenuItem caller = (MenuItem) ae.getSource();
        try {
            if (caller.equals(helpLanguageEnglish)) {
                Config.changeLanguage("en");
            } else if (caller.equals(helpLanguageGerman)) {
                Config.changeLanguage("de");
            }
        } catch (IOException e) {
            new FXDialog(Alert.AlertType.ERROR, res.get("language.error.title"), res.get("language.error.message")).show();
        }
        new FXDialog(Alert.AlertType.INFORMATION, res.get("language.info.title"), res.get("language.info.message")).showAndWait();
    }

    /**
     * Changes the theme corresponding to the {@link ActionEvent} calling the method.
     *
     * @param ae
     *          the {@link ActionEvent} calling the method
     */
    public void changeTheme(ActionEvent ae) {

        if (!(ae.getSource() instanceof MenuItem)) {
            return;
        }

        MenuItem caller = (MenuItem) ae.getSource();
        try {
            if (caller.equals(helpThemeDefault)) {
                Config.changeTheme("default");
            }
            else if (caller.equals(helpThemeJava)) {
                Config.changeTheme("standard-java");
            }
            else if (caller.equals(helpThemeDark)) {
                Config.changeTheme("dark");
            }
        } catch (IOException e) {
            new FXDialog(Alert.AlertType.ERROR, res.get("theme.error.title"), res.get("theme.error.message")).show();
        }
        new FXDialog(Alert.AlertType.INFORMATION, res.get("theme.info.title"), res.get("theme.info.message")).showAndWait();
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
            return res.get("project.unnamed");
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
        UIUtil.invokeInFAT(() -> MainGUI.getPrimaryStage().setTitle(newTitle));
    }

    /**
     * Sets the application title with the name of the opened project.
     *
     * @param project
     *          the opened {@link Project}
     */
    @Override
    public void onProjectOpened(Project project) {
        setApplicationTitle(res.format("title.open-project", VERSION_STRING, getProjectName()));
    }

    /**
     * Sets the application title with the name of the saved project.
     *
     * @param project
     *          the saved {@link Project}
     */
    @Override
    public void onProjectSaved(Project project) {
        setApplicationTitle(res.format("title.open-project", VERSION_STRING, getProjectName()));
        projectSave.setDisable(true);
    }

    /**
     * Sets the application title without any project name.
     *
     * @param project
     *          the closed {@link Project}
     */
    @Override
    public void onProjectClosed(Project project) {
        setApplicationTitle(res.format("title", VERSION_STRING));
    }

    /**
     * Sets the application title with the modified project and marks it as unsaved.
     *
     * @param project
     *          the modified {@link Project}
     */
    @Override
    public void onProjectDirty(Project project) {
        setApplicationTitle(res.format("title.open-unsaved-project", VERSION_STRING, getProjectName()));
        if (Main.getWorkspace().getCurrentProjectFile() != null) {
            projectSave.setDisable(false);
        }
    }

    @Override
    public void machineSizeChanged() {
        schematicsToImage();
    }

    @Override
    public void machineDisplayChanged() {
        schematicsToImage();
    }

    @Override
    public void onSpriteOwnerAdded(SpriteOwner spriteOwner) {
        // not needed here
    }

    @Override
    public void onSpriteOwnerRemoved(SpriteOwner spriteOwner) {
        // not needed here
    }

    @Override
    public void onSpriteOwnerChanged(SpriteOwner spriteOwner) {
        // not needed here
    }

    @Override
    public void onUndoAction(UndoEvent e) {
        if (e.isSaved()) {
            WORKSPACE.setProjectSaved();
        }
        else {
            WORKSPACE.setProjectUnsaved();
        }

        String undo = resMenu.get("project.undo");
        if (e.undoAvailable()) {
            // add command description
            undo += ": " + e.undoCommandName();
        }
        projectUndo.setText(undo);

        String redo = resMenu.get("project.redo");
        if (e.redoAvailable()) {
            // add command description
            redo += ": " + e.redoCommandName();
        }
        projectRedo.setText(redo);
    }
}
