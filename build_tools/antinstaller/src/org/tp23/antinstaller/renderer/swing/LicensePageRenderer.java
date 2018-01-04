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

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.tp23.antinstaller.ValidationException;
import org.tp23.antinstaller.page.LicensePage;
import org.tp23.antinstaller.runtime.ConfigurationException;

/**
 *
 * <p>Renders the license page </p>
 * <p> </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: tp23</p>
* @todo this could be an input type and simple renderer
 * @author Paul Hinds
 * @version $Id: LicensePageRenderer.java,v 1.5 2007/01/12 10:49:09 anothermwilson Exp $
 */
public class LicensePageRenderer
	extends SwingPageRenderer {

    private static final ResourceBundle res = ResourceBundle.getBundle("org.tp23.antinstaller.renderer.Res");

	private JTextArea licenseTextArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane();

	public LicensePageRenderer(){
	}

	public boolean validateFields()throws ValidationException{
		return true; // @todo option to force accepting or tick box to accept
	}

	public void instanceInit() throws Exception {
		String resource = ((LicensePage)page).getResource();
		InputStream licensein = this.getClass().getResourceAsStream(resource);
		if (licensein == null) {
			throw new ConfigurationException("License resource '" + resource + "' is missing from installer");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(licensein));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = reader.readLine()) != null){
			sb.append(line);
			sb.append('\n');
		}

		licenseTextArea.setText(sb.toString());
		licenseTextArea.setTabSize(4);
		licenseTextArea.setAutoscrolls(true);
		licenseTextArea.setCaretPosition(0);
		licenseTextArea.setEditable(false);
		licenseTextArea.setLineWrap(true);
		licenseTextArea.setWrapStyleWord(true);
		licenseTextArea.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		scrollPane.getViewport().add(licenseTextArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(4, 4, 4, 4),
			            BorderFactory.createEtchedBorder()));
		this.add(scrollPane, BorderLayout.CENTER);

        getNextButton().setText( res.getString( "license.next.text" ) );
        getNextButton().setIcon(getImage("/resources/icons/ok.png"));
        getCancelButton().setText( res.getString( "license.cancel.text" ) );

    }

	public void updateInputFields(){
		;
	}



	/**
	 * updateDefaultValues
	 */
	public void updateDefaultValues() {
	}
}
