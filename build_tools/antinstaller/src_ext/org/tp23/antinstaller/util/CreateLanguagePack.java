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
package org.tp23.antinstaller.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.tp23.antinstaller.InstallException;
import org.tp23.antinstaller.Installer;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.runtime.exe.LoadConfigFilter;

public class CreateLanguagePack {

	
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Create LanguagePack for antinstall-config.xml in the current directory?");
			br.readLine();
			
			System.out.println("Enter Locale to create e.g. es_EU");
			String locale = br.readLine().trim();
			
			createLanguagePack(loadConfigFile(new File("."), "antinstall-config.xml" ), locale, new File("."));
			
			System.out.println("done.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstallException e) {
			e.printStackTrace();
		}
	}
	
	public static File guiMain(JFrame root) {
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Select antinstall-config.xml file");
			chooser.setApproveButtonText("Select file");
			FileFilter ff = new FileFilter(){
				public boolean accept(File file){
					return file.getName().equals("antinstall-config.xml") || file.isDirectory();
				}
				public String getDescription() {
					return "antinstall-config.xml files";
				}
			};
			chooser.setFileFilter(ff);
			chooser.showOpenDialog(root);
			File chosen = chooser.getSelectedFile();
			if(chosen != null){
				chooser = new JFileChooser();
				chooser.setDialogTitle("Select output directory");
				chooser.setApproveButtonText("Internationalise file");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setCurrentDirectory(chosen.getParentFile());
				chooser.showOpenDialog(root);
				File dir = chooser.getSelectedFile();
				if(dir != null){
					createLanguagePack(loadConfigFile(chosen.getParentFile(), chosen.getName()), null, dir);
					if(! dir.getName().equals("resources")){
						JOptionPane.showMessageDialog(root, "When the resources files are added to the installer jar\n the parent directory must be /resources/");
					}
					return new File(dir, "LanguagePack.properties");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstallException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Installer loadConfigFile(File rootDir, String configName) throws InstallException{
		InstallerContext ctx = new InstallerContext();
		ctx.setFileRoot(rootDir);
        ctx.setInstallerConfigFile(configName);
		LoadConfigFilter configLoader = new LoadConfigFilter();
		configLoader.exec(ctx);
		Installer installer = ctx.getInstaller();
		return installer;
	}
	
	private static void createLanguagePack(Installer installer, String locale, File outputDir) throws IOException{
		LangPackFileRenderer renderer = new LangPackFileRenderer();
		renderer.renderProperties(installer, outputDir, locale);
	}
}
