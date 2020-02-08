/*
 * Created on Jun 26, 2005 by wyatt
 */
package org.homeunix.thecave.buddi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.DocumentImpl;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.BuddiPluginFactory;
import org.homeunix.thecave.buddi.plugin.api.BuddiRunnablePlugin;
import org.homeunix.thecave.buddi.plugin.api.exception.DataModelProblemException;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.BrowserLauncher;
import org.homeunix.thecave.buddi.util.BuddiCryptoFactory;
import org.homeunix.thecave.buddi.util.FileFunctions;
import org.homeunix.thecave.buddi.util.OperationCancelledException;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.dialogs.BuddiPasswordDialog;
import org.homeunix.thecave.buddi.view.menu.items.FileOpen;

import ca.digitalcave.moss.application.document.exception.DocumentLoadException;
import ca.digitalcave.moss.application.document.exception.DocumentSaveException;
import ca.digitalcave.moss.common.LogUtil;
import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.common.ParseCommands;
import ca.digitalcave.moss.common.ParseCommands.ParseResults;
import ca.digitalcave.moss.common.ParseCommands.ParseVariable;
import ca.digitalcave.moss.common.StreamUtil;
import ca.digitalcave.moss.common.Version;
import ca.digitalcave.moss.crypto.IncorrectPasswordException;
import ca.digitalcave.moss.swing.ApplicationModel;
import ca.digitalcave.moss.swing.LookAndFeelUtil;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.exception.WindowOpenException;
import net.java.dev.SwingWorker;

/**
 * The main class, containing the launch methods for Buddi.  This class 
 * is responsible for setting up the environment (including reading
 * command line options), loading the preferences and languages, 
 * warning the user (if using experimental code), etc.  It also initializes
 * and contains access methods for several environment-type methods,
 * such as what OS we are running on, and what the working directory is.
 * 
 * @author wyatt
 * @author jdidion - Split the LNF dialog into its own class for better maintainability
 */
public class Buddi {

//	private static String userDir;
	private static String pluginsFolder;
	private static String languagesFolder;
	private static String reportsFolder;
	private static List<File> filesToLoad;
	private static Boolean noAutoSave = false;
	private static Boolean debian = false;
	private static Boolean windowsInstaller = false;
	private static Boolean genericUnix = false;
	private static Boolean slackware = false;
	private static Boolean redhat = false;
	private static Boolean legacy = false;
	private static File logFile = null;
	private static Version version = null;

	public static Version getVersion() {
		if (version == null)
			version = Version.getVersionResource("version.txt");
		return version;
	}

	/** 
	 * @return True if running on generic Unix, false otherwise.  This is 
	 * obtained through the startup flag --unix, so it can be 
	 * faked if desired.  This is used to determine which download
	 * to use on Linux - if the flag is set, we will download
	 * a .deb when a new version is released, otherwise we will
	 * download a .jar.
	 */
	public static boolean isUnix(){
		if (genericUnix == null)
			genericUnix = false;
		return genericUnix;
	}
	
	/** 
	 * @return True if running on legacy OSX (<= 10.6), false otherwise.  This is 
	 * obtained through the startup flag --legacy, so it can be 
	 * faked if desired.  This is used to determine which download
	 * to use on OSX.
	 */
	public static boolean isLegacy(){
		if (legacy == null)
			legacy = false;
		return legacy;
	}

	/** 
	 * @return True if running on Slackware, false otherwise.  This is 
	 * obtained through the startup flag --slackware, so it can be 
	 * faked if desired.  This is used to determine which download
	 * to use on Linux - if the flag is set, we will download
	 * a .deb when a new version is released, otherwise we will
	 * download a .jar.
	 */
	public static boolean isSlackware(){
		if (slackware == null)
			slackware = false;
		return slackware;
	}

	/**
	 * @return True if running from a Windows Installer installed .exe, false
	 * otherwise.  This is obtained through the startup flag --windows-installer, so it can be 
	 * faked if desired.  This is used to determine which download
	 * to use on Windows - if the flag is set, we will download
	 * the installer version when a new version is released, otherwise we will
	 * download the standalone .exe.
	 */
	public static boolean isWindowsInstaller(){
		if (windowsInstaller == null)
			windowsInstaller = false;
		return windowsInstaller;
	}


	/** 
	 * @return True if running on Redhat, false otherwise.  This is 
	 * obtained through the startup flag --debian, so it can be 
	 * faked if desired.  This is used to determine which download
	 * to use on Linux - if the flag is set, we will download
	 * a .deb when a new version is released, otherwise we will
	 * download a .jar.
	 */
	public static boolean isRedhat(){
		if (redhat == null)
			redhat = false;
		return redhat;
	}

	/** 
	 * @return True if running on Debian, false otherwise.  This is 
	 * obtained through the startup flag --debian, so it can be 
	 * faked if desired.  This is used to determine which download
	 * to use on Linux - if the flag is set, we will download
	 * a .deb when a new version is released, otherwise we will
	 * download a .jar.
	 */
	public static boolean isDebian(){
		if (debian == null)
			debian = false;
		return debian;
	}

