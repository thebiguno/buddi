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
package org.tp23.antinstaller.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.tp23.antinstaller.Installer;
import org.tp23.antinstaller.PropertiesFileRenderer;
import org.tp23.antinstaller.input.CommentOutput;
import org.tp23.antinstaller.input.InputField;
import org.tp23.antinstaller.input.LargeSelectInput;
import org.tp23.antinstaller.input.OutputField;
import org.tp23.antinstaller.input.SecretPropertyField;
import org.tp23.antinstaller.input.ConditionalField;
import org.tp23.antinstaller.input.SelectInput;
import org.tp23.antinstaller.input.TargetSelectInput;
import org.tp23.antinstaller.input.SelectInput.Option;
import org.tp23.antinstaller.page.Page;

/**
 * <p>Outputs the text from Pages as a Java Properties file. The file produced is compatible
 * with java.util.Properties. </p>
 * <p>It can be used as a stub for creating language packs,  the default text is included as a guide but can be deleted</p>
 * @author Paul Hinds
 */
public class LangPackFileRenderer{

	private static String newLine = System.getProperty("line.separator");
	private static final char[] hexidecimals = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public LangPackFileRenderer() {
	}

	public void renderProperties(Installer installer, File baseDir, String locale) throws IOException {
		Page[] pages = installer.getPages();

		StringBuffer propertiesData = new StringBuffer();
		propertiesData.append("### Ant Installer - language pack auto generated on ");
		propertiesData.append(new Date().toString());
		propertiesData.append(newLine);
		propertiesData.append(newLine);

		propertiesData.append("finishButtonText = " + installer.getFinishButtonText());
		propertiesData.append(newLine);
		propertiesData.append(newLine);

		String property = null;
        String value = null;

        for (int i = 0; i < pages.length; i++) {
			OutputField[] fields = pages[i].getOutputField();

			propertiesData.append(newLine);
			propertiesData.append("## Text from Page:" + pages[i].getName());
			propertiesData.append(newLine);
            property = "page." + pages[i].getName() + ".displayText";
			value = convert(pages[i].getDisplayText(), false);
			propertiesData.append(property + " = " + value);
			propertiesData.append(newLine);

            retrievePropertiesData( fields, propertiesData );

		}
        // create the stub
        if(locale != null) {
			File languagePackStub = new File(baseDir.getAbsolutePath(), "LanguagePack_" + locale + ".properties");
			FileWriter fos = new FileWriter(languagePackStub);
			BufferedWriter writer = new BufferedWriter(fos);
			writer.write(propertiesData.toString());
			writer.flush();
			fos.close();
        }
        else {
			// create the default
			File languagePack = new File(baseDir.getAbsolutePath(), "LanguagePack.properties");
			FileWriter fos = new FileWriter(languagePack);
			BufferedWriter writer = new BufferedWriter(fos);
			writer.write(propertiesData.toString());
			writer.flush();
			fos.close();
        }
	}
	
    private void retrievePropertiesData( OutputField[] fields, StringBuffer propertiesData ) {
        String property = null;
        String value = null;
        String explProperty = null;
        String explValue = null;

        for (int f = 0; f < fields.length; f++) {
        	
        	// use getName() for comments
        	if(fields[f] instanceof CommentOutput){
	            property = fields[f].getName() + ".displayText";
				value = convert(fields[f].getDisplayText(), false);
				propertiesData.append(property + " = " + value);
				propertiesData.append(newLine);

				if (fields[f].getExplanatoryText() != null && fields[f].getExplanatoryText().trim().length() > 0) {
					explProperty = fields[f].getName() + ".explanatoryText";
					explValue = convert(fields[f].getExplanatoryText(), false);
					propertiesData.append(explProperty + " = " + explValue);
					propertiesData.append(newLine);
				}
        	}
        	// use getProperty for input types
        	else {
        		InputField iField = (InputField)fields[f];
	            property = iField.getProperty() + ".displayText";
				value = convert(iField.getDisplayText(), false);
				propertiesData.append(property + " = " + value);
				propertiesData.append(newLine);
				if (iField.getExplanatoryText() != null && iField.getExplanatoryText().trim().length() > 0) {
					explProperty = iField.getProperty() + ".explanatoryText";
					explValue = convert(iField.getExplanatoryText(), false);
					propertiesData.append(explProperty + " = " + explValue);
					propertiesData.append(newLine);
 				}
				if(iField instanceof SelectInput) {
					SelectInput selectInput = (SelectInput)iField;
					for (int o = 0; o < selectInput.getOptions().length; o++) {
						SelectInput.Option option =  selectInput.getOptions()[o];
			            property = selectInput.getProperty() + "." + (o + 1) + ".displayText";
						value = convert(option.getText(), false);
						propertiesData.append(property + " = " + value);
						propertiesData.append(newLine);
					}
				}
				if(fields[f] instanceof LargeSelectInput) {
					LargeSelectInput selectInput = (LargeSelectInput)iField;
					for (int o = 0; o < selectInput.getOptions().length; o++) {
						LargeSelectInput.Option option =  selectInput.getOptions()[o];
			            property = selectInput.getProperty() + "." + (o + 1) + ".displayText";
						value = convert(option.getText(), false);
						propertiesData.append(property + " = " + value);
						propertiesData.append(newLine);
					}
				}
        	}			
        }
    }

	private String convert(String input, boolean doSpaces) {
		if (input == null) {
			// this happens when a page is skipped in text mode
			return "";
		}
		int num = input.length();
		StringBuffer sb = new StringBuffer(num);

		for (int i = 0; i < num; i++) {
			char c = input.charAt(i);
			switch (c) {
				case ' ':
					if (i == 0 || doSpaces) {
						sb.append('\\');
					}
					sb.append(' ');
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\t':
					sb.append("\\t");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '=':
					sb.append("\\=");
					break;
				case ':':
					sb.append("\\:");
					break;
				case '#':
					sb.append("\\#");
					break;
				case '!':
					sb.append("\\!");
					break;

				default:
					if ( (c < 0x0020) || (c > 0x007e) ) {
						sb.append("\\u")
							.append(hex( (c >> 12) & 0xF))
							.append(hex( (c >> 8) & 0xF))
							.append(hex( (c >> 4) & 0xF))
							.append(hex(c & 0xF));
					}
					else {
						sb.append(c);
					}
			}
		}
		return sb.toString();
	}

	private char hex(int val) {
		return hexidecimals[ (val & 0xF)];
	}

}
