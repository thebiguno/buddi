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
package org.tp23.antinstaller.renderer.swing;

import java.net.URL;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;
/**
 * HTMLEditor kit that replaces the source of images in the document
 * with resources loaded from the classpath.
 * @author teknopaul
 */
public class ClasspathHTMLEditorKit extends HTMLEditorKit {

	public ViewFactory getViewFactory(){
		
		return new HTMLEditorKit.HTMLFactory(){
			
			public View create(Element elem){
				if(! elem.getName().equals("img")){
					return super.create(elem);
				}
				return new ImageView(elem){
					public URL getImageURL() {
						String src = (String)getElement().getAttributes().
							getAttribute(HTML.Attribute.SRC);
						return TextPageRenderer.class.getResource(src);
					}
				};
			}
			
		};
		
	}

}
