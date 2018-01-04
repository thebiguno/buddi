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


import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.ValidationException;



/**
 *
 * <p>This "Input type" is strictly for Output! Unlike all the other
 * concrete classes in this package CommentOutputs refer to text
 * that will be printed but nothing will be collected from
 * the user.</p>
 * The text outputted in Comments will have property values resolved
 * if they are in the format ${property.name}
 * @author Paul Hinds
 * @version $Id: CommentOutput.java,v 1.2 2006/03/24 18:25:58 teknopaul Exp $
 */
public class CommentOutput
	extends OutputField {

	private String bold;
	private String title;

	public CommentOutput() {
	}

	public String getBold() {
		return bold;
	}

	public void setBold(String bold) {
		this.bold = bold;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDisplayText() {
		return 	resultContainer.getDefaultValue(super.getDisplayText()) ;
	}

	public String getExplanatoryText() {
		return resultContainer.getDefaultValue(super.getExplanatoryText());
	}

	/**
	 * Called to validate the user input
	 */
	public boolean validate(InstallerContext cxt) throws ValidationException{
		return true;
	}
	
	
	/**
	 * Used by checkConfig to validate the configuration file.
	 * Not used at runtime.
	 * @return boolean
	 */
	public boolean validateObject() {
//  null accepted now if only using explanatory text		
//		if(getDisplayText() == null){
//			System.out.println("Comment:displayText must be set");
//			return false;
//		}
		if(!InputField.optionalBoolean(getBold())){
			System.out.println("Comment:bold must be true or false or null:" + getBold());
			return false;
		}
		if(!InputField.optionalBoolean(getTitle())){
			System.out.println("Comment:title must be true or false or null:" + getTitle());
			return false;
		}
		return true;
	}
}
