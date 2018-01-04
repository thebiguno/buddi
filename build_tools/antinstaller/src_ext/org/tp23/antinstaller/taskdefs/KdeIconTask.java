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
package org.tp23.antinstaller.taskdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.tp23.antinstaller.selfextract.ResourceExtractor;
/**
 * 
 * @author teknopaul
 *
 */
public class KdeIconTask extends Task{

	private String desktop;
	private String icon;
	private String installDir;
	
	public void execute() throws BuildException{
		if(desktop == null || icon == null || installDir == null){
            throw new BuildException("Missing attribute in KdeIconTask");
        }
		
		ResourceExtractor re = new ResourceExtractor();

		File appsDir = null;
		try {
			appsDir = getAppsDir();
		} catch (Exception e) {
			throw new BuildException("Can not determine KDE directory");
		}
		if( appsDir != null && appsDir.exists() ) {
			try {
				String desktopFileName = desktop.substring( desktop.lastIndexOf('/') + 1 );
				log("creating: " + new File( appsDir, desktopFileName).getCanonicalPath());
				re.copyResource(desktop, new File( appsDir, desktopFileName), "@installDir@", installDir);
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				throw new BuildException(sw.getBuffer().toString());
			}
		}
		else{
			log("apps dir does not exist");
		}
		File iconDir = null;
		try {
			iconDir = getIconDir();
		} catch (Exception e) {
			throw new BuildException("Can not determine KDE directory");
		}
		if(iconDir != null && iconDir.exists()){
			try {				
				String iconFileName = icon.substring( icon.lastIndexOf('/') + 1 );
				log("creating: " + new File( iconDir, iconFileName).getCanonicalPath());
				re.copyResourceBinary(icon, new File( iconDir, iconFileName ));
			} catch (IOException e) {
				throw new BuildException(e.getMessage());
			}
		}
		else{
			log("icon dir does not exist");
		}
		try {	
			Process proc = Runtime.getRuntime().exec("kbuildsycoca");
		} catch (Exception e) {
			log("error running kbuildsycoca: " + e.getMessage());
		}
		
	}
	
	private File getKdeDir(String pathdef, String extension) throws IOException, InterruptedException{
		String[] args = {"kde-config", "--path", pathdef};
		Process proc = Runtime.getRuntime().exec(args);
		BufferedReader br = new BufferedReader( new InputStreamReader(proc.getInputStream()));
		String appsDirs = br.readLine();
		int idx = -1;
        if(appsDirs != null) {
            idx = appsDirs.indexOf(':');
        }
        br.close();
	
		if(idx > -1 && idx < appsDirs.length() - 1){
			String privatedir = appsDirs.substring(0, idx - 1);
			if(!privatedir.endsWith("/")){
				privatedir = privatedir + "/"; 
			}
			return new File(privatedir + extension);
		}
		else{
			if(!appsDirs.endsWith("/")){
				appsDirs = appsDirs + "/"; 
			}
			return  new File(appsDirs + extension);
		}
	}
	private File getAppsDir() throws IOException, InterruptedException{
		return getKdeDir("xdgdata-apps", "");
	}
	private File getIconDir() throws IOException, InterruptedException{
		return getKdeDir("icon", "hicolor/48x48/apps");
	}
	
	
	public String getDesktop() {
		return desktop;
	}
	public void setDesktop(String desktop) {
		this.desktop = desktop;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getInstallDir() {
		return installDir;
	}

	public void setInstallDir(String installDir) {
		this.installDir = installDir;
	}
	
}
