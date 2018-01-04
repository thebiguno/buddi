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
package org.tp23.antinstaller.renderer.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import org.tp23.antinstaller.InstallException;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.input.OutputField;



/**
 *
 * <p>Renders text OutputFields, TextOutputFieldRenderer should provide a no args constructor. </p>
 * <p>The package name for TextOutputFieldRenderer is critical</p>
 * BufferedInputStream is used for input to prevent new Buffered reader swallowing more input from the
 * input stream than is strictly required
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: tp23</p>
 * @author Paul Hinds
 * @version $Id: TextOutputFieldRenderer.java,v 1.4 2006/12/21 00:03:01 teknopaul Exp $
 */
public interface TextOutputFieldRenderer {
	public void setContext(InstallerContext ctx);

	public void renderOutput(OutputField field, BufferedReader reader, PrintStream out) throws InstallException, IOException;
	/**
	 * Called when validation fails
	 * @param field InputField
	 * @param in InputStream
	 * @param out PrintStream
	 * @throws IOException
	 */
	public void renderError(OutputField field, BufferedReader reader, PrintStream out) throws IOException;
	/** fields have abort for text since each field has its own input line*/
	public boolean isAbort();
}
