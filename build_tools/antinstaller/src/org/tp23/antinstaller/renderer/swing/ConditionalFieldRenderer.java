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
package org.tp23.antinstaller.renderer.swing;

import java.util.ArrayList;

import javax.swing.JPanel;

import org.tp23.antinstaller.ResourceBundleHelper;
import org.tp23.antinstaller.input.ConditionalField;
import org.tp23.antinstaller.input.InputField;
import org.tp23.antinstaller.renderer.RendererFactory;
import org.tp23.antinstaller.runtime.ConfigurationException;
import org.tp23.antinstaller.runtime.logic.Expression;
import org.tp23.gui.GBCF;

/**
 * Conditionally render a collection of fields.
 * For now, will ONLY handle <code>&lt;hiddeb&gt;</code> fields
 *
 * @author mwilson
 * @version $Id
 * @since 0.7.4 patch 7
 */
public class ConditionalFieldRenderer extends SwingOutputFieldRenderer {
    
    private ResourceBundleHelper resHelper = new ResourceBundleHelper( "org.tp23.antinstaller.renderer.Res" );
    private ConditionalField condField;
    private ArrayList renderers = new ArrayList( );

    public ConditionalFieldRenderer() {
    }

    public void initComponent( JPanel parent ) {

        condField = (ConditionalField) outputField;
        InputField[] fields = condField.getFields();

        for (int i = 0; i < fields.length; i++) {
            try {
                SwingOutputFieldRenderer renderer = RendererFactory.getSwingRenderer(fields[i]);
                renderer.setOutputField(fields[i]);
                renderer.setInstallerContext(ctx);
                renderers.add( renderer );
            }
            catch( ClassNotFoundException clsNotFndExc ) {

            }
        }

    }

    public void updateInputField() {

        if( expressionIsTrue() ) {
            int listSize  = renderers.size();
            for( int i = 0; i < listSize; i++ ) {
                SwingOutputFieldRenderer renderer = (SwingOutputFieldRenderer) renderers.get( i );
                renderer.updateInputField();
            }
        }

    }

    public void updateDefaultValue() {
        if( expressionIsTrue() ) {
            int listSize  = renderers.size();
            for( int i = 0; i < listSize; i++ ) {
                SwingOutputFieldRenderer renderer = (SwingOutputFieldRenderer) renderers.get( i );
                renderer.updateDefaultValue();
            }
        }
    }

    public void renderError() {
        int listSize  = renderers.size();
        for( int i = 0; i < listSize; i++ ) {
            SwingOutputFieldRenderer renderer = (SwingOutputFieldRenderer) renderers.get( i );
            renderer.renderError();
        }
    }

    public int addSelf( JPanel content, GBCF cf, int row, boolean overflow ) {
        return row;
    }

    private boolean expressionIsTrue() {
        Expression expr = null;

        try {
            expr = condField.getExpression();
        }
        catch( ConfigurationException configExc ) {
            ctx.log( resHelper.getMessage( "invalid.conditional.expression", configExc ) );
            return false;
        }

         return expr.evaluate();
    }
}
