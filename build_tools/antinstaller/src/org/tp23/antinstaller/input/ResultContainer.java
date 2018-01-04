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
package org.tp23.antinstaller.input;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.tp23.antinstaller.InstallerContext;



/**
 * <p>Data Holder for results of the data collection and convenience methods for
 * obtaining default values containing ${prop.name}/blah syntax </p>
 * @todo   Ensure in the validator (and Docs) that developers only add ${refs} for properties set on earlier pages
 * @author Paul Hinds
 * @version $Id: ResultContainer.java,v 1.6 2007/01/28 10:25:48 teknopaul Exp $
 */
public class ResultContainer {

	private HashMap properties = new HashMap();
	private Properties environment = InstallerContext.getEnvironment();
	private File installRoot;

	public ResultContainer() {
	}

	/**
	 * fetch a string for File and Directory inputs that expands ${refs} and
	 * also creates absolute paths from relative paths in the default value
	 * @param defaultString String
	 * @return String
	 */
	public String getDefaultFileRef(String defaultString){
		if(defaultString == null) {
            return null;
        }
        
		String expandedRefs = getDefaultValue(defaultString);
		File ref = new File(expandedRefs);
		if(!ref.isAbsolute()){
			String path = null;
			try {
				path = new File(installRoot, expandedRefs).getCanonicalPath();
			}
			catch (IOException ex) {
				// this is a bugger, but it should not happen it implies . or ..
				// can not be resolved, all we can do is return the . or .. and hope
				// it works later
				 path = new File(installRoot, expandedRefs).getAbsolutePath();
			}
			return path;
		} else {
			String path = ref.getAbsolutePath();
			return path;
		}
	}

    /**
	 *
     * Handles dereferenceing ${propName} syntax in default value fields
	 * @param defaultString String a plain String or a String with ${ref} references
	 * @return String
	 */
	public String getDefaultValue(String defaultString) {
		if(defaultString == null) {
            return null;
        }

		char[] characters = defaultString.toCharArray();
		char c;
		StringBuffer result = new StringBuffer();

		StringBuffer propertyNameBuffer = new StringBuffer();
		boolean inProp = false; // state flag indicating parsing a propertyName
		for (int i = 0; i < characters.length;) {
			c = characters[i];
			if ( c == '$' && ( characters.length > i + 1 && characters[i + 1] == '{' ) ){
				if(inProp){
                    //Nested property
                    int endIndex = defaultString.indexOf( '}', i + 1 );
                    if( endIndex != -1 ) {
                        ++endIndex;
                        propertyNameBuffer.append( getDefaultValue( defaultString.substring( i, endIndex ) ) );
                        i = endIndex;
                        continue;
                    }
                    else {
                        result.append(propertyNameBuffer.toString());
                        propertyNameBuffer = new StringBuffer();
                    }
                }
				else{
					inProp = true;
					propertyNameBuffer.append(c);
                    ++i;
                    continue;
				}
			}
			else if (c == '{') {
				if (inProp) {
					propertyNameBuffer.append(c);
					if(characters[i - 1] != '$') {
						inProp=false;
						result.append(propertyNameBuffer.toString());
						propertyNameBuffer = new StringBuffer();
					}
                    ++i;
                    continue;
				}
			}
			else if (c == '}') {
				if (inProp) {
					appendProperty(propertyNameBuffer, result);
					propertyNameBuffer = new StringBuffer();
					inProp = false;
                    ++i;
                    continue;
				}
			}
			if (!inProp) result.append(c);
			else propertyNameBuffer.append(c);
            ++i;
        }
		if(propertyNameBuffer.length() != 0) {
            result.append(propertyNameBuffer.toString());
        }
		return result.toString();
	}



	public HashMap getResults() {
		return properties;
	}
	public void setProperty(String key, String value){
		properties.put(key, value);
	}
	public String getProperty(String key){
		return (String)properties.get(key);
	}

	public void setInstallRoot(File installRoot) {
		this.installRoot = installRoot;
	}
	/**
	 * @since 0.7.1 to support installs from readonly media
	 * @return Map
	 */
	public Map getAllProperties(){
		return properties;
	}

	/**
	 * Appends the property if found or inserts an empty string.
	 * This method now supports loading environment variables.
	 * @param propertyNameBuffer StringBuffer
	 * @param result StringBuffer
	 */
	private void appendProperty(StringBuffer propertyNameBuffer, StringBuffer result) {
		String propertyName = propertyNameBuffer.toString();
		String key = propertyName.substring(2);
		String value = (String)properties.get(key);
		if(value == null && key.startsWith(InstallerContext.ENV_PREFIX)) {
			value = environment.getProperty(key);
		}
		if(value == null && key.startsWith(InstallerContext.JAVA_PREFIX)) {
			value = environment.getProperty(key);
		}
		if (value != null) {
			result.append(value);
		}
	}
}
