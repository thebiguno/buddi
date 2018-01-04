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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.antmod.RuntimeLauncher;
/**
 * usage: 
 * &lt;antinstaller-property name="property.name" value="myValue"/&gt;
 * <br />
 * or: 
 * &lt;antinstaller-property resource="/resources/my.props"/&gt;
 * @author teknopaul
 *
 */
public final class PropertyTask extends Task {
	
	private String name;
	private String value;
    private String resource;
	/**
	 * Ant Tasks must have a noargs constructor
	 */
	public PropertyTask(){
	}

	/**
	 * the "main" method
	 */
    public void execute() throws BuildException {
        if(resource != null){
            executeResource();
        }
        else {
            if(name == null || value == null) {
                throw new BuildException("either resource or (name and value) can not be null for antinstaller-property task");
            }
            executePropVal();
        }
    }
    
    private void executePropVal(){
        InstallerContext ctx = (InstallerContext)getProject().getReference(RuntimeLauncher.CONTEXT_REFERENCE);
        ctx.log("setting property: name=" + name + ", value=" + value);
        ctx.getInstaller().getResultContainer().setProperty(name, value);
    }
    
    // FindBugs - this class should stay final since getResourceAsStream() may not work in subclasses
    private void executeResource() throws BuildException{
        InstallerContext ctx = (InstallerContext)getProject().getReference(RuntimeLauncher.CONTEXT_REFERENCE);
        InputStream is = this.getClass().getResourceAsStream(resource);
        if(is == null){
            ctx.log("Can not find resource: " + resource);
            throw new BuildException("Can not find resource: " + resource);
        }
        try {
            Properties props = new Properties();
            props.load(is);
            is.close();
            if(ctx.getInstaller().isVerbose()){
                ctx.log("loaded properties: " + props.size());
            }
            
            ctx.getInstaller().getResultContainer().getAllProperties().putAll(props);
        } catch (IOException e) {
            ctx.log("Can not load resource: " + resource);
            throw new BuildException("Can not load resource: " + resource);
        }
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

}
