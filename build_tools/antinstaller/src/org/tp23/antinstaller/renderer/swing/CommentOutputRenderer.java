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
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.tp23.antinstaller.input.CommentOutput;
import org.tp23.antinstaller.input.OutputField;
import org.tp23.gui.GBCF;

public class CommentOutputRenderer
	extends SwingOutputFieldRenderer {

    protected AILabel fieldLabel = new AILabel();
    // hack callback, should move this to superclass
    protected JTextComponent explanatoryTextField;

	private static Font boldCommentFont;
	private static Font titleCommentFont;
	static{
		boldCommentFont = new JLabel().getFont();// reusing the variable
		try {
			boldCommentFont = new Font(boldCommentFont.getFamily(), Font.BOLD, boldCommentFont.getSize());
			titleCommentFont = new Font(boldCommentFont.getFamily(), Font.BOLD, 16);
		}
		catch (Exception ex) {
			// lets not fail due to font errors
		}
	}

	public CommentOutputRenderer() {
	}

	public void initComponent(JPanel parent){
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setOutputField(OutputField outputField) {
		this.outputField = (CommentOutput)outputField;// trap ClassCast bugs early
	}
	public void updateInputField(){
	}

//   hack callback, should move this to superclass
    public JTextComponent getExplanatoryTextField() {
        return explanatoryTextField;
    }
//  hack callback, should move this to superclass
    public void setExplanatoryTextField(JTextComponent explanatoryTextField) {
        this.explanatoryTextField = explanatoryTextField;
    }
    /**
     * Since comments may now include expanded properties this should be called when the 
     * field is rendered.  For no ONLY comment fields have property values expanded
     */
	public void updateDefaultValue(){
        fieldLabel.setText(outputField.getDisplayText());
        if(explanatoryTextField != null) {
            explanatoryTextField.setText(outputField.getExplanatoryText());
        }
	}
    private void jbInit() throws Exception {
        
        // FindBugs - cast is performed here to avoid overriding protected superclass field 
        CommentOutput cOutputField = (CommentOutput)outputField;
        
		fieldLabel.setText(cOutputField.getDisplayText());
		
		if( OutputField.isTrue(cOutputField.getBold()) ){
			fieldLabel.setFont(boldCommentFont);
		}
		if( OutputField.isTrue(cOutputField.getTitle()) ){
			fieldLabel.setFont(titleCommentFont);
		}
    }
	public int addSelf(JPanel content, GBCF cf,  int row, boolean overflow) {
		content.add(fieldLabel, cf.getSpan(row));
		if(overflow){
			fieldLabel.setOverflow(SizeConstants.OVERFLOW_TOTAL_SIZE);
		} else {
			fieldLabel.setOverflow(new Dimension(SizeConstants.FIELD_WIDTH + SizeConstants.LABEL_WIDTH, SizeConstants.FIELD_HEIGHT));
		}
		return ++row;
	}


	/**
	 * renderError
	 */
	public void renderError() {
	}
}
