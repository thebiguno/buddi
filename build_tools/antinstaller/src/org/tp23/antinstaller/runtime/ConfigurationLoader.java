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
package org.tp23.antinstaller.runtime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.tp23.antinstaller.InstallException;
import org.tp23.antinstaller.Installer;
import org.tp23.antinstaller.PropertiesFileRenderer;
import org.tp23.antinstaller.input.ConditionalField;
import org.tp23.antinstaller.input.InputField;
import org.tp23.antinstaller.input.OutputField;
import org.tp23.antinstaller.input.ResultContainer;
import org.tp23.antinstaller.input.SelectInput;
import org.tp23.antinstaller.input.TargetInput;
import org.tp23.antinstaller.input.TargetSelectInput;
import org.tp23.antinstaller.page.Page;
import org.tp23.antinstaller.page.ProgressPage;
import org.tp23.antinstaller.page.SimpleInputPage;
import org.tp23.antinstaller.renderer.swing.plaf.LookAndFeelFactory;
import org.tp23.antinstaller.runtime.exe.LoadConfigFilter;
import org.tp23.antinstaller.runtime.exe.PropertyLoaderFilter;
import org.tp23.antinstaller.runtime.logic.ExpressionBuilder;
/**
 *
 * <p>Loads the configuration file into memory as an Installer object. </p>
 * <p>This class also contains the main() method to check the config files for common errors </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: tp23</p>
 * @todo this should be an interface not a class
 * @author Paul Hinds
 * @version $Id: ConfigurationLoader.java,v 1.15 2007/01/28 08:44:43 teknopaul Exp $
 */
public class ConfigurationLoader extends LoadConfigFilter{
	
	/**
	 * Command line config checker
	 * @param args String[]
	 * @throws InstallException 
	 */
	public static void main(String[] args) {
        ConfigurationLoader configurationLoader = new ConfigurationLoader();
		String configFile = INSTALLER_CONFIG_FILE;
		if(args.length > 1 && args[1].endsWith(".xml")){
			configFile = args[1];
		}
		int ret = 1;
		try {
            configurationLoader.readConfig(new File(args[0]), configFile);
			ret = configurationLoader.validate();
            
			if(ret > 0){
				System.out.println("VALIDATION FAILED");
			}
		}
		catch (ConfigurationException ex) {
			ex.printStackTrace();
			System.exit(ret);
		}
		catch (IOException ex) {
			ex.printStackTrace();
			System.exit(ret);
		} catch (InstallException ex) {
			// probably ifProperty syntax wrong
			ex.printStackTrace();
			System.exit(ret);
		}
	}
    
    /**
     * This method is not valid until super.readConfig() has been run
     * @return
     */
    public Installer getInstaller(){
        return installer;
    }
	
