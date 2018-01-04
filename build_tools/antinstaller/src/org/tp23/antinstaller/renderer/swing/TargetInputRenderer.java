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

import javax.swing.JPanel;

import org.tp23.antinstaller.input.InputField;
import org.tp23.antinstaller.input.OutputField;
import org.tp23.antinstaller.input.TargetInput;
import org.tp23.gui.GBCF;

public class TargetInputRenderer
	extends SwingOutputFieldRenderer {

	protected TargetInput outputField;

    protected AILabel fieldLabel = new AILabel();
	protected AICheckBox targetCheckBox = new AICheckBox();

	public TargetInputRenderer() {
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



	public void setOutputField(OutputField outputField) {
		this.outputField = (TargetInput)outputField;
	}
	public void updateInputField(){
		String target = outputField.getTarget();
		int targetIdx = outputField.getIdx();
		boolean selected = targetCheckBox.isSelected();
		outputField.setInputResult(String.valueOf(selected));
		if(selected && !ctx.getCurrentPage().getAllTargets().contains(target)){
			ctx.getCurrentPage().addTarget(targetIdx, target);
		}
		else if(!selected && ctx.getCurrentPage().isTarget(target)){
			ctx.getCurrentPage().removeTarget(targetIdx);
		}
	}
	public void updateDefaultValue(){
		if(!outputField.isEditted()){
			targetCheckBox.setSelected(InputField.isTrue(outputField.getDefaultValue()));
		}
	}

    private void jbInit() throws Exception {
		fieldLabel.setText(outputField.getDisplayText());
		targetCheckBox.setSelected(InputField.isTrue(outputField.getDefaultValue()));
		if(InputField.isTrue(outputField.getForce())){
			targetCheckBox.setEnabled(false);
		}

		targetCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateInputField();
				outputField.setEditted(true);
			}
		});
    }
	public int addSelf(JPanel content,GBCF cf,  int row,boolean overflow) {
		content.add(fieldLabel,cf.getCell(row, 0));
		content.add(targetCheckBox,cf.getCell(row, 1));
		if(overflow){
			targetCheckBox.setOverflow(SizeConstants.OVERFLOW_FIELD_SIZE);
		}
		return ++row;
	}



	/**
	 * renderError
	 */
	public void renderError() {
	}
}
