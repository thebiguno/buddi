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
package org.tp23.antinstaller.renderer.swing.plaf;

import java.lang.reflect.Method;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.input.OutputField;


/**
 * @author Paul Hinds
 * @version $Id: LookAndFeelFactory.java,v 1.9 2007/01/28 21:31:08 teknopaul Exp $
 */
public class LookAndFeelFactory {

	public static final String DEFAULT_LAF = "org.tp23.jgoodies.plaf.plastic.PlasticXPLookAndFeel";
	public static final String GREYMETAL_LAF = "greymetal";
	public static final String NATIVE_LAF = "native";
	public static final String JGOODIES_LAF = "jgoodies";
	public static final String NULL_LAF = "null";
	
	private final String specifiedLAF;
	private final InstallerContext ctx;
	/**
	 * 
	 */
	public LookAndFeelFactory(InstallerContext ctx) {
		this.ctx = ctx;
		this.specifiedLAF = ctx.getInstaller().getLookAndFeel();
	}

	public void setLAF(){
		String lafClassName = null;
		try{
			lafClassName = getLafFromToken(specifiedLAF);
			if(lafClassName == null){
				return;
			}
			LookAndFeel look = (LookAndFeel)Class.forName(lafClassName).newInstance();
			ctx.log("Setting look and feel:" + lafClassName);
			UIManager.setLookAndFeel(look);

			boolean antialias = OutputField.isTrue(ctx.getInstaller().getAntialiased());
			// Reflection used here to avoid dependencies on JGoodies
			if(antialias){
				Method setAntialiased = look.getClass().getMethod("setAntiAliased", new Class[]{boolean.class});
				if(setAntialiased != null){
					ctx.log("Setting antialiasing:" + antialias);
                    // JDK1.5 warning fix
                    Object[] args = new Boolean[]{new Boolean(antialias)};
					setAntialiased.invoke(null, args);
				}
			}
		}catch(Exception ex ){
			ctx.getLogger().log("Can not correctly set Look And Feel:" + ex.getMessage());
			ctx.getLogger().log(ctx.getInstaller(), ex);
		}
	}
    
    public static boolean isDefault(String laf){
        return ( laf == null || laf.equals(JGOODIES_LAF) || laf.equals(DEFAULT_LAF) );
    }
	/**
	 * Gets a look and feel class name respecting the tokens supported
	 * such as jgoodies, null, native and greymetal
	 * @param token
	 * @return look and feel class name
	 */
	public static String getLafFromToken(String token) {
		String laf = null;
        if(token == null || token.equals(JGOODIES_LAF)) {
            laf = DEFAULT_LAF;
        }
        else if(token.equals(NULL_LAF)) {
			laf = null;
		}
		else if(token.equals(NATIVE_LAF)) {
			laf = UIManager.getSystemLookAndFeelClassName();
		}
		else if(token.equals(GREYMETAL_LAF)) {
			laf = "org.tp23.antinstaller.renderer.swing.plaf.ModMetalLookAndFeel";
		}
		else {
			laf = token;
		}
		return laf;
	}
}
