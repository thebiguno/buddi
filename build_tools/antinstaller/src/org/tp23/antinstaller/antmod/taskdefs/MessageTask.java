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
package org.tp23.antinstaller.antmod.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.antmod.RuntimeLauncher;
/**
 * usage: 
 * &lt;antinstaller-message message="output"/&gt;
 * @author teknopaul
 *
 */
public class MessageTask extends Task {
	
	private String message;
	/**
	 * Ant Tasks must have a noargs constructor
	 */
	public MessageTask(){
	}

	/**
	 * the "main" method
	 */
    public void execute() throws BuildException {
    	InstallerContext ctx = (InstallerContext)getProject().getReference(RuntimeLauncher.CONTEXT_REFERENCE);
    	ctx.getMessageRenderer().printMessage(message);
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
