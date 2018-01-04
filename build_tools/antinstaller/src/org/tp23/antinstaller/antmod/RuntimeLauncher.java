/*
 * Copyright  2003-2004 The Apache Software Foundation
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
package org.tp23.antinstaller.antmod;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.antmod.taskdefs.LogTask;
import org.tp23.antinstaller.antmod.taskdefs.MessageTask;
import org.tp23.antinstaller.antmod.taskdefs.PropertyTask;
import org.tp23.antinstaller.runtime.ExecInstall;
import org.tp23.antinstaller.selfextract.NonExtractor;
import org.tp23.antinstaller.selfextract.SelfExtractor;



/**
 * This is a launcher for Ant which swallows all messages and logs.
 *
 * This file has been modified by Paul Hinds for Antinstaller and is not the same
 * as the one delivered with Ant 1.6
 *
 * @since Ant 1.6
 * @version $Id$
 */
public class RuntimeLauncher {

	public final static String CONTEXT_REFERENCE = "antinstaller.internal.context";

	private final Map allProperties = new HashMap();
	private final Project project = new Project();
	private InstallerContext ctx;

	public RuntimeLauncher(InstallerContext ctx) {
		this.ctx = ctx;
	}

	public void updateProps(){
		allProperties.clear();
		allProperties.putAll(InstallerContext.getEnvironment());
		allProperties.putAll(ctx.getInstaller().getResultContainer().getAllProperties());
		// add properties
		String arg;
		String value;
		Iterator iter = allProperties.keySet().iterator();
		while (iter.hasNext()) {
			arg = (String) iter.next();
			value = (String) allProperties.get(arg);
			project.setUserProperty(arg, value);
		}
	}

	public void parseProject(){
		project.setCoreLoader(this.getClass().getClassLoader());
		//project.addBuildListener(this);
        project.init();

		ProjectHelper helper = new ProjectHelper3();
		project.addReference("ant.projectHelper", helper);
		
		//SelfExtractor requirements
		if(SelfExtractor.CONFIG_RESOURCE == ctx.getConfigResource()){
			File buildXml = new File(ctx.getFileRoot(), ctx.getAntBuildFile());
			if(!buildXml.exists()){
				ctx.log("No build file found??: " + buildXml);
			}
			helper.parse(project, buildXml);
			project.setUserProperty("ant.file", buildXml.getAbsolutePath());
		}

		//NonExtractor requirements
		if(NonExtractor.CONFIG_RESOURCE == ctx.getConfigResource()){
			URL buildIS = this.getClass().getResource("/" + ctx.getAntBuildFile());
			helper.parse(project, buildIS);
			project.setUserProperty("ant.file", buildIS.toExternalForm());
			try {
				File enclosingJar = SelfExtractor.getEnclosingJar(this);
				project.setUserProperty(NonExtractor.ANTINSTALLER_JAR_PROPERTY, enclosingJar.getAbsolutePath());
			} catch (RuntimeException e) {
				ctx.log("No enclosing jar found");
			}
		}
		
		//Scripted install requirements
		if(ExecInstall.CONFIG_RESOURCE == ctx.getConfigResource()){
			File buildXml = new File(ctx.getFileRoot(), ctx.getAntBuildFile());
			helper.parse(project, buildXml);
			if(!buildXml.exists()){
				ctx.log("No build file found??: " + buildXml);
			}
			project.setUserProperty("ant.file", buildXml.getAbsolutePath());
		}
		
		project.setBaseDir(ctx.getFileRoot());
		
		// clever stuff for callbacks
		project.addReference(CONTEXT_REFERENCE, ctx);
		project.addTaskDefinition("antinstaller-property", PropertyTask.class);
		project.addTaskDefinition("antinstaller-message", MessageTask.class);
		project.addTaskDefinition("antinstaller-log", LogTask.class);

	}
    /**
	 * Run the launcher to launch Ant with a specific target, there is no classpath
	 * additions set or ant.home; everything should be loaded for this to run correctly.
	 *
	 * @param args the command line arguments
	 */
	public int run(String target){
        try {
			ctx.getLogger().log("internal target execution started:" + target);
        	project.fireBuildStarted();
        	project.executeTarget(target);
			project.fireBuildFinished(null);
			ctx.getLogger().log("internal target execution successful:" + target);
			return 0;
		}
		catch (Throwable t) {
			ctx.getLogger().log("internal target execution error:" + target);
            ctx.getLogger().log(ctx.getInstaller(), t);
			return 1;
		}
	}
}
