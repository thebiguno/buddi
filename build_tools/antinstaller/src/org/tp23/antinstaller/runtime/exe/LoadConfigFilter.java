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
package org.tp23.antinstaller.runtime.exe;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.tp23.antinstaller.InstallException;
import org.tp23.antinstaller.Installer;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.input.AppRootInput;
import org.tp23.antinstaller.input.CheckboxInput;
import org.tp23.antinstaller.input.CommentOutput;
import org.tp23.antinstaller.input.ConditionalField;
import org.tp23.antinstaller.input.ConfirmPasswordTextInput;
import org.tp23.antinstaller.input.DateInput;
import org.tp23.antinstaller.input.DirectoryInput;
import org.tp23.antinstaller.input.ExtValidatedTextInput;
import org.tp23.antinstaller.input.FileInput;
import org.tp23.antinstaller.input.HiddenPropertyInput;
import org.tp23.antinstaller.input.InputField;
import org.tp23.antinstaller.input.LargeSelectInput;
import org.tp23.antinstaller.input.OutputField;
import org.tp23.antinstaller.input.PasswordTextInput;
import org.tp23.antinstaller.input.SelectInput;
import org.tp23.antinstaller.input.TargetInput;
import org.tp23.antinstaller.input.TargetSelectInput;
import org.tp23.antinstaller.input.UnvalidatedTextInput;
import org.tp23.antinstaller.input.ValidatedTextInput;
import org.tp23.antinstaller.page.LicensePage;
import org.tp23.antinstaller.page.Page;
import org.tp23.antinstaller.page.ProgressPage;
import org.tp23.antinstaller.page.SimpleInputPage;
import org.tp23.antinstaller.page.SplashPage;
import org.tp23.antinstaller.page.TextPage;
import org.tp23.antinstaller.runtime.ConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


/**
 * Loads the Ant Install configuration and sets the Installer object back
 * into the context.  A similar technique to the apache digester is used to
 * populate object attributes in this class.
 * N.B. The only types that can be set for object attributes are Strings, booleans
 * or ints.
 * @author Paul Hinds
 * @version $Id: LoadConfigFilter.java,v 1.9 2007/01/28 08:44:39 teknopaul Exp $
 */
public class LoadConfigFilter implements ExecuteFilter {

	//TODO move to InstallerContext
	public static final String INSTALLER_CONFIG_FILE = "antinstall-config.xml";
	
	protected Installer installer = new Installer();
	protected InstallerContext ctx;

	/**
	 * @see org.tp23.antinstaller.runtime.exe.ExecuteFilter#exec(org.tp23.antinstaller.InstallerContext)
	 */
	public void exec(InstallerContext ctx) throws InstallException {
		this.ctx = ctx;
		
		try {
            installer = readConfig(ctx.getFileRoot(), ctx.getInstallerConfigFile());
			ctx.setInstaller(installer);
			ctx.log("Config loaded");
		}
		catch (IOException e) {
			throw new InstallException("Not able to load and read the AntInstaller config", e);
		}
		catch (ConfigurationException e) {
			throw new InstallException("Not able to load and read the AntInstaller config", e);
		}
	}