	public int validate() throws IOException, ConfigurationException, InstallException{
		Page[] pages = installer.getPages();
		boolean foundErrors = false;
		Set pageNames = new HashSet();
		Set targets = new HashSet();
		Set propertyNames = new HashSet();
		Set pagePropertyNames = null;
		
		if(validateInstallerAttributes()){
			foundErrors = true;
		}
		
		for (int p = 0; p < pages.length; p++) {
           System.out.println("Checking page: " + pages[p].getName() );
			if(pageNames.contains(pages[p].getName())){
				System.out.println("Error: page name '"
                                    + pages[p].getName()
                                    + "' repeated - auto loading of configuration will fail");
				foundErrors = true;
			}
			pageNames.add(pages[p].getName());
			
			//TODO check page requirements
			//test ifProperty syntax
			// TODO passes validation even if nothing will evaluate
			if (pages[p] instanceof SimpleInputPage) {
				SimpleInputPage sPage = (SimpleInputPage)pages[p];
				if(sPage.getIfProperty() != null){
	                try {
	                	ResultContainer mock = new ResultContainer();
	                	ExpressionBuilder.parseLogicalExpressions( mock,
	                			sPage.getIfProperty() );
	                }
	                catch( ConfigurationException configExc ){
	                	System.out.println("Error: loading ifProperty," + sPage.getIfProperty() + " ,page: " + pages[p].getName() );
	                	foundErrors = true;   
	                }
				}
            }

			
			pagePropertyNames = new HashSet();
			
			OutputField[] fields = pages[p].getOutputField();
			for (int f = 0; f < fields.length; f++) {
				if(!fields[f].validateObject()){
					foundErrors = true;
					System.out.println("Error in page:" + pages[p].getName());
				}
				if(fields[f] instanceof TargetInput){
					TargetInput tgtInput = (TargetInput)fields[f];
					targets.add(tgtInput.getTarget());
				}
                if(fields[f] instanceof InputField && !(fields[f] instanceof ConditionalField) ){
					InputField genericInput = (InputField)fields[f];
					if(genericInput.getProperty().endsWith(PropertiesFileRenderer.TARGETS_SUFFIX)){
						System.out.println("Error: invalid property name:" + genericInput.getProperty());
						System.out.println("InputField names must not end with -targets");
					}
					String propertyName = genericInput.getProperty();
					//System.out.println("Checking page.property: " + pages[p].getName() + "," + propertyName );
					if(propertyNames.contains(propertyName)){
						//foundErrors = true;
						System.out.println("Repeated property name:"  + propertyName);
						System.out.println("Loading defaults from file will probably not work:"  + propertyName);
					}
					else{
						propertyNames.add(propertyName);
					}
					// repeated properties on the same page are an error always
					if(pagePropertyNames.contains(propertyName)){
						foundErrors = true;
						System.out.println("Repeated property name: page=" + 
								pages[p].getName() + ", property=" + propertyName);
					}
					else{
						pagePropertyNames.add(propertyName);
					}
					
				}
			}
			
		}
		System.out.println("Finished checking config inputs");
		// check page structure
		if(!(pages[pages.length-1] instanceof ProgressPage)){
			foundErrors = true;
			System.out.println("Last Page should be a progress page");
		}
		else{
			if (pages[pages.length-1].getPostDisplayTarget() != null){
				foundErrors = true;
				System.out.println("Progress pages do not support postDisplayTarget");
			}
		}
		// check for targets
		int numOfPageTargets = 0;
		for (int p = 0; p < pages.length; p++) {
            numOfPageTargets += pages[p].getAllTargets().size();
		}
		if(numOfPageTargets == 0){
			System.out.println("Warning: No Page Targets (not a problem if there are target input types)");
		}
		
		Iterator iter = targets.iterator();
		while (iter.hasNext()) {
			String tgt = (String) iter.next();
			if(tgt.endsWith(PropertiesFileRenderer.TARGETS_SUFFIX)){
				System.out.println("Error: invalid target name:" + tgt);
				System.out.println("Target names must not end with -targets");
				foundErrors = true;
			}
		}

		//@todo check targets exist in build.xml remember OSSpecific could be tricky to validate
		
		int numOfTargetInputs = 0;
		// check ifTargets
		ArrayList targetsSoFar = new ArrayList();
		for (int p = 0; p < pages.length; p++) {
			if(pages[p] instanceof SimpleInputPage){
				SimpleInputPage simple = (SimpleInputPage)pages[p];
				String ifTarget = simple.getIfTarget();
				if(ifTarget != null && !targetsSoFar.contains(ifTarget)){
					System.out.println("ifTarget=" + ifTarget);
					System.out.println("ifTarget will never test true, no prior target in page:"+pages[p].getName());
					// disabled due to bug 1412658 could be reinstated with proper test and OSSpecific handling 
					//foundErrors = true;
				}
			}
			// add after to ensure testing previous pages
			targetsSoFar.addAll(pages[p].getAllTargets());
			OutputField[] fields = pages[p].getOutputField();
			for (int f = 0; f < fields.length; f++) {
				if(fields[f] instanceof TargetInput){
					if(numOfTargetInputs == 0){
						System.out.println("Found target input type");
					}
                    numOfTargetInputs++;
					TargetInput ti = (TargetInput)fields[f];
					targetsSoFar.add(ti.getTarget());
				}
				if(fields[f] instanceof TargetSelectInput){
					if(numOfTargetInputs == 0){
						System.out.println("Found target input type");
					}
                    numOfTargetInputs++;
					TargetSelectInput ti = (TargetSelectInput)fields[f];
					SelectInput.Option[] options = ti.getOptions();
					for (int i = 0; i < options.length; i++) {
						SelectInput.Option option = options[i];
						targetsSoFar.add(option.value);
					}
				}
			}
		}
		if(numOfPageTargets == 0 && numOfTargetInputs == 0){
			System.out.println("Warning: No targets found, installer may do nothing.");
		}
//		if(targetsSoFar.contains("default")){
//			System.out.println("Target:target can not be \"default\"");
//			foundErrors = true;
//		}


		System.out.println("Finished checking config");
		if(!foundErrors){
			return 0;
		}
		return 1;
	}

