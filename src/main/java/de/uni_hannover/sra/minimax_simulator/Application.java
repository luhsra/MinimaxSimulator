package de.uni_hannover.sra.minimax_simulator;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import de.uni_hannover.sra.minimax_simulator.config.ConfigurationLoader.ConfigurationException;
import de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader;
import de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader.MissingConfigStrategy;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.resources.DefaultResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.PropertyResourceControl;
import de.uni_hannover.sra.minimax_simulator.resources.ResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.MainWindow;
import de.uni_hannover.sra.minimax_simulator.ui.UI;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.exception.GuiExceptionHandler;
import de.uni_hannover.sra.minimax_simulator.ui.common.exception.SimpleExceptionHandler;

/**
 * The main class of the simulator, Application, is not instanciable itself, but contains a main
 * method and various static variables:
 * 
 * <ul>
 * <li>the {@link MainWindow} singleton instance</li>
 * <li>the {@link Workspace} singleton instance</li>
 * <li>the {@link Version} singleton instance</li>
 * </ul>
 * 
 * 
 * @author Martin
 * 
 */
public class Application
{
	private static Logger				_log;

	private final static String			ERROR_FILE		= "error.log";

	private static ResourceBundleLoader	_resourceLoader;
	private static MainWindow			_window;
	private static Workspace			_workspace;
	private static Version				_version;

	private static boolean				_isDebugging;

	private final static String			resourceFolder	= "./text/";

	private Application()
	{
	}

	public static ResourceBundleLoader getResourceLoader()
	{	
		if (_resourceLoader == null)
		{
			Locale locale;
			if (Config.LOCALE == null || Config.LOCALE.isEmpty())
			{
				locale = Locale.getDefault();
			}
			else
			{
				try
				{
					locale = new Locale(Config.LOCALE);	
				}
				catch (Exception e)
				{
					// locale not supported
					locale = Locale.getDefault();
				}
			}

			_resourceLoader = new DefaultResourceBundleLoader(
				new PropertyResourceControl(resourceFolder), locale);
		}
		return _resourceLoader;
	}

	public static void main(String[] args)
	{
		// Workaround for resizing issues with multiple monitors
		disableD3D();

		// Initialize simple exception handler
		// everything below can go wrong
		Thread.setDefaultUncaughtExceptionHandler(new SimpleExceptionHandler(ERROR_FILE));

		// Read logger settings from .properties inside jar
		setupLogger();

		// Initialize version info
		_version = new Version(Application.class);

		_isDebugging = System.getProperty("application.debug") != null;

		_log.info("Starting version " + _version.getVersionNumber());

		// Initialize config, read from file if existing
		try
		{
			new PropertiesFileConfigLoader(MissingConfigStrategy.USE_DEFAULT).configure(Config.class);
		}
		catch (ConfigurationException e)
		{
			throw new Error("Cannot initialize configuration", e);
		}
		_log.info("Configuration loaded.");

		// Initialize resource loader for clients (text boxes etc...)
		getResourceLoader();

		setXAppTitle();

		// Initialize empty workspace (no project loaded)
		_workspace = new Workspace();

		_log.info("Initializing UI...");

		// Initialize Swing UI in event dispatch thread
		UI.invoke(new Runnable()
		{
			@Override
			public void run()
			{
				// Initialize graphical exception handler that shows exception dialogs
				UncaughtExceptionHandler ueh = new GuiExceptionHandler(_window,
					ERROR_FILE);

				// EDT exception handler
				Thread.currentThread().setUncaughtExceptionHandler(ueh);

				// Other threads' exception handler
				Thread.setDefaultUncaughtExceptionHandler(ueh);

				// set look and feel
				UI.initialize();

//				ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

				_window = new MainWindow(getWorkspace(), getTextResource("application"),
					getTextResource("menu"));
				_window.setVisible(true);
			}
		});
	}

	public static MainWindow getMainWindow()
	{
		return _window;
	}

	public static Workspace getWorkspace()
	{
		return _workspace;
	}

	public static boolean isDebugging()
	{
		return _isDebugging;
	}

	/**
	 * If there is an open, unsaved project in the workspace, the application first asks the user to
	 * save it. If the user chooses to do so, the project is saved to the file it was most recently
	 * saved to, or to a file the user has to specify in a file choosing dialog.
	 */
	public static void shutdown()
	{
		if (UIUtil.confirmCloseProjectAndExit())
			_window.dispose();
	}

	public static Version getVersion()
	{
		return _version;
	}

	public static TextResource getTextResource(String bundleName)
	{
		return _resourceLoader.getTextResource(bundleName);
	}

	public static String getVersionString()
	{
		return _version.getModuleName() + " " + _version.getVersionNumber();
	}

	/**
	 * Using this we can set an application name (title of running application) under GNOME/KDE
	 * which would be missing otherwise.
	 */
	private static void setXAppTitle()
	{
		Toolkit xToolkit = Toolkit.getDefaultToolkit();

		// is X11 the current window toolkit?
		if (xToolkit.getClass().getName().equals("sun.awt.X11.XToolkit"))
		{
			String title = getTextResource("application").get("title-short");
			try
			{
				Field awtAppClassNameField = xToolkit.getClass().getDeclaredField(
					"awtAppClassName");
				awtAppClassNameField.setAccessible(true);
				awtAppClassNameField.set(xToolkit, title);
			}
			catch (Exception e)
			{
				// Don't show to user, just log
				_log.log(Level.WARNING, "Cannot set awtAppClassName", e);
			}
		}
	}

	/* Workaround for
	 * http://stackoverflow.com/questions/6436944/java-illegalstateexception-buffers-have
	 * -not-been-created */
	private static void disableD3D()
	{
		if (System.getProperty("os.name").startsWith("Win"))
			System.setProperty("sun.java2d.d3d", "false");
	}

	private static void setupLogger()
	{
		final InputStream inputStream = Application.class.getResourceAsStream("/logging.properties");

        try
        {
            LogManager.getLogManager().readConfiguration(inputStream);
        }
        catch (final IOException e)
        {
            throw new Error("Cannot initialize logging", e);
        }

		_log = Logger.getLogger(Application.class.getName());
	}
}
