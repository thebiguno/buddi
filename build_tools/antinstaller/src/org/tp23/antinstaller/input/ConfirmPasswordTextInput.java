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
 * a second password filed that checks to see if a password entered twice matches
 * @author Paul Hinds
 * @version $Id$
 */
public class ConfirmPasswordTextInput
	extends PasswordTextInput
{
	private String origField;

	/**
	 * Called to validate the user input, if the confirm flag is set
	 * and there is a password field on the same page with the same property name
	 * both passwords must match for validation to pass
	 */
	public boolean validate(InstallerContext ctx) throws ValidationException{
		OutputField[] otherFields = ctx.getCurrentPage().getOutputField();
		for (int i = 0; i < otherFields.length; i++) {
			if(otherFields[i] instanceof PasswordTextInput &&
					otherFields[i] != this){
				PasswordTextInput pwd = (PasswordTextInput)otherFields[i];
				if(pwd.getProperty().equals(getOrigField())){
					return this.getInputResult().equals(pwd.getInputResult());
				}
			}
		}
		throw new ValidationException("Confirm password requires a PasswordTextInput, on the same page, with  property " + getOrigField());
	}

	public boolean validateObject() {
		if ( ! super.validateObject()){
			return false;
		}
		if(getOrigField() == null){
			System.out.println("ConfirmPassword:origField must be set");
			return false;
		}
		// @TODO check the orig field exists
		return true;
	}
	
	public String getOrigField() {
		return origField;
	}

	public void setOrigField(String origField) {
		this.origField = origField;
	}

}
