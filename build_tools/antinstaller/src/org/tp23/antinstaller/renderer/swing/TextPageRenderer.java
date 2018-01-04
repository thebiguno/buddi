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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;

import org.tp23.antinstaller.ValidationException;
import org.tp23.antinstaller.page.TextPage;
import org.tp23.antinstaller.runtime.ConfigurationException;
/**
 * A page containing a text file's contents, may be HTML in swing.
 * The HTML supported is the standard Swing subset of HTML3.2 so 
 * it really just adds a bit of formatting and looks pretty bad.
 * The page is also parsed and property references in the document
 * are converted to the runtime values.
 * e.g. ${java.user.name} would be replaced with the current user in the HTML text.
 * 
 * Both the html page and embeded images are loaded from the classpath so
 * can be packaged in the jar.
 * 
 * The default font and background are determined by 
 * the LAF.
 * @author teknopaul
 *
 */
public class TextPageRenderer extends SwingPageRenderer{

	private JTextPane textPane = new JTextPane();
	private StringBuffer buffer = new StringBuffer();
	
	public TextPageRenderer() {
	}
	
	public boolean validateFields()throws ValidationException{
		return true;
	}

	public void instanceInit() throws Exception {
		final String resource = ((TextPage)page).getHtmlResource();
		InputStream in = this.getClass().getResourceAsStream(resource);
		if(in == null){
			throw new ConfigurationException("Html page resource is missing:" + resource);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String read = null;
		while ( (read = br.readLine()) != null) {
			buffer.append(read);
		}
		// as per FindBugs 
        br.close();
		
		JLabel defaults = new JLabel();
		textPane.setBackground(defaults.getBackground());
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		HTMLEditorKit classpathKit = new ClasspathHTMLEditorKit();
		textPane.setEditorKit(classpathKit);
		textPane.setAutoscrolls(true);
		
		String rule = "body{font-family:" + defaults.getFont().getFamily() + 
		";font-size:" + defaults.getFont().getSize() + "}";
		classpathKit.getStyleSheet().addRule(rule);
		textPane.setBorder(BorderFactory.createEmptyBorder());

		JScrollPane scroller = new JScrollPane();
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(4, 4, 4, 4),
						BorderFactory.createEtchedBorder()				
						));
		add(scroller, BorderLayout.CENTER);
		scroller.getViewport().add(textPane);
		this.add(scroller, BorderLayout.CENTER);
	}

	public void updateInputFields(){
	}



	/**
	 * updateDefaultValues
	 */
	public void updateDefaultValues() {
		// parse property references
		String parsedHtml = ctx.getInstaller().getResultContainer().getDefaultValue(buffer.toString());
		textPane.setText(parsedHtml);
		textPane.setCaretPosition(0);
	}
}
