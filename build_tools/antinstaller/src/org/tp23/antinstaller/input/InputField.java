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
package org.tp23.antinstaller.input;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * <p>Object representation of an inputField XML Element </p>
 * <p>Also used to hold data of the results of the installer questions </p>
 * @author Paul Hinds
 * @version $Id: InputField.java,v 1.4 2006/12/07 02:50:27 teknopaul Exp $
 */
public abstract class InputField
	extends OutputField {

	// i18n support
	private static ResourceBundle langPack = null;
	static{
		try {
			langPack = ResourceBundle.getBundle("resources.LanguagePack");
		} catch (MissingResourceException e) {
			// ignore, signifies no lang packs installed  
		}
	}

	private String property;
	protected String defaultValue;

	/**
	 * Flag to indicate that the user has already editted this field
	 */
	private boolean editted = false;

	public InputField() {
	}

	public String getDisplayText() {
		if(langPack != null){
			return langPack.getString(getProperty() + ".displayText");
		}
		return displayText;
	}
	public String getExplanatoryText() {
		if(langPack != null){
			try {
				return langPack.getString(getProperty() + ".explanatoryText");
			} catch (MissingResourceException e) {
				// ignore and return null explanatoryText is optional
			}
		}
		return explanatoryText;
	}

	/**
	 * Returns the input result if there is one and if this is a PropertyField
	 * @return String
	 */
	public String getInputResult() {
		return resultContainer.getProperty(property);
	}

	public void setInputResult(String inputResult) {
		resultContainer.setProperty(property, inputResult);
	}
	public boolean isEditted() {
		return editted;
	}
	public void setEditted(boolean editted) {
		this.editted = editted;
	}
	public void setResultContainer(ResultContainer resultContainer) {
		this.resultContainer = resultContainer;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getDefaultValue() {
		return resultContainer.getDefaultValue(defaultValue);
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


}
