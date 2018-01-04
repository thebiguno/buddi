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

package org.tp23.antinstaller.renderer.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import org.tp23.antinstaller.InstallException;
import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.ResourceBundleHelper;
import org.tp23.antinstaller.ValidationException;
import org.tp23.antinstaller.input.ConditionalField;
import org.tp23.antinstaller.input.OutputField;
import org.tp23.antinstaller.runtime.ConfigurationException;

/**
 * Conditionally render a collection of fields
 *
 * @author mwilson
 * @version $Id
 * @since 0.7.4.patch 7
 */
public class ConditionalFieldRenderer implements TextOutputFieldRenderer
{
    private static final ResourceBundleHelper res = new ResourceBundleHelper("org.tp23.antinstaller.renderer.text.Res");

    private InstallerContext context;

    public void setContext( InstallerContext context )
    {
        this.context = context;
    }

    public void renderOutput( OutputField field, BufferedReader reader, PrintStream out )
            throws ValidationException, InstallException, IOException
    {
        ConditionalField conditional = (ConditionalField) field;

        try
        {

            OutputField[] fields = null;

            if( conditional.getExpression().evaluate() )
            {
                fields = conditional.getFields();

                SimpleInputPageRenderer.renderFields( context, fields, reader, out);
            }
        }
        catch( ConfigurationException configExc )
        {
            throw new InstallException( res.getMessage( "invalid.conditional.expression", conditional.getIfProperty()),
                                                        configExc );
        }
        catch( ClassNotFoundException clsNotFoundExc )
        {
            throw new InstallException( res.getMessage( "text.render.not.found" ),
                                                        clsNotFoundExc );
        }
    }

    public void renderError( OutputField field, BufferedReader reader, PrintStream out ) throws IOException
    {
        //renderOutput will have already rendered any errors
    }

    public boolean isAbort()
    {
        return false;
    }
}
