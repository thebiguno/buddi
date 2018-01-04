/*
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
package org.tp23.antinstaller.runtime.exe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

import org.tp23.antinstaller.InstallException;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.input.OutputField;
import org.tp23.antinstaller.renderer.text.Pager;
import org.tp23.antinstaller.runtime.Runner;
import org.tp23.antinstaller.runtime.SwingRunner;
import org.tp23.antinstaller.runtime.TextRunner;


/**
 * A filter that launches a window with release notes if a property has been selected
 * during the install.  The notes are loaded from a resource on the classpath
 * called /release-notes.txt
 * @author Paul Hinds
 * @version $Id: ReleaseNotesFilter.java,v 1.4 2007/01/04 22:57:18 teknopaul Exp $
 */
public class ReleaseNotesFilter implements ExecuteFilter {
	
	private static final ResourceBundle res = ResourceBundle.getBundle("org.tp23.antinstaller.renderer.text.Res");
	private static final String nextChar = res.getString("nextChar");

	/**
	 * The AntInstaller property that must be "true" for the release notes to show
	 */
	public static final String RELEASE_NOTES_PROPERTY = "show.release.notes";

	public ReleaseNotesFilter() {
	}

	public void exec(InstallerContext ctx) throws InstallException {
		if(ctx.isInstallSucceded() ){
			String showReleaseNotes = ctx.getInstaller().getResultContainer().getProperty(RELEASE_NOTES_PROPERTY);
			if(OutputField.isTrue(showReleaseNotes)){
				try {
					InputStream is = ReleaseNotesFilter.class.getResourceAsStream("/release-notes.txt");
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String line = null;
					StringBuffer file = new StringBuffer();
					while((line = br.readLine())!=null){
						file.append(line).append('\n');
					}
                    br.close();
					Runner runner = ctx.getRunner();
					if(runner instanceof TextRunner){
						renderText(file.toString());

					}
					else{ // if(runner instanceof SwingRunner){
						//SwingRunner sRunner = (SwingRunner)runner;
						ReleaseNotesFrame rFrame = new ReleaseNotesFrame("Readme");
						rFrame.init(file.toString());
					}
				}
				catch (IOException e) {
					throw new InstallException("Could not render Release notes",e);
				}
			}
		}
	}
	
	private void renderText(String text) throws IOException{
		BufferedReader commandReader = new BufferedReader(new InputStreamReader(System.in));
		Pager pager = new Pager(text);
		String command = null;
		do {
			if (!pager.next(System.out)) {
				break;
			}
			System.out.println();
			System.out.println(getNextInstructions());
			command = commandReader.readLine();
		}
		while (command.toUpperCase().startsWith(nextChar));
		pager.rest(System.out);
		
	}
	private String getNextInstructions() {
		return res.getString("large_select_next");
	}

}
