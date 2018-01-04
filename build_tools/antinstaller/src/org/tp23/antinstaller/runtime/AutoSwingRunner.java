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

import java.util.List;

import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.page.Page;
import org.tp23.antinstaller.renderer.swing.SwingPageRenderer;
/**
 * Swing runner that starts with the last page bypassing all the other pages.
 * This will be used during auto builds where the properties are already known.
 * @author teknopaul
 *
 */
public class AutoSwingRunner extends SwingRunner {

	public AutoSwingRunner(InstallerContext ctx){
		super(ctx);
	}
	protected void showFirstPage() throws Exception {
		Page[] pages = installer.getPages();
		// run all postDisplayTargets as if the pages were shown
		for (int i = 0; i < pages.length; i++) {
			Page page = pages[i];
			if(page.getPostDisplayTarget() != null){
				if( ifHelper.ifProperty(page) && 
					ifHelper.ifTarget(page, ctx.getInstaller().getPages()) ){ // page would have been shown
					runPost(page);
				}
			}
		}
		
		// shows the LAST page directly which should be the Progress page
		ctx.setCurrentPage(pages[pages.length - 1]);
		List pageRenderers = getPageRenderers();
		renderNext((SwingPageRenderer)pageRenderers.get(pageRenderers.size() - 1));
	}

}
