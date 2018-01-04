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

import org.tp23.antinstaller.InstallException;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.page.Page;

public class AutoTextRunner extends TextRunner {
	
	public AutoTextRunner(InstallerContext ctx) throws IOException{
		super(ctx);
	}

	public boolean runInstaller() throws InstallException {
		Page[] pages = installer.getPages();
		// run all postDisplayTargets as if the pages were shown
		for (int i = 0; i < pages.length; i++) {
			Page page = pages[i];
			if(page.getPostDisplayTarget() != null) {
				if( ifHelper.ifProperty(page) && 
				    ifHelper.ifTarget(page, ctx.getInstaller().getPages()) ) { // page would have been shown
					runPost(page);
				}
			}
		}

		return true;
	}
}
