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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.tp23.antinstaller.input.OutputField;
import org.tp23.antinstaller.input.SelectInput;
import org.tp23.gui.GBCF;

public class SelectInputRenderer
	extends SwingOutputFieldRenderer {
	
	private static final ResourceBundle res = ResourceBundle.getBundle("org.tp23.antinstaller.renderer.Res");

	protected SelectInput inputField;

	protected JLabel fieldLabel = new AILabel();
	protected ButtonGroup optionGroup = new ButtonGroup();

	public SelectInputRenderer() {
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
		this.inputField = (SelectInput)inputField;
	}
	public void updateInputField(){
		Enumeration enumumeration = optionGroup.getElements();
		int i = 0;
		for(; enumumeration.hasMoreElements(); i++){
			JRadioButton o = (JRadioButton)enumumeration.nextElement();
			if(o.isSelected()) {
				inputField.setValue(inputField.getOptions()[i].value);
				break;
			}
		}
		if(i > inputField.getOptions().length) {
			inputField.setValue(inputField.getDefaultValue());
		}
	}
	public void updateDefaultValue() {
		if(!inputField.isEditted()) {
			String newDefault = inputField.getDefaultValue();
			Enumeration enumeration = optionGroup.getElements();
			for(int i = 0; enumeration.hasMoreElements(); i++){
				if(newDefault.equals(inputField.getOptions()[i].value)) {
					JRadioButton jrb = (JRadioButton) enumeration.nextElement();
					jrb.setSelected(true);
					// @todo should break?
				} else {
					enumeration.nextElement();
				}
			}
		}
	}

    private void jbInit() throws Exception {
		fieldLabel.setText(inputField.getDisplayText());
		SelectInput.Option[] options = inputField.getOptions();

		for (int i = 0; i < options.length; i++) {
			JRadioButton jrb = new AIRadioButton(options[i].getText());
			optionGroup.add(jrb);
			if(options[i].value.equals(inputField.getDefaultValue())){
				jrb.setSelected(true);
			}
			jrb.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					inputField.setEditted(true);
				}
			});
		}
    }
	public int addSelf(JPanel content,GBCF cf,  int row,boolean overflow) {
		content.add(fieldLabel,cf.getCell(row, 0));
		Enumeration enumeration = optionGroup.getElements();
		// there should be at least two
		enumeration.hasMoreElements();
		AIRadioButton jrb = (AIRadioButton)enumeration.nextElement();
		content.add(jrb,cf.getCell(row++, 1));
		if(overflow) {
			jrb.setOverflow(SizeConstants.OVERFLOW_FIELD_SIZE);
		}
		JPanel empty = new JPanel();
		while(enumeration.hasMoreElements()){
			jrb = (AIRadioButton)enumeration.nextElement();
			content.add(empty,cf.getCell(row, 0));
			content.add(jrb,cf.getCell(row++, 1));
			if(overflow) {
				jrb.setOverflow(SizeConstants.OVERFLOW_FIELD_SIZE);
			}
		}
		return row;
	}

	/**
	 * renderError
	 */
	public void renderError() {
		ctx.getMessageRenderer().printMessage(res.getString("notValidSelection"));
		// fixed BUG:1295944  ctx.getMessageRenderer().printMessage("Not a valid selection");
		//optionGroup.requestFocus();
	}
}
