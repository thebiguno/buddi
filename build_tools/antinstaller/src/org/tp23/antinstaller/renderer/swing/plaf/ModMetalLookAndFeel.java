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

import java.awt.Font;
import java.awt.Insets;

import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
		
		
/**
 * This LAF is a replacement for Metal for those of us who can't stand the
 * exsessive use of the <b>BOLD</b> font in the default MetalLookAndFeel
 * but don't want a heavy LAF that uses excessive memory or increases download
 * size.  The excessive use of Sun's corporate color purple has also been
 * removed, but icons have been left as they are since they would add
 * to the download size significantly.
 * @author Paul Hinds
 * @version $Id: ModMetalLookAndFeel.java,v 1.3 2006/12/21 00:03:03 teknopaul Exp $
 */		 
public class ModMetalLookAndFeel extends MetalLookAndFeel {
		
	private static final long serialVersionUID = 1L;
	private static boolean isInstalled = false;
    protected static final Font defaultFont = new Font("Dialog",Font.PLAIN,11);
     
     
     public ModMetalLookAndFeel(){
         if(!isInstalled){
             isInstalled = true;
             UIManager.installLookAndFeel(new javax.swing.UIManager.LookAndFeelInfo("ModMetal", "org.tp23.laf.modmetal.ModMetalLookAndFeel"));
         }
     }
     public static void setAntiAliased(boolean antialiased){
         
     }

     public String getID(){
         return "ModMetalLookAndFeel";
     }

     public String getName()
     {
         return "ModMetalLookAndFeel";
     }

     public String getDescription(){
         return "Metal LAF with minor changes to default Fonts";
     }

     public boolean isNativeLookAndFeel(){
         return false;
     }

     public boolean isSupportedLookAndFeel(){
         return true;
     }

     protected void initClassDefaults(UIDefaults table){
         super.initClassDefaults(table);
     }

     protected void createDefaultTheme(){
         setCurrentTheme(new ModMetalTheme());
         super.createDefaultTheme();
     }

     protected void initSystemColorDefaults(UIDefaults table){
         super.initSystemColorDefaults(table);
     }

     protected void initComponentDefaults(UIDefaults table){
         super.initComponentDefaults(table);
         table.put("Button.font", defaultFont);
         table.put("Checkbox.font", defaultFont);
         table.put("CheckboxMenuItem.font", defaultFont);
         table.put("ComboBox.font", defaultFont);
         table.put("ComboBox.font", defaultFont);
         table.put("FormattedTextField.font", defaultFont);
         table.put("Label.font", defaultFont);
         table.put("List.font", defaultFont);
         table.put("Menu.font", defaultFont);
         table.put("MenuItem.font", defaultFont);
         table.put("PopupMenu.font", defaultFont);
         table.put("ProgressBar.font", defaultFont);
         table.put("RadioButton.font", defaultFont);
         table.put("RadioButtonMenuItem.font", defaultFont);
         table.put("TextArea.font", defaultFont);
         table.put("TextField.font", defaultFont);
         table.put("TextPane.font", defaultFont);
         table.put("TabbedPane.font", defaultFont);
         table.put("ToggleButton.font", defaultFont);
         table.put("Tree.font", defaultFont);
         table.put("Viewport.font", defaultFont);
         table.put("OptionPane.errorIcon", LookAndFeel.makeIcon(MetalLookAndFeel.class, "icons/Error.gif"));
         table.put("OptionPane.informationIcon", LookAndFeel.makeIcon(MetalLookAndFeel.class, "icons/Inform.gif"));
         table.put("OptionPane.warningIcon", LookAndFeel.makeIcon(MetalLookAndFeel.class, "icons/Warn.gif"));
         table.put("Button.margin", new Insets(2,4,2,4));
     }
	
}
		
