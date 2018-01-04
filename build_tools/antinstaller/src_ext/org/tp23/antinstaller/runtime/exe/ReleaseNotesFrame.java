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
package org.tp23.antinstaller.runtime.exe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;


/**
 * ReleaseNotes frame for displaying plain text
 * @author Paul Hinds
 * @version $Id: ReleaseNotesFrame.java,v 1.3 2006/12/21 00:03:23 teknopaul Exp $
 */
public class ReleaseNotesFrame extends JFrame {

	private BorderLayout borderLayout1 = new BorderLayout();
	private JEditorPane contentsEditorPane = new JEditorPane();
	private JScrollPane hPanelScrollPane = new JScrollPane();

	public ReleaseNotesFrame(String title) throws HeadlessException {
		super(title);
	}

	public void init(String text){
		contentsEditorPane.setMinimumSize(new Dimension(200, 200));
		contentsEditorPane.setPreferredSize(new Dimension(1000, Integer.MAX_VALUE));
		contentsEditorPane.setEditable(false);
		contentsEditorPane.setContentType("text/plain");
		contentsEditorPane.setCaretPosition(0);
		contentsEditorPane.setText(text);
		this.getContentPane().setLayout(borderLayout1);
		hPanelScrollPane.setMinimumSize(new Dimension(200, 200));
		hPanelScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		this.getContentPane().add(hPanelScrollPane, BorderLayout.CENTER);
		hPanelScrollPane.getViewport().add(contentsEditorPane);
		hPanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.setSize(new Dimension(500,500));
		hPanelScrollPane.setWheelScrollingEnabled(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.show();
		
	}

}
