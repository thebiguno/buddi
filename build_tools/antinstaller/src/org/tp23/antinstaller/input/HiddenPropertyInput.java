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
package org.tp23.antinstaller.input;

import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.ValidationException;

/**
 * @author mwilson
 * @version $Id
 * @since 0.7.4 patch 6
 */
public class HiddenPropertyInput extends InputField
{

    public HiddenPropertyInput()
    {
    }

    public void setValue( String propValue )
    {
        //Use default value to allow updates when page re-displayed
        setDefaultValue( propValue );
    }

    /**
     * Called to validate the non-existent user input
     */
    public boolean validate( InstallerContext cxt ) throws ValidationException
    {
        return true;
    }

    /**
     * Used by checkConfig to validate the configuration file.
     * Not used at runtime.
     *
     * @return boolean
     */
    public boolean validateObject()
    {

        final String typeName = "hidden";
        if( getProperty() == null )
        {
            System.out.println( typeName + ": property must be set" );
            return false;
        }
        if( getDefaultValue() == null )
        {
            System.out.println( typeName + ": value must be set" );
            return false;
        }
        return true;
    }
}
