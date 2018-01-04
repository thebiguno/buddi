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
package org.tp23.antinstaller.selfextract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * Copies resources from the Classpath to the filesystem replacing regex with replacement
 * and correcting line endings
 * @author teknopaul
 *
 */
public class ResourceExtractor {
	
	/**
	 * @param resource Must start with / e.g. /org/tp23/myFile.txt
	 * @param dest
	 * @throws IOException 
	 */
	public void copyResource(String resource, File dest, String regex, String replace) throws IOException{
		InputStream is = this.getClass().getResourceAsStream(resource);
		if(is == null){
			throw new IOException("Can not find resource: " + resource);
		}
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		FileOutputStream bos = new FileOutputStream(dest);
		String line = null;
		while((line = br.readLine()) != null){
			bos.write(line.replaceAll(regex, replace).getBytes());
			bos.write(System.getProperty("line.separator").getBytes());
		}
        bos.flush();
        bos.close();
        br.close();
	}
	/**
	 * @param resource Must start with / e.g. /org/tp23/myFile.txt
	 * @param dest
	 * @throws IOException 
	 */
	public void copyResourceBinary(String resource, File dest) throws IOException{
		InputStream is = this.getClass().getResourceAsStream(resource);
		if(is == null){
			throw new IOException("Can not find resource: " + resource);
		}
		FileOutputStream bos = new FileOutputStream(dest);
		byte[] buffer = new byte[1024];
		for(int read = 0; (read = is.read(buffer)) > 0; ){
			bos.write(buffer, 0, read);
		}
        bos.flush();
        bos.close();
        is.close();
	}

}
