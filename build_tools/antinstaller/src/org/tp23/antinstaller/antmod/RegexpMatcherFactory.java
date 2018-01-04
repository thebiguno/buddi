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
package org.tp23.antinstaller.antmod;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.regexp.RegexpMatcher;


/**
 * This class exists because the default Regexp handler is not part of ant.jar
 * @author Paul Hinds
 * @version $Id: RegexpMatcherFactory.java,v 1.2 2006/12/21 00:03:05 teknopaul Exp $
 */
public class RegexpMatcherFactory {

	/**
	 * 
	 */
	public RegexpMatcherFactory() {
		super();
	}
    public RegexpMatcher newRegexpMatcher() throws BuildException {
    	try{
    		return new org.apache.tools.ant.util.regexp.RegexpMatcherFactory().newRegexpMatcher(null);
    	}
    	catch(BuildException be){
    		return new Jdk14RegexpMatcher();
    	}
    }
}
