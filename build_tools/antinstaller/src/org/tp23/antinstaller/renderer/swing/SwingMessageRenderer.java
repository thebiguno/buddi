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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.renderer.MessageRenderer;
/**
 *
 * <p>Render User messages in Popup windows </p>
 * <p> </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: tp23</p>
 * @author Paul Hinds
 * @version $Id: SwingMessageRenderer.java,v 1.4 2006/12/21 00:02:59 teknopaul Exp $
 */
public class SwingMessageRenderer
	implements MessageRenderer {

	private InstallerContext ctx = null;
	private JFrame owner = null;


	public SwingMessageRenderer() {
	}
	public SwingMessageRenderer(InstallerContext ctx) {
		this.ctx = ctx;
	}

	public void setInstallerContext(InstallerContext ctx){
		this.ctx = ctx;
	}
	public void printMessage(String message){
		MessageRunnable messageRunnable = new MessageRunnable();
		messageRunnable.message = message;
		if(SwingUtilities.isEventDispatchThread()){
			messageRunnable.run();
		}
		else{
			try {
				SwingUtilities.invokeAndWait(messageRunnable);
			} catch (Exception e) { // Interrupted or InvocationTarget
				ctx.log(e);
			}
		}
	}

	public boolean prompt(String message){
		OptionRunnable optionRunnable = new OptionRunnable();
		optionRunnable.message = message;
		if(SwingUtilities.isEventDispatchThread()){
			optionRunnable.run();
		}
		else{
			try {
				SwingUtilities.invokeAndWait(optionRunnable);
			} catch (Exception e) { // Interrupted or InvocationTarget
				ctx.log(e);
			}
		}
		return optionRunnable.ok;
	}
	/**
	 * @param owner The owner to set.
	 */
	public void setOwner(JFrame owner) {
		this.owner = owner;
	}
	
	private class MessageRunnable implements Runnable{
		String message;

		public void run() {
			JOptionPane.showMessageDialog(SwingMessageRenderer.this.owner, 
					message, "Message", JOptionPane.INFORMATION_MESSAGE );
		}
		
	}
	
	private class OptionRunnable implements Runnable{
		String message;
		boolean ok;
		public void run() {
			int ret = JOptionPane.showConfirmDialog(SwingMessageRenderer.this.owner,
					message,
					"Question",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION) {
				ok = true;
			}
			else {
				ok = false;
			}
		}
		
	}
}
