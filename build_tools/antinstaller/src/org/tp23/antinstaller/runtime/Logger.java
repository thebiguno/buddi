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
package org.tp23.antinstaller.runtime;

import java.io.IOException;

import org.tp23.antinstaller.Installer;

public interface Logger {

	public void log(String message);

	public void log(Throwable exception);

    /**
     * Logs the stack trace only if the installer is in verbose mode
     * @param installer
     * @param exception
     */
    public void log(Installer installer, Throwable exception);

	public void setFileName(String fileName) throws IOException;

    /**
     * Get the name of the file used for logging
     * @return path to file or <code>null</code> if not initialised
     */
    public String getFileName();

    public void close();
}
