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

import javax.swing.JPanel;

import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.input.OutputField;
import org.tp23.gui.GBCF;
/**
 *
 * <p>Instances of this interface should have a no args constructor.
* They sould
* be Swing JComponent (e.g. subclass JPanel) and render normally responding
* to update paint and requests to change Look & Feel in a normal way. </p>
 * <p>Instances of this class should follow the naming convention. for each OutputField
* Xxx in the package org.tp23.antinstaller.input  there should exist a SwingOutputFieldRenderer
* called org.tp23.antinstaller.renderer.swing.XxxRenderer</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: tp23</p>
 * @author Paul Hinds
 * @version $Id: SwingOutputFieldRenderer.java,v 1.5 2007/01/04 22:57:17 teknopaul Exp $
 */
public abstract class SwingOutputFieldRenderer{

    //TODO From FindBugs - sometimes this field is not used in subclasses to avoid casting
    // replace with for example, getInputField() that casts in InputRenderers
    // requires work in ConditionalFieldRenderer
	protected OutputField outputField;
	protected InstallerContext ctx;
	
	/**
	 * this should hold a local reference and set the input fields default value
	 * if one exists
	 * @param inputField InputField
	 */
	public void setOutputField(OutputField outputField){
		this.outputField = outputField;
	}
	/**
	 * Init the swing components
	 */
	public abstract void initComponent(JPanel parent);
	/**
	 * Called by the Page prior to firing pagecompletion events
	 */
	public abstract void updateInputField();
	/**
	 * Called to update the defaults from the ResultContainer
	 */
	public abstract void updateDefaultValue();

	/**
	 * Called when validation fails
	 */
	public abstract void renderError();
	/**
	 * Called when the renderer should add itself to the content pane;
	 * @param content the panel to which the Renderer should add itself
	 * @param GridBagConstraintsFactory
	 * @param row the current row index in the table
	 * @param components should adjust preferred size when the overflow flag is set
	 * to compensate for width loss due to the scroll bar
	 * @return the row index after adding all its components
	 */
	public abstract int addSelf(JPanel content, GBCF cf, int row, boolean overflow);

	public void setInstallerContext(InstallerContext ctx){
		this.ctx = ctx;
	}


}
