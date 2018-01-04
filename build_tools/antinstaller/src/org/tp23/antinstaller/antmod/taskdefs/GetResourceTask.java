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

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.tp23.antinstaller.selfextract.ResourceExtractor;
/**
 * Extracts a resource from the classpath into the filesystem
 * @author teknopaul
 *
 */
public class GetResourceTask extends Task{

	String resource;
	String dest;
	String regex;
	String replace;
	
	public void execute() throws BuildException{
		try {
			ResourceExtractor re = new ResourceExtractor();
			if(regex != null){
				re.copyResource(resource, new File(dest), regex, replace);
			}
			else{
				re.copyResourceBinary(resource, new File(dest));
			}
		} catch (IOException e) {
			throw new BuildException(e.getMessage());
		}
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String file) {
		this.dest = file;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getReplace() {
		return replace;
	}
	public void setReplace(String replace) {
		this.replace = replace;
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String token) {
		this.regex = token;
	}
}
