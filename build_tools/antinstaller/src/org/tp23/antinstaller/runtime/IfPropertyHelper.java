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

import org.tp23.antinstaller.InstallException;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.page.Page;
import org.tp23.antinstaller.page.SimpleInputPage;
import org.tp23.antinstaller.runtime.logic.Expression;
import org.tp23.antinstaller.runtime.logic.ExpressionBuilder;


/**
 * <p>Encapsulates code fo the ifProperty feature</p>
 * N.B.  It is the installer generator's responsibility to ensure that properties passed
 * to the less than or greater than test are valid Numbers.
 * The internal Java format used is a Double so avalid regex would be
 * something like [0-9]+\.*[0-9]*  or [0-9]+ for an Integer.
 * The rather strange -=  and += syntax is used because &gt; and &lt;
 * must be escaped to &amp;gt; and &amp;lt; in XML attributes and the legibility
 * of the configuration files would be impared.
 * REF: 1145496
 * @author Paul Hinds
 * @version $Id: IfPropertyHelper.java,v 1.5 2007/01/19 00:24:36 teknopaul Exp $
 */
public class IfPropertyHelper {
	
	private InstallerContext ctx = null;
	public IfPropertyHelper(InstallerContext ctx){
		this.ctx = ctx;
	}

	/**
	 * @return boolean true to SHOW the page
	 */
	public boolean ifProperty(Page next) throws InstallException {
		// show page if ifProperty is specified and property is correct
		if(next instanceof SimpleInputPage) {
			SimpleInputPage conditionalPage = (SimpleInputPage) next;
			String ifProperty = conditionalPage.getIfProperty();
			if (ifProperty != null) {
                Expression expression;
                try {
                    expression = ExpressionBuilder.parseLogicalExpressions( ctx.getInstaller().getResultContainer(),
                                                                                   ifProperty );
                }
                catch( ConfigurationException configExc ) {
                    throw new InstallException( "Error parsing ifProperty condition for page " + next.getName(),
                                                configExc );    
                }
                return expression.evaluate();
            }

        }
		return true; // show the page by default
	}
	
	/**
	 * @return boolean true to SHOW the page
	 */
	public boolean ifTarget(Page next, Page[] pages){
		// skip iftarget specified and target is missing
		if(next instanceof SimpleInputPage){
			SimpleInputPage conditionalPage = (SimpleInputPage) next;
			String ifTarget = conditionalPage.getIfTarget();
			if (ifTarget != null) {
				boolean show = false;
				for (int p = 0; p < pages.length; p++) {
					show |= pages[p].isTarget(ifTarget);
				}
				return show;
			}
		}
		return true;
	}
}