	private boolean validateInstallerAttributes(){
        
        System.out.println("Checking installer: " + installer.getName() );
		boolean foundErrors = false;
		
		String[] validBooleanValues = {"true", "false"};
		foundErrors |= validateValue("antialiased", installer.getAntialiased(), true, validBooleanValues);

		// done in DTD
		//foundErrors |= validateValue("verbose", installer.isVerbose(), true, validBooleanValues);
		//foundErrors |= validateValue("debug", installer.isDebug(), true, validBooleanValues);
		
		String[] validLAFValues = {LookAndFeelFactory.DEFAULT_LAF,
				LookAndFeelFactory.GREYMETAL_LAF,
				LookAndFeelFactory.NATIVE_LAF,
				LookAndFeelFactory.JGOODIES_LAF,
				LookAndFeelFactory.NULL_LAF };
		if( validateValue("lookAndFeel", installer.getLookAndFeel(), true, validLAFValues) ){
			System.out.println("Warning: non standard LookAndFeel ensure the correct classes are on the classpath at runtime:" + installer.getLookAndFeel());
		}
		
		if (installer.getName() == null){
			System.out.println("Error: installer element attribute does not exist: name");
			foundErrors = true;
		}
		
		try {
			String wide = installer.getWide();
			if(wide != null){
				installer.parseWideValue(wide);
			}
		} catch (Exception e) {
			System.out.println("Error: installer element attribute incorrect format (e.g. 600:275): wide");
			foundErrors = true;
		}
		
		String[] validLoadDefaultValues = {PropertyLoaderFilter.FALSE,
                PropertyLoaderFilter.LOAD,
                PropertyLoaderFilter.PROMPT, 
                PropertyLoaderFilter.PROMPT_AUTO};
        
        boolean loadDefaultsNull = true;
        if( installer.supportsAutoBuild() ){
            loadDefaultsNull = false;
        }
		foundErrors |= validateValue("loadDefaults", installer.getLoadDefaults(), loadDefaultsNull, validLoadDefaultValues);

        VersionHelper vHelper = new VersionHelper();
        if( installer.supportsAutoBuild() ){
            if( ! vHelper.isValid(installer.getVersion()) ){
                System.out.println("Error: invalid version attribute, required for -auto builds:" + installer.getVersion());
                foundErrors = true;
            }
        }
        if(installer.getVersion() != null){
            if( ! vHelper.isValid(installer.getVersion()) ){
                System.out.println("Error: invalid version attribute format examples 1.2.0 , 0.2beta:" + installer.getVersion());
                foundErrors = true;
            }
        }
        
		return foundErrors;
	}
	/**
	 * @return foundErrors  (true if there was an error)
	 */
	private boolean validateValue(String att, String value, boolean allowsNull, String[] validValues){
		if(value == null){
			if(!allowsNull){
				System.out.println("Error: installer element attribute does not exist: " + att);
				return true;
			}
			return false;
		}
		else {
			for (int i = 0; i < validValues.length; i++) {
				if(validValues[i].equals(value)){
					return false;
				}
			}
			System.out.println("Error: installer element attribute not valid value: " + att);
			return true;
		}
	}

}
