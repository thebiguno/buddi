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

package org.tp23.antinstaller.runtime.logic;

import org.tp23.antinstaller.input.ResultContainer;
import org.tp23.antinstaller.runtime.ConfigurationException;

/**
 * @author mwilson
 * @version $Id: SimpleExpression.java,v 1.1 2006/09/08 19:16:30 anothermwilson
 *          Exp $
 * @since 0.7.4 patch 2
 */
public class SimpleExpression implements Expression {

	private final Value value1;
	private final ValuesTest testCondition;
	private final Value value2;
	private final String literalValue1;
	private final String literalValue2;

	public SimpleExpression(
				final ResultContainer resultContainer, 
				final String value1, 
				final ValuesTest test, 
				final String value2)
			throws ConfigurationException {
		
		this.literalValue1 = value1;
		this.value1 = getValue(resultContainer, value1);
		this.testCondition = test;
		this.literalValue2 = value2;
		this.value2 = getValue(resultContainer, value2);
		
	}

	public boolean evaluate() {
		return testCondition.getTestResult(value1, value2);
	}

	private Value getValue(ResultContainer container, String valueStr) throws ConfigurationException {
		if ( (valueStr.length() > 0) && (valueStr.charAt(0) == '$') ) {
			if ( valueStr.startsWith("${") && valueStr.endsWith("}") ) {
				return new VariableValue(container, valueStr);
			} else {
				throw new ConfigurationException("Badly formed variable: '" + valueStr + "'");
			}
		} else {
			return new LiteralValue(valueStr);
		}
	}

	public String toString() {
		return "[" + literalValue1 + "==" + literalValue2 + "]";
	}
}
