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
package org.tp23.antinstaller.runtime.exe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.tp23.antinstaller.InstallException;
import org.tp23.antinstaller.InstallerContext;


/**
 * Loads FilterChains from resource files on the classpath with lists of class
 * names listed in order.  In the files lines starting with # and blank
 * lines are ignored
 * @author Paul Hinds
 * @version $Id: FilterFactory.java,v 1.4 2007/01/04 22:57:18 teknopaul Exp $
 */
public class FilterFactory {
	
	public static final String FILTER_RESOURCE = "/antinstall-config.fconfig";

	private FilterFactory() {
	}

	public static FilterChain factory(String configResource) throws InstallException{
		try {
			InputStream is = FilterFactory.class.getResourceAsStream(configResource);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String filterClass = null;
			final List filterChain = new ArrayList();
			while((filterClass = br.readLine())!=null){
				if(filterClass.startsWith("#"))continue;
				filterClass = filterClass.trim();
				if(filterClass.equals("")){
                    continue;
                }
				filterChain.add( Class.forName(filterClass).newInstance() );
			}
            br.close();
			return new DynamicFilterChain(configResource, filterChain);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		throw new InstallException("Can not create FilterChain");
	}
		
	static class DynamicFilterChain implements FilterChain{
		
		private ExecuteFilter[] filters;
		private String configResource;
		
		private DynamicFilterChain(String configResource, List filterChain){
			this.configResource = configResource;
			filters = new ExecuteFilter[filterChain.size()];
			filterChain.toArray(filters);
		}
		public void init(InstallerContext ctx){
			ctx.setConfigResource(configResource);
		}
		public ExecuteFilter[] getFilters(){
			return filters;
		}
	};
}
