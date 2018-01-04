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

import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.text.Document;


/**
 * A JTextField with altered prefered size to facilitate fixing the width
 * but still using a GridBagLayout
 * @author Paul Hinds
 * @version $Id: AITextfield.java,v 1.2 2006/12/09 15:26:09 teknopaul Exp $
 */
public class AITextfield extends JTextField {

	public AITextfield() {
		super();
	}

	public AITextfield(int columns) {
		super(columns);
	}

	public AITextfield(String text) {
		super(text);
	}

	public AITextfield(String text, int columns) {
		super(text, columns);
	}

	public AITextfield(Document doc, String text, int columns) {
		super(doc, text, columns);
	}

	private Dimension prefSize = new Dimension(SizeConstants.FIELD_WIDTH, SizeConstants.FIELD_HEIGHT);

	public Dimension getMinimumSize() {
		return prefSize;
	}

	public Dimension getPreferredSize() {
		return prefSize;
	}
	public void setOverflow(Dimension prefSize) {
		this.prefSize = prefSize;
	}

	public Dimension getMaximumSize() {
		return prefSize;
	}


}