	/**
	 * Currently read the config using any available XML parser
	 * This method reads the config from the file system
     * @param fileRoot The directory where the config file is stored
     * @param the name of the configuration file (usually antinstall-config.xml)
	 * @return Installer
	 */
	public Installer readConfig(File fileRoot, String fileName) throws IOException, ConfigurationException {

        installer.getResultContainer().setInstallRoot(fileRoot);

		File config = new File(fileRoot, fileName);
		if(!config.exists()){ // passed in incorrectly on the command line or bad installer
			throw new IOException();
		}
		InputSource xmlInp = new InputSource(new FileInputStream(config));
		readConfig(xmlInp);
		
		return installer;
	}
	/**
	 * This overloaded method reads from the provided input stream to facilitate
	 * reading configs directly from the Jar, the file root is still needed 
	 * for Ant's basedir. Used by InputStreamLoadConfigFilter
	 * @return Installer
	 */
	protected Installer readConfig(File fileRoot, InputStream configSource) throws IOException, ConfigurationException {

        installer.getResultContainer().setInstallRoot(fileRoot);

		InputSource xmlInp = new InputSource(configSource);
		readConfig(xmlInp);
		
		return installer;
	}
	/**
	 * Currently read the config using any available XML parser
	 * @todo read the installer with only xerces
	 * @return Installer
	 */
	protected Installer readConfig(InputSource xmlInp) throws IOException, ConfigurationException {

		Document doc = null;
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
			
			// RFE [ 1475361 ] Using a custom entity resolver
			String entityResolverClass = System.getProperty("antinstaller.EntityResolverClass");
			EntityResolver er = null;
			if(entityResolverClass != null){
				try{
					er = (EntityResolver)Class.forName(entityResolverClass).newInstance();
				}
				catch(Exception e){
					// Property error use default this is a very specific requirement
					er = new CustomEntityResolver();
				}
			}
			else{
				er = new CustomEntityResolver();
			}
			
			parser.setEntityResolver(er);

			doc = parser.parse(xmlInp);
			Element root = doc.getDocumentElement();
			root.normalize();
			setProperties(installer, root.getAttributes());
			NodeList allPages = root.getElementsByTagName("page");
			//TODO make this pluggable
			getPages(installer, allPages);

		}
		catch (Exception e) {
			throw new IOException("DomFactory error: caused by:" + e.getClass() + ":" + e.getMessage());
		}
		return installer;
	}


	/**
	 * Used when reading the config
	 * @param allPages NodeList
	 * @throws ConfigurationException
	 */
	private void getPages(Installer installerConfig, NodeList allPages) throws ConfigurationException {
		ArrayList pages = new ArrayList();
		for (int i = 0; i < allPages.getLength(); i++) {
			Element pageElem = (Element) allPages.item(i);
			Page page = getPageType(pageElem.getAttribute("type"));
			setProperties(page, pageElem.getAttributes());
			pages.add(page);
			getOutputFields(page, pageElem);
		}
		Page[] pageArr = new Page[pages.size()];
		pages.toArray(pageArr);
		installerConfig.setPages(pageArr);
	}
	/**
	 * Used when reading the config
	 * @param page Page
	 * @param pageElem Element
	 * @throws ConfigurationException
	 */
    private void getOutputFields(Page page, Element pageElem) throws ConfigurationException {
        page.setOutputField( getInnerOutputFields( pageElem ));
    }

    private OutputField[] getInnerOutputFields( Element elem) throws ConfigurationException {
         NodeList allFields = elem.getChildNodes();
         ArrayList fields = new ArrayList();
         for (int i = 0; i < allFields.getLength(); i++) {
             if(! (allFields.item(i) instanceof Element))continue;
             Element fieldElem = (Element)allFields.item(i);
             OutputField field = getOutputFieldType(fieldElem.getNodeName(), fieldElem);
             if(field != null){
                 setProperties(field, fieldElem.getAttributes());
                 fields.add(field);
                 field.setResultContainer(installer.getResultContainer());
             }
         }
         OutputField[] fieldArr = new OutputField[fields.size()];

         return (OutputField[])fields.toArray(fieldArr);
     }

    /**
	 * Used when reading the config
	 * @param type String
	 * @throws ConfigurationException
	 * @return Page
	 */
	private Page getPageType(String type) throws ConfigurationException {
		if (type.equalsIgnoreCase("license")) {
			return new LicensePage();
		}
		else if (type.equalsIgnoreCase("input")) {
			return new SimpleInputPage();
		}
		else if (type.equalsIgnoreCase("progress")) {
			return new ProgressPage();
		}
		else if (type.equalsIgnoreCase("splash")) {
			return new SplashPage();
		}
		else if (type.equalsIgnoreCase("text")) {
			return new TextPage();
		}
		throw new ConfigurationException("Unknown Page type:" + type);
	}
	/**
	 * Used when reading the config
	 * @param type String
	 * @param field Element
	 * @throws ConfigurationException
	 * @return InputField
	 */
	private OutputField getOutputFieldType(String type, Element field) throws ConfigurationException {
		if (type.equalsIgnoreCase("text")) {
			return new UnvalidatedTextInput();
		}
		else if (type.equalsIgnoreCase("directory")) {
			return new DirectoryInput();
		}
		else if (type.equalsIgnoreCase("target")) {
			return new TargetInput();
		}
		else if (type.equalsIgnoreCase("file")) {
			return new FileInput();
		}
		else if (type.equalsIgnoreCase("comment")) {
			return new CommentOutput();
		}
		else if (type.equalsIgnoreCase("checkbox")) {
			return new CheckboxInput();
		}
		else if (type.equalsIgnoreCase("validated")) {
			return new ValidatedTextInput();
		}
		else if (type.equalsIgnoreCase("ext-validated")) {
			return new ExtValidatedTextInput();
		}
		else if (type.equalsIgnoreCase("password")) {
			return new PasswordTextInput();
		}
		else if (type.equalsIgnoreCase("password-confirm")) {
			return new ConfirmPasswordTextInput();
		}
        else if (type.equalsIgnoreCase("hidden")) {
             return new HiddenPropertyInput();
         }
        else if (type.equalsIgnoreCase("date")) {
			return new DateInput();
		}
		else if (type.equalsIgnoreCase("app-root")) {
			return new AppRootInput();
		}
        else if (type.equalsIgnoreCase("conditional")) {
            ConditionalField conditionalField = new ConditionalField();
            OutputField[] outFields = getInnerOutputFields( field );
            InputField[] inFields = new InputField[ outFields.length ];
            for( int i = 0; i < outFields.length; i++ ) {
                inFields[i] = (InputField) outFields[i];
            }
            conditionalField.setFields( inFields );
            return conditionalField;
        }
        else if (type.equalsIgnoreCase("select")) {
			SelectInput sInput = new SelectInput();
			NodeList allOptions = field.getElementsByTagName("option");
			ArrayList options = new ArrayList();
			for (int i = 0; i < allOptions.getLength(); i++) {
				Element optionElem = (Element) allOptions.item(i);
				SelectInput.Option option = sInput.getNewOption();
				option.setText(optionElem.getAttribute("text"));
				option.value = optionElem.getAttribute("value");
				options.add(option);
			}
			SelectInput.Option[] optionArr = new SelectInput.Option[options.size()];
			options.toArray(optionArr);
			sInput.setOptions(optionArr);

			return sInput;
		}
		else if (type.equalsIgnoreCase("target-select")) {
			TargetSelectInput sInput = new TargetSelectInput();
			NodeList allOptions = field.getElementsByTagName("option");
			ArrayList options = new ArrayList();
			for (int i = 0; i < allOptions.getLength(); i++) {
				Element optionElem = (Element) allOptions.item(i);
				SelectInput.Option option = sInput.getNewOption();
				option.setText(optionElem.getAttribute("text"));
				option.value = optionElem.getAttribute("value");
				options.add(option);
			}
			SelectInput.Option[] optionArr = new SelectInput.Option[options.size()];
			options.toArray(optionArr);
			sInput.setOptions(optionArr);

			return sInput;
		}
		else if (type.equalsIgnoreCase("large-select")) {
			LargeSelectInput sInput = new LargeSelectInput();
			NodeList allOptions = field.getElementsByTagName("option");
			ArrayList options = new ArrayList();
			for (int i = 0; i < allOptions.getLength(); i++) {
				Element optionElem = (Element) allOptions.item(i);
				LargeSelectInput.Option option = sInput.getNewOption();
				option.setText(optionElem.getAttribute("text"));
				option.value = optionElem.getAttribute("value");
				options.add(option);
			}
			LargeSelectInput.Option[] optionArr = new LargeSelectInput.Option[options.size()];
			options.toArray(optionArr);
			sInput.setOptions(optionArr);

			return sInput;
		}
		System.out.println("Unrecognised Input Element:"+type);
		return null;
		//throw new ConfigurationException("Unknown Input Field type:" + type);
	}



    /**
	 * Calls bean setter methods based on attribures found. Could use BeanUtils here
	 * but we want to stay clear of external dependencies.
	 * @param bean Object
	 * @param map NamedNodeMap
	 */
	private void setProperties(Object bean, NamedNodeMap map) {
		int numAtts = map.getLength();
		for (int a = 0; a < numAtts; a++) {
			Node attribute = map.item(a);
			String name = attribute.getNodeName();
			String value = attribute.getNodeValue();
			String methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
			Method[] allMethods = bean.getClass().getMethods();
			for (int m = 0; m < allMethods.length; m++) {
				if (allMethods[m].getName().equals(methodName)) {
					try {
						Class[] parameters = allMethods[m].getParameterTypes();
						Object[] paramValues;
						if (parameters[0].equals(Boolean.class)) {
							paramValues = new Boolean[1];
							if ( OutputField.isTrue(value) ) {
								paramValues[0] = Boolean.TRUE;
							}
							else {
								paramValues[0] = Boolean.FALSE;
							}
						}
						else
						if (parameters[0].equals(Integer.class)) {
							paramValues = new Integer[1];
							paramValues[0] = new Integer(value);
						}
						else {
							paramValues = new String[1];
							paramValues[0] = value;
						}
						allMethods[m].invoke(bean, paramValues);
						continue;
					}
					catch (IndexOutOfBoundsException ex) {
					    // not the setter we are looking for
                        // this is the wrong overloaded method
						continue; 
					}
					// Ignore reflection errors and continue
					catch (IllegalArgumentException e) {
					}
					catch (IllegalAccessException e) {					
					}
					catch (InvocationTargetException e) {						
					}
				}
			}
		}
	}

	private static class CustomEntityResolver implements EntityResolver{
		public InputSource resolveEntity(String publicId, String systemId) {
			
			if (publicId.equals("-//tp23 //DTD Ant Installer Config//EN") &&
				systemId.equals("http://antinstaller.sf.net/dtd/antinstall-config-0.2.dtd")) {
				InputSource localSrc = new InputSource(this.getClass().getResourceAsStream(
					"/org/tp23/antinstaller/antinstall-config-0.2.dtd"));
				return localSrc;
			}
			if (publicId.equals("-//tp23 //DTD Ant Installer Config//EN") &&
				systemId.equals("http://antinstaller.sf.net/dtd/antinstall-config-0.3.dtd")) {
				InputSource localSrc = new InputSource(this.getClass().getResourceAsStream(
					"/org/tp23/antinstaller/antinstall-config-0.3.dtd"));
				return localSrc;
			}
			if (publicId.equals("-//tp23 //DTD Ant Installer Config//EN") &&
				systemId.equals("http://antinstaller.sf.net/dtd/antinstall-config-0.4.dtd")) {
				InputSource localSrc = new InputSource(this.getClass().getResourceAsStream(
					"/org/tp23/antinstaller/antinstall-config-0.4.dtd"));
				return localSrc;
			}
			if (publicId.equals("-//tp23 //DTD Ant Installer Config//EN") &&
				systemId.equals("http://antinstaller.sf.net/dtd/antinstall-config-0.5.dtd")) {
				InputSource localSrc = new InputSource(this.getClass().getResourceAsStream(
					"/org/tp23/antinstaller/antinstall-config-0.5.dtd"));
				return localSrc;
			}
			if (publicId.equals("-//tp23 //DTD Ant Installer Config//EN") &&
				systemId.equals("http://antinstaller.sf.net/dtd/antinstall-config-0.6.dtd")) {
				InputSource localSrc = new InputSource(this.getClass().getResourceAsStream(
					"/org/tp23/antinstaller/antinstall-config-0.6.dtd"));
				return localSrc;
			}
			if (publicId.equals("-//tp23 //DTD Ant Installer Config//EN") &&
				systemId.equals("http://antinstaller.sf.net/dtd/antinstall-config-0.7.dtd")) {
				InputSource localSrc = new InputSource(this.getClass().getResourceAsStream(
					"/org/tp23/antinstaller/antinstall-config-0.7.dtd"));
				return localSrc;
			}
			if (publicId.equals("-//tp23 //DTD Ant Installer Config//EN") &&
				systemId.equals("http://antinstaller.sf.net/dtd/antinstall-config-0.8.dtd")) {
				InputSource localSrc = new InputSource(this.getClass().getResourceAsStream(
					"/org/tp23/antinstaller/antinstall-config-0.8.dtd"));
				return localSrc;
			}
			else {
				return null;
			}
		}
	}
}
