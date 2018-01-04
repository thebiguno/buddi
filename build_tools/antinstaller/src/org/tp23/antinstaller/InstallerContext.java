/* 
 * Copyright 2005 Paul Hinds
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tp23.antinstaller;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.taskdefs.Execute;
import org.tp23.antinstaller.page.Page;
import org.tp23.antinstaller.renderer.AntOutputRenderer;
import org.tp23.antinstaller.renderer.MessageRenderer;
import org.tp23.antinstaller.runtime.Logger;
import org.tp23.antinstaller.runtime.Runner;
import org.tp23.antinstaller.runtime.exe.AntLauncherFilter;
import org.tp23.antinstaller.runtime.exe.LoadConfigFilter;
/**
 *
 * <p>A single InstallerContext is created by the ExecInstall class and
 * exist for the duration of the Install screens and the runing of
 * the Ant Script. </p>
 * @author Paul Hinds
 * @version $Id: InstallerContext.java,v 1.10 2007/01/28 08:44:41 teknopaul Exp $
 */
public class InstallerContext {

	/**
	 * This is the prefix for environment variables, unlike Ant this is fixed to
	 * the common prefix of "env".  If you dont like this complain to the bug reports
	 * on sourceforge
	 */
	public static final String ENV_PREFIX = "env.";
	/**
	 * This is the prefix for Java system property variables.
	 * This is fixed to "java."
	 */
	public static final String JAVA_PREFIX = "java.";

	private Logger logger = null;
	private Installer installer = null;
	private MessageRenderer messageRenderer = null;
    private AntOutputRenderer antOutputRenderer = null;
    private Runner runner = null;
    private Page currentPage = null;
    private java.io.File fileRoot = null; // ant basedir
    private BuildListener buildListener = null;
    private AntLauncherFilter antRunner = null;
    private String uIOverride = null;
    private String installerConfigFile = LoadConfigFilter.INSTALLER_CONFIG_FILE;
    private String antBuildFile = "build.xml";
    private String configResource;
    
    
    // called after the Ant part has been run
    private boolean installedSucceded = false;
     
	public InstallerContext() {
	}

	public void setInstallSucceded(boolean installedSucceded){
		this.installedSucceded=installedSucceded;
	}
	public boolean isInstallSucceded(){
		return installedSucceded;
	}
	
	public void log(String message){
		if(logger != null) {
            logger.log(message);
        }
	}
	public void log(Throwable message){
		if(logger != null) {
            logger.log(message);
        }
	}
    public void log(boolean vebose, Throwable message){
        if(vebose && logger != null) {
            logger.log(message);
        }
    }

	/**
	 * Check to see if the system is windoze to be able to return the correct prompted
	 * directories.  This method should be IsNotWindows since it assumes anything
	 * that is not windows is Unix
	 * @return boolean true if not windows in the os.name System Property
	 */
	public static boolean isUnix(){
		return System.getProperty("os.name").toLowerCase().indexOf("windows") == -1;
	}

	/**
	 * Use the standard Ant way to load the environment variables, this is not all inclusive
	 * (but will be come Java 1.5 I imagine)
	 * @return Properties
	 */
	public static Properties getEnvironment(){
		Properties props = new Properties();
		try {
			Vector osEnv = Execute.getProcEnvironment();
			for (Enumeration e = osEnv.elements(); e.hasMoreElements(); ) {
				String entry = (String) e.nextElement();
				int pos = entry.indexOf('=');
				if (pos != -1) {
					props.put(ENV_PREFIX + entry.substring(0, pos),
							  entry.substring(pos + 1));
				}
			}
		}
		catch (Exception ex) {
			// swallow exceptions so this can be loaded statically
			// bit of a bugger if you need the environment on Mac OS 9 but not all apps
			// do so we don't want to die inother situations
			System.out.println("Can't load environment:"+ex.getClass()+","+ex.getMessage());
		}
		Properties javaSysProps = System.getProperties();
		Iterator iter = javaSysProps.keySet().iterator();
		while (iter.hasNext()) {
			Object key = (Object)iter.next();
			props.put(JAVA_PREFIX+key.toString(),javaSysProps.get(key));
		}
		return props;
	}

	// Bean methods
	public Installer getInstaller() {
		return installer;
	}

	public String getMinJavaVersion() {
		return installer.getMinJavaVersion();
	}

	public MessageRenderer getMessageRenderer() {
		return messageRenderer;
	}

	public void setMessageRenderer(MessageRenderer messageRenderer) {
		this.messageRenderer = messageRenderer;
		this.messageRenderer.setInstallerContext(this);
	}
	
    public AntOutputRenderer getAntOutputRenderer() {
		return antOutputRenderer;
    }
    
    public void setAntOutputRenderer(AntOutputRenderer antOutputRenderer) {
		this.antOutputRenderer = antOutputRenderer;
    }
    
    public Page getCurrentPage() {
		return currentPage;
    }
    
    public void setCurrentPage(Page currentPage) {
		this.currentPage = currentPage;
    }
	/**
	 * in SelfExtractor - the directory the install has extracted to <br/>
	 * in Scripted installs - the base directory of the install      <br/>
	 * in NonExtractor - the temporary space created for the build   <br/> 
	 * @return 
	 */
	public File getFileRoot() {
		return fileRoot;
	}

	public void setFileRoot(File fileRoot) {
		this.fileRoot = fileRoot;
	}

	public org.apache.tools.ant.BuildListener getBuildListener() {
		return buildListener;
	}

	public void setBuildListener(org.apache.tools.ant.BuildListener buildListener) {
		this.buildListener = buildListener;
	}

	public AntLauncherFilter getAntRunner() {
		return antRunner;
	}

	public void setAntRunner(AntLauncherFilter antRunner) {
		this.antRunner = antRunner;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

	public void setInstaller(Installer installer) {
		this.installer = installer;
	}

	public String getUIOverride() {
		return uIOverride;
	}

	public void setUIOverride(String override) {
		uIOverride = override;
	}
	
	public boolean isAutoBuild(){
		return uIOverride != null && uIOverride.indexOf("-auto") > -1;
	}

	/**
	 * RFE 1569628, the antinstaller config file to use, defaults to antinstall-config.xml
	 * @return
	 */
	public String getInstallerConfigFile() {
		return installerConfigFile;
	}

	public void setInstallerConfigFile(String installerConfigFile) {
		this.installerConfigFile = installerConfigFile;
	}
	/**
	 * RFE 1569628, the build file to use, defaults to build.xml
	 * There should never be any path info, that is derived elsewhere
	 * @return
	 */
	public String getAntBuildFile() {
		return antBuildFile;
	}

	public void setAntBuildFile(String antBuildFile) {
		this.antBuildFile = antBuildFile;
	}

	public String getConfigResource() {
		return configResource;
	}

	public void setConfigResource(String configResource) {
		this.configResource = configResource;
	}
}



