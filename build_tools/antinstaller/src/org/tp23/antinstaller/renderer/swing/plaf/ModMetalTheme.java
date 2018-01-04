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
package org.tp23.antinstaller.renderer.swing.plaf;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * @author Paul Hinds
 * @version $Id: ModMetalTheme.java,v 1.4 2006/12/21 00:03:03 teknopaul Exp $
 */
public class ModMetalTheme extends DefaultMetalTheme{

        private static final ColorUIResource primary1 = new ColorUIResource(
                                  102, 102, 102);
        private static final ColorUIResource primary2 = new ColorUIResource(
                				  153, 153, 153);
        private static final ColorUIResource primary3 = new ColorUIResource(
                                  204, 204, 204);
        
        
        private static final ColorUIResource secondary1 = new ColorUIResource(
        						  192, 192, 192);
        private static final ColorUIResource secondary2 = new ColorUIResource(
                                  213, 213, 213);
        private static final ColorUIResource secondary3 = new ColorUIResource(
                                  234, 234, 234);


        // these are blue in Metal Default Theme
        protected ColorUIResource getPrimary1() { return primary1; } 
        protected ColorUIResource getPrimary2() { return primary2; }
        protected ColorUIResource getPrimary3() { return primary3; }

        // these are gray in Metal Default Theme
        protected ColorUIResource getSecondary1() { return secondary1; }
        protected ColorUIResource getSecondary2() { return secondary2; }
        protected ColorUIResource getSecondary3() { return secondary3; }

}
