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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import org.tp23.antinstaller.input.OutputField;
import org.tp23.antinstaller.input.UnvalidatedTextInput;
import org.tp23.gui.GBCF;

public class UnvalidatedTextInputRenderer
	extends SwingOutputFieldRenderer {

	protected UnvalidatedTextInput inputField;

    protected AILabel fieldLabel = new AILabel();
    protected AITextfield jTextField = new AITextfield();

	public UnvalidatedTextInputRenderer() {
	}

	public void initComponent(JPanel parent){
		try {
			jbInit();
		}
		catch(Exception e) {
            ctx.log(e.getMessage());
            if(ctx.getInstaller().isVerbose()) {
                ctx.log(e);
            }

		}
	}

	public void setOutputField(OutputField inputField) {
		this.inputField=(UnvalidatedTextInput)inputField;
		this.inputField.setValue(this.inputField.getDefaultValue());
	}
	public void updateInputField(){
		inputField.setValue(jTextField.getText());
	}
	public void updateDefaultValue(){
		if(!inputField.isEditted())jTextField.setText(inputField.getDefaultValue());
	}

    private void jbInit() throws Exception {
		fieldLabel.setText(inputField.getDisplayText());
		jTextField.setText(inputField.getDefaultValue());
		jTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateInputField();
			}
		});
		jTextField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() != '\t') {
					inputField.setEditted(true);
				}
			}
		});

    }
	public int addSelf(JPanel content,GBCF cf,  int row,boolean overflow) {
		content.add(fieldLabel,cf.getCell(row,0));
		content.add(jTextField,cf.getCell(row,1));
		if(overflow){
			jTextField.setOverflow(SizeConstants.OVERFLOW_FIELD_SIZE);
		}
		return ++row;
	}

	/**
	 * renderError
	 */
	public void renderError() {
	}
}
