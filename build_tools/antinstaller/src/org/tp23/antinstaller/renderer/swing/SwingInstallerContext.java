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
package org.tp23.antinstaller.renderer.swing;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.apache.tools.ant.BuildEvent;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.runtime.SwingRunner;

public class SwingInstallerContext{

	private static JFrame masterFrame;
    private JLabel feedBackPanel;
    private ProgressPanel progressPanel;
    private InstallerContext ctx;

	public SwingInstallerContext(InstallerContext ctx,
								 JFrame masterFrame) {
		this.ctx = ctx;
		SwingInstallerContext.masterFrame = masterFrame;
	}

	public JFrame getMasterFrame() {
		return masterFrame;
	}
    public SwingRunner getSwingRunner() {
		return (SwingRunner)ctx.getRunner();
    }

	public void setFeedBackLabel(JLabel feedBackPanel) {
		this.feedBackPanel = feedBackPanel;
	}
	/**
	 * The progress panel is optional so not calling this method
	 * should not cause errors or NPEs
	 * @param progressPanel
	 */
	public void setProgressPanel(ProgressPanel progressPanel) {
		this.progressPanel = progressPanel;
	}
	
	public void buildStarted(BuildEvent buildEvent) {
		provideAntFeedBack(buildEvent.getMessage());
		try {
			SwingUtilities.invokeAndWait(new Runnable(){
				public void run(){
					if(SwingInstallerContext.this.progressPanel != null){
						SwingInstallerContext.this.progressPanel.prepareCalledTargets();
					}
				}
			});
		} catch (Exception e) { //Interrupted or InvocationTarget
			SwingInstallerContext.this.ctx.log(e);
		}
	}
    
	public void buildFinished(BuildEvent buildEvent) {
		if(this.progressPanel != null){
			try {
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						SwingInstallerContext.this.progressPanel.buildFinished();
					}
				});
			} catch (Exception e) { //Interrupted or InvocationTarget
				SwingInstallerContext.this.ctx.log(e);
			}			
		}
	}
    
	public void targetStarted(BuildEvent buildEvent) {
		TargetStarted targetStarted = new TargetStarted();
		targetStarted.buildEvent = buildEvent;
		try {
			if(this.progressPanel != null){
				//Invoke and wait used since strict ordering od started and finished is requried
				SwingUtilities.invokeAndWait(targetStarted);
			}
		} catch (Exception e) { //Interrupted or InvocationTarget
			SwingInstallerContext.this.ctx.log(e);
		}			
	}
    
	public void targetFinished(BuildEvent buildEvent) {
		try {
			//Invoke and wait used since strict ordering od started and finished is requried
			SwingUtilities.invokeAndWait(new Runnable(){
				public void run(){
					if(SwingInstallerContext.this.progressPanel != null){
						SwingInstallerContext.this.progressPanel.targetFinished();
					}
				}
			});
		} catch (Exception e) { //Interrupted or InvocationTarget
			SwingInstallerContext.this.ctx.log(e);
		}
	}
	
	public void provideAntFeedBack(String message){
		// We should never have Ant running without a ProgressPane
		// but do an if null here in case future FilterChains are different
		ProvideAntFeedBack provideAntFeedBack = new ProvideAntFeedBack();
		provideAntFeedBack.message = message;
		try {
			if(feedBackPanel != null){
				SwingUtilities.invokeLater(provideAntFeedBack);
			}
		} catch (Exception e) { //Interrupted or InvocationTarget
			SwingInstallerContext.this.ctx.log(e);
		}
	}
	
	
	/**
	 * @return Returns the ctx.
	 */
	public InstallerContext getInstallerContext() {
		return ctx;
	}
	
	private class TargetStarted implements Runnable{
		private BuildEvent buildEvent;
		public void run(){
			SwingInstallerContext.this.progressPanel.targetStarted(buildEvent);
		}
	}
	private class ProvideAntFeedBack implements Runnable{
		private String message;
		public void run(){
			SwingInstallerContext.this.feedBackPanel.setText(message);
		}
	}
}
