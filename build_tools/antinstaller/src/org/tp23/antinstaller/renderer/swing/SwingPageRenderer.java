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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.ValidationException;
import org.tp23.antinstaller.page.Page;

/**
 *
 * <p>Abstract super class for page renderers.  setPage will always be called. </p>
 * <p>Subclasses should implement instanceInit for initialising swing components
 * on the page. </p>
 * @author Paul Hinds
 * @version $Id: SwingPageRenderer.java,v 1.10 2007/01/19 00:24:35 teknopaul Exp $
 */
public abstract class SwingPageRenderer
	extends JPanel {
	
	private static final ResourceBundle res = ResourceBundle.getBundle("org.tp23.antinstaller.renderer.Res");

	// gui components
	private BorderLayout borderLayout1 = new BorderLayout();
	
    // holds the next back buttons etc
	private JPanel controlPanel = new JPanel();

	private JButton backButton = new JButton();
	private JButton cancelButton = new JButton();
	private JButton nextButton = new JButton();
	private JButton finishButton = new JButton();

	private JPanel titlePanel = new JPanel();
	private JLabel titleLabel = new JLabel();
	private JLabel imagePanel = new JLabel(); // Graphic for the installer
	private GridLayout titleLayout = new GridLayout();

	// app components
	protected Page page;
	protected SwingInstallerContext swingCtx;
	protected InstallerContext ctx;
	protected PageCompletionListener listener;
	private Border bevelBorder;
		

	private static Font titleFont;
	static{
		titleFont = new JLabel().getFont();
		try {
			titleFont = new Font(titleFont.getFamily(), Font.BOLD, 14);
		}
		catch (Exception ex) {
			// lets not fail due to font errors
		}
	}

	public SwingPageRenderer(){
		super();
	}
	public void setPage(Page page){
		this.page = page;
		try {
			jbInit();
		}
		catch (Exception e) {
            ctx.log(e.getMessage());
            if(ctx.getInstaller().isVerbose()) {
                ctx.log(e);
            }
		}
	}

	public void setContext(SwingInstallerContext swingCtx){
		this.ctx = swingCtx.getInstallerContext();
		this.swingCtx = swingCtx;
	}
		
	
	private void jbInit() throws Exception {
		
		this.setDoubleBuffered(true);
        //emptyBorder = BorderFactory.createEmptyBorder(2,5,2,2);
		//bevelBorder = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(116, 116, 112),new Color(166, 166, 161)),BorderFactory.createEmptyBorder(2,SwingInputFieldRenderer.LEFT_INDENT,2,2));
		bevelBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black,1),BorderFactory.createEmptyBorder(2,SizeConstants.LEFT_INDENT,2,2));
		bevelBorder = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),BorderFactory.createEmptyBorder(2,SizeConstants.LEFT_INDENT,2,2));
		Border tripleBorder = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3,4,1,4),bevelBorder);
		this.setLayout(borderLayout1);
		titleLabel.setBorder(tripleBorder);
		controlPanel.setBorder(tripleBorder);

		this.add(titlePanel, BorderLayout.NORTH);
		this.add(controlPanel, BorderLayout.SOUTH);

		// title panel
		titlePanel.add(imagePanel, null);
		titlePanel.add(titleLabel, null);

		titlePanel.setLayout(titleLayout);
		titleLayout.setColumns(1);
		titleLayout.setHgap(0);
		titleLayout.setRows(2);
		titleLayout.setVgap(2);
		titlePanel.setMinimumSize(new Dimension(SizeConstants.PAGE_WIDTH, SizeConstants.TITLE_PANEL_HEIGHT));
		titlePanel.setMaximumSize(new Dimension(SizeConstants.PAGE_WIDTH, SizeConstants.TITLE_PANEL_HEIGHT));
		titlePanel.setPreferredSize(new Dimension(SizeConstants.PAGE_WIDTH, SizeConstants.TITLE_PANEL_HEIGHT));

		titleLabel.setText(page.getDisplayText());
		titleLabel.setFont(titleFont);
		setImage(page.getImageResource());
		imagePanel.setMinimumSize(new Dimension(SizeConstants.PAGE_WIDTH, SizeConstants.TITLE_IMAGE_HEIGHT));
		imagePanel.setMaximumSize(new Dimension(SizeConstants.PAGE_WIDTH, SizeConstants.TITLE_IMAGE_HEIGHT));
		imagePanel.setPreferredSize(new Dimension(SizeConstants.PAGE_WIDTH, SizeConstants.TITLE_IMAGE_HEIGHT));

		// Ctrl Panel
		controlPanel.add(cancelButton, null);
		controlPanel.add(backButton, null);
		controlPanel.add(nextButton, null);
		controlPanel.add(finishButton, null);
		backButton.setText(res.getString("backButton"));// "<< Back");
		cancelButton.setText(res.getString("cancelButton"));// "Cancel");
		nextButton.setText(res.getString("nextButton"));// "Next >>");
		finishButton.setText(ctx.getInstaller().getFinishButtonText());
		finishButton.setEnabled(false);
		setEventListeners();
		setIcons();
	}
	public abstract void instanceInit() throws Exception ;
	public abstract void updateInputFields();
	public abstract void updateDefaultValues();
	public abstract boolean validateFields()throws ValidationException;

	public void setPageCompletionListener(PageCompletionListener listener){
		this.listener = listener;
	}

	private void setImage(String resource) throws Exception{
		if(resource == null){
		   resource = ctx.getInstaller().getDefaultImageResource();
		}
		ImageIcon icon = getImage(resource);
		imagePanel.setIcon(icon);
	}
	
	protected ImageIcon getImage(String resource){
		try {
			if (resource != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				InputStream in = this.getClass().getResourceAsStream(resource);
				byte[] buffer = new byte[2048];
				int read = -1;
				while ( (read = in.read(buffer)) != -1) {
					baos.write(buffer, 0, read);
				}
				ImageIcon icon = new ImageIcon(baos.toByteArray());
				return icon;
			}
		}
		catch (Exception ex) {
			ctx.log("Can't load image resource:" + resource);
			if(ctx.getInstaller().isVerbose()){
				ctx.log(ex);
			}
		}
		return null;
	}
	
	
	private void setEventListeners(){
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				listener.pageBack(page);
			}
		});
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				page.setAbort(true);
                if (ctx.getInstaller().isVerbose()) {
                    ctx.log("Abort called");
                }
				listener.pageComplete(page);
			}
		});
		nextButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				listener.pageComplete(page);

			}
		});
		finishButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(finishButton.getText().equals(res.getString("exit"))) {
                    //TODO FindBugs this will prevent cleanup in FinalizerFilter
					System.exit(0);
				}
				listener.pageComplete(page);
				//((SwingInstallerContext)ctx).getSwingRunner().finish();
			}
		});
	}
	
	private void setIcons(){
		backButton.setIcon(getImage("/resources/icons/back.png"));
		cancelButton.setIcon(getImage("/resources/icons/cancel.png"));
		nextButton.setIcon(getImage("/resources/icons/next.png"));
		finishButton.setIcon(getImage("/resources/icons/finish.png"));
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public InstallerContext getCtx() {
		return ctx;
	}

	public JPanel getControlPanel() {
		return controlPanel;
	}

	public JLabel getImagePanel() {
		return imagePanel;
	}

	public JButton getNextButton() {
		return nextButton;
	}

	public JLabel getTitleLabel() {
		return titleLabel;
	}
    public JButton getFinishButton() {
		return finishButton;
    }
    public JButton getBackButton() {
		return backButton;
    }

}
