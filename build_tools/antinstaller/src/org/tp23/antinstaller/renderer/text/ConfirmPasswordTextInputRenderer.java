package org.tp23.antinstaller.renderer.text;

import java.util.ResourceBundle;

public class ConfirmPasswordTextInputRenderer extends PasswordTextInputRenderer {
	
	private static final ResourceBundle res = ResourceBundle.getBundle("org.tp23.antinstaller.renderer.Res");
	
	protected String getErrorMessage(){
		return res.getString("passwordsDoNotMatch");
	}	

}
