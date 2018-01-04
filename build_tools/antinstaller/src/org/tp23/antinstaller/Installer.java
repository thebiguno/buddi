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

import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import org.tp23.antinstaller.input.OutputField;
import org.tp23.antinstaller.input.ResultContainer;
import org.tp23.antinstaller.page.Page;
import org.tp23.antinstaller.renderer.swing.SizeConstants;
import org.tp23.antinstaller.runtime.IfPropertyHelper;
import org.tp23.antinstaller.runtime.VersionHelper;
import org.tp23.antinstaller.runtime.exe.ExecuteRunnerFilter.AbortException;



/**
 * <p>Object representation of the Installer element in the config. </p>
 * <p>This object holds the reference to all the Pages which in tern hold references
 * to the InputFields,  All the properties in the Installer element are held at this level
 * as is the ResultContainer</p>
 * @author Paul Hinds
 * @version $Id: Installer.java,v 1.9 2007/01/28 08:44:41 teknopaul Exp $
 */
public class Installer {


	// i18n support
	private static ResourceBundle langPack = null;
	static{
		try {
			langPack = ResourceBundle.getBundle("resources.LanguagePack");
		} catch (MissingResourceException e) {
			// ignore, signifies no lang packs installed  
		}
	}

	private String name;
	private String minJavaVersion = "1.4";
	private String ui;// permitted UI override values
	private boolean verbose;
	private boolean debug;
	private String lookAndFeel;
	private String wide;
	private String windowIcon;
	private String defaultImageResource;
	private String finishButtonText = "Install";
    private String antialiased;
    private String loadDefaults;
    private String version = "0.0";
    
	private Page[] pages;
	private ResultContainer resultContainer = new ResultContainer();

	public Installer() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getMinJavaVersion() {
		return minJavaVersion;
	}
    
    public void setMinJavaVersion(String minJavaVersion) throws AbortException {
        if(minJavaVersion != null && ! "".equals(minJavaVersion) ) {
            VersionHelper helper = new VersionHelper();
            if( ! helper.equalOrHigher(System.getProperty("java.version"), minJavaVersion, true) ) {
                throw new AbortException("Incorrect Java version, installer requires: " + minJavaVersion);
            }
        }
        this.minJavaVersion = minJavaVersion;
    }

	public Page[] getPages() {
		return pages;
	}

	public void setPages(Page[] pages) {
		this.pages = pages;
	}

    /**
     * returns full ui attribute with spaces removed (the string is not parsed into tokens).
     * e.g.  "text,swint,swing-auto"
     * @return
     */
	public String getUi() {
		return ui;
	}

    /**
     * Sets the ui attrribute removing any whitespace
     * @param ui
     */
	public void setUi(String ui) {
		this.ui = ui.replaceAll("\\s", "");
	}

	public boolean supportsAutoBuild(){
		return ui != null && ui.indexOf("-auto") > -1;
	}

	public boolean isVerbose() {
		return verbose;
	}
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	public void setVerbose(String strVerbose) {
		this.verbose = OutputField.isTrue(strVerbose);
	}

	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public void setDebug(String strDebug) {
		this.debug = OutputField.isTrue(strDebug);
	}


	public String getLookAndFeel() {
		return lookAndFeel;
	}

	public void setLookAndFeel(String lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}

	public String getWindowIcon() {
		return windowIcon;
	}

	public void setWindowIcon(String windowIcon) {
		this.windowIcon = windowIcon;
	}

	public String getDefaultImageResource() {
		return defaultImageResource;
	}

	public void setDefaultImageResource(String defaultImageResource) {
		this.defaultImageResource = defaultImageResource;
	}

	public String getFinishButtonText() {
		if(langPack != null){
			return langPack.getString("finishButtonText");
		}
		return finishButtonText;
	}

	public void setFinishButtonText(String finishButtonText) {
		this.finishButtonText = finishButtonText;
	}

	public ResultContainer getResultContainer() {
		return resultContainer;
	}

	public String getAntialiased() {
		return antialiased;
	}

	public void setAntialiased(String antialiased) {
		this.antialiased = antialiased;
	}

	public String getWide() {
		return wide;
	}

	public void setWide(String wide) {
		try {
			this.wide = wide;
			parseWideValue(wide);
		} catch (Exception e) {
			// ignore use defaults
		}
	}
	public void parseWideValue(String wide) throws NumberFormatException, StringIndexOutOfBoundsException{
		int pageWidth = Integer.parseInt(wide.substring(0, wide.indexOf(':')));
		int labelWidth = Integer.parseInt(wide.substring(wide.indexOf(':') + 1, wide.length()));
		SizeConstants.PAGE_WIDTH = pageWidth;
		SizeConstants.LABEL_WIDTH = labelWidth;
	}

	public String getLoadDefaults() {
		return loadDefaults;
	}

	public void setLoadDefaults(String loadDefaults) {
		this.loadDefaults = loadDefaults;
	}

	/**
	 * Returns the list of selected targets from the installer obeying
	 * page order. This method is
	 * probably only usefull after the UI screens have finished.  Using prior to that
	 * bear in mind that the user in the Swing GUI can go back and reselect
	 * targets that were not selected previously.
	 * Page targets for pages that were not shown are not returned in the list 
	 * @return Returns a Vector since Ant requires this for the Project class (Should be a List)
	 * @throws InstallException 
	 */
	public Vector getTargets(InstallerContext ctx){
		Vector argsList = new Vector();
		for (int i = 0; i < getPages().length; i++) {
			Page page = getPages()[i];
			List pageTargets = page.getPageTargets();
			boolean shown = conditionalDisplay(page, ctx);
			for (int pt = 0; pt < pageTargets.size(); pt++) {
				Page.IndexedTarget target = (Page.IndexedTarget)pageTargets.get(pt);
				if( ! argsList.contains(target.getTarget()) &&
					shown){
					argsList.add(target.getTarget());
				}
			}
			List elementTargets = page.getElementTargets();
			for (int pt = 0; pt < elementTargets.size(); pt++) {
				Page.IndexedTarget target = (Page.IndexedTarget)elementTargets.get(pt);
				if( ! argsList.contains(target.getTarget()) ){
					argsList.add(target.getTarget());
				}
			}
		}
		return argsList;
	}

	/**
	 * @return boolean true if the page was to be shown
	 * @throws InstallException 
	 */
	private boolean conditionalDisplay(Page page, InstallerContext ctx){
		try {
			IfPropertyHelper helper = new IfPropertyHelper(ctx);
			return helper.ifProperty(page) && helper.ifTarget(page, ctx.getInstaller().getPages());
		} catch (InstallException e) {
			throw new RuntimeException("ifProperty runtime exception");
		}
	}

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


}
