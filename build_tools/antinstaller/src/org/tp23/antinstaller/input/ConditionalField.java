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
import org.tp23.antinstaller.runtime.ConfigurationException;
import org.tp23.antinstaller.runtime.logic.Expression;
import org.tp23.antinstaller.runtime.logic.ExpressionBuilder;

/**
 * Container for other <code>OutputField</code> members that will be
 * conditionally updated. Typical use is with <code>&lt;hidden&gt;</code>
 * to conditionally set property values.
 *
 * @author mwilson
 * @version $Id
 * @since 0.7.4 patch 7
 */
public class ConditionalField extends InputField {
    
    private String ifProperty;
    private Expression expression;
    private InputField[] fields;

    public void setIfProperty(final String expressionStr) {
        this.ifProperty = expressionStr;
    }

    public String getIfProperty() {
        return ifProperty;
    }

    public InputField[] getFields() {
        return fields;
    }

    public void setFields(InputField[] fields) {
        this.fields = fields;
    }

    /**
     * Runtime validation of user input
     *
     * @param context Installer context
     * @return <code>true</code> if user input is valid, otherwise <code>false</code>
     * @throws ValidationException
     */
    public boolean validate(InstallerContext context)
            throws ValidationException {
        try {
            getExpression();
            if ((fields != null) && (fields.length > 0)) {
                int i = 0;
                for (; (i < fields.length) && (fields[i].validate(context)); i++) {
                }

                if (i == fields.length) {
                    return true;
                }
            }
        } catch (ConfigurationException configExc) {
            if(context.getInstaller().isVerbose()) {
                context.log(configExc);
            }
        }
        return false;
    }

    /**
     * Build-time validation of installer configuration file
     *
     * @return <code>true</code> if configuration is ok, otherwise <code>false</code>
     */
    public boolean validateObject() {
        try {
            getExpression();
            if ((fields != null) && (fields.length > 0)) {
                int i = 0;
                for (; (i < fields.length) && (fields[i].validateObject()); i++) {
                }

                if (i == fields.length) {
                    return true;
                }

                System.out.println("Invalid field:" + fields[i]);
            }
        } catch (ConfigurationException configExc) {
            System.out.println("Invalid conditional expression: " + configExc);
        }
        return false;
    }

    /**
     * Get the conditional expression in preparation for evaluation
     *
     * @throws ConfigurationException if the expression is invalid
     */
    public Expression getExpression() throws ConfigurationException {
        if (expression == null) {
            expression = ExpressionBuilder.parseLogicalExpressions(resultContainer, ifProperty);
        }

        return expression;
    }
}
