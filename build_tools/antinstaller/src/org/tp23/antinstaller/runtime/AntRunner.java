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

import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.antmod.RuntimeLauncher;
import org.tp23.antinstaller.page.Page;

/**
 * Abstract Runner superclass that handles the runPost(page) to execute
 * ant tasks mid display.
 * @author teknopaul
 */
public abstract class AntRunner implements Runner{

	private RuntimeLauncher launcher = null;
	private InstallerContext ctx;
	
	public AntRunner(InstallerContext ctx) {
		this.ctx = ctx;
	}

	protected void runPost(Page page){
		String postTarget = page.getPostDisplayTarget();
		if(postTarget != null){
			if(launcher == null){
				prepareLauncher();
			}
			launcher.updateProps();
			launcher.run(postTarget);
		}
	}
	/**
	 * This should never get run if there are no postTargets
	 *
	 */
	private void prepareLauncher(){
		launcher = new RuntimeLauncher(ctx);
		launcher.parseProject();
	}
}
