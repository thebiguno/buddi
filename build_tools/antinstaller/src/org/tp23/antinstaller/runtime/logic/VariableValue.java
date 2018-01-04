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

/**
 * @author mwilson
 * @version $Id: VariableValue.java,v 1.2 2006/12/21 00:03:18 teknopaul Exp $
 * @since 0.7.4 patch 2
 */
public class VariableValue implements Value {

	private final ResultContainer valueHolder;
	private final String propertyName;

	public VariableValue(final ResultContainer container, final String propertyName) {
		valueHolder = container;
		this.propertyName = propertyName;
	}

	public String getValue() {
		/*
		 * Use somewhat misleadingly named method getDefaultValue() It doesn't
		 * return the default value, it returns the current value of a property
		 * referenced by the use of ${propertyname} syntax
		 */
		return valueHolder.getDefaultValue(propertyName);
	}
}
