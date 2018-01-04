/*
 * Copyright  2005 Paul Hinds
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.tp23.antinstaller.taskdefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Manifest;
import org.apache.tools.ant.taskdefs.ManifestException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.zip.ZipOutputStream;
import org.tp23.antinstaller.renderer.swing.plaf.LookAndFeelFactory;
import org.tp23.antinstaller.runtime.ConfigurationLoader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Creates a Installer archive.
 * 
 *
 * @ant.task category="packaging"
 */
public class Installer extends Jar {

    public static final String NON_EXTRACTOR = "NonExtractor";
    public static final String SELF_EXTRACTOR = "SelfExtractor";
    
    /** The extract type to use NonExtractor or SelfExtractor */
    private String extractType;

    /** The AntInstall config file */
    private File installConfig;

    /** The Ant build.xml to be used*/
    private File buildFile;

    /** The location of ant-installer.jar and ant-installer-ext.jar and possibly jgoodies-edited-1_2_2.jar */
    private File antInstallLib;

    /** The location of xercesImpl.jar and xml-apis.jar 
     * This is not requried Xerces is optional and will increase the download size */
    private File xercesLib;

    /** The location of ant.jar and ant-launcher.jar */
    private File antLib;

    /** The AntInstaller Look And Feel to be used */
    private String laf;
    
    /** The icon set to use */
    private String icons;

    /** Stop the build if there is a known error in the config */
    protected boolean failOnError = false;
    
    /** perform the validation */
    protected boolean validateConfig = false;
    
    private boolean buildFileSet = false;
    private boolean configFileSet = false;
    /** Indicates that the build.xml and antinstall-config.xml have been added to the fileset already */
    private boolean startCheckDuplicates = false; 

    /** constructor */
    public Installer() {
        super();
        archiveType = "jar";
        emptyBehavior = "create";
        setEncoding("UTF8");
    }
 
    protected void cleanUp() {
        super.cleanUp();
    }

    public void reset() {
        super.reset();
        extractType = null;
        installConfig = null;
        buildFile = null;
    }

	/**
	 * @param installConfig The installConfig to set.
	 */
	public void setInstallConfig(File installConfig) {
		this.installConfig = installConfig;
        if (!installConfig.exists()) {
            throw new BuildException("AntInstall config: "
                                     + installConfig
                                     + " does not exist.");
        }
        // Create a ZipFileSet for this file, and pass it up.
        ZipFileSet fs = new ZipFileSet();
        fs.setFile(installConfig);
        fs.setFullpath("antinstall-config.xml");
        super.addFileset(fs);
        if(this.buildFile != null) {
            startCheckDuplicates = true;
        }
	}
	/**
	 * @param buildFile The buildFile to set.
	 */
	public void setBuildFile(File buildFile) {
		this.buildFile = buildFile;
        if (!buildFile.exists()) {
            throw new BuildException("AntInstall build file: "
                                     + buildFile
                                     + " does not exist.");
        }
        // Create a ZipFileSet for this file, and pass it up.
        ZipFileSet fs = new ZipFileSet();
        fs.setFile(buildFile);
        fs.setFullpath("build.xml");
        super.addFileset(fs);
        if(this.installConfig != null) {
            startCheckDuplicates = true;
        }
	}
	/**
	 * @param icons The ocons pack to use for buttons.
	 */
	public void setIcons(String icons) {
		this.icons = icons;
        // Create a ZipFileSet for this file, and pass it up.
		File iconJar = new File(antInstallLib, "ai-icons-" + icons + ".jar");
        if (!iconJar.exists()) {
            throw new BuildException("Missing icons: "
                                     + iconJar
                                     + " does not exist.");
        }
		FileSet set = new FileSet();
		set.setFile(iconJar);
		addZipGroupFileset(set);

	}
	/**
	 * @param extractType The extractType to set.
	 */
	public void setExtractType(String extractType) {
		this.extractType = extractType;
	}
	
    public void setFailOnError(boolean fail) {
        failOnError = fail;
    }
    
    public void setValidateConfig(boolean validate) {
    	validateConfig = validate;
    }

    /**
     * The location of ant-installer.jar and possibly jgoodies-edited-1_2_2.jar
     * @param antInstallLib The antInstallLib to set.
     */
    public void setAntInstallLib(File antInstallLib) {
        this.antInstallLib = antInstallLib;
        FileSet set = new FileSet();
        set.setFile(new File(antInstallLib, "ant-installer.jar"));
        addZipGroupFileset(set);
    }
    /**
     * The location of ant.jar and ant-launcher.jar
     * @param antLib The antLib to set.
     */
    public void setAntLib(File antLib) {
        this.antLib = antLib;
        FileSet set = new FileSet();
        set.setFile(new File(antLib, "ant.jar"));
        set.setFile(new File(antLib, "ant-launcher.jar"));
        addZipGroupFileset(set);
    }
    /**
     * @param xercesLib The xercesLib to set.
     */
    public void setXercesLib(File xercesLib) {
        this.xercesLib = xercesLib;
        FileSet set = new FileSet();
        set.setFile(new File(xercesLib, "xercesImpl.jar"));
        set.setFile(new File(xercesLib, "xml-apis.jar"));
        addZipGroupFileset(set);
    }

