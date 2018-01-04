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
package org.tp23.antinstaller;

import java.io.File;
/**
 *
 * <p>Renders a properties file in the base directory for Ant to use and
 *  available for viewing after for Debug</p>
 * In earlier versions this was the only way to access properties it is now
 * mostly redundant.
 * @author Paul Hinds
 * @version $Id: PropertiesFileRenderer.java,v 1.4 2006/12/23 04:07:23 teknopaul Exp $
 */
public interface PropertiesFileRenderer {

	public static final String FILE_ROOT_PROPERTY = "basedir";
    public static final String INSTALLER_VERSION_PROPERTY = "ant.install.config.version";
	public static final String PROPERTIES_FILE_NAME = "ant.install.properties";
	public static final String TARGETS_SUFFIX = "-targets";

	/**
	 * This method no longer throws IOException since the requirement to print properties
	 * has been removed.  By default properties will be printed since they are usefull
	 * for debug but classes implementing this method should swallow all Exceptions
	 * @param ctx InstallerContext
	 * @param baseDir File
	 */
	public void renderProperties(InstallerContext ctx, File baseDir);
}
