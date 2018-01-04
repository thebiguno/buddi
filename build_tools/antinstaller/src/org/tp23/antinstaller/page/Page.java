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
package org.tp23.antinstaller.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.tp23.antinstaller.InstallException;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.input.OutputField;
import org.tp23.antinstaller.input.TargetInput;
import org.tp23.antinstaller.runtime.IfPropertyHelper;
/**
 *
 * <p>Represents a page in the installer. </p>
 * <p>This object maintians an ordered list of targets that have been selected
* so that when ant is run the targets are run in the correct order.  If
* Targets exist in multiple pages they are run in the order they appear in the config file.  </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: tp23</p>
 * @author Paul Hinds
 * @version $Id: Page.java,v 1.10 2007/01/19 00:24:36 teknopaul Exp $
 */

public abstract class Page {

//	 i18n support
	private static ResourceBundle langPack = null;
	static{
		try {
			langPack = ResourceBundle.getBundle("LanguagePack");
		} catch (MissingResourceException e) {
			// ignore, signifies no lang packs installed  
		}
	}
	
	//private static final int MAX_TARGETS = 10;
	private String name;
	private String displayText;
	private String imageResource;
	private OutputField[] outputField;
	private boolean abort;
	/**
	 *  target to be called as the installer is running
	 */
	private String postDisplayTarget;
	private Set targets = new TreeSet();

	public Page() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayText() {
		if(langPack != null){
			return langPack.getString("page." + getName() + ".displayText");
		}
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getImageResource() {
		return imageResource;
	}

	public void setImageResource(String imageResource) {
		this.imageResource = imageResource;
	}

	public OutputField[] getOutputField() {
		return outputField;
	}

	public void setOutputField(OutputField[] outputField) {
		this.outputField = outputField;
	}

	public String getPostDisplayTarget() {
		return postDisplayTarget;
	}

	public void setPostDisplayTarget(String runtimeTarget) {
		this.postDisplayTarget = runtimeTarget;
	}


    /**
	 * These are the ant targets that will be run, this is decided after
	 * the Page has been displayed.  For example if the user chooses not
	 * to enter a field that may signify that a target should not be run
	 * @return A sorted List a list of Ant targets as Strings;
     * @throws InstallException 
	 */
	public List getTargets(InstallerContext ctx) {
		List results = new ArrayList(targets.size());
		try {
			Iterator iter = targets.iterator();
			IfPropertyHelper helper = new IfPropertyHelper(ctx);
			while(iter.hasNext()){
				IndexedTarget idxTarget = (IndexedTarget)iter.next();
				if( IndexedTarget.PAGE.equals(idxTarget.getTargetType()) ){
					if( helper.ifProperty(this) || helper.ifTarget(this, ctx.getInstaller().getPages() ) ){
						results.add(idxTarget.target);
					}
				}
				else{
					results.add(idxTarget.target);
				}
			}
		} catch (InstallException e) {
			// should not happen at runtime if ifProperty has been validated
			e.printStackTrace();
            ctx.log(ctx.getInstaller().isVerbose(), e);
			throw new RuntimeException();
		}
		return results;
	}
	/**
	 * get input targets that are selected and all page targets, independent of
	 * the ifProperty value 
	 * @return
	 */
	public List getAllTargets() {
		List results = new ArrayList(targets.size());
		Iterator iter = targets.iterator();
		while(iter.hasNext()){
			IndexedTarget idxTarget = (IndexedTarget)iter.next();
			results.add(idxTarget.target);
		}
		return results;
	}
	/**
	 * Comma separated list of targets for this page, called when parsing the
	 * config file
	 * @param target String
	 */
	public void setTarget(String targetList) {
		StringTokenizer st = new StringTokenizer(targetList, ",");
		while (st.hasMoreTokens()) {
			targets.add(new IndexedTarget(TargetInput.getGlobalIdx(), 
					st.nextToken(),
					IndexedTarget.PAGE));
		}
	}
	/**
	 * Adds an INPUT target to the Page config
	 * @param idx
	 * @param target
	 */
	public void addTarget(int idx, String target) {
		this.targets.add(new IndexedTarget(idx, target, IndexedTarget.INPUT));
	}
	public void removeTarget(int idx) {
		this.targets.remove(new IndexedTarget(idx, null));
	}
	/**
	 * returns true if the page has the current target set
	 * @param target String
	 * @return boolean
	 */
	public boolean isTarget(String target) {
		if(target == null){
			return false;
		}
		Iterator iter = targets.iterator();
		while(iter.hasNext()) {
			IndexedTarget idxTarget = (IndexedTarget)iter.next();
			if(idxTarget.target.equals(target)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return a List of IndexedTarget objects
	 */
	public List getPageTargets(){
		List toReturn = new ArrayList(targets.size());
		Iterator iter = targets.iterator();
		while(iter.hasNext()) {
			IndexedTarget idxTarget = (IndexedTarget)iter.next();
			if( IndexedTarget.PAGE.equals(idxTarget.targetType) ){
				toReturn.add(idxTarget);
			}
		}
		return toReturn;
	}

	/**
	 * @return a List of IndexedTarget objects
	 */
	public List getElementTargets(){
		List toReturn = new ArrayList(targets.size());
		Iterator iter = targets.iterator();
		while(iter.hasNext()) {
			IndexedTarget idxTarget = (IndexedTarget)iter.next();
			if( IndexedTarget.INPUT.equals(idxTarget.targetType) ){
				toReturn.add(idxTarget);
			}
		}
		return toReturn;
	}
    /**
	 * This is called after the page is displayed, a page can return false to indicate
	 * that the installation should abort.  Should be false if the cancel button is pressed.
	 * System.exit is not called to allow the installer to clean up temporary files.
	 * @return boolean
	 */
	public boolean isAbort() {
		return abort;
	}

	public void setAbort(boolean abort) {
		this.abort = abort;
	}
	public static class IndexedTarget implements Comparable{
		
		private static final String PAGE = "page";
		private static final String INPUT = "input";
		
		int idx;
		String target;
		String targetType = INPUT;
		
		IndexedTarget(int idx, String target){
			this.idx = idx;
			this.target = target;
		}
		IndexedTarget(int idx, String target, String targetType){
			this.idx = idx;
			this.target = target;
			this.targetType = targetType;
		}
		
		public boolean equals(Object target){
			IndexedTarget test = (IndexedTarget)target;
			return test.idx == idx;
		}
        // FindBugs - part of equals() contract
        public int hashCode(){
            return this.idx;
        }
		public int compareTo(Object o) {
			IndexedTarget test = (IndexedTarget)o;
			return idx - test.idx;
		}
		public String getTarget() {
			return target;
		}
		public String getTargetType() {
			return targetType;
		}

	}
}