	private static boolean isAutoSave(){
		if (noAutoSave == null)
			noAutoSave = false;
		return !noAutoSave;
	}

	public static File getPluginsFolder(){
		if (pluginsFolder == null)
			pluginsFolder = OperatingSystemUtil.getUserFolder("Buddi") + File.separator + Const.PLUGIN_FOLDER;
		return new File(pluginsFolder);
	}

	public static File getReportsFolder(){
		if (reportsFolder == null)
			reportsFolder = OperatingSystemUtil.getUserFolder("Buddi") + File.separator + Const.REPORT_FOLDER;
		return new File(reportsFolder);
	}	

	public static File getLanguagesFolder(){
		if (languagesFolder == null)
			languagesFolder = OperatingSystemUtil.getUserFile("Buddi", Const.LANGUAGE_FOLDER).getAbsolutePath();
		Logger.getLogger(Buddi.class.getName()).finest(languagesFolder);
		return new File(languagesFolder);
	}

	/**
	 * Method to start the GUI.  Should be run from the AWT Dispatch thread.
	 */
	private static void launchGUI(){
		//Handle opening files from command line.
		if (filesToLoad.size() > 0){
			for (File f : filesToLoad) {
				openFile(f);
			}
		}
		
		int osxMinorVersion;
		try {
			osxMinorVersion = Integer.parseInt(System.getProperty("os.version").split("\\.")[1]);
		}
		catch (Throwable t){
			osxMinorVersion = 0;
		}

		if (OperatingSystemUtil.isMac() && isLegacy() && osxMinorVersion >= 7){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					String[] buttons = new String[2];
					buttons[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_DOWNLOAD);
					buttons[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL);

					int reply = JOptionPane.showOptionDialog(
							null,
							"It appears that a legacy version of Buddi is running on a new version of OS X.\nThis can result in errors to the look and feel.  Please download\na version of Buddi from http://buddi.digitalcave.ca/download.jsp\nwhich matches your operating system version.",
							"Legacy Buddi Version",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							buttons,
							buttons[0]);

					if (reply == JOptionPane.YES_OPTION){
						try{
							BrowserLauncher.open(Const.DOWNLOAD_URL_STABLE);
						}
						catch (Exception e){
							Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", e);
						}
					}
				}
			});
		}
		else if (OperatingSystemUtil.isMac() && !isLegacy() && osxMinorVersion < 7){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					String[] buttons = new String[2];
					buttons[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_DOWNLOAD);
					buttons[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL);

					int reply = JOptionPane.showOptionDialog(
							null,
							"It appears that a new version of Buddi is running on an old version of OS X.\nThis can result in errors to the look and feel.  Please download a\nversion of Buddi from http://buddi.digitalcave.ca/download.jsp\nwhich matches your operating system version.",
							"Legacy OS X Version",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							buttons,
							buttons[0]);

					if (reply == JOptionPane.YES_OPTION){
						try{
							BrowserLauncher.open(Const.DOWNLOAD_URL_STABLE);
						}
						catch (Exception e){
							Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", e);
						}
					}
				}
			});
		}
		

		//If we have found a new version last time, we prompt for it now.
		if (PrefsModel.getInstance().isShowUpdateNotifications() 
				&& PrefsModel.getInstance().getAvailableVersion() != null){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Version availableVersion = new Version(PrefsModel.getInstance().getAvailableVersion());
					Version thisVersion = getVersion();

					if (thisVersion.compareTo(availableVersion) < 0){
						String[] buttons = new String[2];
						buttons[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_DOWNLOAD);
						buttons[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL);

						int reply = JOptionPane.showOptionDialog(
								null,
								TextFormatter.getTranslation(BuddiKeys.NEW_VERSION_MESSAGE)
								+ " " + PrefsModel.getInstance().getAvailableVersion() + "\n"
								+ TextFormatter.getTranslation(BuddiKeys.NEW_VERSION_MESSAGE_2),
								TextFormatter.getTranslation(BuddiKeys.NEW_VERSION),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.INFORMATION_MESSAGE,
								null,
								buttons,
								buttons[0]);

						if (reply == JOptionPane.YES_OPTION){
							try{
								BrowserLauncher.open(Const.DOWNLOAD_URL_STABLE);
							}
							catch (Exception e){
								Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", e);
							}
						}
					}
				}
			});
		}

		//If we have specified that we want to prompt for a data file at startup,
		// do so (assuming that we have not already opened one from the command
		// line, or Mac Application listeners.
		if (PrefsModel.getInstance().isShowPromptAtStartup()
				&& ApplicationModel.getInstance().getOpenFrames().size() == 0){
			new FileOpen(null).doClick();
		}

		//Handle opening the last user file, if available.
		if (PrefsModel.getInstance().getLastDataFiles() != null
				&& ApplicationModel.getInstance().getOpenFrames().size() == 0) {
			for (File f : PrefsModel.getInstance().getLastDataFiles()) {
				openFile(f);				
			}
		}

		//If no files are available, just create a new one.
		if (ApplicationModel.getInstance().getOpenFrames().size() == 0) {
			try {
				Document model = ModelFactory.createDocument();
				MainFrame mainWndow = new MainFrame(model);
				try{
					mainWndow.openWindow(
							PrefsModel.getInstance().getWindowSize(model.getFile() + ""), 
							PrefsModel.getInstance().getWindowLocation(model.getFile() + ""));
				}
				catch (WindowOpenException woe){}
			}
			catch (ModelException me){
				Logger.getLogger(Buddi.class.getName()).log(Level.SEVERE, "Model Exception", me);
			}
		}

		//Check if we have opened at least one window...
		MainFrame mainFrame = null;
		for (MossFrame mossFrame : ApplicationModel.getInstance().getOpenFrames()) {
			if (mossFrame instanceof MainFrame){
				mainFrame = (MainFrame) mossFrame;
				break;
			}
		}
		if (mainFrame == null)
			Logger.getLogger(Buddi.class.getName()).severe("No Buddi main windows were able to open!");

		//Start the background startup tasks... 
		startVersionCheck(mainFrame);
		startUpdateCheck(mainFrame);


		//Start the auto save timer
		if (isAutoSave()){
			Timer t = new Timer();
			t.schedule(new TimerTask(){
				@Override
				public void run() {
					SwingUtilities.invokeLater(new Runnable(){
						public void run() {	
							for (MossFrame frame : ApplicationModel.getInstance().getOpenFrames()) {
								if (frame instanceof MainFrame){
									MainFrame mainFrame = (MainFrame) frame;
									if (mainFrame.getDocument().isChanged()){
										File autoSaveLocation = ModelFactory.getAutoSaveLocation(mainFrame.getDocument().getFile());

										try {
											((DocumentImpl) mainFrame.getDocument()).saveAuto(autoSaveLocation);
											Logger.getLogger(this.getClass().getName()).finest("Auto saved file to " + autoSaveLocation);
										}
										catch (DocumentSaveException dse){
											Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error saving autosave file", dse);
										}
									}
									else {
										Logger.getLogger(this.getClass().getName()).finest("Did not autosave, as there are no changes to the data file " + mainFrame.getDocument().getFile());
									}		
								}
							}
						};
					});
				}
			}, 
			PrefsModel.getInstance().getAutosaveDelay() * 1000, 
			PrefsModel.getInstance().getAutosaveDelay() * 1000); //Use preferences to decide save period
		}

		//If it has not already been done, disable the splash screen now.
