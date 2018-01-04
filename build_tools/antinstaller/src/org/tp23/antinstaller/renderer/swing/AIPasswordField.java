package org.tp23.antinstaller.renderer.swing;

import java.awt.Dimension;

import javax.swing.JPasswordField;
import javax.swing.text.Document;


/**
 * @author Paul Hinds
 * @version $Id: AIPasswordField.java,v 1.3 2006/12/21 00:02:59 teknopaul Exp $
 */
public class AIPasswordField extends JPasswordField {

	public AIPasswordField() {
		super();
	}

	public AIPasswordField(int columns) {
		super(columns);
	}

	public AIPasswordField(String text) {
		super(text);
	}

	public AIPasswordField(String text, int columns) {
		super(text, columns);
	}

	public AIPasswordField(Document doc, String txt, int columns) {
		super(doc, txt, columns);
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