    /**
     * Overrides the ZIP execute() method which creates filesets
     */
    public void execute(){
        log(".-------------------------------.");
        log("|-(o--~AntInstaller.sf.net~--o)-|");
        log("`-----------------by-Paul-Hinds-´");
        
        if(validateConfig) {
            validateConfig();
        } else if(extractType.equals(SELF_EXTRACTOR)){
            // this reads the config just 
            // to extract the lookAndFeel attribute for the manifest at the moment
            readConfig();
        }
        if( LookAndFeelFactory.isDefault(getLaf()) ){
            FileSet set = new FileSet();
            set.setFile(new File(antInstallLib, "jgoodies-edited-1_2_2.jar"));
            addZipGroupFileset(set);
        }

        super.execute();
    }

    /**
     * override of  parent; validates configuration
     * before initializing the output stream.  The Manifest is set in the Jar superclass's
     * method so Manifest modifications must be performed in this method
     */
    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException {
        // If no buildFile file is specified, it's an error.
        if (buildFile == null && !isInUpdateMode()) {
            throw new BuildException("buildFile attribute is required", getLocation());
        }
        // If no installConfig file is specified, it's an error.
        if (installConfig == null && !isInUpdateMode()) {
            throw new BuildException("installConfig attribute is required", getLocation());
        }
        try{ 
        	addConfiguredManifest(this.getManifest());
        }
        catch(ManifestException me){
        	throw new BuildException("Cant add AntInstaller Manifest", me, getLocation());
        }        
        super.initZipOutputStream(zOut);
    }
	
    protected void zipFile(InputStream is, ZipOutputStream zOut, String vPath,
            long lastModified, File fromArchive, int mode)
            throws IOException {
        
        if(vPath.equalsIgnoreCase("antinstall-config.xml")) {
            if(buildFileSet) {
                log("Two antinstall-config.xml files in jar", Project.MSG_WARN);
            }
            buildFileSet = true;
        }
        if(vPath.equalsIgnoreCase("build.xml")) {
            if(configFileSet) {
                log("Two build.xml files in jar", Project.MSG_WARN);
            }
            configFileSet = true;
        }

        super.zipFile(is, zOut, vPath, lastModified, fromArchive, mode);
    }

    /**
     * This method is only valid after readConfig() or validateConfig() have been run
     * @return Returns the Look And Feel class.
     */
    private String getLaf() {
        if(laf == null){
            return LookAndFeelFactory.DEFAULT_LAF;
        }
        return laf;
    }

    private Manifest getManifest() throws ManifestException{
    	if(extractType.equalsIgnoreCase(NON_EXTRACTOR)){
    		return getNonExtractorManifest();
    	}
        else if(extractType.equalsIgnoreCase(SELF_EXTRACTOR)){
            return getSelfExtractorManifest();
        }
        else {
            throw new BuildException("Invalid extractType: " + extractType);
        }
    }
    
    private Manifest getNonExtractorManifest() throws ManifestException{
    	return getCustomManifest("org.tp23.antinstaller.selfextract.NonExtractor");
    }
    private Manifest getSelfExtractorManifest() throws ManifestException{
    	return getCustomManifest("org.tp23.antinstaller.selfextract.SelfExtractor");
    }
    private Manifest getCustomManifest(String mainClass) throws ManifestException{
    	log("Creating MANIFEST.mf");
    	Manifest newManifest = new Manifest();
    	Manifest.Section mainSection = newManifest.getMainSection();
    	Manifest.Attribute attmc = new Manifest.Attribute();
    	attmc.setName("Main-Class");
    	attmc.setValue(mainClass);
    	mainSection.addAttributeAndCheck(attmc);
    	Manifest.Attribute attlaf = new Manifest.Attribute();
    	attlaf.setName("Look-And-Feel");
    	attlaf.setValue(getLaf());
    	mainSection.addAttributeAndCheck(attlaf);
    	return newManifest;
    	
    }
	
	protected void validateConfig(){
		
		int ret = 1;
		try {
			log("validating config...");
            ConfigurationLoader configurationLoader = new ConfigurationLoader();
            configurationLoader.readConfig(installConfig.getParentFile(), installConfig.getName());
			ret = configurationLoader.validate();
            laf = configurationLoader.getInstaller().getLookAndFeel();
			if(ret != 0){
				err();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			err();
		}
		
		try{
			log("parsing included build.xml...");
			InputSource xmlInp = new InputSource(new FileInputStream(buildFile));
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser parser = parserFactory.newSAXParser();
			parser.parse(xmlInp, new DefaultHandler(){
				public void error(SAXParseException e) throws SAXException{
					throw e;
				}
				public void fatalError(SAXParseException e) throws SAXException{
					throw e;
				}
			} );
			log("build.xml is well formed");
		}
		catch (Exception ex) {
			ex.printStackTrace();
			errNestedBuildXml();
		}
	}

    protected void readConfig(){
        
        try {
            log("reading config...");
            ConfigurationLoader configurationLoader = new ConfigurationLoader();
            configurationLoader.readConfig(installConfig.getParentFile(), installConfig.getName());
            laf = configurationLoader.getInstaller().getLookAndFeel();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            err();
        }
    }
    
    /**
	 * error found on validation of the antinstall config
	 */
	private void err(){
		String errorMsg = "Error in config file:" + installConfig.getAbsolutePath();
        if (failOnError) {
            throw new BuildException(errorMsg);
        } else {
            log(errorMsg, Project.MSG_ERR);
        }
	}
	/**
	 * error found in the build.xml used by ant installer (not the one
	 * running this task)
	 */
	private void errNestedBuildXml(){
		String errorMsg = "Error in included build file:" + buildFile.getAbsolutePath();
        if (failOnError) {
            throw new BuildException(errorMsg);
        } else {
            log(errorMsg, Project.MSG_ERR);
        }
	}
}