//		MossSplashScreen.hideSplash();
	}


	/**
	 * Opens the given file.  Must be run in the event dispatch thread. 
	 * @param f
	 */
	private static void openFile(final File f){
		//Handle opening files from command line.
		if (f.getName().endsWith(Const.DATA_FILE_EXTENSION)){
			try {
				Document model;
				model = ModelFactory.createDocument(f);

				MainFrame mainWndow = new MainFrame(model);
				try {
					mainWndow.openWindow(
							PrefsModel.getInstance().getWindowSize(model.getFile() + ""), 
							PrefsModel.getInstance().getWindowLocation(model.getFile() + ""));
				}
				catch (WindowOpenException woe){}
			}
			catch (DocumentLoadException lme){
				Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Error loading document", lme);
			}
			catch (OperationCancelledException oce){}  //Do nothing
		}
		else if (f.getName().endsWith(Const.EXTRACTED_DATA_FILE_EXTENSION)){
			try {
				Document model;
				model = ModelFactory.importDocument(f);

				MainFrame mainWndow = new MainFrame(model);
				try {
					mainWndow.openWindow(
							PrefsModel.getInstance().getWindowSize(model.getFile() + ""), 
							PrefsModel.getInstance().getWindowLocation(model.getFile() + ""));
				}
				catch (WindowOpenException woe){}
			}
			catch (DocumentLoadException lme){
				Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Error loading document", lme);
			}
			catch (OperationCancelledException oce){}  //Do nothing
		}
		else if (f.getName().endsWith(Const.PLUGIN_EXTENSION)){
			Logger.getLogger(Buddi.class.getName()).info("Trying to copy " + f.getAbsolutePath() + " to " + Buddi.getPluginsFolder() + File.separator + f.getName());
			if (!Buddi.getPluginsFolder().exists()){
				if (!Buddi.getPluginsFolder().mkdirs()){
					Logger.getLogger(Buddi.class.getName()).warning("Error creating Plugins directory!");
				}
			}
			try {
				File dest = new File(Buddi.getPluginsFolder().getAbsolutePath() + File.separator + f.getName());
				if (f.getAbsolutePath().equals(dest.getAbsolutePath()))
					throw new IOException("Cannot copy to the same file.");
				FileFunctions.copyFile(f, dest);

				SwingUtilities.invokeLater(new Runnable(){
					public void run() {	
						String[] options = new String[1];
						options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

						JOptionPane.showOptionDialog(
								null, 
								TextFormatter.getTranslation(BuddiKeys.MESSAGE_PLUGIN_ADDED_RESTART_NEEDED),
								TextFormatter.getTranslation(BuddiKeys.MESSAGE_PLUGIN_ADDED_RESTART_NEEDED_TITLE),
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.INFORMATION_MESSAGE,
								null,
								options,
								options[0]
								);
					}
				});
			}
			catch (IOException ioe){
				Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Error copying plugin to Plugins directory", ioe);
			}
		}
		else if (f.getName().endsWith(Const.LANGUAGE_EXTENSION)){
			Logger.getLogger(Buddi.class.getName()).info("Trying to copy " + f.getAbsolutePath() + " to " + Buddi.getLanguagesFolder() + File.separator + f.getName());
			if (!Buddi.getLanguagesFolder().exists()){
				if (!Buddi.getLanguagesFolder().mkdirs()){
					Logger.getLogger(Buddi.class.getName()).warning("Error creating Languages directory!");
				}
			}
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {	
					try {
						File dest = new File(Buddi.getLanguagesFolder().getAbsolutePath() + File.separator + f.getName());
						if (f.getAbsolutePath().equals(dest.getAbsolutePath()))
							throw new IOException("Cannot copy to the same file.");
						FileFunctions.copyFile(f, dest);

						String[] options = new String[1];
						options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

						JOptionPane.showOptionDialog(
								null, 
								TextFormatter.getTranslation(BuddiKeys.MESSAGE_LANGUAGE_ADDED_RESTART_NEEDED),
								TextFormatter.getTranslation(BuddiKeys.MESSAGE_LANGUAGE_ADDED_RESTART_NEEDED_TITLE),
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.INFORMATION_MESSAGE,
								null,
								options,
								options[0]
								);

						PrefsModel.getInstance().setLanguage(f.getName().replaceAll(Const.LANGUAGE_EXTENSION, ""));
						PrefsModel.getInstance().save();
					}
					catch (IOException ioe){
						Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Error copying translation to Languages directory", ioe);
					}
				}
			});
		}
	}


	/**
	 * Main method for Buddi.  Can pass certain command line options
	 * in.  Use --help flag to see complete list.
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", Const.PROJECT_NAME);

		String help = "USAGE: java -jar Buddi.jar <options> <data file>, where options include:\n"
			+ "--usb\t\tRun on a USB key: put preferences, languages, and plugins in working dir.\n"
			+ "--prefs\tFilename\tPath and name of Preference File (Default varies by platform)\n"
			+ "--verbosity\t0-5\tVerbosity Level (0 = Emergency, 7 = Verbose)\n"
			+ "--languages\tFolder\tFolder to store custom languages (should be writable; default varies by platform)\n"
			+ "--plugins\tFolder\tFolder to store plugins (should be writable; default varies by platform)\n"
			+ "--nosplash\t\tDon't show splash screen on startup\n"
			+ "--lnf\tclassname\tTry to use the given LnF; you must manually include the LnF jar in the classpath.  Set to 'none' to disable all Look and Feel calls\n"
			+ "--extract\tData File\tExtracts the XML contents of the given file, to Filename.xml.  Used for debugging.\n"
			+ "--import\tData File\tImports the XML contents of the given file, from Filename.xml.  Used to load XML files previously extracted by --extract option.\n"
			+ "--log\tlogFile\tLocation to store logs, or 'stdout' / 'stderr' (default varies by platform)\n";
		// Undocumented flag --font	<fontName> will specify a font to use by default
		// Undocumented flag --windows-installer will specify a -Installer.exe download for new versions.
		// Undocumented flag --debian will specify a .deb download for new versions.
		// Undocumented flag --redhat will specify a .rpm download for new versions.
		// Undocumented flag --slackware will specify a -Slackware.tgz download for new versions.
		// Undocumented flag --unix will specify a .tgz download for new versions.
		// Undocumented flag --legacy will specify a -Legacy.dmg download for new versions on OSX.
		// Undocumented flag --noautosave will disable auto save (for development testing).

		List<ParseVariable> variables = new LinkedList<ParseVariable>();
		variables.add(new ParseVariable("--usb", Boolean.class, false));
		variables.add(new ParseVariable("--prefs", String.class, false));
		variables.add(new ParseVariable("--verbosity", Integer.class, false));
		variables.add(new ParseVariable("--plugins", String.class, false));
		variables.add(new ParseVariable("--languages", String.class, false));
		variables.add(new ParseVariable("--log", String.class, false));
		variables.add(new ParseVariable("--nosplash", Boolean.class, false));
		variables.add(new ParseVariable("--noautosave", Boolean.class, false));
		variables.add(new ParseVariable("--extract", String.class, false));
		variables.add(new ParseVariable("--import", String.class, false));
		variables.add(new ParseVariable("--reports", String.class, false));
		variables.add(new ParseVariable("--lnf", String.class, false));

		variables.add(new ParseVariable("--font", String.class, false));

		variables.add(new ParseVariable("--debian", Boolean.class, false));
		variables.add(new ParseVariable("--redhat", Boolean.class, false));
		variables.add(new ParseVariable("--slackware", Boolean.class, false));
		variables.add(new ParseVariable("--unix", Boolean.class, false));
		variables.add(new ParseVariable("--legacy", Boolean.class, false));
		variables.add(new ParseVariable("--windows-installer", Boolean.class, false));

		final ParseResults results = ParseCommands.parse(args, help, variables);

		//Extract file to stdout, and exit.
		if (results.getString("--extract") != null){
			extractFile(new File(results.getString("--extract")));
			while(true){
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Extracting data file...");
			}
		}
		
		if (!"none".equals(results.getString("--lnf"))) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						//Set Buddi-specific LnF options, mostly (all?) for Quaqua.
						//This one removes the width limitation for dialogs.  Since we already will
						// wrap for other OS's, there is no need to have Quaqua do this for you.
						UIManager.put("OptionPane.maxCharactersPerLineCount", Integer.MAX_VALUE);
						//Set the max row count.  Quaqua overrides the value set in MossScrollingComboBox, 
						// so we must set it here manually. 
						UIManager.put("ComboBox.maximumRowCount", Integer.valueOf(10));		

						//Load the correct Look and Feel.  Includes OS specific options, such as Quaqua constants.
						LookAndFeelUtil.setLookAndFeel(results.getString("--lnf"));
					}
				});
			} catch (InterruptedException e) {
				Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, e.getMessage(), e);
			} catch (InvocationTargetException e) {
				Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, e.getMessage(), e);
			}
		}

		//Catch runtime exceptions
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
			public void uncaughtException(Thread arg0, Throwable arg1) {
				Logger.getLogger(Buddi.class.getName()).log(Level.SEVERE, "Uncaught exception", arg1);
				if (arg1 instanceof DataModelProblemException)
					sendBugReport(((DataModelProblemException) arg1).getDataModel());
				else
					sendBugReport();
			}
		});


		//Set up the logging system.  If we have specified --log, we first
		// try using that file.  If that is not specified, we use the OS
		// specific log file location.
		try {
			PrintStream logStream = System.err; //Default to stderr
			if (results.getString("--log") != null){
				if (results.getString("--log").equals("stdout"))
					logStream = System.out;
				else if (results.getString("--log").equals("stderr"))
					logStream = System.err;
				else {
					logFile = new File(results.getString("--log"));
				}
			}
			else
				logFile = OperatingSystemUtil.getLogFile("Buddi", Const.LOG_FILE);

			if (logFile != null){
				if (!logFile.getParentFile().exists())
					logFile.getParentFile().mkdirs();
				if (logFile.getParentFile().exists())
					logStream = new PrintStream(logFile);
			}
			
			Logger logger = Logger.getLogger("org.homeunix.thecave");
			Handler streamHandler = new StreamHandler(logStream, new SimpleFormatter());
			logger.addHandler(streamHandler);
		}
		catch (FileNotFoundException fnfe){
			Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Cannot open log file", fnfe);
		}

		//Set up the log levels
		Integer verbosity = results.getInteger("--verbosity");
		if (verbosity != null){
			Level level;
			switch (verbosity) {
			case 0:
				level = Level.SEVERE;
				break;
			case 1:
				level = Level.WARNING;
				break;
			case 2:
				level = Level.WARNING;
				break;
			case 3:
				level = Level.INFO;
				break;
			case 4:
				level = Level.INFO;
				break;
			case 5:
				level = Level.FINE;
				break;
			case 6:
				level = Level.FINER;
				break;
			case 7:
				level = Level.FINEST;
				break;
			default:
				level = Level.INFO;
				break;
			}

			LogUtil.setLogLevel(level.toString());
			Logger.getLogger(Buddi.class.getName()).finest("Setting log level to " + level);
		}
		else {
			if (Const.DEVEL)
				Logger.getLogger("org.homeunix.thecave").setLevel(Level.FINEST);
			else
				Logger.getLogger(Buddi.class.getName()).setLevel(Level.INFO);
		}

		//Prints the version of Buddi to the logs
		Logger.getLogger(Buddi.class.getName()).info("Buddi version: " + getVersion());
		Logger.getLogger(Buddi.class.getName()).info("Buddi command line arguments: " + Arrays.toString(args));
		Logger.getLogger(Buddi.class.getName()).info("Operating System: " + System.getProperty("os.name") + ", " + System.getProperty("os.arch"));
		Logger.getLogger(Buddi.class.getName()).info("Java VM version: " + System.getProperty("java.version"));

		//Parse all the remaining options
		Boolean usbKey = results.getBoolean("--usb");
		String prefsLocation = results.getString("--prefs");
		String font = results.getString("--font");
		languagesFolder = results.getString("--languages");
		pluginsFolder = results.getString("--plugins");

		noAutoSave = results.getBoolean("--noautosave");
		windowsInstaller = results.getBoolean("--windows-installer");
		slackware = results.getBoolean("--slackware");
		debian = results.getBoolean("--debian");
		redhat = results.getBoolean("--redhat");
		genericUnix = results.getBoolean("--unix");
		legacy = results.getBoolean("--legacy");
		reportsFolder = results.getString("--reports");


		filesToLoad = new LinkedList<File>();
		
		if (results.getString("--import") != null){
			filesToLoad.add(new File(results.getString("--import")));
		}
		for (String s : results.getCommands()) {
			filesToLoad.add(new File(s));
		}

		Logger.getLogger(Buddi.class.getName()).finest("Files to load: " + filesToLoad);

		String currentWorkingDir = System.getProperty("user.dir") + File.separator;

		//Set some directories if USB mode is enabled.
		if (usbKey != null && usbKey){
			if (languagesFolder == null)
				languagesFolder = currentWorkingDir + Const.LANGUAGE_FOLDER;
			if (pluginsFolder == null)
				pluginsFolder = currentWorkingDir + Const.PLUGIN_FOLDER;
			if (prefsLocation == null)
				prefsLocation = currentWorkingDir + Const.PREFERENCE_FILE_NAME;
		}

		//Override the location of the Prefs file, if the -p flag is set.
		// This MUST be called before the first PrefsMode.getInstance() 
		// is called.
		if (prefsLocation != null){
			PrefsModel.setPrefsFile(new File(prefsLocation));
		}

		//If we specify a different font, replace all font instances with it.
		// This is useful for some locales, in which the default font
		// won't work properly.
		if (font != null){
			Enumeration<Object> keys = UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object value = UIManager.get(key);
				if (value instanceof FontUIResource){
					FontUIResource f = (FontUIResource) value;
					UIManager.put(key, new FontUIResource(font, f.getStyle(), f.getSize()));
				}
			}  
		}

		//Let the user know where the working directory is, after
		// we have set up logging properly.
		Logger.getLogger(Buddi.class.getName()).info("Working directory: " + currentWorkingDir);

		//Run any RunnablePlugins which we may have here.
		for (BuddiRunnablePlugin plugin : (List<BuddiRunnablePlugin>) BuddiPluginFactory.getPlugins(BuddiRunnablePlugin.class)) {
			final BuddiRunnablePlugin finalPlugin = plugin;
			try {
				plugin.run();
			}
			catch (RuntimeException re){
				Logger.getLogger(Buddi.class.getName()).log(Level.SEVERE, "Runtime Exception encountered while starting plugin", re);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JOptionPane.showMessageDialog(null, "There was a problem starting the plugin " + finalPlugin.getClass() + ".  Please send a copy of Buddi.log to the plugin author for debugging.");
					}
				});
			}
			catch (Exception e){
				Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Exception encountered while starting plugin", e);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JOptionPane.showMessageDialog(null, "There was a problem starting the plugin " + finalPlugin.getClass() + ".  Please send a copy of Buddi.log to the plugin author for debugging.");
					}
				});
			}
		}

		//Start the GUI in the proper thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				launchGUI();
			}
		});
	}

	/**
	 * Display any information you need to see on first version, such as 
	 * donation requests, etc. 
	 */
	private static void startVersionCheck(final MossFrame frame){
		String[] warningButtons = new String[2];
		warningButtons[0] = "Continue";
		warningButtons[1] = "Quit";
		
//		int warningReply = JOptionPane.showOptionDialog(
//				frame,
//				"This version of Buddi includes major changes to transactions, and is probably\n" +
//				"not yet stable enough for production use.  USE THIS VERSION AT YOUR OWN RISK!!!\n\n" +
//				"Please report any bugs you encounter to Wyatt (wyatt.olson@gmail.com), or via the\n" +
//				"Buddi website.\n\n" +
//				"To repeat: There are very likely serious bugs with this version, and IT IS NOT\n" +
//				"RECOMMENDED FOR USE WITH REAL FINANCIAL DATA.  IF YOU USE IT, AND YOU\n" +
//				"LOSE YOUR DATA, DON'T COMPLAIN TO ME!",
//				"WARNING - Major Unstable Changes to Data and User Interface",
//				JOptionPane.YES_NO_OPTION,
//				JOptionPane.WARNING_MESSAGE,
//				null,
//				warningButtons,
//				warningButtons[1]
//				);
//		if (warningReply != JOptionPane.YES_OPTION){
//			System.exit(0);
//		}
		
		//Set the default My Budget renderer behaviour to be the same as 3.4.0.8.  You can delete this line once most people have upgraded past it.
		if (PrefsModel.getInstance().getLastVersion() != null && PrefsModel.getInstance().getLastVersion().isSamePatch(new Version(3, 4, 0, 8))){
			PrefsModel.getInstance().setShowCurrentBudget(true);
		}
		
		if (PrefsModel.getInstance().getLastVersion() == null 
				|| !PrefsModel.getInstance().getLastVersion().equals(getVersion())){
			PrefsModel.getInstance().updateVersion();


			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					String[] buttons = new String[2];
					buttons[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_DONATE);
					buttons[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_NOT_NOW);

					int reply = JOptionPane.showOptionDialog(
							frame, 
							TextFormatter.getTranslation(BuddiKeys.MESSAGE_ASK_FOR_DONATION),
							TextFormatter.getTranslation(BuddiKeys.MESSAGE_ASK_FOR_DONATION_TITLE),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE,
							null,
							buttons,
							buttons[0]);

					if (reply == JOptionPane.YES_OPTION){
						try{
							BrowserLauncher.open(Const.DONATE_URL);
						}
						catch (Exception e){
							Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", e);
						}
					}
				}
			});
		}
	}

	/**
	 * Starts a thread which checks the Internet for any new versions.
	 */
	public static void startUpdateCheck(final MossFrame frame){
		if (PrefsModel.getInstance().isShowUpdateNotifications()){
			Thread updateThread = new Thread("Update Thread"){
				public void run() {
					try{
						Proxy p;
						if (PrefsModel.getInstance().isShowProxySettings()){
							InetAddress proxyAddress = InetAddress.getByName(PrefsModel.getInstance().getProxyServer());
							InetSocketAddress socketAddress = new InetSocketAddress(proxyAddress, PrefsModel.getInstance().getPort()); 
							p = new Proxy(Proxy.Type.DIRECT, socketAddress);
						}
						else {
							p = Proxy.NO_PROXY;
						}

						URL mostRecentVersion = new URL(Const.PROJECT_URL + Const.VERSION_FILE + "?current=" + Buddi.getVersion().toString());
						URLConnection connection = mostRecentVersion.openConnection(p);

						InputStream is = connection.getInputStream();


						Properties versions = new Properties();

						versions.load(is);

						PrefsModel.getInstance().setAvailableVersion(versions.getProperty(Const.BRANCH));
					}
					catch (IOException ioe){
						Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", ioe);
					}					
				};
			};

			updateThread.start();
		}
	}

	/**
	 * Checks for new updates, and notifies user if they are found.
	 */
	public static void doUpdateCheck(final MossFrame frame){
		SwingWorker updateWorker = new SwingWorker(){
			@Override
			public Object construct() {
				try{
					Proxy p;
					if (PrefsModel.getInstance().isShowProxySettings()){
						InetAddress proxyAddress = InetAddress.getByName(PrefsModel.getInstance().getProxyServer());
						InetSocketAddress socketAddress = new InetSocketAddress(proxyAddress, PrefsModel.getInstance().getPort()); 
						p = new Proxy(Proxy.Type.DIRECT, socketAddress);
					}
					else {
						p = Proxy.NO_PROXY;
					}

					URL mostRecentVersion = new URL(Const.PROJECT_URL + Const.VERSION_FILE);
					URLConnection connection = mostRecentVersion.openConnection(p);

					InputStream is = connection.getInputStream();


					Properties versions = new Properties();

					versions.load(is);

					if (versions.get(Const.BRANCH) == null)
						return getVersion();

					Version availableVersion = new Version(versions.get(Const.BRANCH).toString());
					Version thisVersion = getVersion();

					Logger.getLogger(Buddi.class.getName()).finest("This version: " + thisVersion);
					Logger.getLogger(Buddi.class.getName()).finest("Available version: " + availableVersion);

					if (thisVersion.compareTo(availableVersion) < 0)
						return availableVersion;
				}
				catch (IOException ioe){
					Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", ioe);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							String[] options = new String[1];
							options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

							JOptionPane.showOptionDialog(
									frame, 
									TextFormatter.getTranslation(BuddiKeys.MESSAGE_ERROR_CHECKING_FOR_UPDATES), 
									TextFormatter.getTranslation(BuddiKeys.ERROR), 
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.ERROR_MESSAGE,
									null,
									options,
									options[0]);
						}
					});
				}

				return null;
			}

			@Override
			public void finished() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (get() != null){
							String[] buttons = new String[2];
							buttons[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_DOWNLOAD);
							buttons[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL);

							int reply = JOptionPane.showOptionDialog(
									frame, 
									TextFormatter.getTranslation(BuddiKeys.NEW_VERSION_MESSAGE)
									+ " " + get() + "\n"
									+ TextFormatter.getTranslation(BuddiKeys.NEW_VERSION_MESSAGE_2),
									TextFormatter.getTranslation(BuddiKeys.NEW_VERSION),
									JOptionPane.YES_NO_OPTION,
									JOptionPane.INFORMATION_MESSAGE,
									null,
									buttons,
									buttons[0]);

							if (reply == JOptionPane.YES_OPTION){
								try{
									BrowserLauncher.open(Const.DOWNLOAD_URL_STABLE);
								}
								catch (Exception e){
									Logger.getLogger(Buddi.class.getName()).log(Level.WARNING, "Unknown Exception", e);
								}
							}
						}
						//There was no updates - if we want a confirmation, show it
						else {
							String[] options = new String[1];
							options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

							JOptionPane.showOptionDialog(
									frame, 
									TextFormatter.getTranslation(BuddiKeys.MESSAGE_NO_NEW_VERSION), 
									TextFormatter.getTranslation(BuddiKeys.MESSAGE_NO_NEW_VERSION_TITLE), 
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.INFORMATION_MESSAGE,
									null,
									options,
									options[0]);
						}
					}
				});
				super.finished();
			}
		};

		if (Const.DEVEL) Logger.getLogger(Buddi.class.getName()).finest("Starting update checking...");
		updateWorker.start();
	}


	public static void sendBugReport(Document... models){
//		StringBuilder crashLog = new StringBuilder();
//		crashLogger.getLogger().append("\n---Starting Preferences---\n");
//		crashLogger.getLogger().append(PrefsModel.getInstance().saveToString());
//		crashLogger.getLogger().append("---Finished Preferences---\n\n");

//		if (models != null){
//		for (Document m : models) {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		try {
//		m.saveToStream(baos);

//		crashLogger.getLogger().append("---Starting Data File---\n");
//		crashLogger.getLogger().append(baos.toString());
//		crashLogger.getLogger().append("---Finished Data File---\n\n");
//		}
//		catch (DocumentSaveException dse){
//		crashLogger.getLogger().append("---Starting Data File---\n");
//		crashLogger.getLogger().append("Error: Could not save to stream:");
//		crashLogger.getLogger().append(dse.getMessage());
//		crashLogger.getLogger().append("---Finished Data File---\n\n");					
//		}
//		}
//		}

//		if (logFile != null){
//		try{
//		BufferedReader logReader = new BufferedReader(new FileReader(logFile));
//		crashLogger.getLogger().append("---Starting Log File (" + logFile.getAbsolutePath() + ")---\n");
//		String temp;
//		while ((temp = logReader.readLine()) != null)
//		crashLogger.getLogger().append(temp).append("\n");
//		crashLogger.getLogger().append("---Finished Log File---\n");
//		}
//		catch (IOException ioe){}
//		}


//		if (PrefsModel.getInstance().isSendCrashReports()){
//		try {
//		BrowserLauncher bl = new BrowserLauncher();
//		bl.openURLinBrowser("mailto:wyatt.olson@gmail.com?subject=" 
//		+ URLEncoder.encode("Buddi Crash Report", "UTF-8").replaceAll("\\+", "%20") 
//		+ "&body="
//		+ URLEncoder.encode(
//		"There has been a serious problem encountered in Buddi, " +
//		"and Buddi has closed.  It is recommended that you send a " +
//		"crash report to the program author (Wyatt Olson <wyatt.olson@gmail.com>) " +
//		"to help him troubleshoot the problem.  The following components may " +
//		"be included in the crash report:\n\t-Contents of your Preferences " +
//		"file,\n\t-Contents of all open Data files\n\t-Contents of the Buddi " +
//		"log file\n\nThese components are included as text in the body below, " +
//		"clearly marked between tags such as '---Starting Preferences---' " +
//		"and '---Finished Preferences---'.  If for privacy or other reasons you" +
//		" wish to remove one or more of these components, feel free to do so." +
//		"\n\nIf you use a different mail program (such as a Web based email " +
//		"program), plese copy the entire contents of this email to that program, " +
//		"and send it to Wyatt.\n\nIf you do not wish to send this crash report at " +
//		"all, simply close this window.  You can disable sending future crash " + 
//		"reports in Buddi's Preferences.\n\n\n"
//		+ crashLogger.getLogger().toString()

//		, 
//		"UTF-8").replaceAll("\\+", "%20"));
//		}
//		catch (Exception e){
//		Logger.getLogger().emergency("Unable to send crash email.");
//		}
//		}

//		Logger.getLogger().critical(crashLogger.getLogger().toString());
	}

	private static void extractFile(final File fileToLoad){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					InputStream is;
					BuddiCryptoFactory factory = new BuddiCryptoFactory();
					char[] password = null;

					//Loop until the user gets the password correct, hits cancel, 
					// or some other error occurs.
					while (true) {
						try {
							is = factory.getDecryptedStream(new FileInputStream(fileToLoad), password);
							OutputStream os = new FileOutputStream(new File(fileToLoad.getAbsolutePath() + ".xml"));
							StreamUtil.copyStream(is, os);

							os.flush();
							os.close();
							is.close();					

							System.exit(0);
						}
						catch (IncorrectPasswordException ipe){
							//The password was not correct.  Prompt for a new one.
							BuddiPasswordDialog passwordDialog = new BuddiPasswordDialog();
							password = passwordDialog.askForPassword(false, true);

							//User hit cancel.  Exit.
							if (password == null)
								System.exit(0);
						}
						catch (Exception e){
							e.printStackTrace();
							System.exit(1);
						}
					}
				}
				catch (Exception e){
					e.printStackTrace();
					System.exit(1);
				}

				System.exit(0);
			}
		});
	}
}
