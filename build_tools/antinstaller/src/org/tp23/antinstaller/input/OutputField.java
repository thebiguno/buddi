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

import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.ValidationException;
/**
 * This is the super class of all "Input types".  It is called OutputField since
 * it handles the base features of "Input types" for outputing text for
 * the user to read.  It also encapsulates some convenience methods for
 * interpreting boolean values from the command line and in configuration files.
 */
public abstract class OutputField {

	// i18n support
	private static ResourceBundle langPack = null;
	private static int commentIdx = 0;
	static{
		try {
			langPack = ResourceBundle.getBundle("resources.LanguagePack");
		} catch (MissingResourceException e) {
			// ignore, signifies no lang packs installed 
		}
	}
	
	/* This is redundant unless language packs are used
	 */
	private String name = "comment." + ++commentIdx;
	
	protected String displayText;
	protected String explanatoryText;
	protected ResultContainer resultContainer;
	
	public OutputField() {
		
	}

	/* This is redundant unless language packs are used
	 */
	public String getName() {
		return name;
	}

	/* This is redundant unless language packs are used
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayText() {
		if(langPack != null){
			return langPack.getString(getName() + ".displayText");
		}
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getExplanatoryText() {
		if(langPack != null){
			try {
				return langPack.getString(getName() + ".explanatoryText");
			} catch (MissingResourceException e) {
				// ignore and return null explanatoryText is optional
			}
		}
		return explanatoryText;
	}

	public void setExplanatoryText(String explanatoryText) {
		this.explanatoryText = explanatoryText;
	}

	public void setResultContainer(ResultContainer resultContainer) {
		this.resultContainer = resultContainer;
	}

	/**
	 * Validate the user input (or lack of it)
	 * This method should return false if the validation fails an throw an exception
	 * if it is not possible to validate or there is an error.
	 *
	 * @param cxt InstallerContext
	 * @throws ValidationException thrown in error conditions not validation failure
	 * @return boolean
	 */
	public abstract boolean validate(InstallerContext cxt) throws ValidationException;

	/**
	 * Used to validate the configuration, this can be run prior to distributing the
	 * installer to check that the config is valid. Will not be used at runtime.
	 * @throws ValidationException
	 * @return boolean
	 */
	public abstract boolean validateObject();

	//////////////////////Static convenience methods

	    /** true if specified and true or yes.
	     *  N.B it is possible for X,  isTrue(X) == isFalse(X); 
	     *  This occurs if the value is null.
	     */
		public static boolean isTrue(String value){
			if(value == null)return false;
			return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes");
		}
		/** same as isTrue() but default is false if not specified */
		public static boolean isFalse(String value){
			if(value == null)return false;
			return value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no");
		}
		/**
		 * Return true if the value is set to true or false, returns false if the value is null
		 * @param value String
		 * @return boolean
		 */
		public static boolean requiredBoolean(String value){
			return isTrue(value) || isFalse(value);
		}
		/**
		 * Return true if the value is set to true or false, returns false if the value is null
		 * @param value String
		 * @return boolean
		 */
		public static boolean optionalBoolean(String value){
			return value == null || isTrue(value) || isFalse(value);
		}
}
