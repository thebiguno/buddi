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
package org.tp23.antinstaller;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Helper class that will catch missing bundle entries and handle formatting
 * of message strings
 *
 * @author mwilson
 * @version $Id
 * @since 0.7.4 patch 7
 */
public class ResourceBundleHelper
{
    private String bundleName;
    private ResourceBundle resourceBundle;

    private static final Object[] EMPTY_OBJ_ARRAY = new Object[0];
    private static final String EMPTY_STRING = "";

    public ResourceBundleHelper( final String bundleName )
    {
        this.bundleName = bundleName;
        resourceBundle = ResourceBundle.getBundle( bundleName );
    }

    public String getMessage( final String key )
    {
        return getMessage( key, EMPTY_OBJ_ARRAY );
    }

    public String getMessage( final String key, final Object obj  )
    {
        return getMessage( key, new Object[] { obj } );
    }

    /**
     * Return a formatted message string.
     * @param key resource bundle key
     * @param objArray array of objects that will be used with formatting
     *                 string
     * @return formatted string or error message if the resource bundle
     *         entry is missing
     */
    public String getMessage( String key, Object objArray[] )
    {
        String message;

        //Replace null values with empty strings - safer
        for( int i = 0; i < objArray.length; i++ )
        {
            if( objArray[i] == null )
            {
                objArray[i] = EMPTY_STRING;
            }
        }

        String formatStr = null;

        try
        {
            formatStr = resourceBundle.getString( key );
        }
        catch( MissingResourceException missingResExc )
        {
            //Handle missing resource below...
        }

        if( formatStr == null )
        {
            StringBuffer strBuffer = new StringBuffer( "Message entry with key " );
            strBuffer.append( key );
            strBuffer.append( " is missing from resource bundle " );
            strBuffer.append( bundleName );
            strBuffer.append( "\n" );
            strBuffer.append( bundleName );
            strBuffer.append( ":: " );
            strBuffer.append( key );
            for( int i = 0; i < objArray.length; i++ )
            {
                strBuffer.append( " " );
                strBuffer.append( objArray[i].toString() );
            }

            message = strBuffer.toString();
        }
        else
        {
            MessageFormat msgformat = new MessageFormat( formatStr );
            message = msgformat.format( ( (Object) ( objArray ) ) );
        }

        return message;
    }

}
